<script lang="ts" setup>
import request from "@/utils/request"
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import {onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue";
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {ElMessage, ElMessageBox} from 'element-plus'

const { t } = useI18n()
const router = useRouter()
const SwitchDispatch = ref('')
const flowCardId = ref('')
const glassId = ref('')
const filmsId = ref('')
const iframeUrl = ref('')
const IdleStateOne = ref('#911005')
const IdleStateTwo = ref('#911005')
const IdleStateThree = ref('#911005')
const inkageEntity = ref('#911005')
const requestEntity = ref('#911005')
// const mesReplyEntity = ref('#911005')
const outInkageEntity = ref('#911005')
const outRequestEntity = ref('#911005')
const adjustOne = ref([])
const adjustTwo = ref([])
const scanGlass = ref([])
const carPosition = ref([])
const tableDataCageInformation = ref([])
const tableDataOutGlass = ref([])
const tableDataFeedInto = ref([])
const tableDataAddCageInformation = ref([])
const tableDataUsage = ref([])
const tableDataProcessCardNumber = ref([])
const adjustedRectsOne = ref([])
const adjustedRectsTwo = ref([])
const adjustedRectsThree = ref([])
const adjustedRectsFout = ref([])
const adjustedRectsFive = ref([])
const subRectsCountsOne = ref([])
const subRectsCountsTwo = ref([])
const subRectsCountsThree = ref([])
const subRectsCountsFour = ref([])
const subRectsCountsFive = ref([])
const currentRow = reactive({}) // 当前行的数据 
const currentPage2 = ref(1)
const cellOne=ref(true)
const cellTwo=ref(true)
const cellThree=ref(true)
const cellFour=ref(true)
const cellFive=ref(true)
const dialogHistory = ref(false)
const dialogFormVisiblea = ref(false)
const dialogAddCageInformation = ref(false)
const selectedRow = ref(null)
let language = ref(localStorage.getItem('lang') || 'zh')

let socket = null;
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/hollowGlass/api/talk/hollowGlassMessage`;
const handleMessage = (data) => {
  // 流程卡号表
  if(data.bigStorageCageUsageSummary!=null){
    const summaries = data.bigStorageCageUsageSummary[0];
      const updatedData = summaries.map(summary => ({
        ...summary,
        totalNumber: summary.layer * summary.sumCount,
      }));
    tableDataProcessCardNumber.value = updatedData;
  }else{
    tableDataProcessCardNumber.value = []
    }
  // 进片联机状态
  if (data.inkageEntity != null) {
    inkageEntity.value = data.inkageEntity[0] == true ? 'green' : '#911005';
  }
  // 进片请求状态
  if (data.requestEntity != null) {
    requestEntity.value = data.requestEntity == 1 ? 'green' : '#911005';
  }
  // 启动命令
  // if (data.mesReplyEntity != null) {
  //   mesReplyEntity.value = data.mesReplyEntity == 1 ? 'green' : '#911005';
  // }
  // 出片联机状态
  if (data.outInkageEntity != null) {
    outInkageEntity.value = data.outInkageEntity[0] == true ? 'green' : '#911005';
  }
  // 出片请求状态
  if (data.outRequestEntity != null) {
    outRequestEntity.value = data.outRequestEntity == 1 ? 'green' : '#911005';
  }
  // 一线空闲状态
  if (data.freeOneRequestEntity != null) {
    IdleStateOne.value = data.freeOneRequestEntity == 1 ? 'green' : '#911005';
  }
  // 二线空闲状态
  if (data.freeTwoRequestEntity != null) {
    IdleStateTwo.value = data.freeTwoRequestEntity[0] == true ? 'green' : '#911005';
  }
  // 三线空闲状态
  if (data.freeThreeRequestEntity != null) {
    IdleStateThree.value = data.freeThreeRequestEntity[0] == true ? 'green' : '#911005';
  }
  // 小车位置  出片玻璃表
  if(data.bigStorageCageDetailsOutTask!=null){
    tableDataOutGlass.value = data.bigStorageCageDetailsOutTask[0]
    adjustOne.value = data.bigStorageCageDetailsOutTask[0].filter(rect => rect.slot !== null && rect.slot !== undefined);
  } else {
    tableDataOutGlass.value = [],
    adjustOne.value = []
  }
    if (data.scanGlass != null) {
    scanGlass.value = data.scanGlass[0];
    let width = scanGlass.value.width;
    let height = scanGlass.value.height;
    if (width < height) {
      scanGlass.value.width = height;
      scanGlass.value.height = width;
    }
  }
  // 小车位置  进片玻璃表
  if(data.bigStorageCageDetailsFeedTask!=null){
    tableDataFeedInto.value = data.bigStorageCageDetailsFeedTask[0]
    adjustTwo.value = data.bigStorageCageDetailsFeedTask[0].filter(rect => rect.slot !== null && rect.slot !== undefined); 
  }else{
    tableDataFeedInto.value = []
      }
// 使用率、空闲数
if(data.bigStorageCageUsage!=null){
  tableDataUsage.value = data.bigStorageCageUsage[0]
}else{
    tableDataUsage.value = []
      }
if(data.carPostion!=null){
  carPosition.value = data.carPostion[0]
}else{
  carPosition.value = []
  }
if(data.dispatchHollowSwitch!=null){
  SwitchDispatch.value = data.dispatchHollowSwitch[0]
}else{
  SwitchDispatch.value = ''
  }
  if(data.bigStorageCageInfos!=null){
    adjustedRectsOne.value = data.bigStorageCageInfos[0][1].map((rect, index) => ({
    id: index + 1,
    height: 15/55,
    top: 33/55,
  }));
subRectsCountsOne.value = data.bigStorageCageInfos[0][1].map(rect => rect.count);
adjustedRectsTwo.value = data.bigStorageCageInfos[0][2].map((rect, index) => ({
    id: index + 1,
    height: 15/55,
    top: 33/55,
  }));
subRectsCountsTwo.value = data.bigStorageCageInfos[0][2].map(rect => rect.count);
adjustedRectsThree.value = data.bigStorageCageInfos[0][3].map((rect, index) => ({
    id: index + 1,
    height: 15/55,
    top: 33/55,
  }));
subRectsCountsThree.value = data.bigStorageCageInfos[0][3].map(rect => rect.count);
adjustedRectsFour.value = data.bigStorageCageInfos[0][4].map((rect, index) => ({
    id: index + 1,
    height: 15/55,
    top: 33/55,
  }));
subRectsCountsFour.value = data.bigStorageCageInfos[0][4].map(rect => rect.count);
adjustedRectsFive.value = data.bigStorageCageInfos[0][5].map((rect, index) => ({
    id: index + 1,
    height: 15/55,
    top: 33/55,
  }));
subRectsCountsFive.value = data.bigStorageCageInfos[0][5].map(rect => rect.count);
  }else{
    adjustedRectsOne.value = '',
    adjustedRectsTwo.value = '',
    adjustedRectsThree.value = '',
    adjustedRectsFour.value = ''
    adjustedRectsFive.value = ''
  }
};
// 计算每个大矩形的样式
const rectStyleOne = (rect, index) => ({
  position: 'absolute',
  width: '150px',
  left: '0px',
  top: `${index*rect.top}px`,
  height: `${rect.height}px`,
});
// 计算每个小矩形的样式和数量
const getSubRectsOne = (rectIndex) => {
  const count = subRectsCountsOne.value[rectIndex];
  const subRects = [];
  for (let i = 0; i < count; i++) {
    subRects.push({});
  }
  return subRects;
};
// 计算每个小矩形的样式
const subRectStyleOne = (rectIndex, subIndex) => {
  const width = '18px';
  const marginLeft = '3px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginLeft));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginLeft))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginLeft,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleTwo = (rect, index) => ({
  position: 'absolute',
  width: '150px',
  left: '0px',
  top: `${index*rect.top}px`,
  height: `${rect.height}px`,
});
// 计算每个小矩形的样式和数量
const getSubRectsTwo = (rectIndex) => {
  const count = subRectsCountsTwo.value[rectIndex];
  const subRects = [];
  for (let i = 0; i < count; i++) {
    subRects.push({});
  }
  return subRects;
};
// 计算每个小矩形的样式
const subRectStyleTwo = (rectIndex, subIndex) => {
  const width = '18px';
  const marginLeft = '3px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginLeft));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginLeft))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginLeft,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleThree = (rect, index) => ({
  position: 'absolute',
  width: '150px',
  left: '0px',
  top: `${index*rect.top}px`,
  height: `${rect.height}px`,
});
// 计算每个小矩形的样式和数量
const getSubRectsThree = (rectIndex) => {
  const count = subRectsCountsThree.value[rectIndex];
  const subRects = [];
  for (let i = 0; i < count; i++) {
    subRects.push({});
  }
  return subRects;
};
// 计算每个小矩形的样式
const subRectStyleThree = (rectIndex, subIndex) => {
  const width = '18px';
  const marginLeft = '3px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginLeft));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginLeft))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginLeft,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleFour = (rect, index) => ({
  position: 'absolute',
  width: '150px',
  left: '0px',
  top: `${index*rect.top}px`,
  height: `${rect.height}px`,
});
// 计算每个小矩形的样式和数量
const getSubRectsFour = (rectIndex) => {
  const count = subRectsCountsFour.value[rectIndex];
  const subRects = [];
  for (let i = 0; i < count; i++) {
    subRects.push({});
  }
  return subRects;
};
// 计算每个小矩形的样式
const subRectStyleFour = (rectIndex, subIndex) => {
  const width = '18px';
  const marginLeft = '3px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginLeft));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginLeft))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginLeft,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleFive = (rect, index) => ({
  position: 'absolute',
  width: '150px',
  left: '0px',
  top: `${index*rect.top}px`,
  height: `${rect.height}px`,
});
// 计算每个小矩形的样式和数量
const getSubRectsFive = (rectIndex) => {
  const count = subRectsCountsFive.value[rectIndex];
  const subRects = [];
  for (let i = 0; i < count; i++) {
    subRects.push({});
  }
  return subRects;
};
// 计算每个小矩形的样式
const subRectStyleFive = (rectIndex, subIndex) => {
  const width = '18px';
  const marginLeft = '3px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginLeft));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginLeft))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginLeft,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 点击理片笼信息
const handleSliceCage = () => {
  dialogFormVisiblea.value = true;
  fetchSliceCageInformation(); 
  currentPage2.value = 1;
  window.localStorage.setItem('pagenumber', currentPage2.value)
  filmsId.value = ''
  flowCardId.value = ''
};
// 理片笼信息
const fetchSliceCageInformation = async () => {
  try {     
    const response = await request.post('/hollowGlass/hollowBigStorageCage/queryHollowBigStorageCageDetail',{
        // deviceId: page,
        filmsId: filmsId.value,
        flowCardId: flowCardId.value,
    })
    if (response.code === 200) {  
      ElMessage.success(response.message);
      tableDataCageInformation.value = response.data
    } else {  
      ElMessage.warning(response.msg)
    }  
  } catch (error) {
  } 
}
// 理片笼信息--查询
const fetchInquireCageInformation = async () => {
  let page = window.localStorage.getItem('pagenumber')
  try {     
    const response = await request.post('/hollowGlass/hollowBigStorageCage/queryHollowBigStorageCageDetail',{
        deviceId: page,
        filmsId: filmsId.value,
        flowCardId: flowCardId.value,
    })
    if (response.code === 200) {  
      ElMessage.success(response.message);
      tableDataCageInformation.value = response.data
      filmsId.value = ''
      flowCardId.value = ''
    } else {  
      ElMessage.warning(response.msg)
    }  
  } catch (error) {
  } 
}
// 理片笼信息--添加
const handleBindRack = (row) => {
  selectedRow.value = row; // 更新选中的行数据  
  currentRow.deviceId  = row.deviceId; // 直接设置响应式属性  
  currentRow.slot = row.slot;  
  window.localStorage.setItem('deviceId', row.deviceId)
  window.localStorage.setItem('slot', row.slot)
  dialogAddCageInformation.value = true;
};
// 添加理片笼信息--搜索
const searchOut = async () => {
  try {
    const response = await request.post('/cacheVerticalGlass/bigStorageCageDetails/selectBigStorageCageDetails',{
      glassId:glassId.value
    });
    if (response.code == 200) {
      tableDataAddCageInformation.value = response.data;
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
// 理片笼信息--是否禁用
const toggleEnableState = async (row) => {
  const newState = row.enableState === 1 ? 0 : 1;  
  var url="/hollowGlass/hollowBigStorageCageDetails/updateHollowStorageCageDisabled?slot="+row.slot + "&enableState=" + newState;
      const response = await request.get(url) 
  if (response.code === 200) {
      ElMessage.success(response.message);
      } else {  
      ElMessage.error(response.message);
      }  
  row.enableState = newState;  
}; 
 // 理片笼信息--破损
 const dilapidationAction = async(row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('Mounting.dilapidationWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    ); 
    if (confirmResult === 'confirm') {
      const params = new URLSearchParams();
      params.append('state', '8');
      params.append('deviceId', row.deviceId);
      params.append('glassId', row.glassId);
      params.append('slot', row.slot);
      var url = `/hollowGlass/hollowBigStorageCageDetails/damageHollowBigStorageCageDetails?${params.toString()}`;
    const response = await request.post(url)
      if (response.code === 200) {
       fetchSliceCageInformation(); 
     ElMessage.success(response.message);
      } else {  
      ElMessage.error(response.message);
      }  
    }  
  } catch (error) {
  }  
}; 
 // 理片笼信息--拿走
 const takeAction = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('workOrder.takeMessageWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    ); 
    if (confirmResult === 'confirm') {  
      const params = new URLSearchParams();
      params.append('state', '9');
      params.append('deviceId', row.deviceId);
      params.append('glassId', row.glassId);
      params.append('slot', row.slot);
      var url = `/hollowGlass/hollowBigStorageCageDetails/damageHollowBigStorageCageDetails?${params.toString()}`;
      const response = await request.post(url)
      if (response.code === 200) {
       fetchSliceCageInformation();
       ElMessage.success(response.message);
      } else {  
      ElMessage.error(response.message);
      }  
    }  
  } catch (error) {
  }  
}; 
 // 理片笼信息--删除 
 const deleteAction = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('searchOrder.deleteMessageWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    ); 
    if (confirmResult === 'confirm') {
      const params = new URLSearchParams();
      params.append('state', '8');
      params.append('deviceId', row.deviceId);
      params.append('glassId', row.glassId);
      params.append('slot', row.slot);
      var url = `/hollowGlass/hollowBigStorageCageDetails/deleteHollowBigStorageCageDetails?${params.toString()}`;
      const response = await request.post(url)
    if (response.code === 200) {
       fetchSliceCageInformation();
     ElMessage.success(response.message);
      } else {
      ElMessage.error(response.message);
      }  
    }  
  } catch (error) {
  }  
}; 
// 添加理片笼信息--确认添加
const confirmAddition = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('searchOrder.confirmAdditionWhether'), 
      t('searchOrder.prompt'),  
      {  
        confirmButtonText: t('searchOrder.yes'), 
        cancelButtonText: t('searchOrder.cancel'),
        type: 'warning',  
      } 
    ); 
    if (confirmResult === 'confirm') {  
    let deviceId = window.localStorage.getItem('deviceId')
    let slot = window.localStorage.getItem('slot')
      const dataToSend = {  
        ...row,
        deviceId: deviceId,
        slot: slot
      };  
  const response = await request.post("/cacheVerticalGlass/hollowBigStorageCageDetails/insertHollowBigStorageCageDetails", dataToSend)
    if (response.code === 200) {
      tableDataCageInformation.value = response.data;
      dialogAddCageInformation.value = false;
      ElMessage.success(response.message);
      glassId.value = '';
      tableDataAddCageInformation.value = '';
      } else {  
      ElMessage.error(response.message);
      }  
    }  
  } catch (error) {
  }  
}; 
// 点击分页
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage2.value)
  fetchSliceCageInformation();
};
function handleRowClick(row) {  
  selectedRow.value = row; // 更新选中的行数据  
}
// 任务重置
const handlepTask = async() => { 
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('searchOrder.taskResetWhether'), 
      t('workOrder.prompt'),  
      {  
        confirmButtonText: t('workOrder.yes'), 
        cancelButtonText: t('workOrder.cancel'),
        type: 'warning',  
      } 
    );
 if (confirmResult === 'confirm') {
  const response = await request.post('/hollowGlass/hollowBigStorageCage/resetCage')
    if (response.code === 200) {
      ElMessage.success(response.message);
      } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
}; 
// 调度开关
const handleDispatch = async () => {
  try  {
    const body = {  
      flag: SwitchDispatch.value,
    };
    var url="/hollowGlass/hollowGlassOutRelationInfo/dispatchHollowSwitch?flag="+diaodu.value;
  const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      SwitchDispatch.value = response.data
    }else {  
      ElMessage.error(response.message);
      }  
}
catch (error) {
  }
}
// 历史任务
const handlehistorical = (row) => {
  dialogHistory.value = true;
  iframeUrl.value = `${window.location.origin}/#/hollow/hollowCageHistory`;
};
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
});
onUnmounted(() => {
  if (socket) {
  closeWebSocket(socket);
  }
  });

