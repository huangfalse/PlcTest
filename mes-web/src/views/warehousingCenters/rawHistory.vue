<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {onMounted, ref} from "vue"
import {fetchHistoricalTasks, HistoryTaskParams} from '@/api/glassStorage'
import {ElMessage, ComponentSize} from 'element-plus'
const size = ref<ComponentSize>('default')
const { t } = useI18n()
const tableData = ref([])
const totalRecords = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const timeRange = ref<[string, string]>(['', ''])
const selectTaskState = ref<string[]>([])
const startSlot = ref('')
const targetSlot = ref('')
let getglobalDate = window.localStorage.getItem('getglobalDate')
// 通用查询方法
const queryHistoricalTasks = async (params: HistoryTaskParams) => {
  try {
    const result = await fetchHistoricalTasks(params)
    tableData.value = result.data
    totalRecords.value = result.total
  } catch (error) {
  }
}
// 页面初始化
const init = () => {
  queryHistoricalTasks({
    pageNo: currentPage.value,
    pageSize: pageSize.value,
    beginDate: timeRange.value[0] || window.localStorage.getItem('startTime') || '',
    endDate: timeRange.value[1] || (getglobalDate ? formatTimestamp(new Date(getglobalDate)) : formatTimestamp(new Date()))
  })
}
// 查询按钮
const onSearch = () => {
  queryHistoricalTasks({
    pageNo: 1,
    pageSize: pageSize.value,
    taskState: selectTaskState.value,
    taskType: [],
    beginDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
    endDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || ''
  })
}
// 分页
const onPageChange = (page: number) => {
  currentPage.value = page
  queryHistoricalTasks({
    pageNo: page,
    pageSize: pageSize.value,
    taskState: selectTaskState.value,
    beginDate: timeRange.value[0] || '',
    endDate: timeRange.value[1] || '',
    taskType: [],
  })
}
const handleSizeChange = (val: number) => {
  queryHistoricalTasks({
    pageNo: 1,
    pageSize: pageSize.value,
    taskState: selectTaskState.value,
    beginDate: timeRange.value[0] || '',
    endDate: timeRange.value[1] || '',
    taskType: [],
  })
}
// 格式化时间戳为年月日时间字符串的函数
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1，并补零
  const day = String(date.getDate()).padStart(2, '0'); // 补零
  const hours = String(date.getHours()).padStart(2, '0'); // 补零（如果需要显示时间）
  const minutes = String(date.getMinutes()).padStart(2, '0'); // 补零（如果需要显示时间）
  const seconds = String(date.getSeconds()).padStart(2, '0'); // 补零（如果需要显示时间）
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
const shortcuts = [
  {
    text: '最近一周',
    value: () => {
      const backendTime = getglobalDate ? new Date(getglobalDate) : new Date()
      const oneWeekAgo = getglobalDate ? new Date(getglobalDate) : new Date()
      oneWeekAgo.setHours(0, 0, 0, 0);
      oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
      timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)]
      window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const backendTime = getglobalDate ? new Date(getglobalDate) : new Date()
      const oneWeekAgo = getglobalDate ? new Date(getglobalDate) : new Date()
      oneWeekAgo.setHours(0, 0, 0, 0);
      oneWeekAgo.setMonth(oneWeekAgo.getMonth() - 1)
      timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)]
      window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const backendTime = getglobalDate ? new Date(getglobalDate) : new Date()
      const oneWeekAgo = getglobalDate ? new Date(getglobalDate) : new Date()
      oneWeekAgo.setHours(0, 0, 0, 0);
      oneWeekAgo.setMonth(oneWeekAgo.getMonth() - 3)
      timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)]
      window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
    },
  },
]
// 格式化后端时间并计算一周前的时间
const defaultTime = ref<[Date, Date]>([new Date(), new Date()]);
function parseAndSetTime() {
  const backendTime = getglobalDate ? new Date(getglobalDate) : new Date();
  const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 减去7天
  oneWeekAgo.setHours(0, 0, 0, 0);
  timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
  window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
}
function getStatusType(taskState) {  
switch (taskState) {
  case 0:
    return 'primary';
  case 1:
    return 'warning';
  case 2:
    return 'success';
  case 3:
    return 'danger';
  case 7:
    return 'warning';
  case 8:
    return 'danger';
}  
}
function getStatusText(taskState) {  
switch (taskState) {
  case 0:
    return t('Mounting.pending');//待执行
  case 1:
    return t('Mounting.inAction');//执行中
  case 2:
    return t('film.missionAccomplished');//任务完成
  case 3:
    return t('film.taskFailed');//任务失败
  case 7:
    return t('Mounting.paused');//暂停
  case 8:
    return t('Mounting.dilapidation');//任务破损
}
function getTaskType(){
  switch (taskType) {
    case 1:  
      return t('searchOrder.onFilm');
    case 2:  
      return t('searchOrder.outFilm');
    case 3:  
      return t('searchOrder.dispatch');
  }
}
}
onMounted(() => {
  parseAndSetTime();
  init();
});
  </script>
