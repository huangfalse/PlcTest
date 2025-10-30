<template>
  <div class="auto-test-dashboard">
    <!-- 顶部导航区 -->
    <el-header class="header">
      <h1>MES自动化测试平台</h1>
      <div class="header-actions">
        <el-button type="primary" @click="showModuleStatus">查看模块状态</el-button>
        <el-button type="primary" @click="importTask">导入测试任务</el-button>
        <el-button type="success" @click="startTest" :disabled="!isConfigReady">开始测试</el-button>
        <el-button type="warning" @click="batchTest" :disabled="batchTasks.length === 0">批量测试</el-button>
        <el-button @click="resetAll">重置配置</el-button>
      </div>
    </el-header>

    <!-- 主体内容区 -->
    <el-container>
      <!-- 左侧：模块配置与字段选择 -->
      <el-aside width="320px" class="aside">
        <el-card class="config-card">
          <h3 class="section-title">模块配置</h3>
          <el-select v-model="selectedModule" @change="handleModuleChange" placeholder="选择测试模块" style="width: 100%">
            <el-option label="龙门仓储" value="gantryStorage"></el-option>
            <el-option label="上片模块" value="upperModule"></el-option>
            <el-option label="穿梭仓储" value="storageModule"></el-option>
          </el-select>

          <h3 class="section-title">协议字段选择</h3>
          <el-checkbox-group v-model="checkedFields" class="field-checkbox-group">
            <el-checkbox v-for="field in allFields" :key="field.value" :label="field.value"
              :disabled="isSystemField(field.value)">
              {{ field.label }}
            </el-checkbox>
          </el-checkbox-group>

          <h3 class="section-title">测试参数配置</h3>
          <el-form :model="testParameters" label-width="120px" class="parameter-form">
            <el-form-item label="PlC地址">
              <el-input v-model="testParameters.plcAddress" :min="0" :max="100" size="small"></el-input>
            </el-form-item>
            <el-form-item label="DB块">
              <el-input v-model="testParameters.dbAddress" :min="0" :max="100" size="small"></el-input>
            </el-form-item>
            <el-form-item label="任务重试次数">
              <el-input-number v-model="testParameters.retryCount" :min="0" :max="10" size="small"></el-input-number>
            </el-form-item>
            <el-form-item label="测试间隔(ms)">
              <el-input-number v-model="testParameters.interval" :min="100" :max="10000" size="small"></el-input-number>
            </el-form-item>
            <el-form-item label="模拟故障概率">
              <el-slider v-model="testParameters.failureRate" :min="0" :max="100" show-stops></el-slider>
            </el-form-item>
          </el-form>

          <div class="test-actions">
            <el-button type="primary" size="small" @click="saveCurrentConfig">保存配置</el-button>
            <el-button size="small" @click="loadConfig">加载配置</el-button>
          </div>
        </el-card>
      </el-aside>

      <!-- 右侧：任务监控与结果展示 -->
      <el-main class="main">
        <!-- 批量测试配置 -->
        <el-card class="batch-test-card" v-if="showBatchTestConfig">
          <h3 class="section-title">批量测试配置</h3>
          <el-table :data="batchTasks" style="width: 100%">
            <el-table-column prop="module" label="模块" width="120"></el-table-column>
            <el-table-column prop="start" label="起始位置" width="100"></el-table-column>
            <el-table-column prop="target" label="目标位置" width="100"></el-table-column>
            <el-table-column prop="retryCount" label="重试次数" width="100"></el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="scope">
                <el-button type="danger" size="mini" @click="removeBatchTask(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="batch-actions">
            <el-button type="primary" size="small" @click="addCurrentToBatch">添加当前配置</el-button>
            <el-button type="success" size="small" @click="startBatchTest">执行批量测试</el-button>
          </div>
        </el-card>

        <!-- 任务流转时间线 -->
        <div class="timeline-section">
          <h3 class="section-title">任务流转监测</h3>
          <div class="timeline-container" v-if="runningTasks.length > 0">
            <el-timeline v-for="(task, index) in runningTasks" :key="index" class="task-timeline">
              <el-timeline-item v-for="(event, eventIndex) in task.timeline" :key="eventIndex" :timestamp="event.time"
                :type="event.status" :color="getStatusColor(event.status)" :icon="getEventIcon(event.status)">
                {{ event.desc }}
              </el-timeline-item>
            </el-timeline>
          </div>
          <div class="empty-state" v-else>
            <el-empty description="暂无运行中的任务" />
          </div>
        </div>

        <!-- 测试结果统计 -->
        <div class="result-section mt-20">
          <h3 class="section-title">测试结果统计</h3>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="result-card">
                <div class="result-item">
                  <span class="label">总任务数</span>
                  <span class="value">{{ taskCount.total }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="result-card">
                <div class="result-item">
                  <span class="label">通过数</span>
                  <span class="value success">{{ taskCount.pass }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="result-card">
                <div class="result-item">
                  <span class="label">失败数</span>
                  <span class="value error">{{ taskCount.fail }}</span>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="result-card">
                <div class="result-item">
                  <span class="label">通过率</span>
                  <span class="value"
                    :class="{ success: taskCount.total > 0 && taskCount.pass / taskCount.total > 0.9, error: taskCount.total > 0 && taskCount.pass / taskCount.total < 0.5 }">
                    {{ calculatePassRate() }}%
                  </span>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <div class="result-table-container">
            <el-table :data="testResults" style="width: 100%" border>
              <el-table-column prop="module" label="模块"></el-table-column>
              <el-table-column prop="startTime" label="开始时间"></el-table-column>
              <el-table-column prop="endTime" label="结束时间"></el-table-column>
              <el-table-column prop="status" label="状态" width="80">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '通过' ? 'success' : 'error'" size="small">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="duration" label="耗时(秒)"></el-table-column>
              <el-table-column prop="retryCount" label="重试次数"></el-table-column>
              <el-table-column prop="errorMsg" label="错误信息" v-if="showErrorColumn"
                :show-overflow-tooltip="true"></el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="text" size="small" @click="viewTaskDetail(scope.row)">查看详情</el-button>
                  <el-button type="text" size="small" @click="retryTask(scope.row)">重试</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-main>
    </el-container>

    <!-- 导入任务弹窗 -->
    <el-dialog title="导入测试任务" v-model="importDialogVisible" width="50%">
      <el-upload class="upload-demo" action="#" :auto-upload="false" :on-change="handleFileChange"
        :before-upload="beforeFileUpload" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">拖拽文件到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">仅支持Excel/JSON格式，大小不超过5MB</div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="importDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmImport">确认导入</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 模块状态弹窗 -->
    <el-dialog title="PLC模块状态" v-model="statusDialogVisible" width="70%">
      <div class="status-container">
        <el-tabs v-model="activeStatusTab" @tab-click="handleStatusTabClick">
          <el-tab-pane label="龙门仓储" name="gantryStorage"></el-tab-pane>
          <el-tab-pane label="上片模块" name="upperModule"></el-tab-pane>
          <el-tab-pane label="穿梭仓储" name="storageModule"></el-tab-pane>
        </el-tabs>
        <el-table :data="statusTableData" style="width: 100%" border v-if="statusTableData.length > 0">
          <el-table-column prop="key" label="参数名称"></el-table-column>
          <el-table-column prop="value" label="当前值"></el-table-column>
          <el-table-column prop="updateTime" label="更新时间"></el-table-column>
        </el-table>
        <div v-else class="empty-status">
          <el-empty description="暂无状态数据" />
        </div>
        <div class="status-actions">
          <el-button type="primary" size="small" @click="refreshModuleStatus">刷新状态</el-button>
          <el-button type="warning" size="small" @click="resetModuleStatus">重置模块</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 任务详情弹窗 -->
    <el-dialog title="任务详情" v-model="taskDetailVisible" width="60%">
      <div class="task-detail">
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" :border="true">
            <el-descriptions-item label="任务ID">{{ currentTaskDetail.id }}</el-descriptions-item>
            <el-descriptions-item label="模块">{{ currentTaskDetail.module }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ currentTaskDetail.status }}</el-descriptions-item>
            <el-descriptions-item label="耗时">{{ currentTaskDetail.duration }}秒</el-descriptions-item>
            <el-descriptions-item label="开始时间" :span="2">{{ currentTaskDetail.startTime }}</el-descriptions-item>
            <el-descriptions-item label="结束时间" :span="2">{{ currentTaskDetail.endTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="detail-section">
          <h4>执行日志</h4>
          <el-timeline>
            <el-timeline-item v-for="(event, index) in currentTaskDetail.timeline" :key="index" :timestamp="event.time"
              :type="event.status" :color="getStatusColor(event.status)">
              {{ event.desc }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 模块选择和字段配置
      selectedModule: 'gantryStorage',
      allFields: [
        { label: '联机状态', value: 'onlineStatus', isSystem: false },
        { label: 'PLC请求字', value: 'plcRequest', isSystem: false },
        { label: 'PLC汇报字', value: 'plcReport', isSystem: false },
        { label: '完成1A', value: 'complete1A', isSystem: false },
        { label: '完成1B', value: 'complete1B', isSystem: false },
        { label: '任务状态', value: 'taskStatus', isSystem: true },
        { label: '穿梭车/天车位置', value: 'shuttlePosition', isSystem: false },
        { label: '吸盘/小车位置', value: 'suctionPosition', isSystem: false },
        { label: 'MES任务发送字', value: 'mesSend', isSystem: true },
        { label: 'MES任务确认字', value: 'mesConfirm', isSystem: true },
        { label: '起始', value: 'start', isSystem: true },
        { label: '目标', value: 'target', isSystem: true },
        { label: '玻璃数量', value: 'glassCount', isSystem: false },
        { label: '1号边距', value: 'edge1Margin', isSystem: false },
        { label: '1号长边', value: 'edge1Long', isSystem: false },
        { label: '1号短边', value: 'edge1Short', isSystem: false },
        { label: '1号厚', value: 'edge1Thick', isSystem: false },
        { label: '2号边距', value: 'edge2Margin', isSystem: false },
        { label: '2号长边', value: 'edge2Long', isSystem: false },
        { label: '2号短边', value: 'edge2Short', isSystem: false },
        { label: '2号厚', value: 'edge2Thick', isSystem: false },
        { label: '报警信号', value: 'alarm', isSystem: true }
      ],
      checkedFields: [],
      
      // 测试参数配置
      testParameters: {
        start: 1,
        target: 5,
        retryCount: 2,
        interval: 1000,
        failureRate: 0
      },
      
      // 批量测试相关
      showBatchTestConfig: false,
      batchTasks: [],
      
      // 任务和结果
      runningTasks: [],
      testResults: [],
      taskCount: { total: 0, pass: 0, fail: 0 },
      
      // 弹窗状态
      importDialogVisible: false,
      statusDialogVisible: false,
      taskDetailVisible: false,
      importFile: null,
      
      // 模块状态相关
      activeStatusTab: 'gantryStorage',
      statusTableData: [],
      
      // 当前任务详情
      currentTaskDetail: {},
      
      // 显示错误列
      showErrorColumn: false
    };
  },
  
  computed: {
    // 判断配置是否准备就绪
    isConfigReady() {
      return this.checkedFields.length > 0;
    }
  },
  
  mounted() {
    // 初始化时设置默认字段
    this.setDefaultFieldsByModule(this.selectedModule);
    // 加载本地存储的配置
    this.loadLocalConfig();
  },
  
  methods: {
    // 模块切换时设置默认字段
    handleModuleChange(module) {
      this.setDefaultFieldsByModule(module);
    },
    
    // 根据模块设置默认字段
    setDefaultFieldsByModule(module) {
      if (module === 'gantryStorage') {
        this.checkedFields = this.allFields.map(field => field.value);
      } else if (module === 'shuttle') {
        this.checkedFields = [
          'onlineStatus', 'plcRequest', 'plcReport', 
          'taskStatus', 'mesSend', 'mesConfirm', 
          'start', 'target', 'alarm'
        ];
      } else if (module === 'upperModule') {
        this.checkedFields = [
          'onlineStatus', 'plcRequest', 'plcReport', 'taskStatus', 
          'mesSend', 'mesConfirm', 'start', 'target', 'alarm',
          'complete1A', 'complete1B', 'glassCount'
        ];
      } else if (module === 'storageModule') {
        this.checkedFields = [
          'onlineStatus', 'plcRequest', 'plcReport', 'taskStatus', 
          'mesSend', 'mesConfirm', 'start', 'target', 'alarm',
          'shuttlePosition', 'suctionPosition'
        ];
      }
    },
    
    // 判断是否为系统级必选字段
    isSystemField(fieldValue) {
      return this.allFields.find(item => item.value === fieldValue)?.isSystem || false;
    },
    
    // 导入测试任务
    importTask() {
      this.importDialogVisible = true;
    },
    
    // 文件上传前检查
    beforeFileUpload(file) {
      const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
      const isJson = file.type === 'application/json';
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isExcel && !isJson) {
        this.$message.error('上传文件只能是 Excel 或 JSON 格式!');
        return false;
      }
      if (!isLt5M) {
        this.$message.error('上传文件大小不能超过 5MB!');
        return false;
      }
      return true;
    },
    
    // 文件改变处理
    handleFileChange(file) {
      this.importFile = file.raw;
    },
    
    // 确认导入
    confirmImport() {
      if (!this.importFile) {
        this.$message.warning('请先选择文件');
        return;
      }
      
      // 模拟文件解析逻辑
      const reader = new FileReader();
      reader.onload = (e) => {
        try {
          // 根据文件类型解析
          if (this.importFile.name.endsWith('.json')) {
            const data = JSON.parse(e.target.result);
            if (data.batchTasks) {
              this.batchTasks = data.batchTasks;
              this.showBatchTestConfig = true;
              this.$message.success('批量测试任务导入成功');
            } else if (data.testParameters) {
              this.testParameters = data.testParameters;
              this.$message.success('测试参数配置导入成功');
            }
          } else {
            // 对于Excel文件，可以使用xlsx库解析
            this.$message.success('Excel文件导入成功（实际项目中需要添加解析逻辑）');
          }
        } catch (error) {
          this.$message.error('文件解析失败: ' + error.message);
        }
      };
      reader.readAsText(this.importFile);
      
      this.importDialogVisible = false;
    },
    
    // 开始测试
    startTest() {
      const moduleMap = {
        gantryStorage: '龙门仓储',
        upperModule: '上片模块',
        storageModule: '穿梭仓储'
      };
      const moduleName = moduleMap[this.selectedModule];
      
      // 创建新任务
      const taskId = Date.now();
      const task = {
        id: taskId,
        module: moduleName,
        moduleKey: this.selectedModule,
        startTime: new Date().toLocaleString(),
        timeline: [
          { time: new Date().toLocaleTimeString(), status: 'primary', desc: '测试任务启动' },
          { time: new Date().toLocaleTimeString(), status: 'primary', desc: '开始初始化测试环境' }
        ]
      };
      
      this.runningTasks.push(task);
      const startTime = Date.now();
      
      // 准备任务数据
      const taskData = {
        // 基础字段
        mesSend: 1,
        mesConfirm: 0,
        plcRequest: 0,
        taskStatus: 0,
        // 动态参数
        start: this.testParameters.start,
        target: this.testParameters.target
      };
      
      // 调用后端API
      this.$axios.post(`/api/plc/operation/execute/${this.selectedModule}`, taskData)
        .then(response => {
          const result = response.data;
          const timeline = this.runningTasks.find(t => t.id === taskId).timeline;
          
          timeline.push({
            time: new Date().toLocaleTimeString(),
            status: 'success',
            desc: 'PLC请求发送成功'
          });
          
          // 模拟任务执行（实际项目中可能需要轮询或WebSocket）
          setTimeout(() => {
            // 根据配置的故障概率模拟失败情况
            const shouldFail = Math.random() * 100 < this.testParameters.failureRate;
            
            if (!shouldFail && result.success) {
              timeline.push({
                time: new Date().toLocaleTimeString(),
                status: 'success',
                desc: '任务执行完成'
              });
              
              // 更新测试结果
              this.updateTestResults(taskId, moduleName, '通过', '', startTime, 0);
            } else {
              const errorMsg = '模拟故障发生';
              timeline.push({
                time: new Date().toLocaleTimeString(),
                status: 'error',
                desc: `任务执行失败: ${errorMsg}`
              });
              
              // 更新测试结果
              this.updateTestResults(taskId, moduleName, '失败', errorMsg, startTime, 0);
            }
            
            // 从运行任务列表中移除
            this.removeRunningTask(taskId);
          }, this.testParameters.interval);
        })
        .catch(error => {
          const timeline = this.runningTasks.find(t => t.id === taskId).timeline;
          timeline.push({
            time: new Date().toLocaleTimeString(),
            status: 'error',
            desc: `API调用失败: ${error.message || '网络错误'}`
          });
          
          // 更新测试结果
          this.updateTestResults(taskId, moduleName, '失败', error.message || '网络错误', startTime, 0);
          
          // 从运行任务列表中移除
          this.removeRunningTask(taskId);
        });
    },
    
    // 批量测试
    batchTest() {
      this.showBatchTestConfig = !this.showBatchTestConfig;
    },
    
    // 添加当前配置到批量任务
    addCurrentToBatch() {
      const batchTask = {
        module: this.selectedModule,
        start: this.testParameters.start,
        target: this.testParameters.target,
        retryCount: this.testParameters.retryCount
      };
      
      this.batchTasks.push(batchTask);
      this.$message.success('已添加到批量测试任务');
    },
    
    // 执行批量测试
    startBatchTest() {
      if (this.batchTasks.length === 0) {
        this.$message.warning('请先添加批量测试任务');
        return;
      }
      
      // 逐个执行批量任务
      this.batchTasks.forEach((task, index) => {
        setTimeout(() => {
          this.selectedModule = task.module;
          this.testParameters.start = task.start;
          this.testParameters.target = task.target;
          this.testParameters.retryCount = task.retryCount;
          this.startTest();
        }, index * (this.testParameters.interval + 1000));
      });
      
      this.$message.success(`批量测试已启动，共${this.batchTasks.length}个任务`);
    },
    
    // 移除批量任务
    removeBatchTask(index) {
      this.batchTasks.splice(index, 1);
    },
    
    // 更新测试结果
    updateTestResults(taskId, moduleName, status, errorMsg, startTime, retryCount) {
      const endTime = new Date();
      const duration = Math.round((endTime.getTime() - startTime) / 1000);
      
      this.testResults.push({
        id: taskId,
        module: moduleName,
        startTime: new Date(startTime).toLocaleString(),
        endTime: endTime.toLocaleString(),
        status: status,
        duration: duration,
        retryCount: retryCount,
        errorMsg: errorMsg,
        timeline: this.runningTasks.find(t => t.id === taskId)?.timeline || []
      });
      
      // 更新统计数据
      this.taskCount.total++;
      if (status === '通过') {
        this.taskCount.pass++;
      } else {
        this.taskCount.fail++;
        this.showErrorColumn = true;
      }
    },
    
    // 从运行任务列表中移除
    removeRunningTask(taskId) {
      const index = this.runningTasks.findIndex(t => t.id === taskId);
      if (index > -1) {
        this.runningTasks.splice(index, 1);
      }
    },
    
    // 重置所有配置
    resetAll() {
      this.$confirm('确定要重置所有配置吗？这将清除所有测试结果和运行中的任务。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.checkedFields = [];
        this.runningTasks = [];
        this.testResults = [];
        this.taskCount = { total: 0, pass: 0, fail: 0 };
        this.showErrorColumn = false;
        this.setDefaultFieldsByModule(this.selectedModule);
        this.$message.success('所有配置已重置');
      }).catch(() => {});
    },
    
    // 获取时间线状态颜色
    getStatusColor(status) {
      const colorMap = {
        primary: 'blue',
        success: 'green',
        error: 'red',
        warning: 'orange'
      };
      return colorMap[status] || 'blue';
    },
    
    // 获取事件图标
    getEventIcon(status) {
      const iconMap = {
        primary: 'el-icon-loading',
        success: 'el-icon-circle-check',
        error: 'el-icon-circle-close',
        warning: 'el-icon-warning'
      };
      return iconMap[status] || 'el-icon-information';
    },
    
    // 计算通过率
    calculatePassRate() {
      if (this.taskCount.total === 0) return 0;
      return Math.round((this.taskCount.pass / this.taskCount.total) * 100);
    },
    
    // 保存当前配置
    saveCurrentConfig() {
      const config = {
        selectedModule: this.selectedModule,
        checkedFields: this.checkedFields,
        testParameters: this.testParameters
      };
      
      localStorage.setItem('plcTestConfig', JSON.stringify(config));
      this.$message.success('配置已保存');
    },
    
    // 加载本地配置
    loadLocalConfig() {
      const config = localStorage.getItem('plcTestConfig');
      if (config) {
        try {
          const parsedConfig = JSON.parse(config);
          this.selectedModule = parsedConfig.selectedModule || 'gantryStorage';
          this.checkedFields = parsedConfig.checkedFields || [];
          this.testParameters = parsedConfig.testParameters || this.testParameters;
          this.$message.success('配置已加载');
        } catch (error) {
          console.error('加载配置失败:', error);
        }
      }
    },
    
    // 手动加载配置
    loadConfig() {
      this.loadLocalConfig();
    },
    
    // 显示模块状态
    showModuleStatus() {
      this.statusDialogVisible = true;
      this.refreshModuleStatus();
    },
    
    // 处理状态标签点击
    handleStatusTabClick(tab) {
      this.activeStatusTab = tab.name;
      this.refreshModuleStatus();
    },
    
    // 刷新模块状态
    refreshModuleStatus() {
      this.$axios.get(`/api/plc/operation/status/${this.activeStatusTab}`)
        .then(response => {
          const statusData = response.data.data;
          this.statusTableData = [];
          
          if (statusData) {
            for (const [key, value] of Object.entries(statusData)) {
              this.statusTableData.push({
                key: this.getFieldLabel(key) || key,
                value: value,
                updateTime: new Date().toLocaleTimeString()
              });
            }
          }
        })
        .catch(error => {
          this.$message.error('获取模块状态失败: ' + error.message);
        });
    },
    
    // 重置模块状态
    resetModuleStatus() {
      this.$confirm('确定要重置该模块状态吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post(`/api/plc/operation/reset/${this.activeStatusTab}`)
          .then(() => {
            this.$message.success('模块状态已重置');
            this.refreshModuleStatus();
          })
          .catch(error => {
            this.$message.error('重置模块状态失败: ' + error.message);
          });
      }).catch(() => {});
    },
    
    // 获取字段标签
    getFieldLabel(fieldValue) {
      const field = this.allFields.find(item => item.value === fieldValue);
      return field ? field.label : null;
    },
    
    // 查看任务详情
    viewTaskDetail(task) {
      this.currentTaskDetail = task;
      this.taskDetailVisible = true;
    },
    
    // 重试任务
    retryTask(task) {
      // 如果任务有moduleKey字段，使用它，否则根据模块名称推断
      const moduleMap = {
        '龙门仓储': 'gantryStorage',
        '上片模块': 'upperModule',
        '穿梭仓储': 'storageModule'
      };
      
      this.selectedModule = task.moduleKey || moduleMap[task.module] || 'gantryStorage';
      
      // 重置字段选择
      this.setDefaultFieldsByModule(this.selectedModule);
      
      // 启动测试
      this.startTest();
    }
  }
};
</script>

