<script lang="ts" setup>
import request from "@/utils/request"
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import {inject, nextTick, onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue"
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService'
import {ElMessage, ElMessageBox} from 'element-plus'
const {t} = useI18n()
const router = useRouter()
const timeRange = ref([])
const carPosition = ref([])
const AdjustCarOne = ref([])
const AdjustCarTwo = ref([])
const tableDataOut = ref([])
const tableDataLack = ref([])
const tableDataUsage = ref([])
const tableDataAddCage = ref([])
const tableDataProject = ref([])
const AdjustedRectsOne = ref([])
const AdjustedRectsTwo = ref([])
const AdjustedRectsFour = ref([])
const subRectsCountsOne = ref([])
const subRectsCountsTwo = ref([])
const AdjustedRectsThree = ref([])
const subRectsCountsFour = ref([])
const tableDataIntoGlass = ref([])
const subRectsCountsThree = ref([])
const tableDatacagedetails = ref([])
const tableDataNonToughened = ref([])
const tableDataGlassMessage = ref([])
const tableDataTemperingQueries = ref([])
const mergeFields = ['deviceId', 'slot', 'enableState', 'remainWidth', 'leftCoordinate', 'rightCoordinate']
const props = defineProps({
  showOutboundButton: {
    type: Boolean,
    default: true // 默认显示
  }
})
const singleTableData = ref([])
const tableMode = ref<'paged' | 'single'>('paged')
// 存储合并规则
const spanArr = ref<{ index: number; rowspan: number }[]>([]);
const pos = ref(0)
const currentRow = reactive({}) // 当前行的数据 
const gap = ref('')
const width = ref('')
const filmsId = ref('')
const glassId = ref('')
const deviceId = ref('')
const iframeUrl = ref('')
const slot = ref('')
const engineerId = ref('')
const endDate = ref('')
const thickness = ref('')
const startSlot = ref('')
const beginDate = ref('')
const targetSlot = ref('')
const flowCardId = ref('')
const SwitchSteel = ref('')
const taskTypeList = ref('')
const taskStateList = ref('')
const SwitchDispatch = ref('')
const temperingEngineerId = ref('')
const currentPage = ref(1)
const globalDate = inject('globalDate')
const cellOne = ref(true)
const cellTwo = ref(true)
const cellFour = ref(true)
const cellThree = ref(true)
const DialogAddCage = ref(false)
const dialogHistory = ref(false)
const dialogTemperingTasks = ref(false)
const DialogCageDetails = ref(false)
const DialogNonToughened = ref(false)
const DialogMissingNumber = ref(false)
const DialogCageInformation = ref(false)
const DialogTemperingQueries = ref(false)
const selectedRow = ref(null)
const glassTotal = ref(0)
const totalRecords = ref(0)
const fullTotals = ref(0)
const temperingTotal = ref(0)
const inkageEntity = ref('#911005')
const requestEntity = ref('#911005')
const mesReplyEntity = ref('#911005')
const outInkageEntity = ref('#911005')
const outRequestEntity = ref('#911005')
const outReplyEntity = ref('#911005')
let language = ref(localStorage.getItem('lang') || 'zh')
let socket = null
request.get("/verticalGlass/VerticalJobController/queryAllVerticalSheetCageMessage").then((response) => {
  if (response.code == 200) {
    // 使用率
    //   if (Array.isArray(response.data.bigStorageCageUsage) && response.data.bigStorageCageUsage.length > 0) {
    // tableDataUsage.value = response.data.bigStorageCageUsage
    //   } else {
    //     tableDataUsage.value = [];
    //   }
    // // 工程表
    //   if (Array.isArray(response.data.bigStorageSummary) && response.data.bigStorageSummary.length > 0) {
    //     tableDataProject.value = response.data.bigStorageSummary;
    //   } else {
    //     tableDataProject.value = [];
    //   }
    tableDataUsage.value = response.data.bigStorageCageUsage
    tableDataProject.value = response.data.bigStorageSummary;
    // 进片联机状态
    inkageEntity.value = response.data.inOnlineState == true ? 'green' : '#911005';
    // 进片请求状态
    requestEntity.value = response.data.inPlcRequest == 1 ? 'green' : '#911005';
    // 进片启动命令
    mesReplyEntity.value = response.data.inMesSend == 1 ? 'green' : '#911005';
    // 出片联机状态
    outInkageEntity.value = response.data.outOnlineState == true ? 'green' : '#911005';
    // 出片请求状态
    outRequestEntity.value = response.data.outPlcRequest == 1 ? 'green' : '#911005';
    // 出片启动命令
    outReplyEntity.value = response.data.outMesSend == 1 ? 'green' : '#911005';
    SwitchSteel.value = response.data.temperingSwitch
    SwitchDispatch.value = response.data.dispatchSwitch
    tableDataIntoGlass.value = response.data.inTaskList
    tableDataOut.value = response.data.outTaskList
    AdjustedRectsOne.value = response.data.verticalSheetCageInfoMap[1].map((rect, index) => ({
      id: index + 1,
      height: 20 / 40,
      top: 48 / 40,
    }));
    subRectsCountsOne.value = response.data.verticalSheetCageInfoMap[1].map(rect => rect.count);
    AdjustedRectsTwo.value = response.data.verticalSheetCageInfoMap[2].map((rect, index) => ({
      id: index + 1,
      height: 20 / 40,
      top: 48 / 40,
    }));
    subRectsCountsTwo.value = response.data.verticalSheetCageInfoMap[2].map(rect => rect.count);
  } else {
    ElMessage.warning(response.msg)
  }
});
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/verticalGlass/webSocket/sliceCage`;
const handleMessage = (data) => {
  // 工程表
  if (data.bigStorageSummary != null) {
    tableDataProject.value = data.bigStorageSummary
  } else {
    tableDataProject.value = ''
  }
  // 进片联机状态
  if (data.inOnlineState != null) {
    inkageEntity.value = data.inOnlineState[0] == true ? 'green' : '#911005';
  }
  // 进片请求状态
  if (data.inPlcRequest != null) {
    requestEntity.value = data.inPlcRequest == 1 ? 'green' : '#911005';
  }
  // 进片启动命令
  if (data.inMesSend != null) {
    mesReplyEntity.value = data.inMesSend == 1 ? 'green' : '#911005';
  }
  // 出片联机状态
  if (data.outOnlineState != null) {
    outInkageEntity.value = data.outOnlineState == true ? 'green' : '#911005';
  }
  // 出片请求状态
  if (data.outPlcRequest != null) {
    outRequestEntity.value = data.outPlcRequest == 1 ? 'green' : '#911005';
  }
  // 出片启动命令
  if (data.outMesSend != null) {
    outReplyEntity.value = data.outMesSend == 1 ? 'green' : '#911005';
  }
  //当前指定工程
  if (data.temperingengineerId != null) {
    temperingEngineerId.value = data.temperingengineerId;
  }
  // 出片玻璃
  if (data.outTaskList != null) {
    tableDataOut.value = data.outTaskList
    AdjustCarOne.value = data.outTaskList.filter(rect => rect.slot !== null && rect.slot !== undefined);
  } else {
    tableDataOut.value = '',
    AdjustCarOne.value = ''
  }
  // 进片玻璃
  if (data.inTaskList != null) {
    tableDataIntoGlass.value = data.inTaskList
  } else {
    tableDataIntoGlass.value = ''
  }
//  使用率、空闲数
  if (data.bigStorageCageUsage != null) {
    tableDataUsage.value = data.bigStorageCageUsage
  } else {
    tableDataUsage.value = ''
  }
// 小车位置
  if (data.carPostion != null) {
    carPosition.value = data.carPostion
  } else {
    carPosition.value = ''
  }
  // 钢化开关
  if (data.temperingSwitch != null) {
    SwitchSteel.value = data.temperingSwitch
  } else {
    SwitchSteel.value = ''
  }
  // 调度开关
  if (data.dispatchSwitch != null) {
    SwitchDispatch.value = data.dispatchSwitch
  } else {
    SwitchDispatch.value = ''
  }
  // 小片
  if (data.verticalSheetCageInfoMap != null) {
    AdjustedRectsOne.value = data.verticalSheetCageInfoMap[1].map((rect, index) => ({
      id: index + 1,
      height: 20 / 40,
      top: 38 / 40,
    }));
    subRectsCountsOne.value = data.verticalSheetCageInfoMap[1].map(rect => rect.count);
    AdjustedRectsTwo.value = data.verticalSheetCageInfoMap[2].map((rect, index) => ({
      id: index + 1,
      height: 20 / 40,
      top: 48 / 40,
    }));
    subRectsCountsTwo.value = data.verticalSheetCageInfoMap[2].map(rect => rect.count);
  } else {
    AdjustedRectsOne.value = '',
        AdjustedRectsTwo.value = '',
        AdjustedRectsThree.value = '',
        AdjustedRectsFour.value = ''
  }
};
// 计算每个大矩形的样式
const rectStyleOne = (rect, index) => ({
  position: 'absolute',
  width: '160px',
  right: '0px',
  top: `${index * rect.top}px`,
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
  const marginRight = '8px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginRight));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginRight))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginRight,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleTwo = (rect, index) => ({
  position: 'absolute',
  width: '160px',
  right: '0px',
  top: `${index * rect.top}px`,
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
  const marginRight = '8px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginRight));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginRight))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginRight,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleThree = (rect, index) => ({
  position: 'absolute',
  width: '160px',
  right: '0px',
  top: `${index * rect.top}px`,
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
  const marginRight = '8px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginRight));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginRight))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginRight,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
// 计算每个大矩形的样式
const rectStyleFour = (rect, index) => ({
  position: 'absolute',
  width: '160px',
  right: '0px',
  top: `${index * rect.top}px`,
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
  const width = '16px';
  const marginRight = '8px';
  const totalWidth = 6 * (parseInt(width) + parseInt(marginRight));
  const left = `${(subIndex * (parseInt(width) + parseInt(marginRight))) / totalWidth * 100}%`;
  return {
    position: 'absolute',
    width,
    height: '100%',
    marginRight,
    top: '0px',
    backgroundColor: '#911005',
    left,
  };
};
const getTableRow = (row, type) => {
  switch (type) {
    case 'edit' : {
      router.push({path: '/main/returns/createReturns', query: {ReturnID: 'TH24010101'}})
      break
    }
    case 'delete': {
      alert('我接收到子组件传送的删除信息')
      break
    }
  }
}
// 理片笼信息--添加
const handleBindRack = (row) => {
  selectedRow.value = row; // 更新选中的行数据  
  currentRow.deviceId = row.deviceId; // 直接设置响应式属性
  currentRow.slot = row.slot;
  window.localStorage.setItem('deviceId', row.deviceId)
  window.localStorage.setItem('slot', row.slot)
  DialogAddCage.value = true;
};
// 理片笼信息添加--搜索
const searchOut = async () => {
  try {
    const response = await request.post('/verticalGlass/bigStorageCageDetails/selectBigStorageCageDetails', {
      glassId: glassId.value
    });
    if (response.code == 200) {
      tableDataAddCage.value = response.data;
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
// 是否禁用
const toggleEnableState = async (row) => {
  const newState = row.enableState === 1 ? 0 : 1;
  const response = await request.post('/verticalGlass/verticalSheetCage/updateVerticalSheetCageState', {
    enableState: newState,
    shelf: row.slot
  });
  if (response.code === 200) {
    ElMessage.success(response.message);
  } else {
    ElMessage.error(response.message);
  }
  row.enableState = newState;
};
// 是否破损
const broken = async (row) => {
  try {
    const newState = row.isDamage === 1 ? 0 : 1;
    const response = await request.post('/verticalGlass/bigStorageCageDetails/bigStorageGlassDamageByGlassId', {glassId: row.glassId});
    if (response.code == 200) {
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
    row.isDamage = newState;
  } catch (error) {
  }
}
// 历史任务
const handlehistorical = (row) => {
  dialogHistory.value = true;
  iframeUrl.value = `${window.location.origin}/#/daliSliceCage/sliceCageHistory`;
};
const handleTemperingTasks = (row) => {
  dialogTemperingTasks.value = true;
  iframeUrl.value = `${window.location.origin}/#/daliSliceCage/sliceTemperingTasks`;
}
// 分页-出片
const handleOutPage = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.filmReleasedWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
          cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/VerticalJobController/sendGlassToManualInspect", {glassId: row.glassId})
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
};
// 分页-破损
const handleBreakPage = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('Mounting.dilapidationWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
          cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      // const response = await request.post("/verticalGlass/VerticalJobController/sendGlassToManualInspect",  { glassId: row.glassId })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
};
// 单页-出片
const handleOutSingle = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.filmReleasedWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
          cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/VerticalJobController/sendGlassToManualInspect", {glassId: row.glassId})
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
};
// 单页-破损
const handleBreakSingle = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('Mounting.dilapidationWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
          cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      // const response = await request.post("/verticalGlass/VerticalJobController/sendGlassToManualInspect",  { glassId: row.glassId })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
};
// 指定钢化
const brokeSpecif = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.offConnectionWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/TemperingTask/appointTemperingEngineerId", {
        engineerId: row.engineerId,
        deviceId: 1,
        temperingLayoutId: row.temperingLayoutId,
        isForce: 1,
        target: 930,
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
  TemperingQuery();
};
// 指定工程
const SpecifyProject = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.offConnectionWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/TemperingTask/appointTemperingEngineerId", {
        engineerId: row.engineerId,
        deviceId: 0,
        temperingLayoutId: 0,
        isForce: 0,
        target: 930,
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
};
// 理片笼信息
const openDialog = () => {
  DialogCageInformation.value = true
  // 重置为分页模式并获取数据
  tableMode.value = 'paged'
  fetchCageInformation()
}
// 定义模板中使用的类型
defineExpose({
  openDialog
})
// 获取分页表格数据
const fetchCageInformation = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryVerticalSheetCageDetailPage', {
      pageNo: 1,
      pageSize: 20,
      endTime: "",
      startTime: "",
      deviceId: -1,
      slot: -1,
      flowCardId: "",
      filmsId: "",
      thickness: -1,
      engineerId: "",
      glassId: "",
      state: ""
    })
    if (response.code === 200) {
      ElMessage.success(response.message)
      const formattedTasks = response.data.records.map(task => {
        const formattedTask = {...task};
        if (formattedTask.createTime) {
          formattedTask.createTime = formatTimestamp(formattedTask.createTime);
        }
        if (formattedTask.updateTime) {
          formattedTask.updateTime = formatTimestamp(formattedTask.updateTime);
        }
        return formattedTask;
      });
      tableDataGlassMessage.value = formattedTasks;
      totalRecords.value = response.data.total / 2 || 0
      // 执行合并计算
      nextTick(() => {
        getSpan(tableDataGlassMessage.value, mergeFields);
      });
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('请求失败')
    console.error(error)
  }
}
const toggleTableMode = async () => {
  if (tableMode.value === 'paged') {
    // 分页模式切换到单页模式
    try {
      const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryVerticalSheetCageDetailsList', {
      flowCardId: flowCardId.value,
      filmsId: filmsId.value,
      thickness: -1,
      engineerId: engineerId.value,
      glassId: glassId.value,
      });
      if (response.code === 200) {
        singleTableData.value = response.data;
        nextTick(() => getSpanArr(singleTableData.value)); // 数据加载后计算合并规则
        tableMode.value = 'single'; // 切换模式
      }
    } catch (error) { /* 错误处理 */
    }
  } else {
    // 单页模式切换回分页模式
    tableMode.value = 'paged';
    await fetchCageInformation(); // 重新获取分页数据
  }
}
const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage.value)
  fetchPage(currentPage.value);
};
const fetchPage = async (page) => {
  try {
    let page = window.localStorage.getItem('pagenumber')
    let startTime = window.localStorage.getItem('startTime')
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryVerticalSheetCageDetailPage', {
      pageNo: page,
      pageSize: 20,
      endTime: "",
      startTime: "",
      deviceId: -1,
      slot: -1,
      flowCardId: "",
      filmsId: "",
      thickness: -1,
      engineerId: "",
      glassId: "",
      state: ""
    })
    if (response.code == 200) {
      ElMessage.success(response.message)
      const formattedTasks = response.data.records.map(task => {
        const formattedTask = {...task};
        if (formattedTask.createTime) {
          formattedTask.createTime = formatTimestamp(formattedTask.createTime);
        }
        if (formattedTask.updateTime) {
          formattedTask.updateTime = formatTimestamp(formattedTask.updateTime);
        }
        return formattedTask;
      });
      tableDataGlassMessage.value = formattedTasks;
      totalRecords.value = response.data.total / 2 || 0
      // 执行合并计算
      nextTick(() => {
        getSpan(tableDataGlassMessage.value, mergeFields);
      });
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// 状态标签类型和文本（示例方法，需要根据实际业务实现）
const getStatusTypeb = (state: string) => {
  // 根据状态返回不同的标签类型
  const map: Record<string, string> = {
    'completed': 'success',
    'processing': 'warning',
    'failed': 'danger',
    'pending': 'info'
  }
  return map[state] || ''
}
// 分页-查询按钮
const handleClick = async () => {
  try {
    let startTime = window.localStorage.getItem('startTime')
    let page = window.localStorage.getItem('pagenumber')
    const response = await request.post("/verticalGlass/verticalSheetCageDetails/queryVerticalSheetCageDetailPage", {
      pageNo: 1,
      pageSize: 20,
      glassId: glassId.value,
      startTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[0])) || '',
      endTime: (formatTimestamp(timeRange.value) && formatTimestamp(timeRange.value[1])) || '',
      deviceId: -1,
      slot: -1,
      flowCardId: flowCardId.value,
      filmsId: filmsId.value,
      thickness: -1,
      engineerId: engineerId.value,
    })
    if (response.code == 200) {
      ElMessage.success(response.message)
      const formattedTasks = response.data.records.map(task => {
        const formattedTask = {...task};
        if (formattedTask.createTime) {
          formattedTask.createTime = formatTimestamp(formattedTask.createTime);
        }
        if (formattedTask.updateTime) {
          formattedTask.updateTime = formatTimestamp(formattedTask.updateTime);
        }
        return formattedTask;
      });
      tableDataGlassMessage.value = formattedTasks;
      totalRecords.value = response.data.total / 2 || 0
      // 执行合并计算
      nextTick(() => {
        getSpan(tableDataGlassMessage.value, mergeFields);
      });
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// 合并方法
const getSpan = (data: any[], mergeFields: string[]) => {
  const result: { index: number; rowspan: number }[] = [];
  let pos = 0;
  if (data.length === 0) return [];
  // 初始化首行
  result.push({index: 0, rowspan: 1});
  // 从第二行开始处理
  for (let i = 1; i < data.length; i++) {
    const currentKey = mergeFields.map(f => String(data[i][f])).join('|');
    const prevKey = mergeFields.map(f => String(data[i - 1][f])).join('|');
    if (currentKey === prevKey) {
      // 合并到起始行
      result[pos].rowspan += 1;
      result.push({index: pos, rowspan: 0});
    } else {
      result.push({index: i, rowspan: 1});
      pos = i;
    }
  }
  spanArr.value = result;
};
// 表格合并
const getSpanMethod = ({row, column, rowIndex}: any) => {
  const field = column.property;
  if (!mergeFields.includes(field)) return {rowspan: 1, colspan: 1};

  const target = spanArr.value[rowIndex];
  return target?.rowspan > 0
      ? {rowspan: target.rowspan, colspan: 1}
      : {rowspan: 0, colspan: 0};
};
const handleCpage = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryVerticalSheetCageDetailsList', {
      flowCardId: flowCardId.value,
      filmsId: filmsId.value,
      thickness: -1,
      engineerId: engineerId.value,
      glassId: glassId.value,
    });
    if (response.code == 200) {
      ElMessage.success(response.message)
      singleTableData.value = response.data
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// 非钢化流程卡
// const fetchFlows = async () => {
//   try {  
//     var url="/verticalGlass/bigStorageCageDetails/selectTemperingGlass?isTempering="+0;
//     const response = await request.post(url)
//     if (response.code === 200) {  
//       ElMessage.success(response.message);
//       tableDataNonToughened.value = response.data
//     } else {  
//       ElMessage.warning(response.msg)
//     }  
//   } catch (error) {
//   } 
// }
// 钢化查询--缺片数量
const handleLack = (row) => {
  const {engineerId, temperingLayoutId} = row;
  fetchLack(engineerId, temperingLayoutId);
  DialogMissingNumber.value = true;
}
// 钢化查询--缺片数量
const fetchLack = async (engineerId, temperingLayoutId) => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryLackGlassInfo', {
      engineerId: engineerId,
      temperingLayoutId: temperingLayoutId,
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataLack.value = response.data
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    // 处理错误
  }
}
// 钢化查询--笼内详情
const handleCageDetails = (row) => {
  const {engineerId, temperingLayoutId} = row;
  fetchCageDetails(engineerId, temperingLayoutId);
  DialogCageDetails.value = true;
}
// 钢化查询--笼内详情
const fetchCageDetails = async (engineerId, temperingLayoutId) => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryRealGlassInfo', {
      engineerId: engineerId,
      temperingLayoutId: temperingLayoutId,
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDatacagedetails.value = response.data
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 钢化查询
const TemperingQuery = () => {
  DialogTemperingQueries.value = true;
  fetchFlow();
};
const fetchFlow = async () => {
  try {
    var url = "/verticalGlass/verticalSheetCageDetails/queryTempList";
    const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataTemperingQueries.value = response.data
      temperingTotal.value = response.data.length
      let totalCount = 0;
      let fullCount = 0;
      response.data.forEach(item => {
        totalCount += item.realCount || 0;
        if (item.lackCount == 0) {
          fullCount += 1;
        }
      });
      glassTotal.value = totalCount;
      fullTotals.value = fullCount;

    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
function handleRowClick(row) {
  selectedRow.value = row; // 更新选中的行数据  
}
// 钢化开关
const handleSteel = async () => {
  try {
    // const body = {  
    //   flag: SwitchSteel.value,
    // }; 
    var url = "/verticalGlass/VerticalJobController/temperingSwitch?flag=" + SwitchSteel.value;
    const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      SwitchSteel.value = response.data
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 调度开关
const handleDispatch = async () => {
  try {
    // const body = {  
    //   flag: SwitchDispatch.value,
    // };
    var url = "/verticalGlass/VerticalJobController/dispatchSwitch?flag=" + SwitchDispatch.value;
    const response = await request.post(url)
    if (response.code == 200) {
      ElMessage.success(response.message);
      SwitchDispatch.value = response.data
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 任务重置
const handleTask = async () => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.taskResetWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post('/verticalGlass/bigStorageCage/resetCage')
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
  }
};
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
  parseAndSetTime();
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
  }
});
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
function getcasIsSame(isSame) {
  switch (isSame) {
    case 1:
      return 'danger';
    case 0:
      return 'info';
  }
}
function getStatuscasIsSame(isSame) {
  switch (isSame) {
    case 1:
      return t('searchOrder.same');
    case 0:
      return t('searchOrder.notSame');
  }
}
function tableRowClassName({row}) {
  if (row.isSame === 1) {
    return 'row-red-background';
  }
  return '';
}
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
// 格式化后端时间并计算一周前的时间
const defaultTime = ref<[Date, Date]>([new Date(), new Date()]);
function parseAndSetTime() {
  const backendTime = new Date(globalDate);
  const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 减去7天
  oneWeekAgo.setHours(0, 0, 0, 0);
  timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
  window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
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

onBeforeUnmount(() => {
  closeWebSocket();
});
</script>
<template>
  <div style="height: 600px;">
    <el-button id="searchButton" style="margin-top: 7px;margin-left: 10px;" type="primary" @click="openDialog">
      {{ $t('searchOrder.sliceCagInformation') }}
    </el-button>
    <el-button id="searchButton" style="margin-top: 7px;margin-left: 10px;" type="success" @click="handlehistorical">
      {{ $t('Mounting.historicalTasks') }}
    </el-button>
    <el-button id="searchButton" v-if="showOutboundButton" style="margin-top: 7px;margin-left: 10px;" type="warning" @click="TemperingQuery">
      {{ $t('searchOrder.temperingQueries') }}
    </el-button>
    <el-button id="searchButton" v-if="showOutboundButton" style="margin-top: 7px;margin-left: 10px;" type="info"
               @click="DialogNonToughened=true;fetchFlows()">{{ $t('searchOrder.noTemperedCardInquiry') }}
    </el-button>
    <!-- <el-button style="margin-top: 7px;margin-left: 10px;" id="searchButton" type="danger" @click="handleTask()">{{ $t('searchOrder.taskReset') }}</el-button> -->
    <el-switch v-model="SwitchSteel" :inactive-text="$t('searchOrder.temperedSwitch')" class="mb-2"
               style="margin-top: 7px;margin-left: 10px;" @change="handleSteel"/>
    <el-switch v-model="SwitchDispatch" :inactive-text="$t('searchOrder.schedulingSwitch')" class="mb-2"
               style="margin-top: 7px;margin-left: 10px;" @change="handleDispatch"/>
    <div id="dotClass">
      <div>{{ $t('searchOrder.advanceOnlineStatus') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: inkageEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <div style="margin-left: 70px;">{{ $t('searchOrder.feedRequestStatus') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: requestEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <div style="margin-left: 70px;">{{ $t('searchOrder.startEnterCommand') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: mesReplyEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <div style="margin-left: 70px;">{{ $t('searchOrder.onlineFilmStatus') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: outInkageEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <div style="margin-left: 70px;">{{ $t('searchOrder.outputRequestStatus') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: outRequestEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
      <div style="margin-left: 70px;">{{ $t('searchOrder.startOutCommand') }}</div>
      <i :style="{ marginTop: '2px', backgroundColor: outReplyEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
    </div>
    <!-- // 父级框 -->
    <div class="img-dlpl">
      <div :style="'z-index:999;left:230px;top:' + 350*carPosition[0] + 'px;position:absolute;'" class="img-car1">
        <div
            v-for="(rect, index) in AdjustCarOne"
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
      <div :style="'z-index:999;left:589px;top:' + 350*carPosition[1] + 'px;position:absolute;'" class="img-car2">
        <div
            v-for="(rect, index) in AdjustCarTwo"
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
        <div v-show="cellOne" style="width: 160px;height: 50px;position: relative;top:69px;left: 420px;">
          <div v-for="(rect, rectIndex) in AdjustedRectsOne" :key="rect.id" :style="rectStyleOne(rect, rectIndex)">
            <div
                v-for="(subRect, subIndex) in getSubRectsOne(rectIndex)"
                :key="subIndex"
                :style="subRectStyleOne(rectIndex, subIndex)"
            ></div>
          </div>
        </div>
        <div v-show="cellTwo" style="width: 160px;height: 50px;position: relative;top:73px;left: 420px;">
          <div v-for="(rect, rectIndex) in AdjustedRectsTwo" :key="rect.id" :style="rectStyleTwo(rect, rectIndex)">
            <div
                v-for="(subRect, subIndex) in getSubRectsTwo(rectIndex)"
                :key="subIndex"
                :style="subRectStyleTwo(rectIndex, subIndex)"
            ></div>
          </div>
        </div>
        <div v-show="cellThree" style="width: 160px;height: 50px;position: relative;top:119px;left: 420px;">
          <div v-for="(rect, rectIndex) in AdjustedRectsThree" :key="rect.id" :style="rectStyleThree(rect, rectIndex)">
            <div
                v-for="(subRect, subIndex) in getSubRectsThree(rectIndex)"
                :key="subIndex"
                :style="subRectStyleThree(rectIndex, subIndex)"
            ></div>
          </div>
        </div>
        <div v-show="cellFour" style="width: 160px;height: 50px;position: relative;top:124px;left: 420px;">
          <div v-for="(rect, rectIndex) in AdjustedRectsFour" :key="rect.id" :style="rectStyleFour(rect, rectIndex)">
            <div
                v-for="(subRect, subIndex) in getSubRectsFour(rectIndex)"
                :key="subIndex"
                :style="subRectStyleFour(rectIndex, subIndex)"
            ></div>
          </div>
        </div>
      </div>
    </div>
    <el-card v-if="showOutboundButton" style="flex: 1;margin-left: 1010px;margin-top: -375px;">
      <div style="width: 100%; height: calc(100% - 35px); overflow-y: auto;max-height: 400px;">
        <el-table ref="table" :data="tableDataProject" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"
                  border height="320px">
          <el-table-column :label="$t('Mounting.projectNumber')" align="center" fixed min-width="80" prop="engineerId"/>
          <el-table-column :label="$t('searchOrder.totalFurnaces')" align="center" min-width="68" prop="countTemp"/>
          <el-table-column :label="$t('searchOrder.fullFurnacesNumber')" align="center" min-width="82" prop="fullTemp"/>
          <el-table-column :label="$t('searchOrder.cagesNumber')" align="center" min-width="82" prop="countGlass"/>
          <el-table-column :label="$t('large.are')" align="center" min-width="65" prop="area"/>
          <el-table-column :label="$t('searchOrder.slotNumber')" align="center" min-width="95" prop="countSlot"/>
          <el-table-column :label="$t('searchOrder.progress')" align="center" min-width="75" prop="percent"/>
        </el-table>
      </div>
    </el-card>
    <!-- 使用率、空闲数 -->
    <div style="padding: 10px;display: flex;height:130px;margin-top:1px">
      <div v-for="(item, index) in tableDataUsage" id="occupy" :key="index">
        <el-col style="text-align:left;font-weight: bold;">#{{ item.deviceId }}</el-col>
        <el-col style="text-align:left;display:flex;justify-content: space-between;align-items: center;">
          <span>{{ $t('searchOrder.usage') }}</span><span>{{ item.percentage }}%</span>
        </el-col>
        <hr style="width:80%;margin: 0 auto;"/>
        <el-col style="text-align:left;display:flex;justify-content: space-between;align-items: center;">
          <span>{{ $t('searchOrder.freeSlotsNumber') }}</span><span>{{ item.count }}</span>
        </el-col>
      </div>
    </div>
    <div class="table-container">
      <el-card style="flex: 1;margin-left: 10px;margin-top: 25px;">
        <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;max-height: 200px;">
          <el-table ref="table" :data="tableDataIntoGlass"
                    :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"
                    :row-class-name="tableRowClassName" height="200px">
            <el-table-column :label="$t('searchOrder.intoGlassID')" align="center" min-width="140" prop="glassId"/>
            <el-table-column :label="$t('searchOrder.startPosition')" align="center" min-width="120" prop="startSlot"/>
            <el-table-column :label="$t('searchOrder.targetLocation')" align="center" min-width="120"
                             prop="targetSlot"/>
            <!-- <el-table-column prop="taskState" align="center" :label="$t('Mounting.taskStatus')" min-width="120">
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
        </div>
      </el-card>
      <el-card style="flex: 1;margin-left: 10px;margin-top: 25px;">
        <el-table ref="table" :data="tableDataOut"
                  :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" height="200px">
          <el-table-column :label="$t('searchOrder.outGlassID')" align="center" min-width="140" prop="glassId"/>
          <el-table-column :label="$t('searchOrder.startPosition')" align="center" min-width="120" prop="startSlot"/>
          <el-table-column :label="$t('searchOrder.targetLocation')" align="center" min-width="120" prop="targetSlot"/>
          <!-- <el-table-column prop="taskState" align="center" :label="$t('Mounting.taskStatus')" min-width="157">
            <template #default="scope">
              <el-tag type="success" >{{ scope.row.taskState==0? $t('searchOrder.waitFilmEnter') : $t('searchOrder.onGoing') }}</el-tag>
            </template>
          </el-table-column> -->
          <el-table-column :label="$t('searchOrder.taskstatus')" align="center" min-width="157" prop="taskState">
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
  <!-- 理片笼信息添加 -->
  <el-dialog v-model="DialogAddCage" :title="$t('searchOrder.addCageInformation')" top="10vh" width="70%">
    <div style="margin-bottom: 20px">
      <el-form>
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="4">
            <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('searchOrder.deviceIDColon')" style="width: 14vw">
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
      <el-input v-model="glassId" :placeholder="$t('searchOrder.glassIDEnter')" clearable style="width: 200px;">
      </el-input>
      <el-button plain style="margin-left: 10px;" type="primary" @click="searchOut">{{
          $t('searchOrder.search')
        }}
      </el-button>
    </div>
    <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;max-height: 200px;">
      <el-table ref="table" :data="tableDataAddCage"
                :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" height="100%">
        <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="120" prop="glassId"/>
        <el-table-column :label="$t('searchOrder.pieceInGrid')" align="center" min-width="180" prop="sequence"/>
        <el-table-column :label="$t('searchOrder.processCardNumber')" align="center" min-width="150" prop="flowCardId"/>
        <el-table-column :label="$t('searchOrder.typeGlass')" align="center" min-width="150" prop="glassType"/>
        <el-table-column :label="$t('searchOrder.width')" align="center" min-width="150" prop="width"/>
        <el-table-column :label="$t('Mounting.height')" align="center" min-width="150" prop="height"/>
        <el-table-column :label="$t('searchOrder.thickness')" align="center" min-width="150" prop="thickness"/>
        <el-table-column :label="$t('searchOrder.layoutID')" align="center" min-width="150" prop="temperingLayoutId"/>
        <el-table-column :label="$t('searchOrder.picturesequence')" align="center" min-width="150"
                         prop="temperingFeedSequence"/>
        <el-table-column
            :label="$t('searchOrder.enabledStatus')"
            align="center"
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
        <el-table-column :label="$t('searchOrder.glassGaps')" align="center" min-width="150" prop="gap"/>
        <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="150">
          <template #default="scope">
            <el-button plain type="text" @click="ConfirmAddition(scope.row)">{{ $t('searchOrder.sureadd') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-dialog>
  <el-dialog
      v-model="DialogCageInformation"
      :title="$t('searchOrder.sliceCagInformation')"
      top="2vh"
      width="95%"
  >
    <!-- 分页 -->
    <div v-if="tableMode === 'paged'" style="display: flex;">
      <!-- <el-input v-model="deviceId" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" :placeholder="$t('searchOrder.deviceIDEnter')" /> -->
      <!-- <el-input v-model="slot" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputa" :placeholder="$t('film.gridIDEnter')" /> -->
      <el-input v-model="flowCardId" :placeholder="$t('searchOrder.processCardNumberEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="filmsId" :placeholder="$t('film.filmsEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <!-- <el-input v-model="thickness" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb" :placeholder="$t('searchOrder.thicknessEnter')" /> -->
      <el-input v-model="engineerId" :placeholder="$t('Mounting.temperedLayoutIDEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="glassId" :placeholder="$t('searchOrder.glassIDEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-date-picker
          v-model="timeRange"
          :end-placeholder="$t('workOrder.endTime')"
          :shortcuts="shortcuts"
          :start-placeholder="$t('workOrder.startTime')"
          range-separator="至"
          style="margin-left: 15px;"
          type="datetimerange"
      />
      <el-button
          style="margin-left: 10px;margin-bottom: 10px;"
          type="primary"
          @click="handleClick()"
      >
        {{ $t('order.inquire') }}
      </el-button>
      <el-button
          plain
          style="margin-right: 10px;"
          type="info"
          @click="toggleTableMode"
      >
        {{ tableMode === 'paged' ? $t('sorter.modeOne') : $t('sorter.modeTwo') }}
      </el-button>
    </div>
    <!-- 单页 -->
    <div v-if="tableMode === 'single'" style="display: flex;">
      <el-input v-model="flowCardId" :placeholder="$t('searchOrder.processCardNumberEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="filmsId" :placeholder="$t('film.filmsEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="thickness" :placeholder="$t('searchOrder.thicknessEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="engineerId" :placeholder="$t('Mounting.temperedLayoutIDEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-input v-model="glassId" :placeholder="$t('searchOrder.glassIDEnter')" clearable
                style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb"/>
      <el-button
          style="margin-left: 10px;margin-bottom: 10px;"
          type="primary"
          @click="handleCpage()"
      >
        {{ $t('order.inquire') }}
      </el-button>
      <el-button
          plain
          style="margin-right: 10px;"
          type="info"
          @click="toggleTableMode"
      >
        {{ tableMode === 'paged' ? $t('sorter.modeOne') : $t('sorter.modeTwo') }}
      </el-button>
    </div>
    <!-- 分页 -->
    <el-table v-if="tableMode === 'paged'" ref="table"
              :data="tableDataGlassMessage"
              :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"
              :span-method="getSpanMethod"
              border style="margin-top: 20px;height: 580px;width: 1770px;">
      <el-table-column :label="$t('searchOrder.deviceID')" align="center" fixed min-width="80" prop="deviceId"/>
      <el-table-column :label="$t('searchOrder.gridNumber')" align="center" fixed min-width="80" prop="slot"/>
      <el-table-column
          :label="$t('searchOrder.enabledStatus')" align="center"
          fixed
          min-width="90"
          prop="enableState"
      >
        <template #default="scope">
          <el-tag
              :type="scope.row.enableState === 1 ? 'success' : 'danger'"
              @click="toggleEnableState(scope.row)"
          >
            {{ scope.row.enableState === 1 ? $t('film.start') : $t('film.disable') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('searchOrder.remainingWidth')" align="center" fixed min-width="90"
                       prop="remainWidth"/>
      <el-table-column :label="$t('film.leftMargins')" align="center" fixed min-width="80" prop="leftCoordinate"/>
      <el-table-column :label="$t('film.rightMargins')" align="center" fixed min-width="80" prop="rightCoordinate"/>

      <el-table-column :label="$t('report.processID')" align="center" min-width="80" prop="processId"/>
      <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="150" prop="glassId"/>
      <el-table-column :label="$t('searchOrder.pieceInGrid')" align="center" min-width="145" prop="sequence"/>
      <el-table-column :label="$t('searchOrder.processCardNumber')" align="center" min-width="150" prop="flowCardId"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="80" prop="filmsId"/>
      <el-table-column :label="$t('searchOrder.processCardCoding')" align="center" min-width="125"
                       prop="flowCardSequence"/>
      <el-table-column :label="$t('Mounting.width')" align="center" min-width="80" prop="width"/>
      <el-table-column :label="$t('Mounting.height')" align="center" min-width="80" prop="height"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" min-width="125"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutSequence')" align="center" min-width="125"
                       prop="temperingFeedSequence"/>
      <el-table-column :label="$t('Mounting.glassState')" align="center" min-width="125" prop="state"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" min-width="125" prop="totalLayer"/>
      <el-table-column :label="$t('searchOrder.layer')" align="center" min-width="80" prop="layer"/>
      <el-table-column :label="$t('searchOrder.landingOrder')" align="center" min-width="100" prop="shelfOrder"/>
      <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="100">
        <template #default="scope">
          <el-button v-if="showOutboundButton" plain type="text" @click="handleOutPage(scope.row)">
            {{ $t('searchOrder.outFilm') }}
          </el-button>
          <el-button plain type="text" @click="handleBreakPage(scope.row)">{{ $t('workOrder.breakage') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 单表格 -->
    <el-table
        v-else
        :data="singleTableData"
        :header-cell-style="{background:'#F2F3F5',color:'#1D2129'}"
        default-expand-all
        height="650px"
        row-key="id"
        @row-click="handleRowClick"
        @expand-change="handleExpandChange"
    >
      <el-table-column :label="$t('searchOrder.deviceID')" align="center" min-width="60" prop="deviceId"/>
      <el-table-column :label="$t('film.gridNumber')" align="center" min-width="60" prop="slot"/>
      <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="60" prop="glassId"/>
      <el-table-column :label="$t('Mounting.projectID')" align="center" min-width="80" prop="engineerId"/>
      <el-table-column :label="$t('searchOrder.processCardNumber')" align="center" min-width="125" prop="flowCardId"/>
      <el-table-column :label="$t('searchOrder.processCardCoding')" align="center" min-width="120"
                       prop="flowCardSequence"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="60" prop="filmsId"/>
      <el-table-column :label="$t('searchOrder.groupNumber')" align="center" min-width="60" prop="virtualSlot"/>
      <el-table-column :label="$t('searchOrder.groupOrder')" align="center" min-width="60" prop="slotSequence"/>
      <el-table-column :label="$t('Mounting.width')" align="center" min-width="60" prop="width"/>
      <el-table-column :label="$t('Mounting.height')" align="center" min-width="60" prop="height"/>
      <el-table-column :label="$t('Mounting.thickness')" align="center" min-width="60" prop="thickness"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" min-width="100"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutSequence')" align="center" min-width="120"
                       prop="temperingFeedSequence"/>
      <el-table-column :label="$t('Mounting.thicknessMin')" align="center" min-width="120" prop="minThickness"/>
      <el-table-column :label="$t('Mounting.thicknessMax')" align="center" min-width="120" prop="maxThickness"/>
      <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="100">
        <template #default="scope">
          <el-button v-if="showOutboundButton" plain type="text" @click="handleOutSingle(scope.row)">
            {{ $t('searchOrder.outFilm') }}
          </el-button>
          <el-button plain type="text" @click="handleBreakSingle(scope.row)">{{ $t('workOrder.breakage') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页控件（仅在分页模式显示） -->
    <div v-if="tableMode === 'paged'" style="margin-top: 20px; margin-left: 40%;">
      <el-pagination
          v-model:current-page="currentPage"
          :disabled="false"
          :page-size="pageSize"
          :total="totalRecords"
          layout="prev, pager, next, jumper"
          style="margin-top: 10px;"
          @current-change="handlePageChange"
      />
    </div>
  </el-dialog>
  <!-- 钢化查询-钢化任务 -->
  <el-dialog v-model="dialogTemperingTasks" top="5vh" width="95%" @close="iframeUrl=''">
    <iframe
        :src="iframeUrl"
        frameborder="0"
        height="700px"
        marginheight="2000px"
        marginwidth="2000px"
        width="100%"
    ></iframe>
  </el-dialog>
  <!-- 历史任务 -->
  <el-dialog v-model="dialogHistory" top="5vh" width="95%" @close="iframeUrl=''">
    <iframe
        :src="iframeUrl"
        frameborder="0"
        height="750px"
        marginheight="2000px"
        marginwidth="2000px"
        width="100%"
    ></iframe>
  </el-dialog>
  <!-- 钢化查询 -->
  <el-dialog v-model="DialogTemperingQueries" :title="$t('searchOrder.temperingQueries')" top="5vh" width="85%">
    <div style="margin-bottom: 20px">
      <el-form>
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="2">
            <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('searchOrder.temperingFurnacesColon')" style="width: 14vw">
                {{ temperingTotal }}
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="5">
            <div id="dta" style="font-size: 15px;">
              <el-form-item :label="$t('searchOrder.glassTotalColon')" style="width: 14vw">
                {{ glassTotal }}
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="4">
            <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('searchOrder.fullFurnaces')" style="width: 14vw">
                {{ fullTotals }}
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="4">
            <div id="dt" style="font-size: 15px;">
              <el-button plain type="text" @click="handleTemperingTasks">{{
                  $t('searchOrder.temperingTasks')
                }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table ref="table" :data="tableDataTemperingQueries"
              :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" style="margin-top: 20px;height: 530px;">
      <el-table-column :label="$t('searchOrder.projectnumber')" align="center" fixed min-width="150" prop="engineerId"/>
      <el-table-column :label="$t('Mounting.originalThick')" align="center" min-width="150" prop="thickness"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="150" prop="filmsId"/>
      <el-table-column :label="$t('searchOrder.temperedLayoutNumber')" align="center" min-width="120"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('searchOrder.totalQuantity')" align="center" min-width="150" prop="totalCount"/>
      <el-table-column :label="$t('searchOrder.numberCages')" align="center" min-width="150" prop="realCount"/>
      <el-table-column :label="$t('searchOrder.missingQuantity')" align="center" min-width="150" prop="lackCount"/>
      <el-table-column :label="$t('searchOrder.breakQuantity')" align="center" min-width="150" prop="damageCount"/>
      <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="400">
        <template #default="scope">
          <el-button plain type="text" @click="brokeSpecif(scope.row)">{{
              $t('searchOrder.specifyFurnace')
            }}
          </el-button>
          <el-button plain type="text" @click="SpecifyProject(scope.row)">{{
              $t('searchOrder.specifyProject')
            }}
          </el-button>
          <el-button plain type="text" @click="handleLack(scope.row)">{{
              $t('searchOrder.missingPiecesNumber')
            }}
          </el-button>
          <el-button plain type="text" @click="handleCageDetails(scope.row)">{{
              $t('searchOrder.cageDetails')
            }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
  <!-- 钢化查询--缺片数量 -->
  <el-dialog v-model="DialogMissingNumber" :title="$t('searchOrder.missingPiecesNumber')" top="5vh" width="85%">
    <el-table ref="table" :data="tableDataLack"
              :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" style="margin-top: 20px;height: 600px;">
      <el-table-column :label="$t('searchOrder.projectnumber')" align="center" fixed min-width="150" prop="engineerId"/>
      <el-table-column :label="$t('searchOrder.processCardNumber')" align="center" fixed min-width="150"
                       prop="flowCardId"/>
      <el-table-column :label="$t('Mounting.glassID')" align="center" fixed min-width="150" prop="glassId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" fixed min-width="100"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutSequence')" align="center" fixed min-width="120"
                       prop="temperingFeedSequence"/>
      <el-table-column :label="$t('searchOrder.width')" align="center" min-width="80" prop="width"/>
      <el-table-column :label="$t('Mounting.height')" align="center" min-width="80" prop="height"/>
      <el-table-column :label="$t('searchOrder.thickness')" align="center" min-width="80" prop="thickness"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="80" prop="filmsId"/>
      <el-table-column
          :label="$t('Mounting.state')"
          align="center"
          min-width="80"
          prop="isDamage"
      >
        <template #default="scope">
          <div
              style="position: relative; display: inline-block;"
              @click="scope.row.isDamage === 1 ? null : broken(scope.row)"
          >
            <el-tag
                :type="scope.row.isDamage === 1 ? 'danger' : 'success'"
                style="pointer-events: {{ scope.row.isDamage === 1 ? 'none' : 'auto' }};"
            >
              {{ scope.row.isDamage === 1 ? $t('searchOrder.breakage') : $t('searchOrder.unBroken') }}
            </el-tag>
            <div
                v-if="scope.row.isDamage === 1"
                style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: transparent; z-index: 1;"
            ></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('searchOrder.layer')" align="center" min-width="80" prop="layer"/>
      <el-table-column :label="$t('searchOrder.xCoordinates')" align="center" min-width="80" prop="xcoordinate"/>
      <el-table-column :label="$t('searchOrder.yCoordinates')" align="center" min-width="80" prop="ycoordinate"/>
    </el-table>
  </el-dialog>
  <!-- 钢化查询--笼内详情 -->
  <el-dialog v-model="DialogCageDetails" :title="$t('searchOrder.cageDetails')" top="5vh" width="85%">
    <el-table ref="table" :data="tableDatacagedetails"
              :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" style="margin-top: 20px;height: 600px;">
      <el-table-column :label="$t('searchOrder.projectnumber')" align="center" fixed min-width="150" prop="engineerId"/>
      <el-table-column :label="$t('searchOrder.processCardNumber')" align="center" fixed min-width="150"
                       prop="flowCardId"/>
      <el-table-column :label="$t('Mounting.glassID')" align="center" fixed min-width="150" prop="glassId"/>
      <el-table-column :label="$t('film.gridNumber')" align="center" fixed min-width="150" prop="slot"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" fixed min-width="100"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutSequence')" align="center" fixed min-width="120"
                       prop="temperingFeedSequence"/>
      <el-table-column :label="$t('searchOrder.width')" align="center" min-width="80" prop="width"/>
      <el-table-column :label="$t('Mounting.height')" align="center" min-width="80" prop="height"/>
      <el-table-column :label="$t('searchOrder.thickness')" align="center" min-width="80" prop="thickness"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="80" prop="filmsId"/>
      <el-table-column
          :label="$t('Mounting.state')"
          align="center"
          min-width="80"
          prop="isDamage"
      >
        <template #default="scope">
          <div
              style="position: relative; display: inline-block;"
              @click="scope.row.isDamage === 1 ? null : broken(scope.row)"
          >
            <el-tag
                :type="scope.row.isDamage === 1 ? 'danger' : 'success'"
                style="pointer-events: {{ scope.row.isDamage === 1 ? 'none' : 'auto' }};"
            >
              {{ scope.row.isDamage === 1 ? $t('searchOrder.breakage') : $t('searchOrder.unBroken') }}
            </el-tag>
            <div
                v-if="scope.row.isDamage === 1"
                style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: transparent; z-index: 1;"
            ></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('searchOrder.layer')" align="center" min-width="80" prop="layer"/>
      <el-table-column :label="$t('searchOrder.xCoordinates')" align="center" min-width="80" prop="xcoordinate"/>
      <el-table-column :label="$t('searchOrder.yCoordinates')" align="center" min-width="80" prop="ycoordinate"/>
    </el-table>
  </el-dialog>
  <!-- 非钢化流程卡查询 -->
  <el-dialog v-model="DialogNonToughened" :title="$t('searchOrder.noTemperedCardInquiry')" top="5vh" width="85%">
    <el-table ref="table" :data="tableDataNonToughened"
              :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" style="margin-top: 20px;height: 500px;">
      <el-table-column :label="$t('searchOrder.projectnumber')" align="center" fixed min-width="150" prop="engineerId"/>
      <el-table-column :label="$t('Mounting.originalThick')" align="center" min-width="150" prop="thickness"/>
      <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" min-width="150" prop="filmsId"/>
      <el-table-column :label="$t('searchOrder.temperedLayoutNumber')" align="center" fixed min-width="120"
                       prop="temperingLayoutId"/>
      <el-table-column :label="$t('searchOrder.totalQuantity')" align="center" min-width="150" prop="totalCount"/>
      <el-table-column :label="$t('searchOrder.numberCages')" align="center" min-width="150" prop="realCount"/>
      <el-table-column :label="$t('searchOrder.missingQuantity')" align="center" min-width="150" prop="lackCount"/>
      <el-table-column :label="$t('searchOrder.breakQuantity')" align="center" min-width="150" prop="damageCount"/>
      <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="500">
        <template #default="scope">
          <el-button plain type="text" @click="brokeSpecif(scope.row)">{{
              $t('searchOrder.specifyFurnace')
            }}
          </el-button>
          <el-button plain type="text" @click="SpecifyProject(scope.row)">{{
              $t('searchOrder.specifyProject')
            }}
          </el-button>
          <el-button plain type="text" @click="handleLack(scope.row)">{{
              $t('searchOrder.missingPiecesNumber')
            }}
          </el-button>
          <el-button plain type="text" @click="handleCageDetails(scope.row)">{{
              $t('searchOrder.cageDetails')
            }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<style>
#dt {
  display: block;
  float: left;
  line-height: 20px;
  margin-left: 150px;
}

#dta {
  display: block;
  float: left;
  line-height: 20px;
  margin-left: 80%;
}

#dialog-footer {
  text-align: center;
  margin-top: -15px;
}

#message {
  text-align: center;
  align-items: center;
  color: black;
  width: 200px;
  height: 100px;
  background-color: #337ecc;
  margin-left: 28%;
}

#awatch {
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
  width: 0 !important;
  height: 0;
}

.img-list {
  position: relative;
}

.data-img {
  @apply float-none ;
  width: 100%;
  height: 16rem;
  background: rgba(0, 0, 0, 0);
  opacity: 1;
  border-radius: 0.5rem 0.5rem 0px 0px;
}

.check-img {
  position: absolute;
  width: 3.3125rem;
  height: 2.9375rem;
  top: 20rem;
  right: 57rem;
  z-index: 10;
}

.check-imga {
  position: absolute;
  width: 3.3125rem;
  height: 2.9375rem;
  top: 15rem;
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

.img-dlpl {
  margin-left: 10px;
  margin-top: 0px;
  background-image: url('/lylpl.png');
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 380px;
  width: 1000px;
  max-width: 95%;
  background-size: 1000px 380px;
  overflow: hidden;
  position: relative
}

.img-car1 {
  display: flex;
  background-image: url('/lycar.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 200px;
  width: 180px;
  max-width: 100%;
  background-size: 180px 70px;
  overflow: hidden;
  position: relative
}

.img-car2 {
  display: flex;
  background-image: url('/lycar.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 200px;
  width: 180px;
  max-width: 100%;
  background-size: 180px 70px;
  overflow: hidden;
  position: relative
}

.table-container {
  display: flex;
  flex-wrap: nowrap; /* 防止换行 */
  justify-content: space-between; /* 根据需要调整子元素之间的间距 */
}

.table-container > el-card {
  flex: 1; /* 使两个卡片平分可用空间 */
  margin-bottom: 10px;
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

.row-merged {
  background-color: #f5f7fa !important;
  font-weight: bold;
}
</style>