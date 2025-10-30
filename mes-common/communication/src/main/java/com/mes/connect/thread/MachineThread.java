package com.mes.connect.thread;

import com.alibaba.fastjson.JSONException;
import com.mes.common.JsonConversion;
import com.mes.common.ReadFile;
import com.mes.connect.industrialinterface.ApiImpl;
import com.mes.connect.industrialinterface.IndustrialClient;
import com.mes.connect.entity.*;
import com.mes.connect.modbus.ModbusIpClient;
import com.mes.connect.modbus.ModbusTcpClient;
import com.mes.connect.protocol.ProtocolType;
import com.mes.connect.s7.S7ClientOld;
import com.mes.model.entity.Machine;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 设备线程
 *
 * @author yzx
 * @version 1.0
 */
@Slf4j
public class MachineThread extends Thread {
    /**
     * 当前设备的参数
     */
    private Machine machine;
    private ProtocolType protocolType;
    private IndustrialClient client;
    private PlcParameters plcParameters;
    private LogicConfig logicConfig;
    @Resource
    private ApiImpl api;

    /**
     * 存储所有逻辑线程
     */
    private Map<String, Thread> logicThreads = new ConcurrentHashMap<>();
    /**
     * 控制逻辑线程运行的标志
     */
    private Map<String, Boolean> logicRunningFlags = new ConcurrentHashMap<>();
    /**
     * 主线程运行标志
     */
    private boolean running = true;
    /**
     * 主线程执行间隔
     */
    private int mainThreadInterval = 1000;

    /**
     * 线程池服务
     */
    private final ExecutorService threadPool;
    /**
     * 时间
     */
    private int timeOut = 10;

