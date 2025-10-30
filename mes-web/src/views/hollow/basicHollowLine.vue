<script setup>
import request from "@/utils/request"
import {ElMessage, ElMessageBox} from 'element-plus'
import {computed, onBeforeUnmount, onMounted, onUnmounted, reactive, ref, watch} from "vue";
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {useI18n} from 'vue-i18n'

const { t } = useI18n()
const tableDataProcessCards = ref([])
const tableDataCreateTask = ref([])
const tableDataDetailFilms = ref([])
const tableDataHollowFormula = ref([])
const currentRow = reactive({})
const formattedProcessType = ref([])
const cellClaim = ref('')
const cellForced = ref('')
const bottomRemove = ref('')
const filmRemove = ref('')
const formulaName = ref('')
const leftRemove = ref('')
const rightRemove = ref('')
const topRemove = ref('')
const filmsId = ref('')
const thickness = ref('')
const flowCardId = ref('')
const casOne = ref('')
const casTwo = ref('')
const casThree = ref('')
const casFour = ref('')
const frameOne = ref('')
const frameTwo = ref('')
const frameThree = ref('')
const frameFour = ref('')
const iframeUrl = ref('')
const sealInsert = ref('')
const selectedProjectNo = ref('')
const totalPairQuantity = ref('')
const totalPairQuantitya = ref('')
const selectedFormulaName = ref('')
const intervalFrameHeightOne = ref('')
const intervalFrameHeightTwo = ref('')
const intervalFrameHeightThree = ref('')
const intervalFrameHeightFour = ref('')
const intervalFrameTypeOne = ref('')
const intervalFrameTypeTwo = ref('')
const intervalFrameTypeThree = ref('')
const intervalFrameTypeFour = ref('')
const intervalFrameWidthOne = ref('')
const intervalFrameWidthTwo = ref('')
const intervalFrameWidthThree = ref('')
const intervalFrameWidthFour = ref('')
const freeone = ref('#911005')
const dialogRecipeAdded = ref(false)
const dialogRecipeModifications = ref(false)
const mulan = ref(false)
const dialogStartTask = ref(false)
const dialogHistory = ref(false)
const dialogPauseTask = ref(false)
const dialogDeleteTask = ref(false)
const dialogCreateTask = ref(false)
const dialogMissingFilms = ref(false)
const dialogClaimQuest = ref(false)
const dialogForcedFilming = ref(false)
const dialogPreview = ref(false)
const currentPage = ref(1)
const currentPage2 = ref(1)
const totalRecords = ref(0)
const formulaNameid = ref({ id: null })
const titleformulaName = ref({ formulaName: [] ,formulaId: []})
const titleSelectJson = ref({ processType: [] })
let selectedFormulaId = ref(null)
let socket = null;
const props = defineProps({
  // 线路
  lineNumber: {
    type: Number,
    required: true
  },
  // websockect
  webSocketPath: {
    type: String,
    required: true
  },
  // 线路空闲状态
  idleState: {
    type: String,
    required: true
  },
})
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/hollowGlass/api/talk/${props.webSocketPath}`;
// const currentPage = ref(parseInt(window.localStorage.getItem('currentPage')) || 1);
watch(() => currentPage.value, (newVal) => {
  window.localStorage.setItem('currentPage', newVal);
});
const currentPageKey = computed(() => `93${currentPage.value - 1}`);
const handleMessage = (data) => {
  try {
    // 流程卡表格
    tableDataProcessCards.value = data.queueInfo[0];
    const allPageData = data;
    const currentPageKeyValue = currentPageKey.value;
    const currentPageData = allPageData[currentPageKeyValue];
    if (data.freeRequest != null) {
      freeone.value = data.freeRequest[0] == true ? 'green' : '#911005';
  }
  } catch (error) {
  }
};
// 历史任务
const handlehistorical = (row) => {
  dialogHistory.value = true;
  iframeUrl.value = `${window.location.origin}/#/hollow/basicHollowLineHistory`;
};
// 创建任务
const handleCreateTask = () => {
  dialogCreateTask.value = true;
  fetchFlowCardId();
};
// 创建任务
const fetchFlowCardId = async () => {
  try {
    const response = await request.post('/hollowGlass/hollowGlassRelationInfo/queryHollowAllFlowCard',{
      deviceId: 0,
      filmsId: '',
      flowCardId: '',
      thickness: 0,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataCreateTask.value = response.data;
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 创建任务--查询
const fetchFlowCardSearch = async () => {
  try {
    const response = await request.post('/hollowGlass/hollowGlassRelationInfo/queryHollowAllFlowCard',{
      filmsId: filmsId.value,
      flowCardId: flowCardId.value,
      thickness: thickness.value
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataCreateTask.value = response.data;
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 领取任务
const handleTake = (row) => {
  window.localStorage.setItem('flowCardId', row.flowCardId)
  dialogClaimQuest.value = true;
  fetchHollowFormula()
};
// 强制出片
const handleout = (row) => {
  window.localStorage.setItem('flowCardId', row.flowCardId)
  dialogForcedFilming.value = true;
  fetchHollowFormula()
};
// 中空配方
const fetchHollowFormula = async () => {
  try {
      const response = await request.post('/hollowGlass/hollowFormulaDetails/pageFormulaDetails',{
      pageNo: 1,
      pageSize: 20,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataHollowFormula.value = response.data.records;
        totalRecords.value = response.data.total
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 中空配方-分页
const fetchHollowPageChange = async (page) => {
  try {
    let page = window.localStorage.getItem('pagenumber')
      const response = await request.post('/hollowGlass/hollowFormulaDetails/pageFormulaDetails',{
      pageNo: page,
      pageSize: 20,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataHollowFormula.value = response.data.records;
      totalRecords.value = response.data.total
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 中空配方--修改 
function handleEdit(row) {  
  window.localStorage.setItem('id', row.id)
    formulaName.value = row.formulaName;
    filmRemove.value = removalMethodMap[row.filmRemove];
    bottomRemove.value = row.bottomRemove;
    leftRemove.value = row.leftRemove;
    rightRemove.value = row.rightRemove;
    topRemove.value = row.topRemove;
    casFour.value = removalcas[row.casFour];
    casOne.value = removalcas[row.casOne];
    casThree.value = removalcas[row.casThree];
    casTwo.value = removalcas[row.casTwo];
    frameFour.value = row.frameFour;
    frameOne.value = row.frameOne;
    frameThree.value = row.frameThree;
    frameTwo.value = row.frameTwo;
    intervalFrameHeightOne.value = row.intervalFrameHeightOne;
    intervalFrameHeightTwo.value = row.intervalFrameHeightTwo;
    intervalFrameHeightThree.value = row.intervalFrameHeightThree;
    intervalFrameHeightFour.value = row.intervalFrameHeightFour;
    intervalFrameTypeOne.value =  removalinterval[row.intervalFrameTypeOne];
    intervalFrameTypeTwo.value =  removalinterval[row.intervalFrameTypeTwo];
    intervalFrameTypeThree.value = removalinterval[row.intervalFrameTypeThree];
    intervalFrameTypeFour.value = removalinterval[row.intervalFrameTypeFour];
    intervalFrameWidthOne.value = row.intervalFrameWidthOne;
    intervalFrameWidthTwo.value = row.intervalFrameWidthTwo;
    intervalFrameWidthThree.value = row.intervalFrameWidthThree;
    intervalFrameWidthFour.value = row.intervalFrameWidthFour;
    sealInsert.value = row.sealInsert;
    dialogRecipeModifications.value = true;
}
// 中空配方--添加
function openAddDialog() {  
    formulaName.value = '';
    filmRemove.value = '';
    bottomRemove.value = '';
    leftRemove.value = '';
    rightRemove.value = '';
    topRemove.value = '';
    casFour.value = '';
    casOne.value = '';
    casThree.value = '';
    casTwo.value = '';
    frameFour.value = '';
    frameOne.value = '';
    frameThree.value = '';
    frameTwo.value = '';
    intervalFrameHeightOne.value = '';
    intervalFrameHeightTwo.value = '';
    intervalFrameHeightThree.value = '';
    intervalFrameHeightFour.value = '';
    intervalFrameTypeOne.value = '';
    intervalFrameTypeTwo.value = '';
    intervalFrameTypeThree.value = '';
    intervalFrameTypeFour.value = '';
    intervalFrameWidthOne.value = '';
    intervalFrameWidthTwo.value = '';
    intervalFrameWidthThree.value = '';
    intervalFrameWidthFour.value = '';
    sealInsert.value = '';
    dialogRecipeAdded.value = true;
}
// 中空配方--修改下拉选
const handleFormulaChange = (value) => {
  const selectedItem = titleformulaName.value.find(item => item.formulaName === value);
  if (selectedItem) {
    formulaNameid.value = { id: selectedItem.formulaId };
    window.localStorage.setItem('id', formulaNameid.value.id)
  } else {
    formulaNameid.value = { id: null };
  }
};
// 验证函数
function validateForm() {
  const errors = [];
  if (!formulaName.value) errors.push('必填！');
  if (filmRemove.value === null) errors.push('必填！');
  if (!bottomRemove.value) errors.push('必填！');
  if (!topRemove.value) errors.push('必填！');
  if (!leftRemove.value) errors.push('必填！');
  if (!rightRemove.value) errors.push('必填！');
  if (casOne.value === null) errors.push('必填！');
  if (!frameOne.value) errors.push('必填！');
  if (!intervalFrameHeightOne.value) errors.push('必填！');
  if (!intervalFrameWidthOne.value) errors.push('必填！');
  if (intervalFrameTypeOne.value === null) errors.push('必填！');
  if (!sealInsert.value) errors.push('必填！');
  if (errors.length > 0) {
    ElMessage.error(errors.join(''));
    return false;
  }
  return true;
}
// 中空配方--添加
const handleConfirm = async () => {
  try {
    if (!validateForm()) return;  
    const response = await request.post('/hollowGlass/hollowFormulaDetails/saveFormulaDetails',{
      formulaName: formulaName.value,
      filmRemove: filmRemove.value,
      bottomRemove: bottomRemove.value,
      leftRemove: leftRemove.value,
      rightRemove: rightRemove.value,
      topRemove: topRemove.value,
      casFour: casFour.value,
      casOne: casOne.value,
      casThree: casThree.value,
      casTwo: casTwo.value,
      frameFour: frameFour.value,
      frameOne: frameOne.value,
      frameThree: frameThree.value,
      frameTwo: frameTwo.value,
      intervalFrameHeightOne: intervalFrameHeightOne.value,
      intervalFrameHeightTwo: intervalFrameHeightTwo.value,
      intervalFrameHeightThree: intervalFrameHeightThree.value,
      intervalFrameHeightFour: intervalFrameHeightFour.value,
      intervalFrameTypeOne: intervalFrameTypeOne.value,
      intervalFrameTypeTwo: intervalFrameTypeTwo.value,
      intervalFrameTypeThree: intervalFrameTypeThree.value,
      intervalFrameTypeFour: intervalFrameTypeFour.value,
      intervalFrameWidthOne: intervalFrameWidthOne.value,
      intervalFrameWidthTwo: intervalFrameWidthTwo.value,
      intervalFrameWidthThree: intervalFrameWidthThree.value,
      intervalFrameWidthFour: intervalFrameWidthFour.value,
      sealInsert: sealInsert.value,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      fetchHollowFormula()
      dialogRecipeAdded.value = false
      formulaName.value = '';
      filmRemove.value = '';
      bottomRemove.value = '';
      leftRemove.value = '';
      rightRemove.value = '';
      topRemove.value = '';
      casFour.value = '';
      casOne.value = '';
      casThree.value = '';
      casTwo.value = '';
      frameFour.value = '';
      frameOne.value = '';
      frameThree.value = '';
      frameTwo.value = '';
      intervalFrameHeightOne.value = '';
      intervalFrameHeightTwo.value = '';
      intervalFrameHeightThree.value = '';
      intervalFrameHeightFour.value = '';
      intervalFrameTypeOne.value = '';
      intervalFrameTypeTwo.value = '';
      intervalFrameTypeThree.value = '';
      intervalFrameTypeFour.value = '';
      intervalFrameWidthOne.value = '';
      intervalFrameWidthTwo.value = '';
      intervalFrameWidthThree.value = '';
      intervalFrameWidthFour.value = '';
      sealInsert.value = '';
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 中空配方修改
 const handledit = async() => {  
  let id = window.localStorage.getItem('id')
  try {
    console.log(removalMethodMapa[filmRemove.value]);
    console.log(filmRemove.value);
    
    const response = await request.post('/hollowGlass/hollowFormulaDetails/updateFormulaDetails',{
      id: id,
      formulaName: formulaName.value,
      filmRemove: filmRemove.value,
      // filmRemove: removalMethodMapa[filmRemove.value],
      bottomRemove: bottomRemove.value,
      leftRemove: leftRemove.value,
      rightRemove: rightRemove.value,
      topRemove: topRemove.value,
      casFour: removalcasa[casFour.value],
      casOne: removalcasa[casOne.value],
      casThree: removalcasa[casThree.value],
      casTwo: removalcasa[casTwo.value],
      frameFour: frameFour.value,
      frameOne: frameOne.value,
      frameThree: frameThree.value,
      frameTwo: frameTwo.value,
      intervalFrameHeightOne: intervalFrameHeightOne.value,
      intervalFrameHeightTwo: intervalFrameHeightTwo.value,
      intervalFrameHeightThree: intervalFrameHeightThree.value,
      intervalFrameHeightFour: intervalFrameHeightFour.value,
      intervalFrameTypeOne: removalintervala[intervalFrameTypeOne.value],
      intervalFrameTypeTwo: removalintervala[intervalFrameTypeTwo.value],
      intervalFrameTypeThree: removalintervala[intervalFrameTypeThree.value],
      intervalFrameTypeFour:  removalintervala[intervalFrameTypeFour.value],
      intervalFrameWidthOne: intervalFrameWidthOne.value,
      intervalFrameWidthTwo: intervalFrameWidthTwo.value,
      intervalFrameWidthThree: intervalFrameWidthThree.value,
      intervalFrameWidthFour: intervalFrameWidthFour.value,
      sealInsert: sealInsert.value,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogRecipeModifications.value = false
      fetchHollowFormula()
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 中空配方--删除 
 const handleDelete = async(row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('hollow.deleteHollowFormulaWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
    const response = await request.post('/hollowGlass/hollowFormulaDetails/deleteFormulaDetails',{
      idList: [ row.id ]
    })
    if (response.code === 200) {
      ElMessage.success(response.message);
      fetchHollowFormula()
    } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
};
// 预览
const selectproject = () => {
  dialogPreview.value = true;
  selectProjectNumber();
};
// 工程号
const selectProjectNumber = async () => {
  try {
    var url="/hollowGlass/hollowGlassOutRelationInfo/hollowTaskList?cell="+ props.lineNumber;
    const response = await request.post(url)
    if (response.code == 200) {
      const processTypes = response.data.map((projectNo, index) => ({
        value: projectNo,
        label: projectNo
      }));
      titleSelectJson.value.processType = processTypes;
    } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 预览
const handlePreview = async () => {
  if (!selectedProjectNo.value) {
    ElMessage.error(t('Mounting.selectProject'))
    return
  }
  try {
  var url="/hollowGlass/hollowGlassOutRelationInfo/appointHollowTaskDetails?cell="+ props.lineNumber + "&flowCardId=" + selectedProjectNo.value;
  window.localStorage.setItem('flowCardId', selectedProjectNo.value)
  const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataProcessCards.value = response.data;
      dialogPreview.value = false
      selectedProjectNo.value = ''
      closeWebSocket(socket);
  } else {
      ElMessage.error('Failed to fetch data');
    }
  } catch (error) {
    ElMessage.error('Error fetching data');
  }
};
// 中空配方
const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage.value)
  fetchHollowPageChange(currentPage.value);
};
// 缺片详情
const handleBindRack = (row) => {
  currentPage2.value = 1;
  const { flowCardId } = row;
  currentRow.flowCardId  = row.flowCardId;
  dialogMissingFilms.value = true;
  fetchFlowBind(flowCardId,currentPage2.value);
};
// 缺片详情--点击分页
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage;
  fetchFlowBind(currentRow.flowCardId, currentPage2.value);
};
// 缺片详情
const fetchFlowBind = async (flowCardId, page) => {
  try {
    var url="/hollowGlass/hollowGlassRelationInfo/queryLackByFlowCard?flowCardId="+flowCardId + "&layer=" + 1;
    const response = await request.post(url)
    if (response.code == 200) {
      tableDataDetailFilms.value = response.data[page];
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 开始任务 
const handleBindStartTask = (row) => {
  dialogStartTask.value = true;
};
// 开始任务
const handleStartTask = async () => {
  try {
    let flowCardId = window.localStorage.getItem('flowCardId')
    if (flowCardId !== '') {
    var url="/hollowGlass/hollowGlassOutRelationInfo/startTask?flowCardId="+flowCardId + "&cell=" + props.lineNumber;
    const response = await request.post(url)
      if (response.code == 200) {
        ElMessage.success(response.message);
        dialogStartTask.value = false;
      } else {
        ElMessage.error(response.message);
      }
    } else {
      ElMessage({
        type: 'info',
        message: t('basicData.infonull'),
      })
    }
  } catch (error) {
  }
}
// 暂停任务 
const handleBindPauseTask = (row) => {
  dialogPauseTask.value = true;
};
// 暂停任务
const handlePauseTask = async () => {
  try {
    let flowCardId = window.localStorage.getItem('flowCardId')
    if (flowCardId !== '') {
    var url="/hollowGlass/hollowGlassOutRelationInfo/pauseTask?flowCardId="+flowCardId + "&cell=" + props.lineNumber;
    const response = await request.post(url)
      if (response.code == 200) {
        ElMessage.success(response.message);
        blindc.value = false;
      } else {
        ElMessage.error(response.message);
      }
    } else {
      ElMessage({
        type: 'info',
        message: t('basicData.infonull'),
      })
    }
  } catch (error) {
  }
}
// 删除任务 
const handleBindDeleteTask = (row) => {
  dialogDeleteTask.value = true;
};
// 删除任务
const handleDeleteTask = async () => {
  try {
    let flowCardId = window.localStorage.getItem('flowCardId')
    if (flowCardId !== '') {
    var url="/hollowGlass/hollowGlassOutRelationInfo/deleteHollowTaskDetails?flowCardId="+flowCardId + "&cell=" + props.lineNumber;
    const response = await request.post(url)
      if (response.code == 200) {
        ElMessage.success(response.message);
        dialogDeleteTask.value = false;
      } else {
        ElMessage.error(response.message);
      }
    } else {
      ElMessage({
        type: 'info',
        message: t('basicData.infonull'),
      })
    }
  } catch (error) {
  }
}
// 领取任务
const finishRecipeModifications = async () => {
  let flowCardId = window.localStorage.getItem('flowCardId')
  let id = window.localStorage.getItem('id')
  if (!totalPairQuantity.value) {
    ElMessage.error(t('hollow.totalPairQuantityEnter'))
    return
  }
  if (!selectedFormulaName.value) {
    ElMessage.error(t('hollow.hollowFormulaSelection'))
    return
  }
    try {
      var url="/hollowGlass/hollowGlassOutRelationInfo/receiveTask?cell="+ props.lineNumber + "&flowCardId=" + flowCardId + "&totalPairQuantity=" + totalPairQuantity.value+ "&formulaId=" + id;
      const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogClaimQuest.value = false;
      cellClaim.value = '';
      formulaName.value = '';
      totalPairQuantity.value = '';
    } else {
          ElMessage.error(response.message);
        }
      } catch (error) {
      }
}
// 强制出片
const finishForced = async () => {
  let flowCardId = window.localStorage.getItem('flowCardId')
  let id = window.localStorage.getItem('id')
  if (!totalPairQuantity.value) {
    ElMessage.error(t('hollow.totalPairQuantityEnter'))
    return
  }
  if (!selectedFormulaName.value) {
    ElMessage.error(t('hollow.hollowFormulaSelection'))
    return
  }
  try {
      var url="/hollowGlass/hollowGlassOutRelationInfo/forceOutGlass?cell="+ props.lineNumber + "&flowCardId=" + flowCardId + "&totalPairQuantity=" + totalPairQuantitya.value+ "&formulaId=" + id;
      const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogForcedFilming.value = false;
      cellForced.value = '';
      totalPairQuantitya.value = '';
    } else {
          ElMessage.error(response.message);
        }
      } catch (error) {
      }
}
// 创建任务
function tableRowClassName({ row }) {
  if (row.isThroughSlot == true) {
    return 'row-red-background';
  }
  return '';
}
function nestedTableRowClassName(parentRow, row) {
  if (parentRow.isThroughSlot == true) {
    return 'row-red-background';
  }
  return '';
}
// 转换
const removalMethodMapa = {
  [t('Mounting.removeFilm')]: 1,
  [t('Mounting.notRemoved')]: 0,
};
const removalcasa = {
  [t('hollow.Y')]: 1,
  [t('hollow.F')]: 0,
}
const removalintervala = {
  [t('hollow.aluminumSpacer')]: 1,
  [t('hollow.tpsGlue')]: 5,
}
const handleormulan = () => {
  mulan.value = true;
  fetchHollowFormula();
};
const removalMethodMap = {
  1: t('Mounting.removeFilm'),
  0: t('Mounting.notRemoved')
};
const removalcas = {
  1: t('hollow.Y'),
  0: t('hollow.F')
}
const removalinterval = {
  1: t('hollow.aluminumSpacer'),
  5: t('hollow.tpsGlue')
}
function getCasOne(casOne) {  
  switch (casOne) {  
    case "1":
      return 'success';  
    case "0": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusCasOnea(casOne) {
  switch (casOne) {
    case "1":  
      return t('hollow.Y');
    case "0":  
      return t('hollow.F'); 
    default:
      return '';
  }  
}
function getCasTwo(casTwo) {  
  switch (casTwo) {  
    case "1":
      return 'success';  
    case "0": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusCasTwo(casTwo) {
  switch (casTwo) {
    case "1":  
      return t('hollow.Y');
    case "0":  
      return t('hollow.F'); 
    default:
      return '';
  }  
}
function getCasThree(casThree) {  
  switch (casThree) {  
    case "1":
      return 'success';  
    case "0": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusCasThree(casThree) {
  switch (casThree) {
    case "1":  
      return t('hollow.Y');
    case "0":  
      return t('hollow.F'); 
    default:
      return '';
  }  
}
function getCasFour(casFour) {  
  switch (casFour) {  
    case "1":
      return 'success';  
    case "0": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusCasFour(casFour) {
  switch (casFour) {
    case "1":  
      return t('hollow.Y');
    case "0":  
      return t('hollow.F'); 
    default:
      return '';
  }  
}
function getStatusTypeOne(intervalFrameTypeOne) {  
  switch (intervalFrameTypeOne) {  
    case "1":
      return 'success';  
    case "5": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusTextOne(intervalFrameTypeOne) {
  switch (intervalFrameTypeOne) {
    case "1":  
      return t('hollow.aluminumSpacer');
    case "5":  
      return t('hollow.tpsGlue'); 
    default:
      return '';
  }  
}
function getStatusTypeTwo(intervalFrameTypeTwo) {  
  switch (intervalFrameTypeTwo) {  
    case "1":
      return 'success';  
    case "5": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusTextTwo(intervalFrameTypeTwo) {
  switch (intervalFrameTypeTwo) {
    case "1":  
      return t('hollow.aluminumSpacer');
    case "5":  
      return t('hollow.tpsGlue'); 
    default:
      return '';
  }  
}
function getStatusTypeThree(intervalFrameTypeThree) {  
  switch (intervalFrameTypeThree) { 
    case "1":
      return 'success';  
    case "5": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusTextThree(intervalFrameTypeThree) {
  switch (intervalFrameTypeThree) {
    case "1":  
      return t('hollow.aluminumSpacer');
    case "5":  
      return t('hollow.tpsGlue'); 
    default:
      return '';
  }  
}
function getStatusTypeFour(intervalFrameTypeFour) {  
  switch (intervalFrameTypeFour) {  
    case "1":
      return 'success';  
    case "5": 
      return 'warning';  
    default:
      return 'info'; 
  }  
}  
function getStatusTextFour(intervalFrameTypeFour) {
  switch (intervalFrameTypeFour) {
    case "1":  
      return t('hollow.aluminumSpacer');
    case "5":  
      return t('hollow.tpsGlue'); 
    default:
      return '';
  }  
}
const options = [
  {
    value: 930,
    label: t('hollow.lineOne'),
  },
  {
    value: 931,
    label: t('hollow.lineTwo'),
  }
]
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
  window.localStorage.setItem('flowCardId', '')
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
  }
});
onBeforeUnmount(() => {
  closeWebSocket();
});
</script>
<template>
  <div style="height: 600px;">
    <el-card style="flex: 1;margin-left: 10px;margin-top: 10px;" >
      <div style="display: flex; flex-direction: row; align-items: center; margin-bottom: 10px;">
        <el-button id="searchButton" type="primary" @click="handleormulan">
          {{ $t('hollow.hollowFormula') }}
        </el-button>
        <el-button id="searchButton" type="primary" @click="handleCreateTask">
          {{ $t('hollow.createTask') }}
        </el-button>
        <el-button type="primary" @click="selectproject">
          {{ $t('hollow.previewProject') }}
        </el-button>
    <el-button style="margin-left: 10px;" id="searchButton" type="primary" @click="handlehistorical">
        {{ $t('searchOrder.historicaltasks') }}
    </el-button>
    <el-button style="margin-left: 10px;" id="searchButton" type="success" @click="handleBindStartTask">
          {{ $t('hollow.startTask') }}
        </el-button>
        <el-button style="margin-left: 10px;" id="searchButton" type="info" @click="handleBindDeleteTask">{{
            $t('hollow.deleteTask')
          }}
        </el-button>
        <el-button style="margin-left: 10px;" id="searchButton" type="warning" @click="handleBindPauseTask">{{
            $t('hollow.pauseTask')
          }}
        </el-button>
  </div>
    <div id="dotClass">
        <div style="margin-left: 70px;">{{ $t(`hollow.${idleState}`) }}</div>
        <i :style="{ marginTop: '2px', backgroundColor: freeone, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
       </div>
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table height="720" ref="table" :data="tableDataProcessCards"
                  :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }">
          <el-table-column prop="flowCardId" :label="$t('Mounting.processCards')" align="center"/>
          <el-table-column prop="glassId" :label="$t('Mounting.glassID')" align="center"/>
          <el-table-column prop="hollowSequence" :label="$t('hollow.orderReleased')" align="center"/>
          <el-table-column prop="thickness" :label="$t('searchOrder.thickness')" align="center"/>
          <el-table-column prop="layer" :label="$t('hollow.layerNumber')" align="center"/>
          <el-table-column prop="filmsId" :label="$t('searchOrder.membraneSystem')" align="center"/>
          <el-table-column prop="height" :label="$t('hollow.long')" align="center"/>
          <el-table-column prop="width" :label="$t('searchOrder.width')" align="center"/>
          <el-table-column prop="state" :label="$t('Mounting.state')" align="center">
            <template #default="scope">
            {{ scope.row.state == -1 ? $t('hollow.waitingFilmOut') : $t('hollow.filmComplete') }}
            </template>
          </el-table-column>
        </el-table>
  </div>
  </el-card>
  </div>
  <!-- 创建任务 -->
  <el-dialog v-model="dialogCreateTask" top="5vh" width="85%">
    <div style="display: flex;">
    <el-input v-model="flowCardId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('searchOrder.processCardNumberEnter')" />
    <el-input v-model="filmsId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.filmsEnter')"/>
    <el-input v-model="thickness" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('searchOrder.thicknessEnter')"/>
    <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="fetchFlowCardSearch">
      {{$t('order.inquire')}}</el-button>
  </div>
  <el-table
    ref="table"
    style="margin-top: 20px; height: 700px;"
    :data="tableDataCreateTask"
    :row-class-name="tableRowClassName"
    :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }"
    row-key="projectNumber"
  >
    <el-table-column>
    <template #default="props">
      <el-table :data="props.row.flowCardGlassInfoDTOList" style="width: 100%" 
      :row-class-name="(row) => nestedTableRowClassName(props.row, row)" 
        >
      <el-table-column prop="layer" fixed align="center" :label="$t('hollow.layerNumber')" min-width="80"/>
      <el-table-column prop="thickness" align="center" :label="$t('searchOrder.thickness')" min-width="80"/>
      <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80"/>
      <el-table-column prop="sumCount" align="center" :label="$t('hollow.totalPiecesNumber')" min-width="80"/>
      <el-table-column prop="realCount" align="center" :label="$t('hollow.realPieces')" min-width="80"/>
      <el-table-column prop="pairCount" align="center" :label="$t('hollow.pairsNumber')" min-width="80"/>
      <el-table-column prop="damageCount" align="center" :label="$t('hollow.damageNumber')" min-width="80"/>
      </el-table>
      </template>
    </el-table-column>
    <el-table-column prop="flowCardId" fixed align="center" :label="$t('searchOrder.processCardNumber')" min-width="20"/>
    <el-table-column prop="productName" fixed align="center" :label="$t('hollow.productName')" min-width="20"/>
    <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="270">
        <template #default="scope">
          <el-button type="text" :disabled="scope.row.isThroughSlot" plain @click="handleTake(scope.row)">{{ $t('hollow.claimQuest') }}</el-button>
          <el-button type="text" plain @click="handleBindRack(scope.row)">{{
              $t('hollow.missingFilms')
            }}
          </el-button>
          <el-button type="text" plain @click="handleout(scope.row)">{{
              $t('hollow.forcedFilming')
            }}
          </el-button>
        </template>
      </el-table-column>
  </el-table>
</el-dialog>
<!-- 中空配方添加 -->
<el-dialog v-model="dialogRecipeAdded" top="15vh" width="90%" :title="$t('hollow.hollowFormula')" >
  <div style="margin-left: 50px;margin-top: 10px;margin-bottom: 10px;">
    <el-form label-width="150px" label-position="right">
      <!-- 第一行：3个元素 -->
      <el-row style="margin-top: 10px;">
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.formulaNameColon')" style="width: 90%;">
              <el-input v-model="formulaName" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('Mounting.removalMethodColon')" style="width: 90%;">
              <el-select v-model="filmRemove" clearable style="width: 100%" :placeholder="$t('Mounting.removalMethodSelection')">
                <el-option :label="$t('Mounting.removeFilm')" value="1"></el-option>
                <el-option :label="$t('Mounting.notRemoved')" value="0"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.sealInsertColon')" style="width: 90%;">
              <el-input v-model="sealInsert" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <!-- 第二行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.topRemoveColon')" style="width: 90%;">
              <el-input v-model="topRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.bottomRemoveColon')" style="width: 90%;">
              <el-input v-model="bottomRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.leftRemoveColon')" style="width: 90%;">
              <el-input v-model="leftRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.rightRemoveColon')" style="width: 90%;">
              <el-input v-model="rightRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <!-- 第三行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casOneColon')" style="width: 90%;">
              <el-select v-model="casOne" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casTwoColon')" style="width: 90%;">
              <el-select v-model="casTwo" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casThreeColon')" style="width: 90%;">
              <el-select v-model="casThree" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casFourColon')" style="width: 90%;">
              <el-select v-model="casFour" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <!-- 第四行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameOneColon')" style="width: 90%;">
              <el-input v-model="frameOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameTwoColon')" style="width: 90%;">
              <el-input v-model="frameTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameThreeColon')" style="width: 90%;">
              <el-input v-model="frameThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameFourColon')" style="width: 90%;">
              <el-input v-model="frameFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <!-- 第五行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightOneColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightTwoColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightThreeColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightFourColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <!-- 第六行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthOneColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthTwoColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthThreeColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthFourColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第七行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeOneColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeOne" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeTwoColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeTwo" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeThreeColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeThree" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeFourColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeFour" style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
  <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handleConfirm">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogRecipeAdded = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
<!-- 中空配方修改 -->
<el-dialog v-model="dialogRecipeModifications" top="15vh" width="85%" :title="$t('hollow.editHollowFormula')" >
  <div style="margin-left: 50px;margin-top: 10px;margin-bottom: 10px;">
    <el-form label-width="150px" label-position="right">
      <!-- 第一行：3个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.formulaNameColon')" style="width: 90%;">
              <el-input v-model="formulaName" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('Mounting.removalMethodColon')" style="width: 90%;">
              <el-select v-model="filmRemove" clearable style="width: 100%" :placeholder="$t('screendisplay.pchoice')">
                <el-option :label="$t('Mounting.removeFilm')" value="1"></el-option>
                <el-option :label="$t('Mounting.notRemoved')" value="0"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.sealInsertColon')" style="width: 90%;">
              <el-input v-model="sealInsert" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第二行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.topRemoveColon')" style="width: 90%;">
              <el-input v-model="topRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.bottomRemoveColon')" style="width: 90%;">
              <el-input v-model="bottomRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.leftRemoveColon')" style="width: 90%;">
              <el-input v-model="leftRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.rightRemoveColon')" style="width: 90%;">
              <el-input v-model="rightRemove" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第三行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casOneColon')" style="width: 90%;">
              <el-select v-model="casOne" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casTwoColon')" style="width: 90%;">
              <el-select v-model="casTwo" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casThreeColon')" style="width: 90%;">
              <el-select v-model="casThree" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.casFourColon')" style="width: 90%;">
              <el-select v-model="casFour" clearable style="width: 100%" :placeholder="$t('hollow.casSelection')">
                <el-option :label="$t('hollow.F')" value="0"></el-option>
                <el-option :label="$t('hollow.Y')" value="1"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第四行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameOneColon')" style="width: 90%;">
              <el-input v-model="frameOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameTwoColon')" style="width: 90%;">
              <el-input v-model="frameTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameThreeColon')" style="width: 90%;">
              <el-input v-model="frameThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.frameFourColon')" style="width: 90%;">
              <el-input v-model="frameFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第五行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightOneColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightTwoColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightThreeColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameHeightFourColon')" style="width: 90%;">
              <el-input v-model="intervalFrameHeightFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第六行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthOneColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthOne" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthTwoColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthTwo" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthThreeColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthThree" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameWidthFourColon')" style="width: 90%;">
              <el-input v-model="intervalFrameWidthFour" autocomplete="off" />
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <!-- 第七行：4个元素 -->
      <el-row style="margin-top: 20px;">
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeOneColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeOne" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeTwoColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeTwo" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeThreeColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeThree" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="font-size: 15px;">
            <el-form-item :label="$t('hollow.intervalFrameTypeFourColon')" style="width: 90%;">
              <el-select v-model="intervalFrameTypeFour" clearable style="width: 100%" :placeholder="$t('hollow.intervalFrameTypeSelection')">
                <el-option :label="$t('hollow.aluminumSpacer')" value="1"></el-option>
                <el-option :label="$t('hollow.tpsGlue')" value="5"></el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
  <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handledit">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogRecipeModifications = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 领取任务 -->
  <el-dialog v-model="dialogClaimQuest" top="21vh" width="40%" :title="$t('hollow.claimQuest')">
    <div style="margin-left: 20px;margin-top: 10px;margin-bottom: 10px;">
      <el-form label-width="100px">
        <el-form label-width="210px" label-position="right">
          <el-form-item :label="$t('hollow.totalPairQuantityColon')" :required="true" style="width: 25vw">
            <el-input :placeholder="$t('hollow.totalPairQuantityEnter')" v-model="totalPairQuantity" autocomplete="off" />
          </el-form-item>
          <el-form-item :label="$t('hollow.routeColon')" :required="true" style="width: 25vw;">
            <el-select 
            :placeholder="$t('hollow.lineOne')"
            disabled
             style="width: 270px"  
             v-model="cellClaim">
               <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
          </el-select>
          </el-form-item>
          <el-form-item :label="$t('hollow.hollowFormulaColon')" :required="true" style="width: 25vw;">
        <el-select
            v-model="selectedFormulaName"
            filterable
            clearable
            :placeholder="$t('hollow.hollowFormulaSelection')"
            style="width: 270px"
            @change="handleFormulaChange"
        >
          <el-option
              v-for="item in titleformulaName"
              :key="item.id"
              :label="item.formulaName"
              :value="item.formulaName"
          />
        </el-select>
      </el-form-item>
        </el-form>
      </el-form>
    </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="finishRecipeModifications">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogClaimQuest = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 强制出片 -->
  <el-dialog v-model="dialogForcedFilming" top="21vh" width="40%" :title="$t('hollow.forcedFilming')">
    <div style="margin-left: 20px;margin-top: 10px;margin-bottom: 10px;">
      <el-form label-width="100px">
        <el-form label-width="210px" label-position="right">
          <el-form-item :label="$t('hollow.totalPairQuantityColon')" :required="true" style="width: 25vw">
            <el-input :placeholder="$t('hollow.totalPairQuantityEnter')" v-model="totalPairQuantitya" autocomplete="off" />
          </el-form-item>
          <el-form-item :label="$t('hollow.routeColon')" :required="true" style="width: 25vw;">
            <el-select 
            :placeholder="$t('hollow.lineOne')"
            disabled
             style="width: 270px"  
             v-model="cellForced">
               <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
          </el-select>
          </el-form-item>
          <el-form-item :label="$t('hollow.hollowFormulaColon')" :required="true" style="width: 25vw;">
        <el-select
            v-model="selectedFormulaName"
            filterable
            clearable
            :placeholder="$t('hollow.hollowFormulaSelection')"
            style="width: 270px"
            @change="handleFormulaChange"
        >
          <el-option
              v-for="item in titleformulaName"
              :key="item.id"
              :label="item.formulaName"
              :value="item.formulaName"
          />
        </el-select>
      </el-form-item>
        </el-form>
      </el-form>
    </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="finishForced">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogForcedFilming = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 缺片详情 -->
  <el-dialog v-model="dialogMissingFilms" top="7vh" width="70%" height="500">
    <div style="margin-top: -20px;text-align: center;margin-left: 400px;">
      <el-form-item :label="$t('hollow.cardNumberColon')" style="width: 14vw">
        {{ currentRow.flowCardId }}
      </el-form-item>
    </div>
    <el-table ref="table" style="margin-top: 20px;height: 400px;"
          :data="tableDataDetailFilms" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
      <el-table-column prop="thickness" align="center" :label="$t('Mounting.thickness')" min-width="80"/>
      <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80"/>
      <el-table-column prop="height" align="center" :label="$t('hollow.long')" min-width="80"/>
      <el-table-column prop="width" align="center" :label="$t('Mounting.width')" min-width="80"/>
      <el-table-column prop="lackCount" align="center" :label="$t('hollow.missingNumber')" min-width="80"/>
      <div style="float: right;margin-bottom: 5px;">
        <el-pagination layout="prev, pager, next" :total="50"/>
      </div>
    </el-table>
    <div style="display: flex;margin-left: 40%;margin-top: 10px;">
  <el-pagination
          background
          size="large"
          layout="prev, pager, next"
          :total="30"
          :current-page.sync="currentPage2"
          @current-change="handlePageChange2"
        />
  </div>
  </el-dialog>
  <!-- 开始任务 -->
  <el-dialog v-model="dialogStartTask" top="30vh" width="25%" :title="$t('hollow.startTaskWhether')">
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handleStartTask">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogStartTask = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 暂停任务 -->
  <el-dialog v-model="dialogPauseTask" top="30vh" width="25%" :title="$t('hollow.pauseTaskWhether')">
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handlePauseTask">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogPauseTask = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 删除任务 -->
  <el-dialog v-model="dialogDeleteTask" top="30vh" width="25%" :title="$t('hollow.deleteTaskWhether')">
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handleDeleteTask">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogDeleteTask = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 预览 -->
  <el-dialog v-model="dialogPreview" top="24vh" width="25%" :title="$t('hollow.previewProject')" >
    <div style="margin-left: 50px;margin-bottom: 10px;">
      <el-form-item :label="$t('searchOrder.projectnumber')" :required="true">
        <el-select
            v-model="selectedProjectNo"
            filterable
            clearable
            :placeholder="$t('Mounting.selectProject')"
            style="width: 260px"
        >
          <el-option
              v-for="item in titleSelectJson.processType"
              :key="item.value"
              :label="item.value"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handlePreview">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogPreview = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 中空配方 -->
  <el-dialog v-model="mulan" top="15vh" width="80%" :title="$t('hollow.hollowFormula')">
    <el-button id="searchButton" type="primary"  @click="openAddDialog">
          {{ $t('Mounting.add') }}
        </el-button>
      <el-table ref="table" style="margin-top: 20px;height: 400px;"
                :data="tableDataHollowFormula" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
        <el-table-column prop="formulaName" fixed align="center" :label="$t('hollow.formulaName')" min-width="120"/>
        <!-- <el-table-column prop="filmRemove" align="center" :label="$t('Mounting.removalMethod')"/> -->
        <el-table-column prop="filmRemove" align="center" :label="$t('Mounting.removalMethod')" min-width="120">
          <template #default="scope">
            {{ removalMethodMap[scope.row.filmRemove] || scope.row.filmRemove }}
          </template>
        </el-table-column>
        <el-table-column prop="casOne" align="center" :label="$t('hollow.casOne')" min-width="110">
      <template #default="scope">
        <el-tag :type="getCasOne(scope.row.casOne)">
          {{ getStatusCasOnea(scope.row.casOne) }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="casTwo" align="center" :label="$t('hollow.casTwo')" min-width="110">
      <template #default="scope">
        <el-tag :type="getCasTwo(scope.row.casTwo)">
          {{ getStatusCasTwo(scope.row.casTwo) }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="casThree" align="center" :label="$t('hollow.casThree')" min-width="110">
      <template #default="scope">
        <el-tag :type="getCasThree(scope.row.casThree)">
          {{ getStatusCasThree(scope.row.casThree) }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="casFour" align="center" :label="$t('hollow.casFour')" min-width="110">
      <template #default="scope">
        <el-tag :type="getCasFour(scope.row.casFour)">
          {{ getStatusCasFour(scope.row.casFour) }}
        </el-tag>
      </template>
    </el-table-column>
        <el-table-column prop="frameOne" align="center" :label="$t('hollow.frameOne')"/>
        <el-table-column prop="frameTwo" align="center" :label="$t('hollow.frameTwo')"/>
        <el-table-column prop="frameThree" align="center" :label="$t('hollow.frameThree')"/>
        <el-table-column prop="frameFour" align="center" :label="$t('hollow.frameFour')"/>
        <el-table-column prop="intervalFrameHeightOne" align="center" :label="$t('hollow.intervalFrameHeightOne')" min-width="110"/>
        <el-table-column prop="intervalFrameHeightTwo" align="center" :label="$t('hollow.intervalFrameHeightTwo')" min-width="110"/>
        <el-table-column prop="intervalFrameHeightThree" align="center" :label="$t('hollow.intervalFrameHeightThree')" min-width="110"/>
        <el-table-column prop="intervalFrameHeightFour" align="center" :label="$t('hollow.intervalFrameHeightFour')" min-width="110"/>
        <el-table-column prop="intervalFrameTypeOne" align="center" :label="$t('hollow.intervalFrameTypeOne')" min-width="110">
      <template #default="scope">
        <el-tag :type="getStatusTypeOne(scope.row.intervalFrameTypeOne)">
          {{ getStatusTextOne(scope.row.intervalFrameTypeOne) }}
        </el-tag>
      </template>
    </el-table-column>
        <el-table-column prop="intervalFrameTypeTwo" align="center" :label="$t('hollow.intervalFrameTypeTwo')" min-width="110">
          <template #default="scope">  
         <el-tag :type="getStatusTypeTwo(scope.row.intervalFrameTypeTwo)">  
          {{ getStatusTextTwo(scope.row.intervalFrameTypeTwo) }}  
        </el-tag>  
      </template>
        </el-table-column>
        <el-table-column prop="intervalFrameTypeThree" align="center" :label="$t('hollow.intervalFrameTypeThree')" min-width="110">
          <template #default="scope">  
         <el-tag :type="getStatusTypeThree(scope.row.intervalFrameTypeThree)">  
          {{ getStatusTextThree(scope.row.intervalFrameTypeThree) }}  
        </el-tag>  
      </template>
        </el-table-column>
        <el-table-column prop="intervalFrameTypeFour" align="center" :label="$t('hollow.intervalFrameTypeFour')" min-width="110">
          <template #default="scope">  
         <el-tag :type="getStatusTypeFour(scope.row.intervalFrameTypeFour)">  
          {{ getStatusTextFour(scope.row.intervalFrameTypeFour) }}  
        </el-tag>  
      </template>
        </el-table-column>
        <el-table-column prop="intervalFrameWidthOne" align="center" :label="$t('hollow.intervalFrameWidthOne')" min-width="110"/>
        <el-table-column prop="intervalFrameWidthTwo" align="center" :label="$t('hollow.intervalFrameWidthTwo')" min-width="110"/>
        <el-table-column prop="intervalFrameWidthThree" align="center" :label="$t('hollow.intervalFrameWidthThree')" min-width="110"/>
        <el-table-column prop="intervalFrameWidthFour" align="center" :label="$t('hollow.intervalFrameWidthFour')" min-width="110"/>
        <el-table-column prop="sealInsert" align="center" :label="$t('hollow.sealInsert')"/>
        <el-table-column prop="topRemove" align="center" :label="$t('hollow.topRemove')" min-width="95"> </el-table-column>
        <el-table-column prop="bottomRemove" align="center" :label="$t('hollow.bottomRemove')" min-width="95"> </el-table-column>
        <el-table-column prop="leftRemove" align="center" :label="$t('hollow.leftRemove')" min-width="95"> </el-table-column>
        <el-table-column prop="rightRemove" align="center" :label="$t('hollow.rightRemove')" min-width="95"> </el-table-column>
        <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="270">
            <template #default="scope">
              <el-button type="text" plain @click="handleEdit(scope.row)">{{ $t('productStock.exit') }}</el-button>
              <el-button type="text" plain @click="handleDelete(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
         <div style="margin-top: 20px;margin-left: 40%;">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :size="large"
          :disabled="disabled"
          layout="prev, pager, next, jumper"
          :total="totalRecords"
          @current-change="handlePageChange"
          style="margin-top: 10px;"
      />
    </div>
        <template #footer>
      <div id="dialog-footer">
        <el-button @click="mulan = false">{{ $t('Mounting.close') }}</el-button>
      </div>
    </template>
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
<style scoped>
#dt { display:block; float:left;line-height: 20px;margin-left: 100px;}
#dta { display:block; float:left;line-height: 20px;margin-left: 80%;}
#dialog-footer {
  text-align: center;
  margin-top: -15px;
}
#dotClass {
  display: flex;
  margin-left: 20px; 
  size: 50px;
  margin-top: 20px;
  margin-bottom: 10px;
}
::v-deep(.row-red-background) {
  background-color: rgb(224.6, 242.8, 215.6) !important;
}
</style>