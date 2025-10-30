<script lang="ts" setup>
import request from "@/utils/request"
import * as XLSX from 'xlsx'
import {useI18n} from 'vue-i18n'
import {saveAs} from 'file-saver'
import {useRouter} from "vue-router"
import {ElMessage} from 'element-plus'
import {inject, onMounted, reactive, ref} from "vue"

const { t } = useI18n()
const router = useRouter()
const currentPage2 = ref(1)
const totalRecords = ref(0)
const slot = ref('')
const rawGlassWidth = ref('')
const rawGlassHeight = ref('')
const rawGlassfilmsId = ref('')
const rawGlassThickness = ref('')
const timeRange = ref([])
const tableData = ref([])
const selectValuesa = reactive([])
const globalDate = inject('globalDate')
let language = ref(localStorage.getItem('lang') || 'zh')
let getglobalDate = window.localStorage.getItem('getglobalDate')
const handleHistorical = async () => {
  try {
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/loadGlass/largenScreen/queryDailyProduction", {
      pageNo: 1,
      pageSize: 20,
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
      tableData.value = formattedData;
      totalRecords.value = response.data.total / 2 || 0
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
    const response = await request.post("/loadGlass/largenScreen/queryDailyProduction", {
      pageNo: page,
      pageSize: 20,
      beginDate: (timeRange.value && timeRange.value[0]) || '',
      endDate: (timeRange.value && timeRange.value[1]) || '',
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableData.value = formattedData;
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
    const response = await request.post("/loadGlass/largenScreen/queryDailyProduction", {
      pageNo: 1,
      pageSize: 20,
      beginDate: (timeRange.value && timeRange.value[0]) || '',
      endDate: (timeRange.value && timeRange.value[1]) || '',
    })
    if (response.code == 200) {
      currentPage2.value = 1
      ElMessage.success(response.message);
      const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableData.value = formattedData;
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
  const data = JSON.parse(JSON.stringify(tableData.value));
  // 表头转换为中文或国际化文本
  const headerMap = {
    date: t('large.date'),
    countOutOne: t('large.countOutOne'),
    totalAreaOutOne: t('large.totalAreaOutOne'),
    countOutTwo: t('large.countOutTwo'),
    totalAreaOutTwo: t('large.totalAreaOutTwo'),
    countIn: t('large.countIn'),
    totalAreaIn: t('large.totalAreaIn'),
    countOut: t('large.temperedSteelQuantity'),
    totalAreaOut: t('large.totalAreaOut'),
    hollowCountOutOne: t('large.hollowCountOutOne'),
    hollowTotalAreaOutOne: t('large.hollowTotalAreaOutOne'),
    hollowCountOutTwo: t('large.hollowCountOutTwo'),
    hollowTotalAreaOutTwo: t('large.hollowTotalAreaOutTwo')
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
  saveAs(blob, '生产情况导出.xlsx');
};
</script>
<template>
  <div>
    <div style="display: flex;width: 1770px;">
      <el-date-picker v-model="timeRange" type="daterange" range-separator="至"
        :start-placeholder="$t('workOrder.startTime')" :end-placeholder="$t('workOrder.endTime')"
        value-format="YYYY-MM-DD" style="margin-left: 15px;">
      </el-date-picker>
      <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="setHistorical()">{{
        $t('report.inquire') }}</el-button>
        <el-button type="primary" @click="exportToExcel">{{$t('large.exportToExcel')}}</el-button>
    </div>
    <el-table ref="table" style="margin-top: 20px;height: 580px;width: 1770px;" :data="tableData"
      :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
      <el-table-column prop="date" align="center" :label="$t('large.date')" min-width="70" />
      <el-table-column prop="countOutOne" align="center" :label="$t('large.countOutOne')" min-width="40" />
      <el-table-column prop="totalAreaOutOne" align="center" :label="$t('large.totalAreaOutOne')" min-width="40" />
      <el-table-column prop="countOutTwo" align="center" :label="$t('large.countOutTwo')" min-width="50" />
      <el-table-column prop="totalAreaOutTwo" align="center" :label="$t('large.totalAreaOutTwo')" min-width="50" />
      <el-table-column prop="countIn" align="center" :label="$t('large.countIn')" min-width="50" />
      <el-table-column prop="totalAreaIn" align="center" :label="$t('large.totalAreaIn')" min-width="50" />
      <el-table-column prop="countOut" align="center" :label="$t('large.temperedSteelQuantity')" min-width="40" />
      <el-table-column prop="totalAreaOut" align="center" :label="$t('large.totalAreaOut')" min-width="40" />
      <el-table-column prop="hollowCountOutOne" align="center" :label="$t('large.hollowCountOutOne')" min-width="40" />
      <el-table-column prop="hollowTotalAreaOutOne" align="center" :label="$t('large.hollowTotalAreaOutOne')"
        min-width="40" />
      <el-table-column prop="hollowCountOutTwo" align="center" :label="$t('large.hollowCountOutTwo')" min-width="40" />
      <el-table-column prop="hollowTotalAreaOutTwo" align="center" :label="$t('large.hollowTotalAreaOutTwo')"
        min-width="40" />
    </el-table>
    <div style="margin-top: 20px;margin-left: 40%;">
      <el-pagination v-model:current-page="currentPage2" :page-size="pageSize" :size="large" :disabled="disabled"
        layout="prev, pager, next, jumper" :total="totalRecords" @current-change="handlePageChange2"
        style="margin-top: 10px;" />
    </div>
  </div>
</template>
<style scoped>
</style>