<template>  
<div>  
  <div style="display: flex; width: 1570px;">
    <el-select v-model="selectTaskState" filterable :placeholder="$t('Mounting.taskStatus')" clearable multiple
    style="width: 200px;margin-left: 10px;">
      <el-option :label="$t('Mounting.pending')" value="0"></el-option>
      <el-option :label="$t('Mounting.inAction')" value="1"></el-option>
      <el-option :label="$t('film.missionAccomplished')" value="2"></el-option>
      <el-option :label="$t('film.taskFailed')" value="3"></el-option>
      <el-option :label="$t('Mounting.paused')" value="7"></el-option>
      <el-option :label="$t('Mounting.dilapidation')" value="8"></el-option>
  </el-select>
  <el-date-picker
      style="margin-left: 15px;"
       v-model="timeRange"
       type="datetimerange"
       :shortcuts="shortcuts"
       range-separator="至"
       :start-placeholder="$t('workOrder.startTime')"
       :end-placeholder="$t('workOrder.endTime')"
     />
    <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="onSearch()">{{$t('order.inquire')}}</el-button>
     </div>
        <el-table ref="table" style="margin-top: 20px;height: 580px;width: 1770px;" :data="tableData" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
          <el-table-column prop="id" align="center" :label="$t('Mounting.projectID')" min-width="40" />
          <!-- <el-table-column prop="rawSequence" align="center" :label="$t('Mounting.originalPreface')" min-width="40" /> -->
          <el-table-column prop="startSlot" align="center" :label="$t('film.startingGrid')" min-width="40" />
          <el-table-column prop="endSlot" align="center" :label="$t('film.targetGrid')" min-width="40" />
          <el-table-column prop="rawMargin" align="center" :label="$t('film.originalEdge')" min-width="60" />
          <el-table-column prop="rawGlassWidth" align="center" :label="$t('film.width')" min-width="40" />
          <el-table-column prop="rawGlassHeight" align="center" :label="$t('film.height')" min-width="40" />
          <el-table-column prop="rawGlassThickness" align="center" :label="$t('film.thickness')" min-width="60" />
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="40" />
          <el-table-column prop="rawQuantity" align="center" :label="$t('film.remainingNumberOriginalFilms')" min-width="50" />
          <el-table-column
            align="center"
            :label="$t('Mounting.taskStatus')"
            min-width="50"
            prop="taskState"
          >
          <template #default="scope">  
        <el-tag :type="getStatusType(scope.row.taskState)">  
          {{ getStatusText(scope.row.taskState) }}  
        </el-tag>  
      </template> 
          </el-table-column>
          <el-table-column prop="taskType" align="center" :label="$t('searchOrder.taskType')" min-width="50" />
          <el-table-column prop="formattedCreateTime" align="center" :label="$t('workOrder.startTime')" min-width="55" />
          <el-table-column prop="formattedUpdateTime" align="center" :label="$t('workOrder.endTime')" min-width="55" />
        </el-table>
  <div style="margin-top: 20px;margin-left: 40%;">
       <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 15, 20]"
      :size="large"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalRecords"
      @size-change="handleSizeChange"
      @current-change="onPageChange"
      style="margin-top: 10px;"
    />
    </div>
  </div>
  </template>
<style scoped>
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>