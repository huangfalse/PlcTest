<template>  
  <div>  
<div style="display: flex;width: 1770px;">
  <el-input v-model="flowCardId" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" :placeholder="$t('Mounting.processCards')" />
  <el-select v-model="selectValuesa[0]" filterable :placeholder="$t('hollow.mandatoryWhether')" clearable
      style="width: 200px;margin-left: 10px;">
      <el-option :label="$t('hollow.notMandatory')" value="0"></el-option>
      <el-option :label="$t('hollow.mandatory')" value="1"></el-option>
    </el-select>
    <el-select v-model="selectValuesa[1]" filterable :placeholder="$t('Mounting.state')" clearable
      style="width: 200px;margin-left: 10px;">
      <el-option :label="$t('Mounting.inAction')" value="1"></el-option>
      <el-option :label="$t('Mounting.paused')" value="2"></el-option>
      <el-option :label="$t('Mounting.end')" value="3"></el-option>
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
        <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="sethistorical()">{{$t('order.inquire')}}</el-button>
   </div>
      <el-table ref="table" style="margin-top: 20px;height: 580px;width: 1770px;" :data="tableDatax" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
    <el-table-column prop="flowCardId" align="center" :label="$t('Mounting.processCards')" min-width="80" />
    <el-table-column prop="totalLayer" align="center" :label="$t('hollow.totalPairQuantity')" min-width="80" />
    <el-table-column prop="totalPairQuantity" align="center" :label="$t('hollow.totalPairQuantity')" min-width="80" />
        <el-table-column
          align="center"
          :label="$t('hollow.mandatoryWhether')"
          min-width="80"
          prop="isForce"
        >
        <template #default="scope">  
      <el-tag :type="getStatusTypea(scope.row.isForce)">  
        {{ getStatusTexta(scope.row.isForce) }}  
      </el-tag>  
    </template> 
        </el-table-column>
        <el-table-column
          align="center"
          :label="$t('Mounting.state')"
          min-width="80"
          prop="state"
        >
        <template #default="scope">  
      <el-tag :type="getStatusTypeb(scope.row.state)">  
        {{ getStatusTextb(scope.row.state) }}  
      </el-tag>  
    </template> 
        </el-table-column>
        <el-table-column prop="formattedCreateTime" align="center" :label="$t('workOrder.startTime')" min-width="100" />
        <el-table-column prop="formattedUpdateTime" align="center" :label="$t('workOrder.endTime')" min-width="100" />
        <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center">
            <template #default="scope">
          <el-button type="text" plain @click="handleptask(scope.row)">{{ $t('hollow.recipeInformation') }}</el-button>
          <el-button type="text" plain @click="handlecagedetails(scope.row)">{{ $t('hollow.queueInformation') }}</el-button>
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
<!-- 队列信息 -->
  <el-dialog v-model="dialogFormVisiblef" top="5vh" width="65%" :title="$t('hollow.queueInformation')">
    <el-table  ref="table" style="margin-top: 20px;height: 450px;"
        :data="tableDatacagedetails" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
        <el-table-column prop="flowCardId" :label="$t('Mounting.processCards')" align="center" width="140"/>
          <el-table-column prop="glassId" :label="$t('Mounting.glassID')" align="center" width="140"/>
          <el-table-column prop="hollowSequence" :label="$t('hollow.orderReleased')" align="center"/>
          <el-table-column prop="thickness" :label="$t('Mounting.thickness')" align="center"/>
          <el-table-column prop="layer" :label="$t('hollow.layerNumber')" align="center"/>
          <el-table-column prop="filmsId" :label="$t('searchOrder.membraneSystem')" align="center"/>
          <el-table-column prop="height" :label="$t('hollow.long')" align="center"/>
          <el-table-column prop="width" :label="$t('Mounting.width')" align="center"/>
          <el-table-column prop="state" :label="$t('Mounting.state')" align="center">
            <template #default="scope">
            {{ scope.row.state == -1 ? $t('hollow.waitingFilmOut') : $t('hollow.filmComplete') }}
            </template>
          </el-table-column>
        </el-table>