<style scoped>
.auto-test-dashboard {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  border-bottom: 1px solid #eaecef;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
}

.header h1 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.aside {
  background-color: #f5f7fa;
  border-right: 1px solid #eaecef;
  padding: 15px;
  overflow-y: auto;
}

.main {
  padding: 20px;
  overflow: auto;
  flex: 1;
}

.mt-20 {
  margin-top: 20px;
}

.config-card, .batch-test-card, .timeline-section, .result-section {
  background-color: #fff;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.field-checkbox-group {
  max-height: 200px;
  overflow-y: auto;
  padding: 5px 0;
}

.field-checkbox-group .el-checkbox {
  margin-bottom: 8px;
  display: block;
}

.parameter-form {
  margin-top: 15px;
}

.parameter-form .el-form-item {
  margin-bottom: 15px;
}

.test-actions, .batch-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.batch-actions {
  justify-content: flex-end;
}

.timeline-container {
  max-height: 400px;
  overflow-y: auto;
}

.task-timeline {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px dashed #ebeef5;
}

.task-timeline:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.result-card {
  text-align: center;
  transition: all 0.3s;
}

.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
}

.label {
  color: #606266;
  font-size: 14px;
}

.value {
  font-weight: 600;
  font-size: 18px;
}

.success {
  color: #67c23a;
}

.error {
  color: #f56c6c;
}

.result-table-container {
  margin-top: 20px;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

.status-container {
  max-height: 500px;
  overflow-y: auto;
}

.status-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  justify-content: flex-end;
}

.task-detail {
  max-height: 500px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h4 {
  margin-bottom: 10px;
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>