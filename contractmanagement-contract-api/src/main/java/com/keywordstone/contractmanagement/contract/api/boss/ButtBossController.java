package com.keywordstone.contractmanagement.contract.api.boss;

import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyMerchantDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 对接Boss系统控制类
 *
 * @author SL
 * @date 2018/05/14
 */
@RestController
@RequestMapping("/buttBoss")
@Api(description ="对接Boss系统api")
public class ButtBossController {

    @Autowired
    private ButtBossService buttBossService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("查询客商信息")
    public ResultDTO search(@RequestBody ReqBossDTO params) {
        ResultDTO rst = buttBossService.selecctCompanyInfo(params);
        return rst;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增商户编号")
    public ResultDTO add(@RequestBody ReqBossDTO params) {
        ResultDTO rst = buttBossService.addCompanyMerchant(params);
        return rst;
    }
}
