<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <div class="header-left">
        <h2>MES测试系统</h2>
      </div>
      <div class="header-right">
        <el-button type="success" @click="startTest">
          <i class="el-icon-play"></i> 开始测试
        </el-button>
        <el-button type="warning" @click="stopTest">
          <i class="el-icon-stop"></i> 停止测试
        </el-button>
      </div>
    </div>
    
    <div class="dashboard-content">
      <el-aside width="400px" class="config-aside">
        <el-card class="config-section">
          <h3><i class="el-icon-sitemap"></i> 项目配置</h3>
          <el-form label-width="100px" size="">
            <el-form-item label="项目选择">
              <el-select v-model="selectedProject" placeholder="请选择项目" @change="handleProjectChange" class="full-width">
                <el-option
                  v-for="project in projectList"
                  :key="project.value"
                  :label="project.label"
                  :value="project.value">
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="模块选择">
              <el-select v-model="selectedModule" placeholder="请选择模块" @change="handleModuleChange" class="full-width">
                <el-option
                  v-for="module in availableModules"
                  :key="module"
                  :label="module"
                  :value="module">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-card>
        
        <el-card class="config-section">
          <h3><i class="el-icon-list"></i> 协议字段配置</h3>
          <div class="field-container">
            <div class="field-header">
              <span>字段列表</span>
              <el-button type="text" size="small" icon="el-icon-plus" @click="addNewField" class="add-btn">添加字段</el-button>
            </div>
            <div class="field-list">
              <div v-for="field in allFields" :key="field.value" class="field-item">
                <div class="field-info">
                  <el-checkbox v-model="checkedFields" :label="field.value">{{ field.label }}</el-checkbox>
                </div>
                <div class="field-address">
                  <span class="field-address-label">DB块索引:</span>
                  <el-input v-model="field.address" size="mini" placeholder="输入DB块索引" style="width: 100px;"></el-input>
                </div>
                <el-button v-if="!isSystemField(field.value)" type="text" size="mini" icon="el-icon-delete" class="remove-btn" @click="removeField(field.value)"></el-button>
              </div>
            </div>
          </div>
        </el-card>
        
        <el-card class="config-section">
          <h3><i class="el-icon-sliders"></i> 测试参数配置</h3>
          <el-form :model="testParameters" label-width="100px" size="small">
            <el-form-item label="PLC地址">
              <el-input v-model="testParameters.plcAddress" placeholder="输入PLC地址"></el-input>
            </el-form-item>
            <el-form-item label="DB块">
              <el-input v-model="testParameters.dbAddress" placeholder="输入DB块"></el-input>
            </el-form-item>
            <el-form-item label="超时时间">
              <el-input-number v-model="testParameters.timeout" :min="1" :max="60" controls-position="right"></el-input-number>
              <span style="margin-left: 10px;">秒</span>
            </el-form-item>
            <el-form-item label="重试次数">
              <el-input-number v-model="testParameters.retryCount" :min="0" :max="10" controls-position="right"></el-input-number>
              <span style="margin-left: 10px;">次</span>
            </el-form-item>
          </el-form>
          
          <div style="margin-top: 15px; text-align: center;">
            <el-button type="primary" @click="saveCurrentConfig">保存配置</el-button>
            <el-button @click="resetConfig">重置配置</el-button>
          </div>
        </el-card>
      </el-aside>
      
      <el-main class="monitor-main">
        <div class="monitor-section">
          <div class="section-header">
            <h3><i class="el-icon-monitor"></i> 任务监控</h3>
            <div class="section-actions">
              <el-button type="primary" size="small" @click="showBatchTestDialog">
                <i class="el-icon-files"></i> 批量测试
              </el-button>
              <el-button type="success" size="small" @click="showImportDialog">
                <i class="el-icon-upload2"></i> 导入任务
              </el-button>
              <el-button size="small" @click="refreshTasks">
                <i class="el-icon-refresh"></i> 刷新
              </el-button>
            </div>
          </div>
          
          <div class="task-container">
            <h4>当前任务</h4>
            <div v-if="currentTasks.length === 0" class="empty-state">
              <div class="empty-icon"><i class="el-icon-clipboard"></i></div>
              <div class="empty-text">暂无任务</div>
            </div>
            <div v-else class="task-list">
              <div v-for="task in currentTasks" :key="task.id" class="task-item" @click="showTaskDetail(task)">
                <div class="task-info">
                  <span class="task-id">{{ task.id }}</span>
                  <span class="task-module">{{ task.module }}</span>
                  <span class="task-project">{{ task.project }}</span>
                </div>
                <el-tag :type="getStatusType(task.status)">{{ task.status }}</el-tag>
              </div>
            </div>
          </div>
          
          <div class="result-container">
            <h4>测试结果统计</h4>
            <div class="result-stats">
              <div class="stat-item">
                <div class="stat-number">{{ testStats.total }}</div>
                <div class="stat-label">总测试数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number pass">{{ testStats.passed }}</div>
                <div class="stat-label">通过</div>
              </div>
              <div class="stat-item">
                <div class="stat-number fail">{{ testStats.failed }}</div>
                <div class="stat-label">失败</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ testStats.running }}</div>
                <div class="stat-label">进行中</div>
              </div>
            </div>
          </div>
          
          <div class="result-list">
            <h4>测试结果</h4>
            <el-table 
              :data="testResults" 
              style="width: 100%"
              border
              stripe
              :cell-style="{ padding: '8px 10px' }">
              <el-table-column prop="id" label="任务ID" width="100"></el-table-column>
              <el-table-column prop="project" label="项目" width="120"></el-table-column>
              <el-table-column prop="module" label="模块" width="120"></el-table-column>
              <el-table-column prop="field" label="字段" width="120"></el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="startTime" label="开始时间" width="160"></el-table-column>
              <el-table-column prop="duration" label="耗时" width="100"></el-table-column>
              <el-table-column prop="result" label="结果"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-main>
    </div>
    
    <!-- 批量测试弹窗 -->
    <el-dialog title="批量测试" :visible.sync="batchTestDialogVisible" width="60%">
      <div class="batch-test-container">
        <el-form :model="batchTestForm" label-width="100px">
          <el-form-item label="项目选择">
            <el-select v-model="batchTestForm.projectIds" multiple placeholder="请选择项目" style="width: 100%">
              <el-option
                v-for="project in projectList"
                :key="project.value"
                :label="project.label"
                :value="project.value">
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="模块选择">
            <el-checkbox-group v-model="batchTestForm.modules">
              <el-checkbox v-for="module in availableModules" :key="module" :label="module">{{ module }}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="测试字段">
            <el-transfer
              v-model="batchTestForm.selectedFields"
              :data="allFields.map(field => ({key: field.value, label: field.label}))"
              :titles="['可选字段', '已选字段']"
              :props="{key: 'key', label: 'label'}">
            </el-transfer>
          </el-form-item>
        </el-form>
        
        <div class="batch-footer">
          <el-button @click="batchTestDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="startBatchTest">开始批量测试</el-button>
        </div>
      </div>
    </el-dialog>
    
    <!-- 任务导入弹窗 -->
    <el-dialog title="导入任务" :visible.sync="importDialogVisible" width="50%">
      <el-upload
        class="upload-demo"
        drag
        action="/api/plcSend/test/import"
        :on-success="handleImportSuccess"
        :before-upload="beforeUpload">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      
      <div class="import-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport">确认导入</el-button>
      </div>
    </el-dialog>
    
    <!-- 任务详情弹窗 -->
    <el-dialog title="任务详情" :visible.sync="taskDetailDialogVisible" width="60%">
      <div class="task-detail-container" v-if="selectedTask">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ selectedTask.id }}</el-descriptions-item>
          <el-descriptions-item label="项目">{{ selectedTask.project }}</el-descriptions-item>
          <el-descriptions-item label="模块">{{ selectedTask.module }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(selectedTask.status)">{{ selectedTask.status }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ selectedTask.startTime }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ selectedTask.endTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="耗时">{{ selectedTask.duration || '-' }}</el-descriptions-item>
          <el-descriptions-item label="执行人">{{ selectedTask.operator || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <div class="task-timeline">
          <h4>任务流转时间线</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in selectedTask.timeline"
              :key="index"
              :timestamp="activity.timestamp">
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
    
    <!-- 模块状态弹窗 -->
    <el-dialog title="模块状态" :visible.sync="moduleStatusDialogVisible" width="50%">
      <div class="status-container">
        <el-table 
          :data="moduleStatusData" 
          style="width: 100%"
          border
          stripe>
          <el-table-column prop="module" label="模块名称"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastUpdate" label="最后更新"></el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="checkModuleStatus(scope.row)">检查状态</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 脚本部分保持不变
import axios from 'axios'
import * as XLSX from 'xlsx'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Test',
  data() {
    return {
      selectedProject: 'default', // 默认项目
      selectedModule: '',
      checkedFields: ['onlineState', 'plcRequest'],
      projectList: [],
      availableModules: ['gantryStorage', 'upperModule', 'storageModule'],
      fieldDbIndexes: {},
      allFields: [],
      
      // 测试参数
      testParameters: {
        plcAddress: '192.168.10.21',
        dbAddress: 'DB38',
        retryCount: 3,
        interval: 1000,
        failureRate: 5,
        timeout: 30
      },
      
      // 批量测试
      batchTasks: [],
      showBatchTestConfig: false,
      
      // 任务监控
      runningTasks: [],
      taskCount: {
        total: 0,
        pass: 0,
        fail: 0
      },
      testResults: [],
      
      // 弹窗状态
      importDialogVisible: false,
      moduleStatusVisible: false,
      taskDetailVisible: false,
      currentTaskDetail: null,
      
      // 文件上传
      uploadFile: null,
      
      // 定时器
      statusTimer: null,
      testTimer: null,
      
      // 系统字段（不可取消勾选）
      systemFields: ['onlineState', 'plcRequest', 'taskId'],
      
      // 弹窗状态
      batchTestDialogVisible: false,
      taskDetailDialogVisible: false,
      moduleStatusDialogVisible: false,
      
      // 批量测试表单
      batchTestForm: {
        projectIds: [],
        modules: [],
        selectedFields: []
      },
      
      // 任务数据
      currentTasks: [],
      selectedTask: null,
      testStats: {
        total: 0,
        passed: 0,
        failed: 0,
        running: 0
      },
      moduleStatusData: [],
      
      // 错误信息列显示控制
      showErrorColumn: false
    };
  },
  computed: {
    // 检查配置是否准备就绪
    isConfigReady() {
      return this.selectedModule && this.checkedFields.length > 0;
    }
  },
  mounted() {
    // 页面加载时获取项目列表
    this.fetchProjectList();
    
    // 加载保存的配置
    this.loadConfig();
    
    // 启动状态监控
    this.startStatusMonitoring();
  },
  beforeUnmount() {
    // 清理定时器
    if (this.statusTimer) {
      clearInterval(this.statusTimer);
    }
    if (this.testTimer) {
      clearInterval(this.testTimer);
    }
  },
  methods: {
    // 获取项目列表
    async fetchProjectList() {
      try {
        const response = await axios.get('/api/plcSend/address-mapping/list');
        if (response.data && response.data.code === 200) {
          this.projectList = response.data.data.map(item => ({
            value: item.projectId,
            label: item.projectName || item.projectId,
            plcIp: item.plcIp,
            dbArea: item.dbArea,
            addressMapping: item.addressMapping,
            remarks: item.remarks
          }));
          
          // 如果当前选择的项目不在列表中，设置为第一个项目
          if (this.projectList.length > 0 && !this.projectList.find(p => p.value === this.selectedProject)) {
            this.selectedProject = this.projectList[0].value;
            this.handleProjectChange();
          }
        } else {
          ElMessage.error('获取项目列表失败');
        }
      } catch (error) {
        console.error('获取项目列表失败:', error);
        ElMessage.error('获取项目列表失败');
      }
    },
    
    // 处理项目变更
    async handleProjectChange() {
      // 确保projectList存在且不是空数组
      if (!this.projectList || this.projectList.length === 0) {
        console.log('项目列表未加载');
        return;
      }
      
      const project = this.projectList.find(p => p.value === this.selectedProject);
      if (project) {
        // 更新PLC地址和DB块
        this.testParameters.plcAddress = project.plcIp || 'null';
        this.testParameters.dbAddress = project.dbArea || 'null';
        this.selectedModule = project.remarks || 'null';
        
        // 获取完整的项目配置（包含地址映射）
        if (this.selectedProject && this.selectedProject !== 'default') {
          try {
            const response = await axios.get(`/api/plcSend/address-mapping/project/config?projectId=${this.selectedProject}`);
            if (response.data && response.data.code === 200 && response.data.data) {
              const projectConfig = response.data.data;
              // 使用完整的地址映射更新字段配置
              if (projectConfig.addressMapping) {
                this.updateFieldAddresses(projectConfig.addressMapping);
              }
            } else {
              console.error('获取项目配置失败:', response.data?.message);
              // 回退到使用项目列表中的地址映射
              if (project.addressMapping) {
                try {
                  const addressMapping = JSON.parse(project.addressMapping);
                  this.updateFieldAddresses(addressMapping);
                } catch (e) {
                  console.error('解析地址映射失败:', e);
                }
              }
            }
          } catch (error) {
            console.error('获取项目配置失败:', error);
            // 回退到使用项目列表中的地址映射
            if (project.addressMapping) {
              try {
                const addressMapping = JSON.parse(project.addressMapping);
                this.updateFieldAddresses(addressMapping);
              } catch (e) {
                console.error('解析地址映射失败:', e);
              }
            }
          }
        } else {
          // 对于默认项目，使用项目列表中的地址映射
          if (project.addressMapping) {
            try {
              const addressMapping = JSON.parse(project.addressMapping);
              this.updateFieldAddresses(addressMapping);
            } catch (e) {
              console.error('解析地址映射失败:', e);
            }
          }
        }
      } else {
        console.log('未找到选中的项目:', this.selectedProject);
      }
      
      // 清空当前任务状态
      this.runningTasks = [];
      this.testResults = [];
      this.updateTaskCount();
    },
    
    // 更新字段地址
    updateFieldAddresses(addressMapping) {
      // 字段名到中文标签的映射关系
      const fieldLabels = {
        'onlineState': '联机状态',
        'plcRequest': 'PLC请求',
        'plcReport': 'PLC汇报',
        'finishA': '完成信号A',
        'finishB': '完成信号B',
        'state': '设备状态',
        'onePosition': '一号位置',
        'twoPosition': '二号位置',
        'mesSend': 'MES发送',
        'mesConfirm': 'MES确认',
        'start': '起始位置',
        'target': '目标位置',
        'mesGlassCount': '玻璃数量',
        'rawMargin1': '原片边距1',
        'mesWidth1': '玻璃宽度1',
        'mesHeight1': '玻璃高度1',
        'rawThickness1': '玻璃厚度1',
        'rawMargin2': '原片边距2',
        'mesWidth2': '玻璃宽度2',
        'mesHeight2': '玻璃高度2',
        'rawThickness2': '玻璃厚度2',
        'alarmInfo': '报警信息'
      };
      
      // 根据addressMapping动态生成allFields数组
      const newFields = [];
      
      // 遍历addressMapping，生成新的字段列表
      for (const [fieldName, address] of Object.entries(addressMapping)) {
        newFields.push({
          label: fieldLabels[fieldName] || fieldName, // 使用映射的中文标签，如果没有则使用字段名
          value: fieldName,
          address: address
        });
      }
      
      // 更新allFields
      this.allFields = newFields;
      
      // 如果是第一次加载，默认选中系统关键字段
      if (!this.checkedFields || this.checkedFields.length === 0 || this.checkedFields.includes('')) {
        this.checkedFields = ['onlineState', 'plcRequest'];
      }
    },
    
    // 处理模块变更
    handleModuleChange() {
      // 根据模块类型设置默认字段
      if (this.selectedModule === 'gantryStorage') {
        this.checkedFields = ['onlineState', 'plcRequest', 'taskId', 'startPos', 'targetPos', 'deviceStatus'];
      } else if (this.selectedModule === 'upperModule') {
        this.checkedFields = ['onlineState', 'plcRequest', 'taskId', 'runMode', 'errorCode', 'currentSpeed'];
      } else if (this.selectedModule === 'storageModule') {
        this.checkedFields = ['onlineState', 'plcRequest', 'taskId', 'currentPos', 'taskStatus', 'completion'];
      }
    },
    
    // 判断是否为系统字段
    isSystemField(field) {
      return this.systemFields.includes(field);
    },
    
    // 添加新字段
    addNewField() {
      ElMessageBox.prompt('请输入新字段名称', '添加字段', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^[a-zA-Z][a-zA-Z0-9_]*$/,
        inputErrorMessage: '字段名称必须以字母开头，只能包含字母、数字和下划线'
      }).then(({ value }) => {
        // 检查字段是否已存在
        if (this.allFields.find(f => f.value === value)) {
          ElMessage.warning('字段已存在');
          return;
        }
        
        // 添加新字段
        this.allFields.push({
          label: value,
          value: value,
          address: 0,
          system: false
        });
        
        ElMessage.success('字段添加成功');
      }).catch(() => {
        // 用户取消
      });
    },
    
    // 删除字段
    removeField(field) {
      if (this.isSystemField(field)) {
        ElMessage.warning('系统字段不能删除');
        return;
      }
      
      ElMessageBox.confirm(`确定要删除字段 "${field}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 从allFields中移除
        this.allFields = this.allFields.filter(f => f.value !== field);
        
        // 从checkedFields中移除
        this.checkedFields = this.checkedFields.filter(f => f !== field);
        
        ElMessage.success('字段删除成功');
      }).catch(() => {
        // 用户取消
      });
    },
    
    // 更新字段地址
    updateFieldAddress(field, address) {
      const fieldIndex = this.allFields.findIndex(f => f.value === field);
      if (fieldIndex !== -1) {
        this.allFields[fieldIndex].address = parseInt(address) || 0;
      }
    },
    
    // 保存当前配置
    async saveCurrentConfig() {
      try {
        // 构建地址映射JSON
        const addressMapping = {};
        this.allFields.forEach(field => {
          if (field.address !== undefined) {
            addressMapping[field.value] = field.address;
          }
        });
        
        // 保存到本地存储
        localStorage.setItem('plcTestConfig', JSON.stringify({
          selectedProject: this.selectedProject,
          selectedModule: this.selectedModule,
          checkedFields: this.checkedFields,
          testParameters: this.testParameters,
          allFields: this.allFields
        }));
        
        // 只有当selectedProject不是'default'时才调用API
        if (this.selectedProject && this.selectedProject !== 'default') {
          // 准备更新数据
          const updateData = {
            projectId: this.selectedProject,
            plcIp: this.testParameters.plcAddress,
            dbArea: this.testParameters.dbAddress,
            addressMapping: JSON.stringify(addressMapping)
          };
          
          // 调用API更新配置
          const response = await axios.put(`/api/plcSend/address-mapping/project/${this.selectedProject}`, updateData);
        
        if (response.data && response.data.code === 200) {
            ElMessage.success('配置保存成功');
          } else {
            ElMessage.error('配置保存失败');
          }
        } else {
          // 如果是默认项目，只保存到本地
          ElMessage.success('配置已保存到本地');
        }
      } catch (error) {
        console.error('保存配置失败:', error);
        ElMessage.error('配置保存失败');
      }
    },
    
    // 加载配置
    loadConfig() {
      const savedConfig = localStorage.getItem('plcTestConfig');
      if (savedConfig) {
        try {
          const config = JSON.parse(savedConfig);
          this.selectedProject = config.selectedProject || 'default';
          this.selectedModule = config.selectedModule || 'gantryStorage';
          this.checkedFields = config.checkedFields || ['onlineState', 'plcRequest', 'taskId'];
          this.testParameters = { ...this.testParameters, ...config.testParameters };
          
          if (config.allFields) {
            this.allFields = config.allFields;
          }
        } catch (e) {
          console.error('加载配置失败:', e);
        }
      }
    },
    
    // 重置配置
    resetAll() {
      ElMessageBox.confirm('确定要重置所有配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 重置数据
        this.selectedProject = 'default';
        this.selectedModule = 'gantryStorage';
        this.checkedFields = ['onlineState', 'plcRequest', 'taskId'];
        this.testParameters = {
          plcAddress: '192.168.10.21',
          dbAddress: 'DB38',
          retryCount: 3,
          interval: 1000,
          failureRate: 5
        };
        
        // 清空任务
        this.runningTasks = [];
        this.testResults = [];
        this.batchTasks = [];
        this.updateTaskCount();
        
        // 清除本地存储
        localStorage.removeItem('plcTestConfig');
        
        ElMessage.success('配置已重置');
      }).catch(() => {
        // 用户取消
      });
    },
    
    // 开始测试
    async startTest() {
      if (!this.isConfigReady) {
        ElMessage.warning('请先完成配置');
        return;
      }
      
      try {
        // 构建测试参数
        const testParams = {
          projectId: this.selectedProject,
          module: this.selectedModule,
          fields: this.checkedFields,
          parameters: this.testParameters
        };
        
        // 调用测试API
        const response = await axios.post(`/api/plcSend/test/execute/${this.selectedModule}`, testParams);
        
        if (response.data && response.data.code === 200) {
          const taskId = response.data.data.taskId;
          
          // 添加到运行任务列表
          this.runningTasks.push({
            id: taskId,
            module: this.selectedModule,
            project: this.selectedProject,
            status: 'running',
            startTime: new Date().toLocaleTimeString(),
            timeline: [
              { time: new Date().toLocaleTimeString(), desc: '任务已提交', status: 'primary' }
            ]
          });
          
          ElMessage.success('测试任务已提交');
          
          // 开始监控任务状态
          this.monitorTask(taskId);
        } else {
          ElMessage.error('提交测试任务失败');
        }
      } catch (error) {
        console.error('提交测试任务失败:', error);
        ElMessage.error('提交测试任务失败');
      }
    },
    
    // 监控任务状态
    async monitorTask(taskId) {
      const checkStatus = async () => {
        try {
          const response = await axios.get(`/api/plcSend/test/status/${this.selectedModule}?taskId=${taskId}`);
          
          if (response.data && response.data.code === 200) {
            const taskData = response.data.data;
            const taskIndex = this.runningTasks.findIndex(t => t.id === taskId);
            
            if (taskIndex !== -1) {
              // 更新任务状态
              this.runningTasks[taskIndex].status = taskData.status;
              
              // 添加状态变化事件
              this.runningTasks[taskIndex].timeline.push({
                time: new Date().toLocaleTimeString(),
                desc: `状态更新: ${taskData.status}`,
                status: taskData.status === 'success' ? 'success' : 
                       taskData.status === 'failed' ? 'danger' : 'primary'
              });
              
              // 如果任务完成，添加到结果列表
              if (taskData.status === 'success' || taskData.status === 'failed') {
                this.testResults.push({
                  id: taskId,
                  module: this.selectedModule,
                  project: this.selectedProject,
                  startTime: this.runningTasks[taskIndex].startTime,
                  endTime: new Date().toLocaleTimeString(),
                  status: taskData.status === 'success' ? '通过' : '失败',
                  duration: taskData.duration || 0,
                  retryCount: taskData.retryCount || 0,
                  errorMsg: taskData.errorMsg || ''
                });
                
                // 更新任务计数
                this.updateTaskCount();
                
                // 从运行任务列表中移除
                this.runningTasks.splice(taskIndex, 1);
                
                // 显示错误信息列（如果有错误）
                if (taskData.errorMsg) {
                  this.showErrorColumn = true;
                }
              } else {
                // 继续监控
                setTimeout(checkStatus, 2000);
              }
            }
          }
        } catch (error) {
          console.error('获取任务状态失败:', error);
        }
      };
      
      // 开始检查状态
      setTimeout(checkStatus, 1000);
    },
    
    // 批量测试
    batchTest() {
      this.showBatchTestConfig = true;
    },
    
    // 添加当前配置到批量任务
    addCurrentToBatch() {
      if (!this.isConfigReady) {
        ElMessage.warning('请先完成配置');
        return;
      }
      
      this.batchTasks.push({
        module: this.selectedModule,
        project: this.selectedProject,
        start: this.testParameters.startPos || 0,
        target: this.testParameters.targetPos || 0,
        retryCount: this.testParameters.retryCount
      });
      
      ElMessage.success('已添加到批量任务列表');
    },
    
    // 开始批量测试
    async startBatchTest() {
      if (this.batchTasks.length === 0) {
        ElMessage.warning('批量任务列表为空');
        return;
      }
      
      try {
        // 调用批量测试API
        const response = await axios.post('/api/plcSend/test/batch', {
          tasks: this.batchTasks
        });
        
        if (response.data && response.data.code === 200) {
          ElMessage.success('批量测试已提交');
          this.showBatchTestConfig = false;
          
          // 添加到运行任务列表
          response.data.data.taskIds.forEach((taskId, index) => {
            this.runningTasks.push({
              id: taskId,
              module: this.batchTasks[index].module,
              project: this.batchTasks[index].project,
              status: 'running',
              startTime: new Date().toLocaleTimeString(),
              timeline: [
                { time: new Date().toLocaleTimeString(), desc: '批量任务已提交', status: 'primary' }
              ]
            });
            
            // 监控任务状态
            this.monitorTask(taskId);
          });
        } else {
          ElMessage.error('提交批量测试失败');
        }
      } catch (error) {
        console.error('提交批量测试失败:', error);
        ElMessage.error('提交批量测试失败');
      }
    },
    
    // 移除批量任务
    removeBatchTask(index) {
      this.batchTasks.splice(index, 1);
    },
    
    // 导入任务
    importTask() {
      this.importDialogVisible = true;
    },
    
    // 处理文件变化
    handleFileChange(file) {
      this.uploadFile = file.raw;
    },
    
    // 文件上传前检查
    beforeFileUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                     file.type === 'application/vnd.ms-excel';
      const isJSON = file.type === 'application/json';
      const isLt5M = file.size / 1024 / 1024 < 5;
      
      if (!isExcel && !isJSON) {
        ElMessage.error('只能上传Excel或JSON格式文件!');
      }
      if (!isLt5M) {
        ElMessage.error('文件大小不能超过5MB!');
      }
      return (isExcel || isJSON) && isLt5M;
    },
    
    // 确认导入
    async confirmImport() {
      if (!this.uploadFile) {
        ElMessage.warning('请先选择文件');
        return;
      }
      
      try {
        const formData = new FormData();
        formData.append('file', this.uploadFile);
        
        const response = await axios.post('/api/plcSend/test/import', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        
        if (response.data && response.data.code === 200) {
          ElMessage.success('导入成功');
          this.importDialogVisible = false;
          
          // 添加导入的任务到批量任务列表
          if (response.data.data && response.data.data.tasks) {
            this.batchTasks = [...this.batchTasks, ...response.data.data.tasks];
          }
        } else {
          ElMessage.error('导入失败');
        }
      } catch (error) {
        console.error('导入失败:', error);
        ElMessage.error('导入失败');
      }
    },
    
    // 显示任务详情
    showTaskDetail(task) {
      this.selectedTask = task;
      this.taskDetailDialogVisible = true;
    },
    
    // 显示批量测试弹窗
    showBatchTestDialog() {
      this.batchTestDialogVisible = true;
    },
    
    // 显示导入弹窗
    showImportDialog() {
      this.importDialogVisible = true;
    },
    
    // 处理导入成功
    handleImportSuccess(response) {
      if (response.data && response.data.code === 200) {
        ElMessage.success('文件上传成功');
        // 可以在这里处理上传成功后的逻辑
      }
    },
    
    // 文件上传前检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                     file.type === 'application/vnd.ms-excel';
      const isJSON = file.type === 'application/json';
      const isLt10M = file.size / 1024 / 1024 < 10;
      
      if (!isExcel && !isJSON) {
        ElMessage.error('只能上传Excel或JSON格式文件!');
        return false;
      }
      if (!isLt10M) {
        ElMessage.error('文件大小不能超过10MB!');
        return false;
      }
      return true;
    },
    
    // 取消测试
    stopTest() {
      ElMessageBox.confirm('确定要停止当前测试吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // 修改为使用路径参数的方式调用API
          const response = await axios.post(`/api/plcSend/test/stop/${this.selectedModule}`);
          
          if (response.data && response.data.success) {
            ElMessage.success('测试已停止');
            // 清空运行中的任务
            this.runningTasks = [];
            this.testStats.running = 0;
          } else {
            ElMessage.error('停止测试失败');
          }
        } catch (error) {
          console.error('停止测试失败:', error);
          ElMessage.error('停止测试失败');
        }
      }).catch(() => {
        // 用户取消
      });
    },
    
    // 重置模块状态
    async resetModuleStatus() {
      try {
        const response = await axios.post(`/api/plcSend/test/reset/${this.selectedModule}`);
        
        if (response.data && response.data.code === 200) {
          ElMessage.success('模块状态已重置');
        } else {
          ElMessage.error('重置模块状态失败');
        }
      } catch (error) {
        console.error('重置模块状态失败:', error);
        ElMessage.error('重置模块状态失败');
      }
    },
    
    // 查看任务详情
    viewTaskDetail(task) {
      this.currentTaskDetail = task;
      this.taskDetailVisible = true;
    },
    
    // 重试任务
    async retryTask(task) {
      try {
        const response = await axios.post(`/api/plcSend/test/retry/${task.module}`, {
          taskId: task.id,
          projectId: task.project
        });
        
        if (response.data && response.data.code === 200) {
          ElMessage.success('任务重试已提交');
          
          // 添加到运行任务列表
          this.runningTasks.push({
            id: task.id,
            module: task.module,
            project: task.project,
            status: 'running',
            startTime: new Date().toLocaleTimeString(),
            timeline: [
              { time: new Date().toLocaleTimeString(), desc: '任务重试已提交', status: 'primary' }
            ]
          });
          
          // 监控任务状态
          this.monitorTask(task.id);
        } else {
          ElMessage.error('任务重试失败');
        }
      } catch (error) {
        console.error('任务重试失败:', error);
        ElMessage.error('任务重试失败');
      }
    },
    
    // 更新任务计数
    updateTaskCount() {
      this.taskCount.total = this.testResults.length;
      this.taskCount.pass = this.testResults.filter(r => r.status === '通过').length;
      this.taskCount.fail = this.testResults.filter(r => r.status === '失败').length;
    },
    
    // 计算通过率
    calculatePassRate() {
      if (this.taskCount.total === 0) return 0;
      return Math.round((this.taskCount.pass / this.taskCount.total) * 100);
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'primary': '#409EFF',
        'success': '#67C23A',
        'warning': '#E6A23C',
        'danger': '#F56C6C',
        'info': '#909399'
      };
      return colorMap[status] || '#409EFF';
    },
    
    // 获取事件图标
    getEventIcon(status) {
      const iconMap = {
        'primary': 'el-icon-loading',
        'success': 'el-icon-circle-check',
        'warning': 'el-icon-warning',
        'danger': 'el-icon-circle-close',
        'info': 'el-icon-info'
      };
      return iconMap[status] || 'el-icon-info';
    },
    
    // 启动状态监控
    startStatusMonitoring() {
      // 每30秒刷新一次状态
      this.statusTimer = setInterval(() => {
        this.refreshTasks();
        this.refreshModuleStatus();
      }, 30000);
    },
    
    // 刷新任务列表
    async refreshTasks() {
      try {
        // 后端没有直接提供获取所有任务的接口
        // 这里使用本地数据模拟，实际应用中可能需要调整实现逻辑
        // 或者后端需要新增相应接口
        // const response = await axios.get('/api/plc/test/tasks');
        
        // 使用模拟数据
        this.currentTasks = this.runningTasks;
        
        // 更新任务统计
        this.testStats.total = this.testResults.length;
        this.testStats.passed = this.testResults.filter(r => r.status === '通过').length;
        this.testStats.failed = this.testResults.filter(r => r.status === '失败').length;
        this.testStats.running = this.currentTasks.length;
      } catch (error) {
        console.error('刷新任务列表失败:', error);
      }
    },
    
    // 刷新模块状态
    async refreshModuleStatus() {
      try {
        // 后端没有直接提供获取所有模块状态的接口
        // 这里使用本地数据模拟，实际应用中可能需要调整实现逻辑
        // 或者后端需要新增相应接口
        // const response = await axios.get('/api/plc/test/modules/status');
        
        // 使用模拟数据保持界面正常工作
        if (!this.moduleStatusData || this.moduleStatusData.length === 0) {
          // 如果没有数据，初始化一些模拟模块
          this.moduleStatusData = [
            { module: 'gantryStorage', status: 'idle', lastUpdate: new Date().toLocaleTimeString() },
            { module: 'upperModule', status: 'idle', lastUpdate: new Date().toLocaleTimeString() },
            { module: 'storageModule', status: 'idle', lastUpdate: new Date().toLocaleTimeString() }
          ];
        } else {
          // 更新现有模块的时间戳
          this.moduleStatusData.forEach(module => {
            module.lastUpdate = new Date().toLocaleTimeString();
          });
        }
      } catch (error) {
        console.error('刷新模块状态失败:', error);
      }
    },
    
    // 检查模块状态
    async checkModuleStatus(module) {
      try {
        // 修改为使用后端提供的readPlcStatus接口
        const response = await axios.get(`/api/plcSend/test/status?projectId=${module.module}`);
        
        // 由于接口返回的是PlcBaseData对象而非直接的状态信息，需要进行适配
        const index = this.moduleStatusData.findIndex(m => m.module === module.module);
        if (index !== -1) {
          // 假设onlineState字段表示模块是否在线
          const status = response.data ? 'online' : 'offline';
          
          this.moduleStatusData[index] = {
            ...this.moduleStatusData[index],
            status: status,
            lastUpdate: new Date().toLocaleTimeString(),
            details: response.data || {}
          };
          
          ElMessage.success('模块状态更新成功');
        }
      } catch (error) {
        console.error('检查模块状态失败:', error);
        ElMessage.error('检查模块状态失败');
      }
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'running': 'warning',
        'success': 'success',
        '通过': 'success',
        'failed': 'danger',
        '失败': 'danger',
        'online': 'success',
        'offline': 'danger',
        'idle': 'info'
      };
      return statusMap[status] || 'primary';
    },
    
    // 重置配置
    resetConfig() {
      ElMessageBox.confirm('确定要重置所有配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.resetAll();
      }).catch(() => {
        // 用户取消
      });
    }
  }
};
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  overflow: hidden;
}

.dashboard-header {
  background-color: #2c3e50;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}

.header-left h2 {
  margin: 0;
  font-size: 1.4rem;
  font-weight: 500;
  display: flex;
  align-items: center;
}

.header-left h2::before {
  content: "⚙️";
  margin-right: 10px;
}

.header-right {
  display: flex;
  gap: 12px;
}

.header-right .el-button {
  transition: all 0.2s ease;
}

.header-right .el-button:hover {
  transform: translateY(-2px);
}

.dashboard-content {
  display: flex;
  flex: 1;
  overflow: hidden;
  padding: 15px;
  gap: 15px;
}

.config-aside {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 15px;
  width: 400px;
  flex-shrink: 0;
  overflow-y: auto;
}

.monitor-main {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}

.config-section {
  margin-bottom: 20px;
  border-radius: 6px;
  border: none;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.config-section:hover {
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

.config-section h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  color: #303133;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 10px;
  display: flex;
  align-items: center;
}

.config-section h3 i {
  margin-right: 8px;
  color: #409EFF;
}

.full-width {
  width: 100%;
  margin-bottom: 10px;
}

.monitor-section {
  height: 100%;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.section-header h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
  display: flex;
  align-items: center;
}

.section-header h3 i {
  margin-right: 8px;
  color: #409EFF;
}

.section-actions {
  display: flex;
  gap: 10px;
}

.section-actions .el-button {
  transition: all 0.2s ease;
}

.section-actions .el-button:hover {
  transform: translateY(-2px);
}

.task-container, .result-container, .result-list {
  margin-bottom: 25px;
}

.task-container h4, .result-container h4, .result-list h4 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #606266;
  font-weight: 500;
  display: flex;
  align-items: center;
}

.task-container h4::before,
.result-container h4::before,
.result-list h4::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 16px;
  background-color: #409EFF;
  margin-right: 8px;
  border-radius: 2px;
}

.task-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  background-color: #f9fafc;
  border-radius: 6px;
  border-left: 4px solid #409EFF;
  width: calc(50% - 7.5px);
  box-sizing: border-box;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.task-item:hover {
  background-color: #ecf5ff;
  transform: translateY(-2px);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

.task-info {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.task-id {
  font-weight: 500;
  color: #303133;
  min-width: 60px;
}

.task-module, .task-project {
  color: #606266;
  font-size: 14px;
  background-color: rgba(144, 147, 153, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
}

.result-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  gap: 15px;
}

.stat-item {
  text-align: center;
  padding: 20px 15px;
  background-color: #f9fafc;
  border-radius: 6px;
  flex: 1;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-number {
  transform: scale(1.05);
}

.stat-number.pass {
  color: #67C23A;
}

.stat-number.fail {
  color: #F56C6C;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  background-color: #f9fafc;
  border-radius: 6px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 15px;
  color: #dcdfe6;
}

.empty-text {
  font-size: 16px;
}

/* 字段管理样式 */
.field-container {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #f0f0f0;
  border-radius: 6px;
}

.field-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 500;
}

.add-btn {
  color: #409EFF;
  transition: all 0.2s;
}

.add-btn:hover {
  color: #66b1ff;
  background-color: rgba(64, 158, 255, 0.1);
}

.field-list {
  padding: 10px;
}

.field-item {
  display: flex;
  align-items: center;
  padding: 10px 8px;
  border-bottom: 1px solid #f5f5f5;
  transition: background-color 0.2s;
}

.field-item:last-child {
  border-bottom: none;
}

.field-item:hover {
  background-color: #f9fafc;
}

.field-address {
  display: flex;
  align-items: center;
  margin-left: auto;
  margin-right: 10px;
}

.field-address-label {
  width: 70px;
  text-align: right;
  margin-right: 8px;
  font-size: 12px;
  color: #909399;
}

.remove-btn {
  color: #ff4949;
  opacity: 0.7;
  transition: all 0.2s;
}

.remove-btn:hover {
  color: #ff4949;
  opacity: 1;
  background-color: rgba(255, 73, 73, 0.1);
}

/* 批量测试弹窗样式 */
.batch-test-container {
  max-height: 500px;
  overflow-y: auto;
}

.batch-actions {
  margin-bottom: 15px;
}

.batch-footer {
  margin-top: 20px;
  text-align: right;
}

/* 任务导入弹窗样式 */
.import-footer {
  margin-top: 20px;
  text-align: right;
}

/* 任务详情弹窗样式 */
.task-detail-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 10px 0;
}

.task-timeline {
  margin-top: 20px;
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
}

.task-timeline h4 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #606266;
}

/* 状态弹窗样式 */
.status-container {
  min-height: 200px;
}

/* 表格样式优化 */
.el-table {
  border-radius: 6px;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: 500;
}

.el-table--border th,
.el-table--border td {
  border-right: 1px solid #f0f0f0;
}

.el-table--border::after,
.el-table--group::after,
.el-table::before {
  background-color: #f0f0f0;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #c0c4cc;
}
</style>