    /**
     * @param machine
     * @param api
     * @throws IOException
     */
    public MachineThread(Machine machine, ApiImpl api) throws IOException {
        this.machine = machine;
        this.api = api;
        this.logicConfig = JsonConversion.jsonToObjectByJackson(ReadFile.readJson(machine.getLogicFile()).toString(), LogicConfig.class);
        this.plcParameters = JsonConversion.jsonToObjectByJackson(ReadFile.readJson(machine.getMachineFile()).toString(), PlcParameters.class);
        this.logicConfig.getLogics();
        this.plcParameters.initialization();
        // 手动配置线程池参数   核心线程数,最大线程数,线程空闲时间,,有界队列，防止OOM,拒绝策略：调用者执行
        threadPool = new ThreadPoolExecutor(
                10,
                10,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        for (LogicItem logicItem : logicConfig.getLogics()) {
            if (logicItem.getLogicInterval() == 0) {
                //默认1000毫秒
                logicItem.setLogicInterval(1000);
            }
            for (Logic logic : logicItem.getLogic()) {
                logic.setAddress(this.plcParameters.getMap().get(logic.getCodeId()).getAddress());
                logic.setPlcDataType(this.plcParameters.getMap().get(logic.getCodeId()).getPlcDataType());
            }
            for (ReturnValue returnValue : logicItem.getReturnValue()) {
                returnValue.setAddress(this.plcParameters.getMap().get(returnValue.getCodeId()).getAddress());
                returnValue.setPlcDataType(this.plcParameters.getMap().get(returnValue.getCodeId()).getPlcDataType());
            }
        }


        switch (machine.getProtocolType().getName()) {
            case "ModbusTcp":
                client = new ModbusTcpClient(machine.getIp(), machine.getPort(), 1);
                break;
            case "ModbusIp":
                client = new ModbusIpClient(machine.getIp(), machine.getPort());
                break;
            case "S7":
                break;
            case "S7Old":
                client = new S7ClientOld(machine.getPlcType().getName(), machine.getIp(), machine.getPort(), 0, 1);
                ;
                break;
            default:
                log.error("无效的协议类型: {}", protocolType);
                throw new IllegalArgumentException("无效的协议类型: " + protocolType);
        }
    }

    public void executeTask(Runnable task) {
        threadPool.execute(task);
    }

    /**
     * 应用关闭时调用
     */
    public void threadPoolShutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(timeOut, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    /**
     * 运行入口
     */
    @Override
    public void run() {
        // 主线程持续运行，定期读取PLC参数并监控连接状态
        while (running) {
            try {
                // 检查PLC连接状态
                if (!client.isConnected()) {
                    log.info("PLC尝试连接... 设备IP: {}", machine.getIp());
                    tryReconnect();
                }
                // 读取PLC参数，为逻辑处理提供最新数据
                this.readPlcParameter();
                plcParameters.initialization();
                // 检查逻辑线程状态，开启/重启 线程
                checkLogicThreadsStatus();
                // 等待下一个执行周期
                Thread.sleep(mainThreadInterval);
            } catch (InterruptedException e) {
                log.info("主线程被中断，准备退出");
                running = false;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("执行过程中发生错误: {}", e.getMessage(), e);
                // 尝试重新连接
                tryReconnect();
                // 等待一段时间再继续执行
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    log.info("主线程被中断，准备退出");
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        }
        // 线程退出前关闭所有资源
        shutdown();
        log.info("MachineThread已退出，设备IP: {}", machine.getIp());
    }

    /**
     * 为指定逻辑项创建并启动子线程
     *
     * @param logicItem
     */
    private void startLogicThread(LogicItem logicItem) {
        String logicId = this.machine.getId() + " - " + this.machine.getName() + " - " + logicItem.getName();
        // 假设每个LogicItem有唯一ID
        if (logicThreads.containsKey(logicId) && logicThreads.get(logicId).isAlive()) {
            return;
        }
        // 设置运行标志
        logicRunningFlags.put(logicId, true);
        // 创建并启动线程
        executeTask(() -> {
            System.out.println("任务在线程池中执行: " + Thread.currentThread().getName());
            log.info("逻辑项线程启动: {}", logicId);
            // 逻辑项子线程持续运行的循环
            while (logicRunningFlags.getOrDefault(logicId, false)) {
                try {
                    // 执行实际业务逻辑
                    basicsLogic(logicItem);
                    // 根据逻辑项的执行频率设置等待时间，默认1000ms
                    Thread.sleep(logicItem.getLogicInterval());
                } catch (InterruptedException e) {
                    log.info("逻辑项线程被中断，准备退出: {}", logicId);
                    logicRunningFlags.put(logicId, false);
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    log.error("执行逻辑项失败: {}, 错误: {}", logicId, e.getMessage(), e);
                    // 等待一段时间再继续执行
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        log.info("逻辑项线程被中断，准备退出: {}", logicId);
                        logicRunningFlags.put(logicId, false);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
//        // 设置线程名称
//        thread.setName(logicId);
//        // 存储线程引用
//        logicThreads.put(logicId, thread);
//        // 启动线程
//        thread.start();
        //log.info("已启动逻辑项线程: {}", logicId);
    }

    /**
     * 检查逻辑线程状态，重启已终止的线程
     */
    private void checkLogicThreadsStatus() {
        for (LogicItem logicItem : logicConfig.getLogics()) {
            String logicId = this.machine.getId() + " - " + this.machine.getName() + " - " + logicItem.getName();
            Thread thread = logicThreads.get(logicId);
            boolean isFlags = logicRunningFlags.getOrDefault(logicId, false);
            // 如果线程不存在或已终止且运行标志为true，则重启线程
            boolean result = (thread == null || !thread.isAlive()) && !isFlags;
            if (result) {
                startLogicThread(logicItem);
            }
        }
    }

    /**
     * 尝试重新连接PLC
     */
    private void tryReconnect() {
        if (client != null) {
            try {
                client.disconnect();
                Thread.sleep(2000);
                client.connect();
                boolean reconnected = client.isConnected();
                if (reconnected) {
                    log.info("PLC重新连接成功: {}", machine.getIp());
                } else {
                    log.error("PLC重新连接失败: {}", machine.getIp());
                }
            } catch (Exception e) {
                log.error("重新连接PLC异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 关闭线程前的清理工作
     */
    public void shutdown() {
        running = false;
        // 停止所有逻辑线程
        for (String logicId : logicRunningFlags.keySet()) {
            logicRunningFlags.put(logicId, false);
            Thread thread = logicThreads.get(logicId);
            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }
        }
        // 等待所有逻辑线程结束
        for (String logicId : logicThreads.keySet()) {
            Thread thread = logicThreads.get(logicId);
            if (thread != null && thread.isAlive()) {
                try {
                    // 等待最多1秒
                    thread.join(1000);
                } catch (InterruptedException e) {
                    log.error("等待逻辑线程结束时被中断: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }

        // 清空线程映射
        logicThreads.clear();
        logicRunningFlags.clear();

        // 关闭PLC连接
        if (client != null) {
            try {
                client.disconnect();
                log.info("PLC连接已关闭: {}", machine.getIp());
            } catch (Exception e) {
                log.error("关闭PLC连接异常: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 示例   解读配置 根据接口返内容给PLC
     *
     * @param logicItem
     * @throws JSONException
     * @throws IOException
     */
    public void basicsLogic(LogicItem logicItem) throws JSONException, IOException {
        //1.读取PLC当前参数
        try {
            //遍历逻辑
            boolean isEqual = true;
            for (Logic logic : logicItem.getLogic()) {
                String plcValue = plcParameters.getMap().get(logic.getCodeId()).getReadValue().toString();
                if (!logic.getValue().contains(plcValue)) {
                    isEqual = false;
                    logic.setEquals(false);
                    logicItem.setEquals(false);
                    break;
                }
            }
            if (isEqual) {
                log.info("满足条件:{}", logicItem.getName());
                //3.查询此逻辑下需要返回给PLC的数据 result可接收  HTTP接口,视图,存储过程 等  如下
                List<String> resultWrite = null;
                if (logicItem.getApiConfigBefore() != null) {
                    ApiConfig apiConfig = logicItem.getApiConfigBefore();
                    resultWrite = api.callApi(apiConfig, plcParameters);
                    log.info("ApiConfigBefore  外置接口[{}:{}]返回的内容:{}", apiConfig.getType(), apiConfig.getAddress(), resultWrite);
                }
                //写入PLC返回值成功时调用接口
                if (basicsResult(resultWrite, logicItem.getReturnValue())) {
                    ApiConfig apiConfig = logicItem.getApiConfigAfter();
                    if (logicItem.getApiConfigAfter() != null) {
                        List<String> result = api.callApi(apiConfig, plcParameters);
                        log.info("ApiConfigAfter  外置接口[{}:{}]返回的内容:{}", apiConfig.getType(), apiConfig.getAddress(), result);
                    }
                }


            }
        } catch (Exception e) {
            log.error("执行basicsLogic失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 传入需要发送的数据
     *
     * @param sendData
     * @param returnValue
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public boolean basicsResult(List<String> sendData, List<ReturnValue> returnValue) throws JSONException, IOException {
        for (ReturnValue itemReturnValue : returnValue) {
            //需要返回PLC的值
            String values = "";
            if (itemReturnValue.isFixed()) {
                //固定值
                values = itemReturnValue.getValue();
            } else {
                //按传递的参数值
                values = sendData.get(Integer.valueOf(itemReturnValue.getValue()) - 1);
            }
            if (values != null && !"".equals(values)) {
                //还需增添  不同类型调用不同方法
                switch (itemReturnValue.getPlcDataType()) {
                    case "Word":
                        client.writeRegister(itemReturnValue.getAddress(), (int) Double.parseDouble(values));
                        break;
                    case "String":
                        client.writeString(itemReturnValue.getAddress(), values);
                        break;
                    case "Float":
                        client.writeFloat(itemReturnValue.getAddress(), Float.valueOf(values));
                        break;
                    case "bit":
                        client.writeBit(itemReturnValue.getAddress(), Boolean.valueOf(values));
                        break;
                    default:
                        log.error("不支持的数据类型: {}", itemReturnValue.getPlcDataType());
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * 按照json文件读取plc内容
     *
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private PlcParameters readPlcParameter() throws IOException, JSONException {
        List<Parameters> parametersList = plcParameters.getParameters();
        for (int i = 0; i < parametersList.size(); i++) {
            //根据类型读取
            switch (parametersList.get(i).getPlcDataType()) {
                case "Word":
                    int value = client.readRegister(parametersList.get(i).getAddress());
                    parametersList.get(i).setReadValue(value);
                    break;
                case "string":
                    //多寄存器    传递数量
                    String strValue = client.readString(parametersList.get(i).getAddress(), 2);
                    parametersList.get(i).setReadValue(strValue);
                    break;
                case "bit":
                    //读线圈
                    boolean bitValue = client.readBit(parametersList.get(i).getAddress());
                    parametersList.get(i).setReadValue(bitValue);
                    break;
                default:
                    log.error("不支持的数据类型: {}", parametersList.get(i).getPlcDataType());
                    return null;
            }
        }
        plcParameters.setParameters(parametersList);
        return plcParameters;
    }
}