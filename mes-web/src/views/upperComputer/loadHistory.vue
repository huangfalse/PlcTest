<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import {inject, onMounted, reactive, ref} from "vue";
import {ElMessage, ComponentSize} from 'element-plus'
import request from "@/utils/request"
const {t} = useI18n()
const globalDate = inject('globalDate');
const router = useRouter()
const timeRange = ref([])
const selectValuesa = reactive([]);
const tableDatax = ref([])
const slot = ref('');
const pageSize = ref(20)
const large = ref('large')
const disabled = ref(false)
const currentPage2 = ref(1)
const totalRecords = ref(0)
const size = ref<ComponentSize>('default')
let language = ref(localStorage.getItem('lang') || 'zh')
let getglobalDate = window.localStorage.getItem('getglobalDate')
//进入页面查询 历史任务
const historical = async () => {
  try {
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/loadGlass/loadGlassHistoryTask/queryFinishLoadGlassHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      taskState: [],
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
      totalRecords.value = response.data.total || 0
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage2.toString())
  historicalTwo(currentPage2.value);
}
const handleSizeChange = (val: number) => {
  historicalTwo(currentPage2.value);
}
const historicalTwo = async (pageNo) => {
  try {
    let celllist = []
    if (selectValuesa[0] != null && selectValuesa[0] != 'undefined') {
      if (selectValuesa[0] != "") {
        celllist = [selectValuesa[0]];
      }
    }
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/loadGlass/loadGlassHistoryTask/queryFinishLoadGlassHistoryTask", {
      pageNo: pageNo,
      pageSize: pageSize.value,
      taskState: celllist,
      startTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || startTime,
      endTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || getglobalDate,
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableDatax.value = formattedData;
      totalRecords.value = response.data.total || 0
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// const historicalTwo = async (page) => {
//   try { 
// let celllist=[]
// if(selectValuesa[0]!=null&&selectValuesa[0]!='undefined'){
//   if(selectValuesa[0]!=""){
//     celllist=[selectValuesa[0]];
//   }
// }
//   let page = window.localStorage.getItem('pagenumber')
//   let startTime = window.localStorage.getItem('startTime')
//       const response = await request.post("/loadGlass/loadGlassHistoryTask/queryFinishLoadGlassHistoryTask", {
//         pageNo: page,
//         pageSize: pageSize.value,
//         taskState: celllist,
//         startTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
//         endTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
//     })
//       if (response.code == 200) {
//         ElMessage.success(response.message);
//         const formattedData = response.data.records.map(record => ({
//         ...record,
//         formattedCreateTime: formatTimestamp(record.createTime),
//         formattedUpdateTime: formatTimestamp(record.updateTime),
//       }));
//         // tableDatax.value = response.data.records;
//         tableDatax.value = formattedData;
//         totalRecords.value = response.data.total
//       } else {
//         ElMessage.error(response.message);
//     }
// }
// catch (error) {
//     console.error(error);
//   }
// }
// 历史查询点击
const setHistorical = async () => {
  try {
    let celllist = []
    if (selectValuesa[0] != null && selectValuesa[0] != 'undefined') {
      if (selectValuesa[0] != "") {
        celllist = [selectValuesa[0]];
      }
    }
    let startTime = window.localStorage.getItem('startTime')
    let page = window.localStorage.getItem('pagenumber')
    const response = await request.post("/loadGlass/loadGlassHistoryTask/queryFinishLoadGlassHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      taskState: celllist,
      startTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
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
      totalRecords.value = response.data.total || 0
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
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
    <div style="display: flex;width: 1700px;">
      <el-select v-model="selectValuesa[0]" :placeholder="$t('Mounting.taskStatus')" clearable filterable
                 style="width: 200px;margin-left: 10px;">
        <el-option :label="$t('Mounting.pending')" value="0"></el-option>
        <el-option :label="$t('Mounting.inAction')" value="1"></el-option>
        <el-option :label="$t('film.missionAccomplished')" value="2"></el-option>
        <el-option :label="$t('film.taskFailed')" value="3"></el-option>
        <el-option :label="$t('Mounting.paused')" value="7"></el-option>
        <el-option :label="$t('Mounting.dilapidation')" value="8"></el-option>
      </el-select>
      <el-date-picker
          v-model="timeRange"
          :end-placeholder="$t('reportmanage.endtime')"
          :shortcuts="shortcuts"
          :start-placeholder="$t('reportmanage.starttime')"
          range-separator="至"
          style="margin-left: 15px;"
          type="datetimerange"
      />
      <el-button style="margin-left: 10px;margin-bottom: 10px;" type="primary" @click="setHistorical()">
        {{ $t('order.inquire') }}
      </el-button>
    </div>
    <el-table ref="table" :data="tableDatax" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"
              style="margin-top: 20px;height: 580px;width: 1700px;">
      <el-table-column :label="$t('Mounting.width')" align="center" min-width="70" prop="rawGlassWidth"/>
      <el-table-column :label="$t('Mounting.height')" align="center" min-width="70" prop="rawGlassHeight"/>
      <el-table-column :label="$t('Mounting.thickness')" align="center" min-width="80" prop="rawGlassThickness"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="80" prop="filmsId"/>
      <el-table-column :label="$t('Mounting.upperPositionNumber')" align="center" min-width="80" prop="slot"/>
      <el-table-column :label="$t('Mounting.totalNumberFilms')" align="center" min-width="80" prop="totalCount"/>
      <el-table-column :label="$t('Mounting.completedQuantity')" align="center" min-width="80" prop="finishCount"/>
      <el-table-column :label="$t('hollow.damageNumber')" align="center" min-width="80" prop="damageCount"/>
      <el-table-column
          :label="$t('Mounting.taskStatus')"
          align="center"
          min-width="50"
          prop="taskState"
      >
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.taskState)">
            {{ getStatusText(scope.row.taskState) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('workOrder.startTime')" align="center" min-width="100" prop="formattedCreateTime"/>
      <el-table-column :label="$t('workOrder.endTime')" align="center" min-width="100" prop="formattedUpdateTime"/>
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