</el-dialog>
<!-- 配方信息 -->
<el-dialog v-model="dialogFormVisiblea" top="23vh" width="55%" :title="$t('hollow.recipeInformation')" >
    <div style="margin-left: 50px;margin-top: 10px;margin-bottom: 10px;">
     <el-form  label-width="150px">
      <el-form label-width="150px" label-position="right">
        <el-row style="margin-top: 10px;">
          <el-col :span="8">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('hollow.formulaNameColon')" style="width: 16vw;">
                <el-input disabled v-model="formulaName" autocomplete="off" />
              </el-form-item></div></div>
          </el-col>
          <el-col :span="10">
            <div id="dta" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('Mounting.removalMethodColon')" style="width: 16vw;">
            <el-select v-model="filmRemove" clearable  style="width: 220px" disabled>
        <el-option :label="$t('Mounting.removeFilm')" value="1"></el-option>
        <el-option :label="$t('Mounting.notRemoved')" value="0"></el-option>
      </el-select>
              </el-form-item>
            </div>
              </div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 20px;">
          <el-col :span="8">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('hollow.bottomRemoveColon')" style="width: 16vw;">
                <el-input disabled v-model="bottomRemove" autocomplete="off" />
              </el-form-item></div></div>
          </el-col>
          <el-col :span="10">
            <div id="dta" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('hollow.topRemoveColon')" style="width: 16vw;">
                <el-input disabled v-model="topRemove" autocomplete="off" />
           </el-form-item>
            </div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 20px;">
          <el-col :span="8">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('hollow.leftRemoveColon')" style="width: 16vw;">
                <el-input disabled v-model="leftRemove" autocomplete="off" />
              </el-form-item></div></div>
          </el-col>
          <el-col :span="10">
            <div id="dta" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('hollow.rightRemoveColon')" style="width: 16vw;">
            <el-input disabled v-model="rightRemove" autocomplete="off" />
              </el-form-item>
            </div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
        </el-dialog>
