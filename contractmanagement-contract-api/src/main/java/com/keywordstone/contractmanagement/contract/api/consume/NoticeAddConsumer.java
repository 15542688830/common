package com.keywordstone.contractmanagement.contract.api.consume;

import com.keywordstone.contractmanagement.contract.api.dto.NoticeDTO;
import com.keywordstone.contractmanagement.contract.api.module.EmailVO;
import com.keywordstone.contractmanagement.contract.api.service.NoticeService;
import com.keywordstone.contractmanagement.contract.api.utils.EmailUtil;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyInfoDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.PersonInfoDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.service.CompanyInfoSdkService;
import com.keywordstone.contractmanagement.usercenter.sdk.service.UserInfoSdkService;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.enums.DataFlagEnum;
import com.keywordstone.framework.common.database.mybatis.entity.DContParty;
import com.keywordstone.framework.common.database.mybatis.entity.DContPartyExample;
import com.keywordstone.framework.common.database.mybatis.service.DContPartyService;
import com.keywordstone.framework.common.queue.rocketmq.annotation.RocketConsume;
import com.keywordstone.framework.common.queue.rocketmq.client.RocketMqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RocketConsume(topic = "addNotice", tags = "1", clazz = NoticeDTO.class)
public class NoticeAddConsumer extends RocketMqConsumer<NoticeDTO> {

    private static final String NAME = "</br></br></br></br></br>****************************\n此消息由合同管理系统发出\n****************************";

    private static final String INFIX = "--";

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserInfoSdkService userInfoSdkService;
    @Autowired
    private DContPartyService dContPartyService;
    @Autowired
    private CompanyInfoSdkService companyInfoSdkService;

    @Override
    public boolean consume(NoticeDTO noticeDTO) {
        ResultDTO rst = noticeService.addNotice(noticeDTO);

        sendEmail(noticeDTO);

        return rst.isSuccess();
    }

    //邮件提醒
    private void sendEmail(NoticeDTO noticeDTO) {
        if (StringUtils.isNotEmpty(noticeDTO.getReceiverId())) {
            ResultDTO<PersonInfoDTO> resultDTO = userInfoSdkService.getPersonByUserId(noticeDTO.getReceiverId());

            if (resultDTO.isSuccess() && resultDTO.getData() != null &&
                    StringUtils.isNotEmpty(resultDTO.getData().getPersonEmail())) {

                StringBuilder subject = new StringBuilder();

                DContPartyExample dContPartyExample = new DContPartyExample();
                if (StringUtils.isNotEmpty(noticeDTO.getEntityId())) {

                    dContPartyExample.createCriteria().andContIdEqualTo(noticeDTO.getEntityId())
                            .andPartyTypeEqualTo(1).andDataFlagEqualTo(DataFlagEnum.FUNCTIONAL.getValue());
                    ResultDTO<List<DContParty>> dContPartyResultDTO = dContPartyService.selectByExample(dContPartyExample);
                    log.info("entityId:" + noticeDTO.getEntityId());
                    if (dContPartyResultDTO.isSuccess() && dContPartyResultDTO.getData() != null
                            && dContPartyResultDTO.getData().size() > 0) {
                        log.info("partyId:" + dContPartyResultDTO.getData().get(0).getPartyId());
                        try {
                            ResultDTO<CompanyInfoDTO> resultDTO1 = companyInfoSdkService.getCompUnitByCompId(dContPartyResultDTO.getData().get(0).getPartyId());

                            if (resultDTO1.isSuccess() && resultDTO1.getData() != null
                                    && StringUtils.isNotEmpty(resultDTO1.getData().getComName())) {
                                subject.append(resultDTO1.getData().getComName());
                            }
                        } catch (Exception e) {
                            log.error("获取客商名称失败："+e.getMessage(), e);
                        }
                    }
                }

                EmailUtil emailUtil = new EmailUtil();
                EmailVO emailVO = new EmailVO();
//                emailVO.setHtmlMsg(subject.toString() + INFIX + noticeDTO.getWorkName() + NAME);
//                emailVO.setSubject(subject.toString());
                emailVO.setHtmlMsg("您有新的合同审批任务" + INFIX + noticeDTO.getWorkName() + NAME);
                emailVO.setSubject("合同审批任务");
                emailVO.setToEmail(resultDTO.getData().getPersonEmail());
                emailVO.setToName(resultDTO.getData().getPersonName());
                emailUtil.sendEmail(emailVO);
                log.info("调用工具类发送邮件！"+"邮件内容：" + emailVO.toString());
            } else {
                log.info("该接收用户没有邮箱地址！"+"接收用户：" + noticeDTO.getReceiverId());
            }
        }
    }
}
