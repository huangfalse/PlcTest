<script lang="ts" setup>
import request from "@/utils/request"
import {ElMessage} from 'element-plus'
import {inject, onMounted, reactive, ref} from "vue"
import {useI18n} from 'vue-i18n'

const { t } = useI18n()
const globalDate = inject('globalDate')
const timeRange = ref([])
const reportData = ref([])
const selectOptionsType = ref([])
const selectOptionsReason = ref([])
const selectOptionsResponsible = ref([])
const selectOptionsEquipment = ref([])
const currentPage1 = ref(1)
const currentPage2 = ref(1)
const totalRecords1 = ref(0)
const totalRecords2 = ref(0)
const pageSize = ref(5)
const report = ref({type: 'completed'})
const completedShow = ref(true)
const breakageShow = ref(false)
const selectedType = ref(true)
const selectedStatus = ref(false)
const selectedRows = ref<any[]>([]);// 选中行数据存储
const batchEditData = ref<any>({})// 破损状态存储批量编辑数据
// 多选变化处理
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection;
};
//  完工-报工逻辑
const handleConfirmCompleted = async () => {
  const validStates = [1, 7];
  // 筛选有效行
  const validRows = selectedRows.value.filter(row =>
      validStates.includes(Number(row?.reportState))
  );
  if (validRows.length === 0) {
    ElMessage.warning(t('report.selectRightReport'))
    return;
  }
  try {
    const response = await request.post('/loadGlass/report/submitRport', validRows);
    if (response.code === 200) {
      ElMessage.success(response.message);
      fetchCompletedData();
      selectedRows.value = [];
    } else {
      ElMessage.warning(response.message);
    }
  } catch (error) {
    ElMessage.error('请求异常');
  }
};
const dialogVisible = ref(false)
const batchEditForm = reactive({
  breakageType: '',
  breakageReason: '',
  responsibleProcess: '',
  responsiblePersonnel: '',
  responsibleTeam: '',
  responsibleEquipment: '',
  remark: ''
})
// 打开批量编辑弹窗
const openBatchEditDialog = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning(t('report.selectRecord'))
    return
  }
  // 重置表单
  Object.assign(batchEditForm, {
    breakageType: '',
    breakageReason: '',
    responsibleProcess: '',
    responsiblePersonnel: '',
    responsibleTeam: '',
    responsibleEquipment: '',
    remark: ''
  })
  dialogVisible.value = true
}
// 新增类型变更处理函数
const handleReportTypeChange = (type: string) => {
  if (type === 'completed') {
    fetchCompletedData();
    breakageShow.value = false
    completedShow.value = true
  } else if (type === 'breakage') {
    fetchDamageData();
    completedShow.value = false
    breakageShow.value = true
  }
};
const handlePageChange1 = (newPage) => {
  currentPage1.value = newPage;
  fetchCompletedData();
};
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  fetchDamageData();
};
// 完工数据调用
const fetchCompletedData = async () => {
  try {
    const response = await request.post('/loadGlass/report/queryReport', {
      pageNo: currentPage1.value,
      pageSize: pageSize.value,
      state: 3,
      glassId: '',
      startTime: (timeRange.value && timeRange.value[0]) || '',
      endTime: (timeRange.value && timeRange.value[1]) || '',
      workingProcedure: ''
    });
    if (response.code === 200 && response.data?.records) {

      reportData.value = response.data.records;
      totalRecords1.value = response.data.total || 0
    } else {
      ElMessage.warning(response.message || '请求失败');
    }
  } catch (error) {
    ElMessage.error('请求异常');
  }
};
// 查询完工数据
const selectCompletedData = async () => {
  const response = await request.post("/loadGlass/report/queryReport", {
    pageNo: currentPage1.value,
    pageSize: pageSize.value,
    startTime: (timeRange.value && timeRange.value[0]) || '',
    endTime: (timeRange.value && timeRange.value[1]) || '',
    workingProcedure: report.value.workingProcedure,
    reportState: report.value.status
  })
  if (report.value.type == 8) {
    selectedType.value = false;
  } else {
    selectedType.value = true;
  }
  if ((report.value.status == 1 || report.value.status == 7) && (report.value.type == 8 || report.value.type == 1)) {
    selectedStatus.value = true;
  } else {
    selectedStatus.value = false;
  }
  if (response.code === 200) {
    const formattedData = response.data.map(record => ({
      ...record,
      formattedCreateTime: formatTimestamp(record.damageTime),
    }));
    reportData.value = formattedData;
    ElMessage.success(response.message);
  } else {
    ElMessage.error(response.message);
  }
};
// 破损数据调用
const fetchDamageData = async () => {
  try {
    const response = await request.post('/loadGlass/damage/queryDamage', {
      pageNo: currentPage2.value,
      pageSize: pageSize.value,
      state: 1,
      glassId: '',
      startTime: (timeRange.value && timeRange.value[0]) || '',
      endTime: (timeRange.value && timeRange.value[1]) || '',
      workingProcedure: ''
    });
    if (response.code === 200 && response.data?.records) {
      reportData.value = response.data.records;
      totalRecords2.value = response.data.total || 0
    } else {
      ElMessage.warning(response.message || '请求失败');
    }
  } catch (error) {
    ElMessage.error('请求异常');
  }
};
// 批量编辑提交逻辑
const submitBatchEdit = () => {
  // 存储编辑数据到组件状态
  batchEditData.value = {...batchEditForm}
  // 遍历所有选中行，更新数据
  selectedRows.value.forEach(row => {
    row.breakageType = batchEditForm.breakageType
    row.breakageReason = batchEditForm.breakageReason
    row.responsibleProcess = batchEditForm.responsibleProcess
    row.responsiblePersonnel = batchEditForm.responsiblePersonnel
    row.responsibleTeam = batchEditForm.responsibleTeam
    row.responsibleEquipment = batchEditForm.responsibleEquipment
  })
  dialogVisible.value = false
  ElMessage.success()
}
// 报工提交逻辑
const handleConfirmBreakage = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning(t('report.selectRecord'))
    return
  }
  const validStates = [1, 7];
  const validRows = selectedRows.value.filter(row =>
      validStates.includes(Number(row?.reportState))
  );
  if (validRows.length === 0) {
    ElMessage.warning(t('report.selectRightReport'))
    return;
  }
  try {
    // 构造请求数据（整合批量编辑数据）
    const requestData = {
      breakageType: batchEditData.value?.breakageType || '',
      breakageReason: batchEditData.value?.breakageReason || '',
      responsibleProcess: batchEditData.value?.responsibleProcess || '',
      responsiblePersonnel: batchEditData.value?.responsiblePersonnel || '',
      responsibleTeam: batchEditData.value?.responsibleTeam || '',
      responsibleEquipment: batchEditData.value?.responsibleEquipment || '',
      damageList: validRows.map(row => ({
        id: row.id,
        glassId: row.glassId,
        deviceId: row.deviceId,
        engineerId: row.engineerId,
        temperingLayoutId: row.temperingLayoutId,
        breakageType: row.breakageType,
        breakageReason: row.breakageReason,
        responsibleProcess: row.responsibleProcess,
        responsiblePersonnel: row.responsiblePersonnel,
        responsibleTeam: row.responsibleTeam,
        responsibleEquipment: row.responsibleEquipment
      }))
    }
    const response = await request.post('/loadGlass/damage/submitRport', requestData)
    if (response.code === 200) {
      ElMessage.success(response.message)
      fetchDamageData()
      selectedRows.value = []
    } else {
      ElMessage.error(response.message)
    }  
  } catch (error) {
    ElMessage.error()
  }
}
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
// 格式化后端时间并计算一周前的时间
const defaultTime = ref<[Date, Date]>([new Date(), new Date()]);
function parseAndSetTime() {
  const backendTime = new Date(globalDate);
  const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000);
  oneWeekAgo.setHours(0, 0, 0, 0);
  timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
  window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
}
function gettypeType(type: number) {  
  switch (type) {
    case 9: 
      return 'success';  
    case 8: 
      return 'danger';  
  }
}  
function gettypeText(type: number) {
  switch (type) {
    case 9:  
      return t('report.takeout'); 
    case 8:  
      return t('report.broke'); 
  }  
}
function getStatusType(status: number) {  
  switch (status) {  
    case 1:
      return 'warning';  
    case 3: 
      return 'info';  
    case 7: 
      return 'success';  
    case 8: 
      return 'primary';  
  }
}  
function getStatusText(status: number) {
  switch (status) {
    case 1:  
      return t('report.dReportWork');
    case 3:  
      return t('report.reportedWork'); 
    case 7:  
      return t('report.hasBeenFilled'); 
    case 8:  
      return t('report.synced'); 
  }  
}
onMounted(() => {
  fetchCompletedData();
  parseAndSetTime();
});
</script>
<template>
  <!-- 完工 -->
  <div v-if="completedShow" style="height: 600px;">
    <div style="display: flex; flex-direction: row; align-items: center; margin-top: 20px;">
      <el-select v-model="report.type" :placeholder="$t('report.typeSelection')" style="margin-left: 10px;width: 100px;"
                 @change="handleReportTypeChange">
        <el-option :label="$t('report.completed')" value="completed"></el-option>
        <el-option :label="$t('workOrder.breakage')" value="breakage"></el-option>
      </el-select>
      <span style="margin-left: 10px;" class="demonstration">{{ $t('report.productionTime') }}</span>
      <el-date-picker
          v-model="timeRange"
          :end-placeholder="$t('workOrder.endTime')"
          :start-placeholder="$t('workOrder.startTime')"
          range-separator="至"
          style="margin-left: 15px;"
          type="datetimerange"
          value-format="YYYY-MM-DD hh:mm:ss">
      </el-date-picker>
      <el-select v-model="report.status" :placeholder="$t('report.stateSelection')" style="margin-left: 10px;">
        <el-option :label="$t('report.dReportWork')" value="1"></el-option>
        <el-option :label="$t('report.reportedWork')" value="3"></el-option>
        <el-option :label="$t('report.hasBeenFilled')" value="7"></el-option>
        <el-option :label="$t('report.synced')" value="8"></el-option>
      </el-select>
      <el-select v-model="report.workingProcedure" :placeholder="$t('report.processSelection')"
                 style="margin-left: 10px;">
        <el-option :label="$t('report.all')" value="0"></el-option>
        <el-option :label="$t('report.incise')" value="切割"></el-option>
        <el-option :label="$t('report.edging')" value="磨边"></el-option>
        <el-option :label="$t('report.steel')" value="钢化"></el-option>
      </el-select>
      <el-button style="margin-left: 10px;" type="primary" @click="selectCompletedData()">
        {{ $t('order.inquire') }}
      </el-button>
      <el-button
          style="margin-left: 10px;"
          type="success"
          @click="handleConfirmCompleted"
      >
        {{ $t('report.signingWork') }}
      </el-button>
    </div>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table
            ref="table"
            :data="reportData"
            :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
            height="700"
            row-key="id"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"/>
          <el-table-column :label="$t('Mounting.projectNumber')" align="center" min-width="120" prop="engineerId"/>
          <el-table-column :label="$t('Mounting.processCards')" align="center" min-width="160" prop="flowCardId"/>
          <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="130" prop="glassId"/>
          <el-table-column :label="$t('report.layer')" align="center" min-width="135" prop="layer"/>
          <el-table-column :label="$t('workOrder.line')" align="center" min-width="135" prop="line"/>
          <el-table-column :label="$t('report.deviceName')" align="center" min-width="135" prop="deviceName"/>
          <el-table-column :label="$t('large.orderNumber')" align="center" min-width="135" prop="productSortNumber"/>
          <el-table-column :label="$t('report.remark')" align="center" min-width="135" prop="remark"/>
          <el-table-column :label="$t('report.signingWorkState')" align="center" min-width="120" prop="reportState">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.reportState)">
                {{ getStatusText(scope.row.reportState) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('report.reportName')" align="center" min-width="135" prop="teamsGroupsName"/>
          <el-table-column :label="$t('report.process')" align="center" min-width="135" prop="workingProcedure"/>
          <el-table-column :label="$t('searchOrder.updateTime')" align="center" min-width="160" prop="updateTime"/>
          <el-table-column :label="$t('Mounting.creationTime')" align="center" min-width="160" prop="createTime"/>
        </el-table>
        <div style="margin-top: 20px;margin-left: 40%;">
          <el-pagination
              v-model:current-page="currentPage1"
              :disabled="disabled"
              :page-size="pageSize"
              :size="large"
              :total="totalRecords1"
              layout="prev, pager, next, jumper"
              style="margin-top: 10px;"
              @current-change="handlePageChange1"
          />
        </div>
      </div>
    </el-card>
  </div>
  <!-- 破损 -->
  <div v-if="breakageShow" style="height: 600px;">
    <div style="display: flex; flex-direction: row; align-items: center; margin-top: 20px;">
      <el-select v-model="report.type" :placeholder="$t('report.typeSelection')" style="margin-left: 10px;width: 100px;"
                 @change="handleReportTypeChange">
        <el-option :label="$t('report.completed')" value="completed"></el-option>
        <el-option :label="$t('workOrder.breakage')" value="breakage"></el-option>
      </el-select>
      <span class="demonstration" style="margin-left: 10px;">{{ $t('report.productionTime') }}</span>
      <el-date-picker
          v-model="timeRange"
          :end-placeholder="$t('workOrder.endTime')"
          :start-placeholder="$t('workOrder.startTime')"
          range-separator="至"
          style="margin-left: 15px;"
          type="datetimerange"
          value-format="YYYY-MM-DD hh:mm:ss">
      </el-date-picker>
      <el-select v-model="report.status" :placeholder="$t('report.stateSelection')" style="margin-left: 10px;">
        <el-option :label="$t('report.dReportWork')" value="1"></el-option>
        <el-option :label="$t('report.reportedWork')" value="3"></el-option>
        <el-option :label="$t('report.hasBeenFilled')" value="7"></el-option>
        <el-option :label="$t('report.synced')" value="8"></el-option>
      </el-select>
      <el-select v-model="report.workingProcedure" :placeholder="$t('report.processSelection')"
                 style="margin-left: 10px;">
        <el-option :label="$t('report.all')" value="0"></el-option>
        <el-option :label="$t('report.incise')" value="切割"></el-option>
        <el-option :label="$t('report.edging')" value="磨边"></el-option>
        <el-option :label="$t('report.steel')" value="钢化"></el-option>
      </el-select>
      <el-button style="margin-left: 10px;" type="primary" @click="selectReportData()">
        {{ $t('order.inquire') }}
      </el-button>
      <el-button
          type="primary"
          @click="openBatchEditDialog"
      >
        {{ $t('report.fillingBulk') }}
      </el-button>
      <el-button
          style="margin-left: 10px;"
          type="success"
          @click="handleConfirmBreakage"
      >
        {{ $t('report.signingWork') }}
      </el-button>
    </div>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table
            ref="table"
            :data="reportData"
            :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
            height="700"
            row-key="id"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"/>
          <el-table-column :label="$t('Mounting.projectNumber')" align="center" min-width="120" prop="engineerId"/>
          <el-table-column :label="$t('Mounting.processCards')" align="center" min-width="160" prop="flowCardId"/>
          <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="130" prop="glassId"/>
          <el-table-column :label="$t('report.layer')" align="center" min-width="135" prop="layer"/>
          <el-table-column :label="$t('workOrder.line')" align="center" min-width="135" prop="line"/>
          <el-table-column :label="$t('report.deviceName')" align="center" min-width="135" prop="deviceName"/>
          <el-table-column :label="$t('large.orderNumber')" align="center" min-width="135" prop="productSortNumber"/>
          <el-table-column :label="$t('report.remark')" align="center" min-width="135" prop="remark"/>
          <!-- <el-table-column prop="reportState" align="center" :label="$t('report.signingWorkState')" min-width="135" /> -->
          <el-table-column :label="$t('report.signingWorkState')" align="center" min-width="120" prop="reportState">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.reportState)">
                {{ getStatusText(scope.row.reportState) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('report.reportName')" align="center" min-width="135" prop="teamsGroupsName"/>
          <el-table-column :label="$t('report.process')" align="center" min-width="135" prop="workingProcedure"/>
          <el-table-column :label="$t('searchOrder.updateTime')" align="center" min-width="160" prop="updateTime"/>
          <el-table-column :label="$t('Mounting.creationTime')" align="center" min-width="160" prop="createTime"/>
        </el-table>
        <div style="margin-top: 20px;margin-left: 40%;">
          <el-pagination
              v-model:current-page="currentPage2"
              :disabled="disabled"
              :page-size="pageSize"
              :size="large"
              :total="totalRecords2"
              layout="prev, pager, next, jumper"
              style="margin-top: 10px;"
              @current-change="handlePageChange2"
          />
        </div>
      </div>
    </el-card>
    <!-- 批量编辑弹窗 -->
    <el-dialog v-model="dialogVisible" width="50%">
      <el-form :model="batchEditForm" label-width="120px">
        <el-form-item :label="$t('report.typeBreakage')">
          <el-select
              v-model="batchEditForm.breakageType"
              :placeholder="$t('report.causeBreakageSelection')"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option v-for="item in selectOptionsType" :key="item.basicName" :label="item.basicName"
                       :value="item.basicName"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('report.causeBreakage')">
          <el-select
              v-model="batchEditForm.breakageReason"
              :placeholder="$t('report.typeBreakageSelection')"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option v-for="item in selectOptionsReason" :key="item.basicName" :label="item.basicName"
                       :value="item.basicName"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('report.responsibleProcess')">
          <el-select
              v-model="batchEditForm.responsibleProcess"
              :placeholder="$t('report.typeBreakageSelection')"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option :label="$t('report.incise')" value="切割"></el-option>
            <el-option :label="$t('report.edging')" value="磨边"></el-option>
            <el-option :label="$t('report.steel')" value="钢化"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('report.responsiblePersonnel')">
          <el-input
              v-model="batchEditForm.responsiblePersonnel"
              :placeholder="$t('report.responsiblePersonnelEnter')"
              autocomplete="off"
          />
        </el-form-item>
        <el-form-item :label="$t('report.responsibleTeam')">
          <el-select
              v-model="batchEditForm.responsibleTeam"
              :placeholder="$t('report.responsibleTeamSelection')"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option v-for="item in selectOptionsResponsible" :key="item.basicName" :label="item.basicName"
                       :value="item.basicName"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('report.responsibleEquipment')">
          <el-select
              v-model="batchEditForm.responsibleEquipment"
              :placeholder="$t('report.responsibleEquipmentSelection')"
              clearable
              filterable
              style="width: 100%"
          >
            <el-option v-for="item in selectOptionsEquipment" :key="item.basicName" :label="item.basicName"
                       :value="item.basicName"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">{{ $t('Mounting.cancel') }}</el-button>
          <el-button type="primary" @click="submitBatchEdit">{{ $t('Mounting.confirm') }}</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>