function getStateType(taskState) {
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

function getStateText(taskState) {
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
function getStatusType(enableState: number) {  
  switch (enableState) {  
    case 100:
      return 'success';  
    case 102: 
      return 'warning';  
  }  
}  
function getStatusText(enableState: number) {
  switch (enableState) {
    case 100:  
      return t('searchOrder.inCage');
    case 102:  
      return t('searchOrder.artificialFilming'); 
  }  
}
function getcasOnea(isSame) {  
  switch (isSame) {  
    case 1:
      return 'danger';  
    case 0: 
      return 'info';  
  }  
}  
function getStatuscasOnea(isSame) {
  switch (isSame) {
    case 1:  
      return t('searchOrder.same');
    case 0:  
      return t('searchOrder.notSame'); 
  }  
}
function tableRowClassName({ row }) {
  if (row.isSame === 1) {
    return 'row-red-background';
  }
  return '';
}
onBeforeUnmount(() => {
  closeWebSocket();
});
</script>
<template>
  <div style="height: 600px;">
    <el-button style="margin-top: 7px;margin-left: 10px;" id="searchButton" type="primary" @click="handleSliceCage">{{ $t('searchOrder.sliceCagInformation') }}</el-button>
    <el-button style="margin-top: 7px;margin-left: 10px;" id="searchButton" type="success" @click="handlehistorical">{{ $t('searchOrder.historicaltasks') }}</el-button>
    <el-button style="margin-top: 7px;margin-left: 10px;" id="searchButton" type="danger" @click="handlepTask()">{{ $t('searchOrder.taskReset') }}</el-button>
    <el-switch style="margin-top: 7px;margin-left: 10px;" v-model="SwitchDispatch" class="mb-2" :inactive-text="$t('searchOrder.schedulingSwitch')" @change="handleDispatch" />
    <div id="dotClass">
        <div>{{ $t('searchOrder.advanceOnlineStatus') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: inkageEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
        <div style="margin-left: 70px;">{{ $t('searchOrder.feedRequestStatus') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: requestEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <!-- <div style="margin-left: 70px;">{{ $t('searchOrder.startCommand') }}</div> -->
      <!-- <i :style="{ marginTop: '2px', backgroundColor: mesReplyEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i> -->
        <div style="margin-left: 70px;">{{ $t('searchOrder.onlineFilmStatus') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: outInkageEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
        <div style="margin-left: 70px;">{{ $t('searchOrder.outputRequestStatus') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: outRequestEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
        <div style="margin-left: 70px;">{{ $t('hollow.idleStateOneColon') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: IdleStateOne, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
        <div style="margin-left: 70px;">{{ $t('hollow.idleStateTwoColon') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: IdleStateTwo, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
        <div style="margin-left: 70px;">{{ $t('hollow.idleStateThreeColon') }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: IdleStateThree, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      </div>
    <div style="display: flex; align-items: flex-start; gap: 20px; height: 70vh;">
<!-- // 父级框 -->
<div class="img-zkdlpl" >
    <div class="img-car1" :style="'z-index:999;left:212px;top:' + 1300*carPosition[0] + 'px;position:absolute;'">
      <div  
      v-for="(rect, index) in adjustOne"  
      :key="rect.id"  
      :style="{
       width: '30px',
       height: '5px',
       backgroundColor: '#409EFF',
       marginLeft: 5+ `px`,
       top: '10px',
       }"  
    >
    </div>
    </div>
    <div class="img-car4" :style="'z-index:999;left:588px;top:' + 450*carPosition[1] + 'px;position:absolute;'">
      <div  
      v-for="(rect, index) in adjustTwo"  
      :key="rect.id"  
      :style="{
       width: '30px',
       height: '5px',
       backgroundColor: '#409EFF',
       marginLeft: 5+ `px`,
       top: '10px',
       }"  
    >
    </div>
    </div>
    <div style="position: relative;">
    <div v-show="cellOne" style="width: 150px;height: 33px;position: relative;top:292px;left: 418px;">
      <div v-for="(rect, rectIndex) in adjustedRectsOne" :key="rect.id" :style="rectStyleOne(rect, rectIndex)">
    <div
      v-for="(subRect, subIndex) in getSubRectsOne(rectIndex)"
      :key="subIndex"
      :style="subRectStyleOne(rectIndex, subIndex)"
    ></div>
    </div>
    </div>
 <div v-show="cellTwo" style="width: 150px;height: 33px;position: relative;top:295px;left: 418px;">
  <div v-for="(rect, rectIndex) in adjustedRectsTwo" :key="rect.id" :style="rectStyleTwo(rect, rectIndex)">
    <div
      v-for="(subRect, subIndex) in getSubRectsTwo(rectIndex)"
      :key="subIndex"
      :style="subRectStyleTwo(rectIndex, subIndex)"
    ></div>
    </div>
    </div>
  <div v-show="cellThree" style="width: 150px;height: 33px;position: relative;top:298px;left: 418px;">
    <div v-for="(rect, rectIndex) in adjustedRectsThree" :key="rect.id" :style="rectStyleThree(rect, rectIndex)">
    <div
      v-for="(subRect, subIndex) in getSubRectsThree(rectIndex)"
      :key="subIndex"
      :style="subRectStyleThree(rectIndex, subIndex)"
    ></div>
    </div>
    </div>
  <div v-show="cellFour" style="width: 150px;height: 33px;position: relative;top:301px;left: 418px;">
    <div v-for="(rect, rectIndex) in adjustedRectsFour" :key="rect.id" :style="rectStyleFour(rect, rectIndex)">
    <div
      v-for="(subRect, subIndex) in getSubRectsFour(rectIndex)"
      :key="subIndex"
      :style="subRectStyleFour(rectIndex, subIndex)"
    ></div>
    </div>
    </div>
  <div v-show="cellFive" style="width: 150px;height: 33px;position: relative;top:304px;left: 418px;">
    <div v-for="(rect, rectIndex) in adjustedRectsFive" :key="rect.id" :style="rectStyleFive(rect, rectIndex)">
    <div
      v-for="(subRect, subIndex) in getSubRectsFive(rectIndex)"
      :key="subIndex"
      :style="subRectStyleFive(rectIndex, subIndex)"
    ></div>
    </div>
    </div>
</div>
</div>
<el-card style="flex: 1; height: 500px; overflow-y: auto;">
      <div style="width: 100%; height: calc(100% - 35px); overflow-y: auto;max-height: 450px;">
       <el-table height="550px" ref="table" border 
       :data="tableDataProcessCardNumber" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
         <el-table-column prop="flowCardId" align="center" :label="$t('searchOrder.processCardNumber')" min-width="140" />
         <el-table-column prop="sumCount" align="center" :label="$t('searchOrder.totalLogarithms')" min-width="120" />
         <el-table-column prop="pairCount" align="center" :label="$t('searchOrder.numberPairs')" min-width="120" />
         <el-table-column prop="totalNumber" align="center" :label="$t('hollow.totalPiecesNumber')" min-width="100" />
         <el-table-column prop="realCount" align="center" :label="$t('hollow.realPieces')" min-width="120" />
         <el-table-column prop="slotCount" align="center" :label="$t('searchOrder.numberOccupied')" min-width="120" />
       </el-table>
       </div>
    </el-card>
  </div>
  <!-- 使用率、空闲数 -->
    <div style="padding: 10px;display: flex;height:110px;margin-top: -170px;">
            <div v-for="(item, index) in tableDataUsage" :key="index" id="occupy">  
               <el-col style="text-align:left;font-weight: bold;">#{{ item.device_id }}</el-col>  
               <el-col style="text-align:left;display:flex;justify-content: space-between;align-items: center;">  
                 <span>{{ $t('searchOrder.usage') }}</span><span>{{ item.percentage }}%</span>  
               </el-col>  
               <hr style="width:80%;margin: 0 auto;" />  
               <el-col style="text-align:left;display:flex;justify-content: space-between;align-items: center;">  
                 <span>{{ $t('searchOrder.freeSlotsNumber') }}</span><span>{{ item.count }}</span>  
               </el-col>  
             </div>  
    </div>
    <div class="table-container">
    <el-card style="flex: 1;margin-left: 10px;margin-top: 3px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;max-height: 140px;">
      <el-table height="120px" ref="table"
        :row-class-name="tableRowClassName"
        :data="tableDataFeedInto" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
          <el-table-column prop="glassId" align="center" :label="$t('searchOrder.intoGlassID')" min-width="140" />
          <el-table-column prop="startSlot" align="center" :label="$t('searchOrder.startPosition')" min-width="120" />
          <el-table-column prop="targetSlot" align="center" :label="$t('searchOrder.targetLocation')" min-width="120" />
        <!-- <el-table-column prop="taskState" align="center" :label="$t('searchOrder.taskstatus')" min-width="125">
        <template #default="scope">
          <el-tag type="success" >{{ scope.row.taskState==0? $t('searchOrder.waitFilmEnter') : $t('searchOrder.onGoing') }}</el-tag>
        </template>
        </el-table-column> -->
        <el-table-column :label="$t('searchOrder.taskstatus')" align="center" min-width="120" prop="taskState">
          <template #default="scope">
            <el-tag :type="getStateType(scope.row.taskState)">
              {{ getStateText(scope.row.taskState) }}
            </el-tag>
          </template>
        </el-table-column>
          <el-table-column prop="isSame" align="center" :label="$t('searchOrder.whetherSame')" min-width="125">
          <template #default="scope">
        <el-tag :type="getcasOnea(scope.row.isSame)">
          {{ getStatuscasOnea(scope.row.isSame) }}
        </el-tag>
      </template>
          </el-table-column>
        </el-table>
       </div>
    </el-card>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 3px;">
      <el-table height="120px" ref="table" 
       :data="tableDataOutGlass" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
         <el-table-column prop="glassId" align="center" :label="$t('searchOrder.outGlassID')" min-width="140" />
         <el-table-column prop="startSlot" align="center" :label="$t('searchOrder.startPosition')" min-width="120" />
         <el-table-column prop="targetSlot" align="center" :label="$t('searchOrder.targetLocation')" min-width="120" />
        <!-- <el-table-column prop="taskState" align="center" :label="$t('searchOrder.taskstatus')" min-width="157">
          <template #default="scope">
            <el-tag type="success" >{{ scope.row.taskState==0? $t('searchOrder.waitFilmEnter') : $t('searchOrder.onGoing') }}</el-tag>
          </template>
        </el-table-column> -->
        <el-table-column :label="$t('searchOrder.taskstatus')" align="center" min-width="120" prop="taskState">
          <template #default="scope">
            <el-tag :type="getStateType(scope.row.taskState)">
              {{ getStateText(scope.row.taskState) }}
            </el-tag>
          </template>
        </el-table-column>
       </el-table>
            </el-card>
  </div>
  </div>
  <!-- 添加理片笼信息 -->
  <el-dialog v-model="dialogAddCageInformation" top="10vh" width="70%" :title="$t('searchOrder.addCageInformation')" >
    <div style="margin-bottom: 20px">
      <el-form>
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="4">
              <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('searchOrder.deviceIDColon')"  style="width: 14vw">
                {{ currentRow.deviceId }}
              </el-form-item>
              </div>
          </el-col>
          <el-col :span="5">
            <div id="dta" style="font-size: 15px;">
          <el-form-item :label="$t('searchOrder.gridNumberColon')" style="width: 14vw">
                {{ currentRow.slot }}
              </el-form-item>
              </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
      <div style="margin-top: -20px;margin-bottom: 40px;margin-left: 30px">
          <el-input v-model="glassId"   :placeholder="$t('searchOrder.glassIDEnter')" clearable style="width: 200px;">
          </el-input>
          <el-button type="primary" plain style="margin-left: 10px;" @click="searchOut">{{ $t('searchOrder.search') }}</el-button>
    </div>
    <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;max-height: 200px;">
        <el-table height="100%" ref="table"
        :data="tableDataAddCageInformation" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
          <el-table-column prop="glassId" align="center" :label="$t('Mounting.glassID')" min-width="120" />
          <el-table-column prop="sequence" align="center" :label="$t('searchOrder.pieceInGrid')" min-width="180" />
          <el-table-column prop="flowCardId" align="center" :label="$t('searchOrder.processCardNumber')" min-width="150" />
          <el-table-column prop="glassType" align="center" :label="$t('searchOrder.typeGlass')" min-width="150"/>
          <el-table-column prop="width" align="center" :label="$t('searchOrder.width')" min-width="150"/>
          <el-table-column prop="height" align="center" :label="$t('Mounting.height')" min-width="150"/>
          <el-table-column prop="thickness" align="center" :label="$t('searchOrder.thickness')" min-width="150"/>
          <el-table-column prop="temperingLayoutId" align="center" :label="$t('Mounting.temperedLayoutID')" min-width="150"/>
          <el-table-column prop="temperingFeedSequence" align="center" :label="$t('Mounting.temperedLayoutSequence')" min-width="150"/>
          <el-table-column
          align="center"
            :label="$t('searchOrder.enabledStatus')"
            min-width="80"
            prop="state"
          >
          <template #default="scope">  
          <el-tag  
            :type="scope.row.state === 1 ? 'success' : 'danger'"  
          >  
          {{ scope.row.state === 1 ? $t('searchOrder.enable') : $t('searchOrder.disable') }}
          </el-tag>  
        </template> 
          </el-table-column>
          <el-table-column prop="gap" align="center" :label="$t('searchOrder.glassGaps')" min-width="150"/>
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="150">
            <template #default="scope">
              <el-button type="text" plain @click="confirmAddition(scope.row)">{{ $t('') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
      </div>
  </el-dialog>
  <!-- 理片笼信息 -->
<el-dialog v-model="dialogFormVisiblea" top="2vh" width="97%" :title="$t('searchOrder.sliceCagInformation')">
  <div style="display: flex;">
    <el-input v-model="flowCardId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('searchOrder.processCardNumberEnter')" />
    <el-input v-model="filmsId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.filmsEnter')"/>
    <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="fetchInquireCageInformation">
      {{$t('order.inquire')}}</el-button>
  </div>
    <el-table  
          :data="tableDataCageInformation" 
          @row-click="handleRowClick" 
          height="700"
          row-key="id" 
          default-expand-all
          :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" 
        >  
          <el-table-column type="expand">  
            <template #default="props">  
              <div v-if="props.row.hollowBigStorageCageDetails && props.row.hollowBigStorageCageDetails.length">  
                <el-table  
                  :data="props.row.hollowBigStorageCageDetails"  
                  border
                  style="width: 98%;margin-left: 20px;"  
                  row-key="id"  
                  :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" 
                >  
          <!-- <el-table-column prop="menuName" label="二级菜单栏" align="center" min-width="140" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"/>   -->
          <el-table-column prop="glassId" :label="$t('Mounting.glassID')" align="center" min-width="130"/>
          <el-table-column prop="flowCardId" align="center" :label="$t('searchOrder.processCardNumber')" min-width="130" />
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80" />
          <el-table-column prop="width" align="center" :label="$t('searchOrder.width')" min-width="80" />
          <el-table-column prop="height" align="center" :label="$t('Mounting.height')" min-width="80" />
          <el-table-column prop="thickness" align="center" :label="$t('searchOrder.thickness')" min-width="80" />
          <el-table-column prop="temperingLayoutId" align="center" :label="$t('Mounting.temperedLayoutID')" min-width="100" />
          <el-table-column prop="temperingFeedSequence" align="center" :label="$t('Mounting.temperedLayoutSequence')" min-width="120" />
          <el-table-column
            align="center"
            :label="$t('searchOrder.enabledStatus')"
            min-width="80"
            prop="state"
          >
          <template #default="scope">  
        <el-tag :type="getStatusType(scope.row.state)">  
          {{ getStatusText(scope.row.state) }}  
        </el-tag>  
      </template> 
          </el-table-column>
          <el-table-column prop="gap" align="center" :label="$t('searchOrder.glassGaps')" min-width="80" />
           <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center"  min-width="220">
            <template #default="scope">
              <el-button type="text" plain @click="dilapidationAction(scope.row)">{{ $t('Mounting.dilapidation') }}</el-button>
              <el-button type="text" plain @click="takeAction(scope.row)">{{ $t('searchOrder.takeout') }}</el-button>
              <el-button type="text" plain @click="deleteAction(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
                </el-table>
              </div>  
            </template>  
          </el-table-column>  
          <el-table-column prop="id" :label="$t('searchOrder.daliTableID')" align="center" min-width="100"/>  
          <el-table-column prop="deviceId" align="center" :label="$t('searchOrder.sliceCageNumber')" min-width="150" />
          <el-table-column prop="slot" align="center" :label="$t('searchOrder.gridNumber')" min-width="150" />
          <el-table-column
          align="center"
            :label="$t('searchOrder.enabledStatus')"
            min-width="80"
            prop="enableState"
          >
          <template #default="scope">  
          <el-tag  
            :type="scope.row.enableState === 1 ? 'success' : 'danger'"  
            @click="toggleEnableState(scope.row)"
          >  
            {{ scope.row.enableState === 1 ? $t('searchOrder.enable') : $t('searchOrder.disable')}}  
          </el-tag>  
        </template> 
          </el-table-column>
          <el-table-column prop="remainWidth" align="center" :label="$t('searchOrder.remainingWidth')" min-width="120" />
          
       <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center">
            <template #default="scope">
              <el-button type="text" plain  @click="handleBindRack(scope.row)">{{ $t('Mounting.add') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
        <div>
  </div>
  </el-dialog>
  <!-- 历史任务 -->
<el-dialog v-model="dialogHistory" top="5vh" width="95%" @close="iframeUrl=''">
     <iframe 
     :src="iframeUrl" 
     marginwidth="2000px"
     marginheight="2000px"
     width="100%" 
     height="750px" 
     frameborder="0"
     ></iframe>
  </el-dialog>
</template>
<style>
#dt { display:block; float:left;line-height: 20px;margin-left: 100px;}
#dta { display:block; float:left;line-height: 20px;margin-left: 80%;}
#dialog-footer{
  text-align: center;
  margin-top: -15px;
}
#message{
  text-align: center;
  align-items: center;
  color: black;
   width: 200px;
   height: 100px;
   background-color: #337ecc;
   margin-left: 28%;
}
#awatch{
  height: 450px;
}
#occupy {
    height: 100%;
    width: 15%;
    background-color: white;
    margin: 0px 8px 0px 8px;
    border: 1px #EBEEF5 solid;
    text-align: center;
    padding: 5px;
}
#biao {
    font-size: 12px;
}
#zhi {
    font-size: 18px;
    font-weight: bold;
}
#demo-pagination-block + #demo-pagination-block {
  margin-top: 10px;
}
#demo-pagination-block #demonstration {
  margin-bottom: 16px;
}
::-webkit-scrollbar {
     width: 0 !important;
   }
   ::-webkit-scrollbar {
     width: 0 !important;height: 0;
   }
   .img-list{
  position:relative;
}
.data-img{
  @apply float-none ;
  width:100%;
  height:16rem;
  background: rgba(0, 0, 0, 0);
  opacity: 1;
  border-radius: 0.5rem 0.5rem 0px 0px;
}
.check-img{
  position: absolute;
  width: 3.3125rem;
  height: 2.9375rem;
  top:20rem;
  right: 57rem;
  z-index: 10;
}
.check-imga{
  position: absolute;
  width: 3.3125rem;
  height: 2.9375rem;
  top:15rem;
  right: 28.5rem;
  z-index: 10;
}
.vertical {
    width: 45px;
    height: 25px;
    background-color: #409EFF;
    top: 485px; /* 初始位置 */
    left: 899px; /* 水平居中 */
    transform: translateX(-50%);
    animation: move-vertical 6s infinite; /* 从上到下动画，持续6秒，无限循环 */
}
@keyframes move-vertical {
    0% {
        top: 485px; /* 起始位置 */
    }
    100% {
        top: calc(100% - 210px); /* 从上到下结束位置 */
    }
}
.img-zkdlpl{
  margin-left: 20px;
  margin-top: 0px;
  background-image:url('/zhongkong.png');
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 500px;
  width: 800px;
  max-width: 100%;
  background-size: 800px 500px;
  overflow: hidden;
  position:relative
}
.img-car1{
  display: flex;
  background-image:url('/xiaoche.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 200px;
  width: 200px;
  max-width: 100%;
  background-size: 200px 70px;
  overflow: hidden;
  position:relative
}
.img-car4{
  display: flex;
  background-image:url('/xiaoche.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 200px;
  width: 200px;
  max-width: 100%;
  background-size: 200px 70px;
  overflow: hidden;
  position:relative
}
.table-container {
  display: flex;
  flex-wrap: nowrap; /* 防止换行 */
  justify-content: space-between; /* 根据需要调整子元素之间的间距 */
}
.table-container > el-card {
  flex: 1; /* 使两个卡片平分可用空间 */
  margin-bottom: 10px; /* 可选，根据需要添加底部间距 */
}
#dotClass {
  display: flex;
  margin-left: 20px; 
  size: 50px;
  margin-top: 20px;
  margin-bottom: 10px;
}
.row-red-background {
  background-color: #CDAF95 !important;
}
</style>