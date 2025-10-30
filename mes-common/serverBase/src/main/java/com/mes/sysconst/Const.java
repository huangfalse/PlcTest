package com.mes.sysconst;

import java.util.Arrays;
import java.util.List;

/**
 * @Author : zhoush
 * @Date: 2025/5/28 17:00
 * @Description:
 */
public class Const {
    /**
     * 默认密码
     */
    public static final String DEFULT_PASSWORD = "123456";

    /**
     * 默认角色
     */
    public static final Long DEFULT_ROLE = 1L;

    /**
     * PP表工程表状态
     * 1 流程卡创建
     * 2 第一次优化保存
     * 10 钢化推荐创建保存
     * 20 优化结果保存
     * 100 优化已完成，mes可领取
     * 200 MES已领取
     */
    public static final Integer ENGINEERING_WAIT = 100;
    public static final Integer ENGINEERING_RECEIVE = 200;

    /**
     * plc任务交互常量
     */
    /**
     * 工作状态 及 一般请求字 启动字
     */
    public static final Integer PLC_OTHER = 2;
    public static final Integer PLC_ON = 1;
    public static final Integer PLC_OFF = 0;

    /**
     * PLC联机时的设备状态
     * 0空闲
     * 1忙碌
     * 2禁用
     */
    public static final Integer PLC_FREE = 0;
    public static final Integer PLC_BUSY = 1;
    public static final Integer PLC_DISABLE = 2;
    /**
     * 设备运行线路
     * 1
     * 2
     */
    public static final Integer WORK_ONE = 1;
    public static final Integer WORK_TWO = 2;
    /**
     * 卧理进出片位编号
     * 进片位2001
     * 出片位2002
     */
    public static final Integer HISTORY_IN = 2001;
    public static final Integer HISTORY_OUT = 2002;
    /**
     * 设备启用/禁用状态
     * 启用 1
     * 禁用 0
     */
    public static final Integer MES_ENABLE = 1;
    public static final Integer MES_DISABLE = 0;

    /**
     * 任务状态
     * 0 创建任务  未磨边
     * 1 执行中   磨边中
     * 2 完成     磨边完成
     * 3 失败
     * 7 暂停
     * 8 破损
     */
    public static final Integer TASK_STATE_NEW = 0;
    public static final Integer TASK_STATE_START = 1;
    public static final Integer TASK_STATE_SUCCESS = 2;
    public static final Integer TASK_STATE_FAIL = 3;
    public static final Integer TASK_STATE_PAUSE = 7;
    public static final Integer TASK_STATE_DAMAGE = 8;
    public static final List<Integer> TASK_STATE_INCOMPLETE_ALL = Arrays.asList(TASK_STATE_NEW, TASK_STATE_START, TASK_STATE_PAUSE);

    /**
     * 任务类型
     * 1 进
     * 2 出
     * 3 直通
     * 4 调度
     * 5 结束
     * 6 上片
     */
    public static final Integer TASK_TYPE_IN = 1;
    public static final Integer TASK_TYPE_OUT = 2;
    public static final Integer TASK_TYPE_THROUGH = 3;
    public static final Integer TASK_TYPE_DISPATCH = 4;
    public static final Integer TASK_TYPE_OVER = 5;
    public static final Integer TASK_TYPE_LOADGLASS = 6;

    /**
     * 玻璃尺寸
     * 1 大板尺寸标准
     */
    public static final Integer LARGE_GLASS_LR = 3000;

    /**
     * 0 创建
     * 1 已发送上片任务
     * 2 上片已完成
     * 3 已发送oot文件
     * 4 切割已完成
     */
    public static final Integer RAW_QUEUE_NEW = 0;
    public static final Integer RAW_QUEUE_START_LOAD = 1;
    public static final Integer RAW_QUEUE_SUCCESS_LOAD = 2;
    public static final Integer RAW_QUEUE_START_CUT = 3;
    public static final Integer RAW_QUEUE_SUCCESS_CUT = 4;
    public static final List<Integer> RAW_QUEUE_LOAD_ALL = Arrays.asList(0, 1);


    /**
     * 各笼内，仓储内玻璃状态
     * 0 创建
     * 100 在里面
     * 101 在外面
     * 102 执行中
     * 8 破损
     * 笼内玻璃状态集合
     * 笼内玻璃以及正在进片玻璃集合
     */
    public static final Integer GLASS_STATE_NEW = 0;
    public static final Integer GLASS_STATE_IN = 100;
    public static final Integer GLASS_STATE_OUT = 101;
    public static final Integer GLASS_STATE_EXECUTING = 102;
    public static final Integer GLASS_STATE_DAMAGE = 8;
    public static final Integer GLASS_STATE_TAKE = 9;
    public static final List<Integer> GLASS_STATE_IN_ALL = Arrays.asList(100, 102);
    public static final List<Integer> GLASS_STATE_IN_ALL_ZERO = Arrays.asList(0, 100, 102);

