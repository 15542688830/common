package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description: 工作量返回值
 * @date: Created in 16:40 2018/3/12
 * @Modified By:
 */
@Data
public class TaskVolumeResultDTO {

    //统计维度  0:周 1:月 2:年
    private Integer type;
    //X轴数据
    private String[] xAxis;
    //Y轴数据已完成工作
    private Integer[] completeTaskCount;
    //Y轴数据未完成工作
    private Integer[] unCompleteTaskCount;
}
