// 测试配置管理 - 集中管理测试环境的配置参数
const TestConfig = {
  // API配置
  api: {
    baseUrl: process.env.VUE_APP_BASE_API || '/api',
    plcOperation: {
      execute: '/plc/operation/execute',
      status: '/plc/operation/status',
      reset: '/plc/operation/reset'
    },
    timeout: 30000, // API请求超时时间(ms)
    retryCount: 3 // API请求重试次数
  },

  // 测试环境配置
  environment: {
    mockEnabled: true, // 是否启用模拟数据
    debugMode: false, // 是否启用调试模式
    logLevel: 'info', // 日志级别: error, warn, info, debug
    simulationSpeed: 1.0 // 模拟速度倍率(1.0 = 正常速度)
  },

  // PLC模块配置
  plcModules: {
    gantryStorage: {
      name: '龙门仓储',
      description: '龙门式仓储模块，用于玻璃的存取管理',
      maxPositions: 50,
      defaultFields: [
        'onlineStatus', 'plcRequest', 'plcReport', 'taskStatus',
        'shuttlePosition', 'suctionPosition', 'mesSend', 'mesConfirm',
        'start', 'target', 'alarm'
      ]
    },
    upperModule: {
      name: '上片模块',
      description: '上片模块，负责将玻璃装载到生产线',
      maxPositions: 20,
      defaultFields: [
        'onlineStatus', 'plcRequest', 'plcReport', 'taskStatus',
        'mesSend', 'mesConfirm', 'start', 'target', 'alarm',
        'complete1A', 'complete1B', 'glassCount'
      ]
    },
    storageModule: {
      name: '穿梭仓储',
      description: '穿梭车仓储模块，用于物料的高效存储',
      maxPositions: 100,
      defaultFields: [
        'onlineStatus', 'plcRequest', 'plcReport', 'taskStatus',
        'shuttlePosition', 'suctionPosition', 'mesSend', 'mesConfirm',
        'start', 'target', 'alarm'
      ]
    }
  },

  // 测试场景配置
  testScenarios: {
    normal: {
      name: '正常场景',
      description: '模拟正常的PLC操作流程',
      probability: 0.6,
      executionTime: { min: 1000, max: 3000 }
    },
    timeout: {
      name: '超时场景',
      description: '模拟PLC响应超时的情况',
      probability: 0.1,
      executionTime: { min: 8000, max: 12000 }
    },
    communicationError: {
      name: '通信错误',
      description: '模拟通信中断的情况',
      probability: 0.15,
      executionTime: { min: 500, max: 1500 }
    },
    dataError: {
      name: '数据错误',
      description: '模拟数据传输错误的情况',
      probability: 0.1,
      executionTime: { min: 1000, max: 2000 }
    },
    alarmTrigger: {
      name: '报警触发',
      description: '模拟设备报警的情况',
      probability: 0.05,
      executionTime: { min: 500, max: 1000 }
    }
  },

  // 界面配置
  ui: {
    theme: 'default', // 默认主题
    refreshInterval: 5000, // 自动刷新间隔(ms)
    maxDisplayTasks: 50, // 最大显示任务数
    autoScroll: true, // 是否自动滚动到最新任务
    toastDuration: 3000 // 提示消息持续时间(ms)
  },

  // 批量测试配置
  batchTest: {
    maxConcurrentTasks: 5, // 最大并发任务数
    defaultInterval: 1000, // 默认任务间隔(ms)
    saveHistory: true, // 是否保存历史记录
    exportFormats: ['excel', 'json', 'csv'] // 支持的导出格式
  },

  // 日志配置
  logging: {
    enabled: true,
    saveToLocal: true,
    maxLogEntries: 1000,
    logLevels: ['error', 'warn', 'info', 'debug'],
    logModules: ['api', 'ui', 'plc', 'system']
  },

  /**
   * 获取API完整路径
   * @param {string} endpoint - API端点
   * @returns {string} 完整的API路径
   */
  getApiUrl(endpoint) {
    return `${this.api.baseUrl}${endpoint}`;
  },

  /**
   * 获取PLC模块配置
   * @param {string} moduleKey - 模块键名
   * @returns {object|null} 模块配置
   */
  getModuleConfig(moduleKey) {
    return this.plcModules[moduleKey] || null;
  },

  /**
   * 获取测试场景配置
   * @param {string} scenarioKey - 场景键名
   * @returns {object|null} 场景配置
   */
  getScenarioConfig(scenarioKey) {
    return this.testScenarios[scenarioKey] || null;
  },

  /**
   * 更新配置
   * @param {object} newConfig - 新的配置对象
   */
  updateConfig(newConfig) {
    Object.assign(this, newConfig);
    this.saveToLocalStorage();
  },

  /**
   * 从本地存储加载配置
   */
  loadFromLocalStorage() {
    try {
      const savedConfig = localStorage.getItem('testConfig');
      if (savedConfig) {
        const parsedConfig = JSON.parse(savedConfig);
        Object.assign(this, parsedConfig);
        return true;
      }
    } catch (error) {
      console.error('加载配置失败:', error);
    }
    return false;
  },

  /**
   * 保存配置到本地存储
   */
  saveToLocalStorage() {
    try {
      localStorage.setItem('testConfig', JSON.stringify(this));
      return true;
    } catch (error) {
      console.error('保存配置失败:', error);
      return false;
    }
  },

  /**
   * 重置配置为默认值
   */
  resetToDefaults() {
    const defaultConfig = JSON.parse(JSON.stringify(this.constructor.defaultConfig));
    Object.assign(this, defaultConfig);
    this.saveToLocalStorage();
  }
};

// 存储默认配置，用于重置
TestConfig.constructor.defaultConfig = JSON.parse(JSON.stringify(TestConfig));

// 自动从本地存储加载配置
TestConfig.loadFromLocalStorage();

export default TestConfig;