    /**
     * 亚美尼亚大理片出入口
     * 930 磨边后卧转立
     * 931 磨边后人工上片
     * 932 钢化进片
     * 933 钢化出片
     * 934 中空一线
     */
    public static final Integer VERTICAL_CACHE_EDG_ONE = 930;
    public static final Integer VERTICAL_CACHE_EDG_TWO = 931;
    public static final Integer VERTICAL_CACHE_TEMP_IN = 932;
    public static final Integer VERTICAL_CACHE_TEMP_OUT = 933;
    public static final Integer VERTICAL_CACHE_HOLLOW_ONE = 934;
    public static final List<Integer> OUT_TARGET_POSITION_ALL = Arrays.asList(930, 931);

    /**
     * 上海大理片各位置配置
     * 900 磨边后卧转立1
     * 901 磨边后卧转立2
     * 902 大理片人工出片台
     * 1001 进片车
     * 1002 出片车
     * 2001 大理片直通台
     * 2002 大理片后出片台
     */
    public static final Integer VERTICAL_CACHE_IN_C11 = 900;
    public static final Integer VERTICAL_CACHE_IN_B11 = 901;
    public static final Integer VERTICAL_CACHE_IN_D02 = 902;
    public static final Integer VERTICAL_CACHE_IN_CAR = 1001;
    public static final Integer VERTICAL_CACHE_OUT_CAR = 1002;
    public static final Integer VERTICAL_CACHE_THROUGH_D01 = 2001;
    public static final Integer VERTICAL_CACHE_OUT_D03 = 2002;


    /**
     * 大理片笼玻璃工序阶段
     * 0、钢化
     * 1、中空
     */
    public static final Integer VERTICAL_TEMPING = 0;
    public static final Integer VERTICAL_HOLLOW = 1;

    /**
     * 钢化小片表
     * -1 生成任务
     * 0  出片完成
     * 1  摆片完成
     * 2  进炉完成
     * 3  钢化完成
     * 4  钢化出片完成
     * 7  取消钢化
     * 8  破损
     * 摆片相关状态集合
     * 未摆片状态集合
     */
    public static final Integer TEMPERING_NEW = -1;
    public static final Integer TEMPERING_OUT = 0;
    public static final Integer TEMPERING_DROP = 1;
    public static final Integer TEMPERING_START = 2;
    public static final Integer TEMPERING_SUCCESS = 3;
    public static final Integer TEMPERING_END = 4;
    public static final Integer TEMPERING_CANCEL = 7;
    public static final Integer TEMPERING_DAMAGE = 8;
    public static final List<Integer> TEMPERING_PUT_ALL = Arrays.asList(TEMPERING_NEW, TEMPERING_OUT, TEMPERING_DROP);
    public static final List<Integer> TEMPERING_UNPUT_ALL = Arrays.asList(TEMPERING_NEW, TEMPERING_OUT);

    /**
     * 报工类型/状态
     * 类型：8 破损
     * 类型：9 拿走
     * 状态：1 未报工
     * 状态：3 已报工
     * 状态：7 未报工已现补
     * 状态：8 已同步现补给ERP
     * 现补状态集合：7,8
     */
    public static final Integer REPORT_TYPE_DAMAGE = 8;
    public static final Integer REPORT_STATE_COMPLETE = 1;
    public static final Integer REPORT_STATE_REPORTED = 3;
    public static final Integer REPORT_STATE_PATCHED = 7;
    public static final Integer REPORT_STATE_SYNC = 8;
    public static final List<Integer> REPORT_STATE_PATCH_ALL = Arrays.asList(REPORT_STATE_PATCHED, REPORT_STATE_SYNC);


    /**
     * 成功返回状态字
     */
    public static final Integer SUCCESS_CODE = 200;

    /**
     * 大理片笼最大任务数
     */
    public static final Integer TASK_MAX_COUNT = 6;
    /**
     * 大理片笼最大任务数
     */
    public static final String TEMPERING_PROMPT_LIST = "temperingList";


    /**
     * 钢化阶段
     * 1、钢化前
     * 2、钢化中
     * 3、钢化后
     */
    public static final Integer TEMPING_BEFORE_STAGE = 1;
    public static final Integer TEMPING_STAGE = 2;
    public static final Integer TEMPING_AFTER_STAGE = 3;


    /**
     * 钢化设备号
     */
    public static final Integer TEMPING_DEVICE_ID = 930;
    /**
     * 默认线路
     */
    public static final Integer WORK_LINE_ONE = 1;
    public static final Integer WORK_LINE_TWO = 2;


    /**
     * 磨边状态
     */
    public static final Integer EDGING_STATE_UNEDGED = 0;
    public static final Integer EDGING_STATE_EDGING = 1;
    public static final Integer EDGING_STATE_EDGED = 2;
    public static final Integer EDGING_STATE_DAMAGE = 8;
}
