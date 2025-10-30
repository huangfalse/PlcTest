package com.mes.optimize;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author : zhoush
 * @Date: 2025/7/23 9:33
 * @Description:
 */
@Data
@TableName("pp.optimize_project")
public class OptimizeProject {

    /**
     * 工程编号
     */
    private String projectNo;

    /**
     * 状态：
     * -2删除；
     * -1默认；
     * <p>
     * 1流程卡创建；
     * 2第一次优化保存;
     * <p>
     * 10钢化推荐创建保存；
     * 20优化结果保存
     * <p>
     * 100优化已完成
     * 200MES已领取
     */
    private Integer state;
}
