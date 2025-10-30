// PLC测试工具类 - 用于模拟PLC设备行为和提供测试场景
class PlcTestUtil {
  constructor() {
    // PLC状态数据
    this.plcState = {
      gantryStorage: {
        onlineStatus: true,
        plcRequest: 0,
        plcReport: 0,
        complete1A: false,
        complete1B: false,
        taskStatus: 0,
        shuttlePosition: 1,
        suctionPosition: 0,
        mesSend: 0,
        mesConfirm: 0,
        start: 1,
        target: 5,
        glassCount: 0,
        edge1Margin: 0,
        edge1Long: 0,
        edge1Short: 0,
        edge1Thick: 0,
        edge2Margin: 0,
        edge2Long: 0,
        edge2Short: 0,
        edge2Thick: 0,
        alarm: 0
      },
      upperModule: {
        onlineStatus: true,
        plcRequest: 0,
        plcReport: 0,
        complete1A: false,
        complete1B: false,
        taskStatus: 0,
        mesSend: 0,
        mesConfirm: 0,
        start: 1,
        target: 5,
        glassCount: 0,
        alarm: 0
      },
      storageModule: {
        onlineStatus: true,
        plcRequest: 0,
        plcReport: 0,
        taskStatus: 0,
        shuttlePosition: 1,
        suctionPosition: 0,
        mesSend: 0,
        mesConfirm: 0,
        start: 1,
        target: 5,
        alarm: 0
      }
    };

    // 测试场景配置
    this.testScenarios = {
      normal: 'normal',           // 正常执行
      timeout: 'timeout',         // 超时测试
      communicationError: 'communicationError', // 通信错误
      partialDataLoss: 'partialDataLoss',       // 部分数据丢失
      incorrectData: 'incorrectData',           // 数据错误
      alarmTrigger: 'alarmTrigger'              // 触发报警
    };

    // 任务执行模拟器
    this.taskSimulator = {
      runningTasks: {},
      intervals: {}
    };
  }

  /**
   * 获取模块状态
   * @param {string} module - 模块名称
   * @returns {object} 模块状态对象
   */
  getModuleStatus(module) {
    if (!this.plcState[module]) {
      throw new Error(`不支持的模块: ${module}`);
    }
    return { ...this.plcState[module] };
  }

  /**
   * 重置模块状态
   * @param {string} module - 模块名称
   */
  resetModuleStatus(module) {
    if (!this.plcState[module]) {
      throw new Error(`不支持的模块: ${module}`);
    }
    
    // 停止该模块的所有运行中任务
    this.stopAllTasks(module);
    
    // 重置状态
    const initialState = this.getInitialState(module);
    this.plcState[module] = { ...initialState };
  }

