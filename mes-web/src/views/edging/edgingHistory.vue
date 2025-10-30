<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import {inject, onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox, ComponentSize} from 'element-plus'
import request from "@/utils/request"
const { t } = useI18n()
const size = ref<ComponentSize>('default')
const globalDate = inject('globalDate');
const router = useRouter()
const timeRange = ref([])
const selectValuesa = reactive([]);
const tableDatax = ref([])
const currentPage2 = ref(1)
const totalRecords = ref(0)
const pageSize = ref(20)
const glassId = ref('');
let language = ref(localStorage.getItem('lang') || 'zh')
let getglobalDate = window.localStorage.getItem('getglobalDate')
const historical = async () => {
  try {
  let startTime = window.localStorage.getItem('startTime')
      const response = await request.post("/cacheGlass/edgHistoryTask/queryEdgHistoryTaskPage", {
        pageNo: 1,
        pageSize: pageSize.value,
        taskStateList: [],
        startTime: startTime,
        endTime: getglobalDate
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
    console.error(error);
  }
}
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage2.value)
  historicalTwo(currentPage2.value);
};
const handleSizeChange = (val: number) => {
  historicalTwo(currentPage2.value);
}
const historicalTwo = async (page) => {
  try { 
let celllist=[]
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
  let page = window.localStorage.getItem('pagenumber')
  let startTime = window.localStorage.getItem('startTime')
      const response = await request.post("/cacheGlass/edgHistoryTask/queryEdgHistoryTaskPage", {
        pageNo: page,
        pageSize: pageSize.value,
        taskStateList: celllist,
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
    console.error(error);
  }
}
// 历史查询点击
const setHistorical = async () => {
try { 
let celllist=[]
if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
  if(selectValuesa[0]!=""){
    celllist=[selectValuesa[0]];
  }
}
  let startTime = window.localStorage.getItem('startTime')
  let page = window.localStorage.getItem('pagenumber')
  const response = await request.post("/cacheGlass/edgHistoryTask/queryEdgHistoryTaskPage", {
        pageNo: 1,
        pageSize: pageSize.value,
        glassId: glassId.value,
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
        // tableDatax.value = response.data.records;
        tableDatax.value = formattedData;
        totalRecords.value = response.data.total
      } else {
        ElMessage.error(response.message);
    }
}
catch (error) {
    console.error(error);
  }
}
// 磨边前任务重发
 const handleTake = async(row) => { 
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('workOrder.taskBeforeEdgingWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/cacheGlass/edgHistoryTask/edgBeforeTaskResend", {
        glassId: row.glassId,
        width: row.width,
        height: row.height,
        thickness: row.thickness,
    })
    if (response.code === 200) {
      ElMessage.success(response.message);
      } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
}; 
// 磨边后任务重发
 const handleBindRack = async(row) => { 
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('workOrder.taskAfterEdgingWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/cacheGlass/edgHistoryTask/edgAfterTaskResend", {
        glassId: row.glassId,
        width: row.width,
        height: row.height,
        thickness: row.thickness,
    })
    if (response.code === 200) {
      ElMessage.success(response.message);
      } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
}; 
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
function parseAndSetTime() {
  const backendTime = new Date(getglobalDate);
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
}
onMounted(() => {
  parseAndSetTime();
  historical();
});
  </script>
<template>  
    <div>  
  <div style="display: flex;width: 1770px;">
    <el-input v-model="glassId" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputa" :placeholder="$t('searchOrder.glassIDEnter')" />
    <!-- <el-select v-model="selectValuesa[0]" filterable :placeholder="$t('Mounting.taskStatus')" clearable
        style="width: 200px;margin-left: 10px;">
          <el-option :label="$t('Mounting.pending')" value="0"></el-option>
          <el-option :label="$t('Mounting.inAction')" value="1"></el-option>
          <el-option :label="$t('film.missionAccomplished')" value="2"></el-option>
          <el-option :label="$t('film.taskFailed')" value="3"></el-option>
          <el-option :label="$t('Mounting.paused')" value="7"></el-option>
          <el-option :label="$t('Mounting.dilapidation')" value="8"></el-option>
      </el-select> -->
            <el-date-picker
            style="margin-left: 15px;"
             v-model="timeRange"
             type="datetimerange"
             :shortcuts="shortcuts"
             range-separator="至"
             :start-placeholder="$t('workOrder.startTime')"
             :end-placeholder="$t('workOrder.endTime')"
           />
      <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="setHistorical()">{{$t('order.inquire')}}</el-button>
     </div>
        <el-table ref="table" style="margin-top: 20px;height: 580px;width: 1770px;" :data="tableDatax" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
      <el-table-column prop="glassId" align="center" :label="$t('Mounting.glassID')" min-width="70" />
      <el-table-column prop="width" align="center" :label="$t('Mounting.width')" min-width="70" />
      <el-table-column prop="height" align="center" :label="$t('Mounting.height')" min-width="70" />
      <el-table-column prop="thickness" align="center" :label="$t('Mounting.thickness')" min-width="80" />
    <!-- <el-table-column prop="rawGlassFilmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80" /> -->
    <!-- <el-table-column prop="slot" align="center" :label="$t('Mounting.upperPositionNumber')" min-width="80" /> -->
      <!-- <el-table-column prop="totalCount" align="center" :label="$t('Mounting.totalNumberFilms')" min-width="80" /> -->
      <!-- <el-table-column prop="finishCount" align="center" :label="$t('Mounting.completedQuantity')" min-width="80" /> -->
      <!-- <el-table-column prop="damageCount" align="center" :label="$t('hollow.damageNumber')" min-width="80" /> -->
          <!-- <el-table-column
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
          </el-table-column> -->
          <el-table-column prop="formattedCreateTime" align="center" :label="$t('workOrder.startTime')" min-width="100" />
          <el-table-column prop="formattedUpdateTime" align="center" :label="$t('workOrder.endTime')" min-width="100" />
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="400">
            <template #default="scope">
              <el-button type="text" plain @click="handleTake(scope.row)">{{
                  $t('workOrder.taskBeforeEdging')
                }}
              </el-button>
              <el-button type="text" plain @click="handleBindRack(scope.row)">{{
                  $t('workOrder.taskAfterEdging')
                }}
              </el-button>
            </template>
          </el-table-column>
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
<style scoped>
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>