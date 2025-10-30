package com.mes.connect.modbus;

import com.mes.connect.industrialinterface.AddressParser;
import com.mes.connect.industrialinterface.IndustrialDataHandler;
import com.mes.connect.industrialinterface.IndustrialServer;
import com.mes.connect.addressparser.ModbusIpAddressParser;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Modbus IP服务器实现（基于UDP）
 *
 * @author yzx
 * @version 1.0
 */
public class ModbusIpServer implements IndustrialServer {
    private static final Logger logger = Logger.getLogger(ModbusIpServer.class.getName());
    private DatagramSocket serverSocket;
    private Thread serverThread;
    private boolean running;
    private IndustrialDataHandler handler;
    private final int port;
    private int timeOut = 10;
    private final ExecutorService threadPool;
    private final AddressParser addressParser = new ModbusIpAddressParser();

    public ModbusIpServer(int port) {
        this.port = port;
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
    }

    public void executeTask(Runnable task) {
        threadPool.execute(task);
    }

    /**
     * 应用关闭时调用
     */
    public void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(timeOut, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    @Override
    public synchronized void start() throws IOException {
        if (!running) {
            serverSocket = new DatagramSocket(port);
            threadPool.submit(this::serverLoop);
            //serverThread = new Thread(this::serverLoop); 显示创建线程
            serverThread.setDaemon(true);
            serverThread.start();
            running = true;
            logger.info("Modbus IP (UDP) server started on port " + port);
        }
    }

    @Override
    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                if (serverThread != null) {
                    serverThread.interrupt();
                    serverThread.join(1000);
                }
                threadPool.shutdownNow();
            } catch (InterruptedException e) {
                logger.warning("Error stopping Modbus IP server: " + e.getMessage());
            } finally {
                serverSocket = null;
                serverThread = null;
                logger.info("Modbus IP (UDP) server stopped");
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setDataHandler(IndustrialDataHandler handler) {
        this.handler = handler;
    }

    @Override
    public void close() throws IOException {
        stop();
    }

    private void serverLoop() {
        byte[] receiveBuffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                threadPool.submit(() -> handleRequest(receivePacket));
            } catch (IOException e) {
                if (running) {
                    logger.warning("Error receiving packet: " + e.getMessage());
                }
            }
        }
    }

    private void handleRequest(DatagramPacket requestPacket) {
        byte[] requestData = new byte[requestPacket.getLength()];
        System.arraycopy(requestPacket.getData(), requestPacket.getOffset(), requestData, 0, requestPacket.getLength());

        try {
            byte[] responseData = processRequest(requestData);

            DatagramPacket responsePacket = new DatagramPacket(
                    responseData,
                    responseData.length,
                    requestPacket.getAddress(),
                    requestPacket.getPort()
            );

            serverSocket.send(responsePacket);
        } catch (Exception e) {
            logger.warning("Error processing request: " + e.getMessage());
        }
    }

    private byte[] processRequest(byte[] request) {
        int transactionId = ((request[0] & 0xFF) << 8) | (request[1] & 0xFF);
        int unitId = request[6] & 0xFF;
        int functionCode = request[7] & 0xFF;

        try {
            switch (functionCode) {

                case 1:
                    // 读线圈
                    return handleReadCoils(request, transactionId, unitId);
                case 2:
                    // 读离散输入
                    return handleReadDiscreteInputs(request, transactionId, unitId);
                case 3:
                    // 读保持寄存器
                    return handleReadHoldingRegisters(request, transactionId, unitId);
                case 4:
                    // 读输入寄存器
                    return handleReadInputRegisters(request, transactionId, unitId);
                case 5:
                    // 写单个线圈
                    return handleWriteSingleCoil(request, transactionId, unitId);
                case 6:
                    // 写单个寄存器
                    return handleWriteSingleRegister(request, transactionId, unitId);
                case 15:
                    // 写多个线圈
                    return handleWriteMultipleCoils(request, transactionId, unitId);
                case 16:
                    // 写多个寄存器
                    return handleWriteMultipleRegisters(request, transactionId, unitId);
                default:
                    // 不支持的功能码
                    return buildExceptionResponse(transactionId, unitId, functionCode, 1);
            }
        } catch (Exception e) {
            logger.warning("Error processing request: " + e.getMessage());
            // 服务器设备故障
            return buildExceptionResponse(transactionId, unitId, functionCode, 4);
        }
    }

    private byte[] handleReadCoils(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 1, 3);
        }

        byte[] response = new byte[9 + (quantity + 7) / 8];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = (byte) (response.length - 6);
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 1;
        // 字节数
        response[8] = (byte) ((quantity + 7) / 8);

        // 填充线圈数据
        for (int i = 0; i < quantity; i++) {
            String addressStr = String.format("MB.0x%04d.%d", address + i, 0);
            boolean value = handler.handleReadBit(addressStr);
            int byteIndex = 9 + i / 8;
            int bitIndex = i % 8;
            if (value) {
                response[byteIndex] |= (1 << bitIndex);
            }
        }

        return response;
    }

    private byte[] handleReadDiscreteInputs(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 2, 3);
        }

        byte[] response = new byte[9 + (quantity + 7) / 8];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = (byte) (response.length - 6);
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 2;
        // 字节数
        response[8] = (byte) ((quantity + 7) / 8);

        // 填充离散输入数据
        for (int i = 0; i < quantity; i++) {
            String addressStr = String.format("MB.1x%04d.%d", address + i, 0);
            boolean value = handler.handleReadBit(addressStr);
            int byteIndex = 9 + i / 8;
            int bitIndex = i % 8;
            if (value) {
                response[byteIndex] |= (1 << bitIndex);
            }
        }

        return response;
    }

    private byte[] handleReadInputRegisters(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 4, 3);
        }

        byte[] response = new byte[9 + quantity * 2];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = (byte) (response.length - 6);
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 4;
        // 字节数
        response[8] = (byte) (quantity * 2);

        // 填充输入寄存器数据
        for (int i = 0; i < quantity; i++) {
            String addressStr = String.format("MB.3x%04d", address + i);
            int value = handler.handleReadRegister(addressStr);
            int byteIndex = 9 + i * 2;
            response[byteIndex] = (byte) (value >> 8);
            response[byteIndex + 1] = (byte) value;
        }

        return response;
    }

    private byte[] handleReadHoldingRegisters(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 3, 3);
        }

        byte[] response = new byte[9 + quantity * 2];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = (byte) (response.length - 6);
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 3;
        // 字节数
        response[8] = (byte) (quantity * 2);

        // 填充保持寄存器数据
        for (int i = 0; i < quantity; i++) {
            String addressStr = String.format("MB.4x%04d", address + i);
            int value = handler.handleReadRegister(addressStr);
            int byteIndex = 9 + i * 2;
            response[byteIndex] = (byte) (value >> 8);
            response[byteIndex + 1] = (byte) value;
        }

        return response;
    }

    private byte[] handleWriteSingleCoil(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int value = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);

        boolean coilValue = (value == 0xFF00);
        String addressStr = String.format("MB.0x%04d.%d", address, 0);

        handler.handleWriteBit(addressStr, coilValue);

        // 返回与请求相同的数据作为确认
        byte[] response = new byte[12];
        System.arraycopy(request, 0, response, 0, 12);
        return response;
    }

    private byte[] handleWriteSingleRegister(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int value = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);

        String addressStr = String.format("MB.4x%04d", address);
        handler.handleWriteRegister(addressStr, value);

        // 返回与请求相同的数据作为确认
        byte[] response = new byte[12];
        System.arraycopy(request, 0, response, 0, 12);
        return response;
    }

    private byte[] handleWriteMultipleCoils(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        int byteCount = request[12] & 0xFF;
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 15, 3);
        }

        // 处理线圈值
        for (int i = 0; i < quantity; i++) {
            int byteIndex = 13 + i / 8;
            int bitIndex = i % 8;
            boolean value = ((request[byteIndex] & (1 << bitIndex)) != 0);
            String addressStr = String.format("MB.0x%04d.%d", address + i, 0);
            handler.handleWriteBit(addressStr, value);
        }

        // 构建响应
        byte[] response = new byte[12];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = 6;
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 15;
        // 起始地址高字节
        response[8] = (byte) (address >> 8);
        // 起始地址低字节
        response[9] = (byte) address;
        // 数量高字节
        response[10] = (byte) (quantity >> 8);
        // 数量低字节
        response[11] = (byte) quantity;

        return response;
    }

    private byte[] handleWriteMultipleRegisters(byte[] request, int transactionId, int unitId) {
        int address = ((request[8] & 0xFF) << 8) | (request[9] & 0xFF);
        int quantity = ((request[10] & 0xFF) << 8) | (request[11] & 0xFF);
        int byteCount = request[12] & 0xFF;
        //数据 超过2000 抛异常
        if (quantity > 2000) {
            // 请求数量过大
            return buildExceptionResponse(transactionId, unitId, 16, 3);
        }

        int[] values = new int[quantity];

        // 解析寄存器值
        for (int i = 0; i < quantity; i++) {
            int byteIndex = 13 + i * 2;
            values[i] = ((request[byteIndex] & 0xFF) << 8) | (request[byteIndex + 1] & 0xFF);
        }

        // 处理写入请求
        String addressStr = String.format("MB.4x%04d", address);
        handler.handleWriteRegisters(addressStr, values);

        // 构建响应
        byte[] response = new byte[12];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = 6;
        response[6] = (byte) unitId;
        // 功能码
        response[7] = 16;
        // 起始地址高字节
        response[8] = (byte) (address >> 8);
        // 起始地址低字节
        response[9] = (byte) address;
        // 数量高字节
        response[10] = (byte) (quantity >> 8);
        // 数量低字节
        response[11] = (byte) quantity;

        return response;
    }

    private byte[] buildExceptionResponse(int transactionId, int unitId, int functionCode, int exceptionCode) {
        byte[] response = new byte[9];
        response[0] = (byte) (transactionId >> 8);
        response[1] = (byte) transactionId;
        // 协议标识符高字节
        response[2] = 0;
        // 协议标识符低字节
        response[3] = 0;
        // 长度高字节
        response[4] = 0;
        // 长度低字节
        response[5] = 3;
        response[6] = (byte) unitId;
        // 异常功能码
        response[7] = (byte) (functionCode + 0x80);
        // 异常码
        response[8] = (byte) exceptionCode;
        return response;
    }
}    