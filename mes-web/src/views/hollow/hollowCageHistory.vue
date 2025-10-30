<template>  
<div>  
<div style="display: flex;width: 1770px;">
  <el-input v-model="glassId" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" :placeholder="$t('searchOrder.glassIDEnter')" />
  <el-input v-model="startSlot" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputa" :placeholder="$t('film.startingGrid')" />
  <el-input v-model="targetSlot" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb" :placeholder="$t('film.targetGrid')" />
        <el-select v-model="selectValuesa[0]" filterable :placeholder="$t('searchOrder.taskstatus')" clearable
        style="width: 200px;margin-left: 10px;">
          <el-option :label="$t('Mounting.pending')" value="0"></el-option>
          <el-option :label="$t('Mounting.inAction')" value="1"></el-option>
          <el-option :label="$t('film.missionAccomplished')" value="2"></el-option>
          <el-option :label="$t('film.taskFailed')" value="3"></el-option>
          <el-option :label="$t('Mounting.paused')" value="7"></el-option>
          <el-option :label="$t('Mounting.dilapidation')" value="8"></el-option>
      </el-select>
      <el-select v-model="selectValuesa[1]" filterable :placeholder="$t('searchOrder.taskType')" clearable
        style="width: 200px;margin-left: 10px;">
        <el-option :label="$t('searchOrder.onFilm')" value="4"></el-option>
        <el-option :label="$t('searchOrder.outFilm')" value="5"></el-option>
        <el-option :label="$t('searchOrder.dispatch')" value="6"></el-option>
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
        <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="handleClick()">{{$t('order.inquire')}}</el-button>
   </div>
  <div style="margin-bottom: 10px;margin-top: 20px;margin-left: 20px;">
      <el-form>
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="5">
              <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('large.hollowGlassCountOutOne')"  style="width: 14vw">
                {{ hollowCountOutOne }}
              </el-form-item>
              </div>
          </el-col>
          <el-col :span="5">
              <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('large.hollowGlassTotalAreaOutOne')" style="width: 14vw">
                {{ hollowTotalAreaOutOne }}
              </el-form-item>
              </div>
          </el-col>
          <el-col :span="5">
            <div id="dta" style="font-size: 15px;">
          <el-form-item :label="$t('large.hollowGlassCountOutTwo')" style="width: 14vw">
                {{ hollowCountOutTwo }}
              </el-form-item>
              </div>
          </el-col>
          <el-col :span="4">
              <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('large.hollowGlassTotalAreaOutTwo')" style="width: 14vw">
                {{ hollowTotalAreaOutTwo }}
              </el-form-item>
              </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
      <el-table ref="table" style="margin-top: 20px;height: 580px;width: 1770px;" :data="tableDatax" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
    <el-table-column prop="glassId" align="center" :label="$t('Mounting.glassID')" min-width="80" />
    <el-table-column prop="startSlot" align="center" :label="$t('film.startingGrid')" min-width="80" />
        <el-table-column prop="targetSlot" align="center" :label="$t('film.targetGrid')" min-width="80" />
        <el-table-column
          align="center"
          :label="$t('Mounting.taskStatus')"
          min-width="80"
          prop="taskState"
        >
        <template #default="scope">  
      <el-tag :type="getStatusTypeb(scope.row.taskState)">  
        {{ getStatusTextb(scope.row.taskState) }}  
      </el-tag>  
    </template> 
        </el-table-column>
        <el-table-column
          align="center"
          :label="$t('searchOrder.taskType')"
          min-width="80"
          prop="taskType"
        >
        <template #default="scope">  
      <el-tag :type="getStatusTypea(scope.row.taskType)">  
        {{ getStatusTexta(scope.row.taskType) }}  
      </el-tag>  
    </template> 
        </el-table-column>
        <el-table-column prop="formattedCreateTime" align="center" :label="$t('workOrder.startTime')" min-width="100" />
        <el-table-column prop="formattedUpdateTime" align="center" :label="$t('workOrder.endTime')" min-width="100" />
      </el-table>
<div style="margin-top: 20px;margin-left: 40%;">
    <el-pagination
      v-model:current-page="currentPage2"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 15, 20]"
      :size="large"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="totalRecords"
      @size-change="handleSizeChange"
      @current-change="handlePageChange2"
      style="margin-top: 10px;"
    />
  </div>
