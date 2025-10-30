package com.mes;

import com.mes.connect.industrialinterface.ApiImpl;
import com.mes.connect.thread.MachineThread;
import com.mes.model.entity.Machine;
import com.mes.model.service.MachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * AppRunnerConfig 启动
 *
 * @author yzx
 * @version 1.0
 */
@Slf4j
@Component
@Order(1)
public class AppRunnerConfig implements ApplicationRunner {

    @Resource
    ApiImpl api;
    @Autowired
    MachineService machineService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO Auto-generated method stub
//        //1.根据数据库设备表加载数据  得到全部 设备信息
//        List<Machine> listMachine = machineService.getMachineConfig();
//        //2.根据设备配置进行加载多线程
//        for (int i = 0; i < listMachine.size(); i++) {
//            if ("ON".equals(listMachine.get(i).getOpen())) {
//                try {
//                    Machine machine = listMachine.get(i);
//                    Thread thread = new Thread(new MachineThread(machine, api));
//                    thread.start();
//                    thread.setName(machine.getName());
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    System.out.println("多线程异常！！");
//                }
//            }
//        }
    }
}