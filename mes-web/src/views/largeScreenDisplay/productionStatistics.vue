<script lang="ts" setup>
import request from "@/utils/request"
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import {inject, onMounted, reactive, ref} from "vue"
import {ElMessage} from 'element-plus'
import {saveAs} from 'file-saver'
import * as XLSX from 'xlsx'

const { t } = useI18n()
const globalDate = inject('globalDate')
const router = useRouter()
const timeRange = ref([])
const tableDatax = ref([])
const selectValuesa = reactive([])
const currentPage2 = ref(1)
const totalRecords = ref(0)
const slot = ref('')
const rawGlassWidth = ref('')
const rawGlassHeight = ref('')
const rawGlassfilmsId = ref('')
const rawGlassThickness = ref('')
let language = ref(localStorage.getItem('lang') || 'zh')
let getglobalDate = window.localStorage.getItem('getglobalDate')
const handleHistorical = async () => {
  try {
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/loadGlass/largenScreen/queryProduction", {
      pageNo: 1,
      pageSize: 20,
      beginDate: startTime,
      endDate: getglobalDate
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDatax.value = response.data;
      totalRecords.value = response.data.total / 2 || 0
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
    console.error(error);
  }
}
// 点击分页
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage2.value)
  fetchHistorical(currentPage2.value);
};
const fetchHistorical = async (page) => {
  try {
    let pslot = ''
    let celllist = []
    if (selectValuesa[0] != null && selectValuesa[0] != 'undefined') {
      if (selectValuesa[0] != "") {
        celllist = [selectValuesa[0]];
      }
    }
    if (slot.value != "") {
      pslot = slot.value
    } else {
      pslot = '0'
    }
    let page = window.localStorage.getItem('pagenumber')
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/loadGlass/largenScreen/queryProduction", {
      pageNo: page,
      pageSize: 20,
      beginDate: (timeRange.value && timeRange.value[0]) || '',
      endDate: (timeRange.value && timeRange.value[1]) || '',
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDatax.value = response.data;
      totalRecords.value = response.data.total / 2 || 0
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
    let pslot = ''
    let celllist = []
    if (selectValuesa[0] != null && selectValuesa[0] != 'undefined') {
      if (selectValuesa[0] != "") {
        celllist = [selectValuesa[0]];
      }
    }
    if (slot.value != "") {
      pslot = slot.value
    } else {
      pslot = '0'
    }
    let startTime = window.localStorage.getItem('startTime')
    let page = window.localStorage.getItem('pagenumber')
    const response = await request.post("/loadGlass/largenScreen/queryProduction", {
      pageNo: 1,
      pageSize: 20,
      beginDate: (timeRange.value && timeRange.value[0]) || '',
      endDate: (timeRange.value && timeRange.value[1]) || '',
    })
    if (response.code == 200) {
      currentPage2.value = 1
      ElMessage.success(response.message);
      tableDatax.value = response.data;
      totalRecords.value = response.data.total / 2 || 0
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
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
// 格式化后端时间并计算一周前的时间
const defaultTime = ref<[Date, Date]>([new Date(), new Date()]);
function parseAndSetTime() {
  const backendTime = new Date(getglobalDate);
  const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 减去7天
  oneWeekAgo.setHours(0, 0, 0, 0);
  timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
  window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
}
onMounted(() => {
  parseAndSetTime();
  handleHistorical();
});
const exportToExcel = () => {
  // 表格数据（深拷贝防止污染）
  const data = JSON.parse(JSON.stringify(tableDatax.value));
  // 表头转换为中文或国际化文本
  const headerMap = {
        productDate: t('large.date'),
        edgBeginTime: t('large.cuttingStartTime'),
        edgEndTime: t('large.cuttingEndTime'),
        edgTimeTotal: t('large.totalCuttingTime'),
        edgTimeFree: t('large.cuttingFreeTime'),
        edgTimeDiff: t('large.cuttingWorkingTime'),
        countOut: t('large.totalCuttingNumber'),
        totalAreaOut: t('large.totalCuttingArea'),
        bigBeginTime: t('large.temperingStartTime'),
        bigEndTime: t('large.temperingEndTime'),
        bigTimeTotal: t('large.temperingTotalTime'),
        bigTimeFree: t('large.temperingFreeTime'),
        bigTimeDiff: t('large.temperingWorkingTime'),
        temperingLayoutCount: t('large.temperingFurnaceNumber'),
        temperingGlassCount: t('large.totalQuantityTemperedSteel'),
        temperingArea: t('large.totalAreaTemperedSteel'),
        hollowBeginTime: t('large.hollowStartTime'),
        hollowEndTime: t('large.hollowEndTime'),
        hollowTimeTotal: t('large.hollowTotalTime'),
        hollowTimeFree: t('large.hollowFreeTime'),
        hollowTimeDiff: t('large.hollowWorkingTime'),
        hollowGlassCount: t('large.hollowTotalNumber'),
        hollowArea: t('large.hollowTotalArea'),
      };

  // 将数据中的 key 替换为中文表头
  const exportData = data.map(row => {
    const newRow = {};
    for (const key in headerMap) {
      newRow[headerMap[key]] = row[key];
    }
    return newRow;
  });
  // 转换为工作表
  const worksheet = XLSX.utils.json_to_sheet(exportData);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
  // 导出为文件
  const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
  const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
  saveAs(blob, '生产统计导出.xlsx');
};
</script>
<template>
  <div>
    <div style="display: flex;width: 2000px;">
      <el-date-picker v-model="timeRange" type="daterange" range-separator="至"
        :start-placeholder="$t('workOrder.startTime')" :end-placeholder="$t('workOrder.endTime')"
        value-format="YYYY-MM-DD" style="margin-left: 15px;">
      </el-date-picker>
      <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="setHistorical()">{{
        $t('report.inquire') }}</el-button>
      <el-button type="primary" @click="exportToExcel">{{ $t('large.exportToExcel') }}</el-button>
    </div>
    <el-table ref="table" style="margin-top: 20px;height: 580px;width: 2000px;" :data="tableDatax"
      :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
      <el-table-column prop="productDate" align="center" :label="$t('large.date')" min-width="70" />
      <el-table-column prop="edgBeginTime" align="center" :label="$t('large.cuttingStartTime')" min-width="120" />
      <el-table-column prop="edgEndTime" align="center" :label="$t('large.cuttingEndTime')" min-width="120" />
      <el-table-column prop="edgTimeTotal" align="center" :label="$t('large.totalCuttingTime')" min-width="50" />
      <el-table-column prop="edgTimeFree" align="center" :label="$t('large.cuttingFreeTime')" min-width="50" />
      <el-table-column prop="edgTimeDiff" align="center" :label="$t('large.cuttingWorkingTime')" min-width="50" />
      <el-table-column prop="countOut" align="center" :label="$t('large.totalCuttingNumber')" min-width="50" />
      <el-table-column prop="totalAreaOut" align="center" :label="$t('large.totalCuttingArea')" min-width="50" />
      <el-table-column prop="bigBeginTime" align="center" :label="$t('large.temperingStartTime')" min-width="120" />
      <el-table-column prop="bigEndTime" align="center" :label="$t('large.temperingEndTime')" min-width="120" />
      <el-table-column prop="bigTimeTotal" align="center" :label="$t('large.temperingTotalTime')" min-width="40" />
      <el-table-column prop="bigTimeFree" align="center" :label="$t('large.temperingFreeTime')" min-width="50" />
      <el-table-column prop="bigTimeDiff" align="center" :label="$t('large.temperingWorkingTime')" min-width="40" />
      <el-table-column prop="temperingLayoutCount" align="center" :label="$t('large.temperingFurnaceNumber')" min-width="50" />
      <el-table-column prop="temperingGlassCount" align="center" :label="$t('large.totalQuantityTemperedSteel')" min-width="50" />
      <el-table-column prop="temperingArea" align="center" :label="$t('large.totalAreaTemperedSteel')" min-width="50" />
      <el-table-column prop="hollowBeginTime" align="center" :label="$t('large.hollowStartTime')" min-width="120" />
      <el-table-column prop="hollowEndTime" align="center" :label="$t('large.hollowEndTime')" min-width="120" />
      <el-table-column prop="hollowTimeTotal" align="center" :label="$t('large.hollowTotalTime')" min-width="50" />
      <el-table-column prop="hollowTimeFree" align="center" :label="$t('large.hollowFreeTime')" min-width="50" />
      <el-table-column prop="hollowTimeDiff" align="center" :label="$t('large.hollowWorkingTime')" min-width="50" />
      <el-table-column prop="hollowGlassCount" align="center" :label="$t('large.hollowTotalNumber')" min-width="50" />
      <el-table-column prop="hollowArea" align="center" :label="$t('large.hollowTotalArea')" min-width="50" />
    </el-table>
  </div>
</template>
<style scoped>
</style>