</div>
</template>  
<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import request from "@/utils/request"
import {inject, onMounted, reactive, ref} from "vue";
import {ElMessage, ComponentSize} from 'element-plus'
const {t} = useI18n()
let language = ref(localStorage.getItem('lang') || 'zh')
const size = ref<ComponentSize>('default')
const globalDate = inject('globalDate');
const router = useRouter()
const timeRange = ref([])
const selectValuesa = reactive([]);
const tableDatax = ref([])
const currentPage2 = ref(1)
const totalRecords = ref(0)
const rawGlassHeight = ref('');
const rawGlassThickness = ref('');
const flowCardId = ref('');
const rawGlassfilmsId = ref('');
const formattedCreate = ref('') 
const formulaName = ref('') 
const filmRemove = ref('') 
const topRemove = ref('') 
const bottomRemove = ref('') 
const leftRemove = ref('') 
const rightRemove = ref('') 
const frameOne = ref('') 
const frameTwo = ref('') 
const frameThree = ref('') 
const frameFour = ref('') 
const sealInsert = ref('') 
const casOne = ref('') 
const casTwo = ref('') 
const casThree = ref('') 
const casFour = ref('') 
const intervalFrameTypeOne = ref('') 
const intervalFrameWidthOne = ref('') 
const intervalFrameHeightOne = ref('') 
const intervalFrameTypeTwo = ref('') 
const intervalFrameWidthTwo = ref('') 
const intervalFrameHeightTwo = ref('') 
const intervalFrameTypeThree = ref('') 
const intervalFrameWidthThree = ref('') 
const intervalFrameHeightThre = ref('') 
const intervalFrameTypeFour = ref('') 
const intervalFrameWidthFour = ref('') 
const intervalFrameHeightFour = ref('') 
const pageSize = ref(20)
const tableDatacagedetails = ref([])
const dialogFormVisiblea = ref(false)
const dialogFormVisiblef = ref(false)
let getglobalDate = window.localStorage.getItem('getglobalDate')
const historical = async () => {
try {
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowGlassOutRelationInfo/queryHollowHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      cell: 931,
      flowCardId: '',
      isForceList: [],
      stateList: [],
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
};
const handleSizeChange = (val: number) => {
  historicala(currentPage2.value);
}
const historicala = async (page) => {
try {
let celllist=[]
let stateList=[]
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
let page = window.localStorage.getItem('pagenumber')
let startTime = window.localStorage.getItem('startTime')
    const response = await request.post("/hollowGlass/hollowGlassOutRelationInfo/queryHollowHistoryTask", {
      pageNo: page,
      pageSize: pageSize.value,
      cell: 931,
      flowCardId: flowCardId.value,
      isForceList: celllist,
      stateList: stateList,
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
      tableDatax.value = formattedData;
      totalRecords.value = response.data.total
    } else {
      ElMessage.error(response.message);
  }
}
catch (error) {
}
}
// 配方信息
const handleptask = (row) => {
  fetchcagedl(row); 
  dialogFormVisiblea.value = true;
};
const removalMethodMap = {
  1: t('Mounting.removeFilm'),
  0: t('Mounting.notRemoved')
};
const fetchcagedl = async (row) => {
  try  {
    var url="/hollowGlass/hollowFormulaDetails/getFormulaDetailsById?id="+ row.formulaId;
    const response = await request.get(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
        formulaName.value = response.data.formulaName
        filmRemove.value = removalMethodMap[response.data.filmRemove];
        topRemove.value = response.data.topRemove
        bottomRemove.value = response.data.bottomRemove
        leftRemove.value = response.data.leftRemove
        rightRemove.value = response.data.rightRemove
        frameOne.value = response.data.frameOne
        frameTwo.value = response.data.frameTwo
        frameThree.value = response.data.frameThree
        frameFour.value = response.data.frameFour
        sealInsert.value = response.data.sealInsert
        casOne.value = response.data.casOne
        casTwo.value = response.data.casTwo
        casThree.value = response.data.casThree
        casFour.value = response.data.casFour
        intervalFrameTypeOne.value = response.data.intervalFrameTypeOne
        intervalFrameWidthOne.value = response.data.intervalFrameWidthOne
        intervalFrameHeightOne.value = response.data.intervalFrameHeightOne
        intervalFrameTypeTwo.value = response.data.intervalFrameTypeTwo
        intervalFrameWidthTwo.value = response.data.intervalFrameWidthTwo
        intervalFrameHeightTwo.value = response.data.intervalFrameHeightTwo
        intervalFrameTypeThree.value = response.data.intervalFrameTypeThree
        intervalFrameWidthThree.value = response.data.intervalFrameWidthThree
        intervalFrameHeightThre.value = response.data.intervalFrameHeightThre
        intervalFrameTypeFour.value = response.data.intervalFrameTypeFour
        intervalFrameWidthFour.value = response.data.intervalFrameWidthFour
        intervalFrameHeightFour.value = response.data.intervalFrameHeightFour
    } else {
      ElMessage.error(response.message);
    }
}
catch (error) {
  }
}
// 队列信息
const handlecagedetails = (row) => {
  const { id } = row;
  fetchcagedetails(id); 
  dialogFormVisiblef.value = true;
};
const fetchcagedetails = async (id) => {
  try  {
    var url="/hollowGlass/hollowGlassQueueInfo/queryHollowGlassQueueInfoByTask?taskId="+ id;
    const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      const formattedData = response.data.map(record => ({
      ...record,
      formaCreateTime: formatTimestamp(record.createTime),
      formaUpdateTime: formatTimestamp(record.updateTime),
    }));
    tableDatacagedetails.value = formattedData;
    } else {
      ElMessage.error(response.message);
    }
}
catch (error) {
  }
}
// 历史查询点击
const sethistorical = async () => {
try { 
let celllist=[]
let stateList=[]
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
let startTime = window.localStorage.getItem('startTime')
let page = window.localStorage.getItem('pagenumber')
const response = await request.post("/hollowGlass/hollowGlassOutRelationInfo/queryHollowHistoryTask", {
      pageNo: 1,
      pageSize: pageSize.value,
      cell: 931,
      flowCardId: flowCardId.value,
      isForceList: celllist,
      stateList: stateList,
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
function getStatusTypeb(state: number) {  
switch (state) {  
  case 1:
    return 'primary';  
  case 2: 
    return 'success';  
  case 3: 
    return 'info';  
}  
}
function getStatusTextb(state: number) {
switch (state) {
  case 1:  
    return t('Mounting.inAction');//执行中
  case 2:  
    return t('Mounting.paused');//暫停
  case 3:  
    return t('Mounting.end');//結束
}  
}
function getStatusTypea(isForce: number) {  
switch (isForce) {  
  case 0: 
    return 'primary';  //非強製
  case 1: 
    return 'success';  //強制
}  
}  
function getStatusTexta(isForce: number) {
switch (isForce) {
  case 0:  
    return t('hollow.notMandatory');
  case 1:  
  return t('hollow.mandatory');
}  
}
onMounted(() => {
parseAndSetTime();
historical();
});
</script>
<style scoped>  
#dt { display:block; float:left;line-height: 20px;margin-left: 10px;}
#dta { display:block; float:left;line-height: 20px;margin-left: 50%;}
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>