</div>
</template>  
<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import request from "@/utils/request"
import {inject, onMounted, reactive, ref} from "vue";
import {ElMessage, ComponentSize} from 'element-plus'
let language = ref(localStorage.getItem('lang') || 'zh')
const {t} = useI18n()
const size = ref<ComponentSize>('default')
const globalDate = inject('globalDate');
const router = useRouter()
const timeRange = ref([])
const selectValuesa = reactive([]);
const tableDatax = ref([])
const currentPage2 = ref(1)
const totalRecords = ref(0)
const hollowCountOutOne = ref(0)
const hollowCountOutTwo = ref(0)
const hollowTotalAreaOutOne = ref(0)
const hollowTotalAreaOutTwo = ref(0)
const pageSize = ref(20)
const glassId = ref('');
const startSlot = ref('');
const targetSlot = ref('');
let getglobalDate = window.localStorage.getItem('getglobalDate')
// 处理输入事件
const handleInputa = (value: string) => {
  const isNumeric = /^\d*$/.test(value);
  if (isNumeric) {
    startSlot.value = value;
  } else {
    startSlot.value = '';
  }
};
const handleInputb = (value: string) => {
  const isNumeric = /^\d*$/.test(value);
  if (isNumeric) {
    targetSlot.value = value;
  } else {
    targetSlot.value = '';
  }
};
const Daily = async () => {
try {
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowDailyProduction", {
      pageNo: 1,
      pageSize: pageSize.value,
      glassId: '',
      startSlot: 0,
      targetSlot: 0,
      taskStateList: [],
      taskTypeList: [],
      beginDate: startTime,
      endDate: getglobalDate
  })
    if (response.code == 200) {
      hollowCountOutOne.value = response.data.hollowCountOutOne
      hollowCountOutTwo.value = response.data.hollowCountOutTwo
      hollowTotalAreaOutOne.value = response.data.hollowTotalAreaOutOne
      hollowTotalAreaOutTwo.value = response.data.hollowTotalAreaOutTwo
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
const historical = async () => {
try {
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowBigStorageCageHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      glassId: '',
      startSlot: 0,
      targetSlot: 0,
      taskStateList: [],
      taskTypeList: [],
      beginDate: startTime,
      endDate: getglobalDate
  })
    if (response.code == 200) {
      ElMessage.success(response.message);
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
        // tableDatax.value = response.data.records;
        tableDatax.value = formattedData;
      totalRecords.value = response.data.total
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
const handlePageChange2 = (newPage) => {
currentPage2.value = newPage;
window.localStorage.setItem('pagenumber', currentPage2.value)
historicala(currentPage2.value);
Dailya(currentPage2.value);
};
const handleSizeChange = (val: number) => {
  historicala(currentPage2.value);
Dailya(currentPage2.value);
}
const historicala = async (page) => {
try { 
let celllist=[]
let stateList=[]
let pstartSlot= ''
let ptargetSlot=''
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
if(selectValuesa[1]!=null&&selectValuesa[1]!='undefined'){
  if(selectValuesa[1]!=""){
    stateList=[selectValuesa[1]];
  }
}
if(startSlot.value != ""){
  pstartSlot = startSlot.value
}else{
  pstartSlot = '0'
}
if(targetSlot.value != ""){
  ptargetSlot = targetSlot.value
}else{
  ptargetSlot = '0'
}
let page = window.localStorage.getItem('pagenumber')
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowBigStorageCageHistoryTask", {
      pageNo: page,
      pageSize: pageSize.value,
      glassId: glassId.value,
      startSlot: pstartSlot,
      targetSlot: ptargetSlot,
      taskStateList: celllist,
      taskTypeList: stateList,
      beginDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
  })
    if (response.code == 200) {
      ElMessage.success(response.message);
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
        // tableDatax.value = response.data.records;
        tableDatax.value = formattedData;
      totalRecords.value = response.data.total
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
const Dailya = async (page) => {
try { 
let celllist=[]
let stateList=[]
let pstartSlot= ''
let ptargetSlot=''
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
if(selectValuesa[1]!=null&&selectValuesa[1]!='undefined'){
  if(selectValuesa[1]!=""){
    stateList=[selectValuesa[1]];
  }
}
if(startSlot.value != ""){
  pstartSlot = startSlot.value
}else{
  pstartSlot = '0'
}
if(targetSlot.value != ""){
  ptargetSlot = targetSlot.value
}else{
  ptargetSlot = '0'
}
let page = window.localStorage.getItem('pagenumber')
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowDailyProduction", {
      pageNo: page,
      pageSize: pageSize.value,
      glassId: glassId.value,
      startSlot: pstartSlot,
      targetSlot: ptargetSlot,
      taskStateList: celllist,
      taskTypeList: stateList,
      beginDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
  })
    if (response.code == 200) {
      hollowCountOutOne.value = response.data.hollowCountOutOne
      hollowCountOutTwo.value = response.data.hollowCountOutTwo
      hollowTotalAreaOutOne.value = response.data.hollowTotalAreaOutOne
      hollowTotalAreaOutTwo.value = response.data.hollowTotalAreaOutTwo
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
const handleClick = async () => {
  await sethistorical();
  await Dailyhistorical();
};
// 历史查询
const sethistorical = async () => {
try { 
let startTime = window.localStorage.getItem('startTime')
let celllist=[]
let stateList=[]
let pstartSlot= ''
let ptargetSlot=''
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
if(selectValuesa[1]!=null&&selectValuesa[1]!='undefined'){
  if(selectValuesa[1]!=""){
    stateList=[selectValuesa[1]];
  }
}
if(startSlot.value != ""){
  pstartSlot = startSlot.value
}else{
  pstartSlot = '0'
}
if(targetSlot.value != ""){
  ptargetSlot = targetSlot.value
}else{
  ptargetSlot = '0'
}
let page = window.localStorage.getItem('pagenumber')
const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowBigStorageCageHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      glassId: glassId.value,
      startSlot: pstartSlot,
      targetSlot: ptargetSlot,
      taskStateList: celllist,
      taskTypeList: stateList,
      beginDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
  })
    if (response.code == 200) {
        currentPage2.value = 1
        ElMessage.success(response.message);
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
        tableDatax.value = formattedData;
      totalRecords.value = response.data.total
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
const Dailyhistorical = async () => {
try { 
let startTime = window.localStorage.getItem('startTime')
let celllist=[]
let stateList=[]
let pstartSlot= ''
let ptargetSlot=''
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
if(selectValuesa[1]!=null&&selectValuesa[1]!='undefined'){
  if(selectValuesa[1]!=""){
    stateList=[selectValuesa[1]];
  }
}
if(startSlot.value != ""){
  pstartSlot = startSlot.value
}else{
  pstartSlot = '0'
}
if(targetSlot.value != ""){
  ptargetSlot = targetSlot.value
}else{
  ptargetSlot = '0'
}
let page = window.localStorage.getItem('pagenumber')
const response = await request.post("/hollowGlass/hollowBigStorageCageHistoryTask/queryHollowDailyProduction", {
      pageNo: 1,
      pageSize: pageSize.value,
      glassId: glassId.value,
      startSlot: pstartSlot,
      targetSlot: ptargetSlot,
      taskStateList: celllist,
      taskTypeList: stateList,
      beginDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endDate: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
  })
    if (response.code == 200) {
      hollowCountOutOne.value = response.data.hollowCountOutOne
      hollowCountOutTwo.value = response.data.hollowCountOutTwo
      hollowTotalAreaOutOne.value = response.data.hollowTotalAreaOutOne
      hollowTotalAreaOutTwo.value = response.data.hollowTotalAreaOutTwo
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
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
      const backendTime = new Date(getglobalDate)
      const oneWeekAgo = new Date(getglobalDate)
      oneWeekAgo.setHours(0, 0, 0, 0);
      oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
      timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)]
      window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
    },
  },
  {
    text: '最近一个月',
    value: () => {
      const backendTime = new Date(getglobalDate)
      const oneWeekAgo = new Date(getglobalDate)
      oneWeekAgo.setHours(0, 0, 0, 0);
      oneWeekAgo.setMonth(oneWeekAgo.getMonth() - 1)
      timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)]
      window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
    },
  },
  {
    text: '最近三个月',
    value: () => {
      const backendTime = new Date(getglobalDate)
      const oneWeekAgo = new Date(getglobalDate)
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
const backendTime = new Date(getglobalDate);
const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 减去7天
oneWeekAgo.setHours(0, 0, 0, 0);
timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
}
function getStatusTypeb(taskState: number) {  
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
function getStatusTextb(taskState: number) {
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
}
function getStatusTypea(taskType: number) {  
switch (taskType) {  
  case 4: 
    return 'primary';  
  case 5: 
    return 'success';  
  case 6:
    return 'warning';  
}  
}  
function getStatusTexta(taskType: number) {
switch (taskType) {
  case 4:  
    return t('searchOrder.onFilm');
  case 5:  
  return t('searchOrder.outFilm');
  case 6:  
  return t('searchOrder.dispatch');
}  
}
onMounted(() => {
parseAndSetTime();
historical();
Daily()
});
</script>
<style scoped>
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>