  /**
   * 执行PLC任务
   * @param {string} module - 模块名称
   * @param {object} taskData - 任务数据
   * @param {string} scenario - 测试场景类型
   * @param {function} callback - 回调函数
   * @returns {string} 任务ID
   */
  executeTask(module, taskData, scenario = this.testScenarios.normal, callback) {
    if (!this.plcState[module]) {
      throw new Error(`不支持的模块: ${module}`);
    }

    // 生成任务ID
    const taskId = `task_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
    
    // 更新PLC状态为任务开始
    this.updatePlcState(module, {
      mesSend: 1,
      taskStatus: 1, // 任务进行中
      ...taskData
    });

    // 存储任务信息
    this.taskSimulator.runningTasks[taskId] = {
      module,
      taskData,
      scenario,
      startTime: Date.now(),
      status: 'running'
    };

    // 根据场景模拟任务执行
    this.simulateTaskExecution(taskId, callback);

    return taskId;
  }

  /**
   * 模拟任务执行
   * @param {string} taskId - 任务ID
   * @param {function} callback - 回调函数
   */
  simulateTaskExecution(taskId, callback) {
    const task = this.taskSimulator.runningTasks[taskId];
    if (!task) return;

    const { module, scenario } = task;
    
    // 根据场景设置不同的执行时间和行为
    let executionTime = 2000; // 默认2秒
    let success = true;
    let errorMessage = '';

    switch (scenario) {
      case this.testScenarios.normal:
        executionTime = 2000 + Math.random() * 3000; // 2-5秒随机
        break;
      case this.testScenarios.timeout:
        executionTime = 10000; // 10秒超时
        break;
      case this.testScenarios.communicationError:
        executionTime = 1000;
        success = false;
        errorMessage = 'PLC通信错误';
        break;
      case this.testScenarios.partialDataLoss:
        executionTime = 3000;
        // 模拟部分数据丢失
        this.updatePlcState(module, { plcReport: Math.floor(Math.random() * 100) });
        break;
      case this.testScenarios.incorrectData:
        executionTime = 3000;
        // 模拟数据错误
        this.updatePlcState(module, { 
          shuttlePosition: 999, 
          alarm: 1 
        });
        break;
      case this.testScenarios.alarmTrigger:
        executionTime = 1500;
        // 触发报警
        this.updatePlcState(module, { alarm: 1 });
        success = false;
        errorMessage = '设备报警触发';
        break;
      default:
        break;
    }

    // 设置定时器模拟任务执行
    this.taskSimulator.intervals[taskId] = setTimeout(() => {
      if (success) {
        // 任务成功完成
        this.updatePlcState(module, {
          taskStatus: 2, // 任务完成
          plcReport: 1,
          mesConfirm: 1,
          complete1A: true,
          complete1B: true,
          shuttlePosition: task.taskData.target || 5
        });
        
        task.status = 'completed';
        task.endTime = Date.now();
        
        if (callback) {
          callback(null, {
            success: true,
            taskId,
            module,
            duration: Math.round((task.endTime - task.startTime) / 1000)
          });
        }
      } else {
        // 任务失败
        this.updatePlcState(module, {
          taskStatus: 3, // 任务失败
          alarm: 1
        });
        
        task.status = 'failed';
        task.endTime = Date.now();
        task.error = errorMessage;
        
        if (callback) {
          callback(new Error(errorMessage), {
            success: false,
            taskId,
            module,
            error: errorMessage
          });
        }
      }
      
      // 清理定时器
      delete this.taskSimulator.intervals[taskId];
    }, executionTime);
  }

  /**
   * 停止特定任务
   * @param {string} taskId - 任务ID
   */
  stopTask(taskId) {
    if (this.taskSimulator.intervals[taskId]) {
      clearTimeout(this.taskSimulator.intervals[taskId]);
      delete this.taskSimulator.intervals[taskId];
    }
    
    if (this.taskSimulator.runningTasks[taskId]) {
      const task = this.taskSimulator.runningTasks[taskId];
      // 更新状态为已停止
      this.updatePlcState(task.module, {
        taskStatus: 0,
        mesSend: 0
      });
      
      task.status = 'stopped';
      task.endTime = Date.now();
    }
  }

  /**
   * 停止模块的所有任务
   * @param {string} module - 模块名称
   */
  stopAllTasks(module) {
    Object.keys(this.taskSimulator.runningTasks).forEach(taskId => {
      if (this.taskSimulator.runningTasks[taskId].module === module) {
        this.stopTask(taskId);
      }
    });
  }

  /**
   * 更新PLC状态
   * @param {string} module - 模块名称
   * @param {object} updates - 更新的字段
   */
  updatePlcState(module, updates) {
    if (!this.plcState[module]) {
      throw new Error(`不支持的模块: ${module}`);
    }
    
    this.plcState[module] = {
      ...this.plcState[module],
      ...updates
    };
  }

  /**
   * 获取模块的初始状态
   * @param {string} module - 模块名称
   * @returns {object} 初始状态
   */
  getInitialState(module) {
    switch (module) {
      case 'gantryStorage':
        return {
          onlineStatus: true,
          plcRequest: 0,
          plcReport: 0,
          complete1A: false,
          complete1B: false,
          taskStatus: 0,
          shuttlePosition: 1,
          suctionPosition: 0,
          mesSend: 0,
          mesConfirm: 0,
          start: 1,
          target: 5,
          glassCount: 0,
          edge1Margin: 0,
          edge1Long: 0,
          edge1Short: 0,
          edge1Thick: 0,
          edge2Margin: 0,
          edge2Long: 0,
          edge2Short: 0,
          edge2Thick: 0,
          alarm: 0
        };
      case 'upperModule':
        return {
          onlineStatus: true,
          plcRequest: 0,
          plcReport: 0,
          complete1A: false,
          complete1B: false,
          taskStatus: 0,
          mesSend: 0,
          mesConfirm: 0,
          start: 1,
          target: 5,
          glassCount: 0,
          alarm: 0
        };
      case 'storageModule':
        return {
          onlineStatus: true,
          plcRequest: 0,
          plcReport: 0,
          taskStatus: 0,
          shuttlePosition: 1,
          suctionPosition: 0,
          mesSend: 0,
          mesConfirm: 0,
          start: 1,
          target: 5,
          alarm: 0
        };
      default:
        return {};
    }
  }

  /**
   * 生成模拟测试数据
   * @param {string} module - 模块名称
   * @param {number} count - 数据条目数
   * @returns {array} 测试数据数组
   */
  generateTestData(module, count = 10) {
    const data = [];
    const moduleMap = {
      'gantryStorage': '龙门仓储',
      'upperModule': '上片模块',
      'storageModule': '穿梭仓储'
    };

    for (let i = 0; i < count; i++) {
      const start = Math.floor(Math.random() * 10) + 1;
      const target = Math.floor(Math.random() * 10) + 1;
      const duration = Math.floor(Math.random() * 10) + 1;
      const status = Math.random() > 0.2 ? '通过' : '失败';
      const startTime = new Date(Date.now() - i * 60000).toLocaleString();
      const endTime = new Date(Date.now() - (i - duration) * 60000).toLocaleString();

      data.push({
        id: `test_${Date.now()}_${i}`,
        module: moduleMap[module],
        moduleKey: module,
        startTime,
        endTime,
        status,
        duration,
        retryCount: 0,
        errorMsg: status === '失败' ? '模拟随机故障' : '',
        timeline: [
          { time: startTime.split(' ')[1], status: 'primary', desc: '测试任务启动' },
          { time: new Date(Date.now() - (i - 0.5) * 60000).toLocaleTimeString(), status: 'primary', desc: '开始初始化测试环境' },
          { time: new Date(Date.now() - (i - 1) * 60000).toLocaleTimeString(), status: status === '通过' ? 'success' : 'error', desc: status === '通过' ? '任务执行完成' : '任务执行失败' }
        ]
      });
    }

    return data;
  }

  /**
   * 模拟网络延迟
   * @param {number} minDelay - 最小延迟(ms)
   * @param {number} maxDelay - 最大延迟(ms)
   * @returns {Promise} 延迟Promise
   */
  simulateNetworkDelay(minDelay = 100, maxDelay = 1000) {
    const delay = minDelay + Math.random() * (maxDelay - minDelay);
    return new Promise(resolve => setTimeout(resolve, delay));
  }
}

// 导出单例实例
export default new PlcTestUtil();