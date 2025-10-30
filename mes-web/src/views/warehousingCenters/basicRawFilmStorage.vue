<script lang="ts" setup>
import {defineAsyncComponent, defineProps, inject, onMounted, reactive, ref} from "vue"
import {useRouter} from "vue-router"
import {ElMessage, ElMessageBox} from 'element-plus'
import {useI18n} from 'vue-i18n'
import WebSocketStorage from '@/utils/WebSocketStorage'
import {
  backWarehousing,
  BackWarehousingParams,
  deviceIdParams,
  glassWarehousing,
  InboundRequests,
  InboundRequestsParams,
  insertRawGlassDetails,
  LeftingStationRawGlassDetails,
  LeftingStationRawGlassDetailsParams,
  OutboundRequestParams,
  outWarehousingRequest,
  queryAllMessage,
  queryLeftingStation,
  queryRawGlassList,
  queryUnBindRawGlassList,
  rawGlassUnBind,
  rawStorageSwitch,
  reissueTask,
  deleteTask,
  removeRawGlassDetails,
  updateRawGlassDetails,
  updateStorageCageState
} from '@/api/glassStorage'
import rawGlassDialog from './rawGlassDialog.vue'
import rawGlassDialogLY from './rawGlassDialogLY.vue'
import rawGlassLayout from './rawGlassLayout.vue'
import rawGlassLayoutLY from './rawGlassLayoutLY.vue'
 
const { t } = useI18n()
const currentRowData = ref<any>({})
const tableDataQueryUnBind = ref<any[]>([])
const router = useRouter()
const tableDataTask = ref([])
const tableDataStation = ref([])
const timeRange = ref([])
const selectValue = reactive([])
const slot = ref('')
const iframeUrl = ref('')
const patternWidth = ref('')
const patternHeight = ref('')
const patternThickness = ref('')
const rawWidth = ref('')
const rawHeight = ref('')
const rawThickness = ref('')
const filmsId = ref('')
const deviceId = ref('')
const id = ref('')
const manufacturer = ref('')
const rawNumber = ref('')
const shelf = ref('')
const rawMargin = ref('')
const remainQuantity = ref('')
const switchTask = ref('')
const currentPrimaryRow = ref(null)
const Add = ref(false)
const Edit = ref(false)
const EditLY = ref(false)
const dialogHistory = ref(false)
const dialogFormDetail = ref(false)
const dialogFormStorage = ref(false)
const dialogFormInboundRequests = ref(false)
const dialogFormOutboundRequests = ref(false)
const dialogFormOutbound = ref(false)
const dialogFormReset = ref(false)
const leftingStation = ref('');
const globalDate = inject('globalDate')
const report = ref({
  taskState: '',
});
let webSocket: WebSocket | null = null
let startTime = window.localStorage.getItem('startTime')
const formData = reactive({
  id: '',
  shelf: '',
  deviceId: '',
  slot: '',
  rawWidth: '',
  rawHeight: '',
  rawThickness: '',
  filmsId: '',
  rawMargin: '',
  remainQuantity: '',
  manufacturer: '',
  rawNumber: ''
});
const props = defineProps({
    showOutboundButton: {
    type: Boolean,
    default: true // 默认显示
  },
    showOutboundButtonLY: {
    type: Boolean,
    default: true // 默认显示
  },
  // 图片页面
  rawGlassPath: {
    type: String,
    required: true,
    default: 'rawGlassLayout'
  },
})
// 动态导入组件
const DynamicComponent = defineAsyncComponent(() =>
  import(`./dynamicComponents/${props.rawGlassPath}.vue`)
)
queryAllMessage().then((data) => {
  tableDataTask.value = data.historyTaskList;
  switchTask.value = data.rawStorageSwitch;
}).catch((error) => {
});
const websocketService = WebSocketStorage.getInstance()
onMounted(() => {
  const handleMessage = (data) => {
    // 格式化任务列表时间
    const formattedTasks = data.historyTaskList.map(task => {
      const formattedTask = {...task};
      if (formattedTask.createTime) {
        formattedTask.createTime = formatTimestamp(formattedTask.createTime);
      }
      if (formattedTask.updateTime) {
        formattedTask.updateTime = formatTimestamp(formattedTask.updateTime);
      }
      return formattedTask;
    });
    tableDataTask.value = formattedTasks;
    // 仓储任务开关
    if (data.rawStorageSwitch != null) {
      switchTask.value = data.rawStorageSwitch
    } else {
      switchTask.value = ''
    }
  }
  websocketService.addListener(handleMessage)
  // 清理函数
  return () => {
    websocketService.removeListener(handleMessage)
  }
})
// 原片仓储详情
const handleDetail = async (row) => {
  dialogFormDetail.value = true;
  fetchDetail();
};
const fetchDetail = async () => {
  try {
    const params: deviceIdParams = {
      deviceIdList: [99, 100]
    }
    const data = await queryRawGlassList(params);
    if (Array.isArray(data)) {
      const updatedData = data.map(group => {
        const updatedDetails = group.rawGlassDetailsList.map(item => ({
          ...item,
          enableState: group.enableState
        }));
 
        return {
          ...group,
          rawGlassDetailsList: updatedDetails
        };
      });
 
      tableDataStation.value = updatedData;
    } else {
      tableDataStation.value = [];
    }
  } catch (error) {
    tableDataStation.value = [];
  }
};
// 吊装位管理
const handleStorage = (row) => {
  dialogFormStorage.value = true;
  for (const key in formData) {
    formData[key] = '';
  }
  queryUnBind()
};
const handleSlotChange = (value) => {
  leftingStation.value = value;
  queryLeftingStationRawGlassDetails()
};
// 空架子到吊装位
const handleInboundRequests = (row) => {
  dialogFormInboundRequests.value = true;
};
// 出库
const handleOutbound = (row) => {
  slot.value = row.slot;
  rawWidth.value = row.rawWidth;
  rawHeight.value = row.rawHeight;
  rawThickness.value = row.rawThickness;
  dialogFormOutbound.value = true;
};
// 回库
const handleOutboundRequests = (row) => {
  leftingStation.value = String(formData.slot);
  dialogFormOutboundRequests.value = true;
};
// 上片位架子复位
const handleShelfReset = (row) => {
  dialogFormReset.value = true;
};
// 查询查询吊装位架子原片信息
const queryLeftingStationRawGlassDetails = async (row) => {
  try {
    const params: LeftingStationRawGlassDetailsParams = {
      leftingStation: leftingStation.value
    };
    const response = await LeftingStationRawGlassDetails(params);
    // 如果返回了数据，更新到表单中
    if (response && Array.isArray(response) && response.length > 0) {
      const data = response[0];
      formData.id = data.id || '';
      formData.shelf = data.shelf || '';
      formData.deviceId = data.deviceId || '';
      formData.rawWidth = data.rawWidth || '';
      formData.rawHeight = data.rawHeight || '';
      formData.rawThickness = data.rawThickness || '';
      formData.filmsId = data.filmsId || '';
      formData.rawMargin = data.rawMargin || '';
      formData.remainQuantity = data.remainQuantity || '';
      formData.manufacturer = data.manufacturer || '';
      formData.rawNumber = data.rawNumber || '';
      ElMessage.success('吊装位数据已加载');
    } else if (response && Array.isArray(response) && response.length === 0) {
      // 如果返回空数组，清空相关字段
      formData.rawWidth = '';
      formData.rawHeight = '';
      formData.rawThickness = '';
      formData.filmsId = '';
      formData.rawMargin = '';
      formData.remainQuantity = '';
      formData.manufacturer = '';
      formData.rawNumber = '';
      ElMessage.info('该吊装位暂无数据');
    } else {
      console.log('响应格式异常:', response);
      ElMessage.warning('数据格式异常');
    }
    leftingStation.value = '';
  }
  catch (error) {
    ElMessage.error(t('basicData.glassnull'));
  }
}
// 入库请求 空架子到吊装位
const queryInboundRequests = async (row) => {
  try {
    const params: InboundRequestsParams = {
      leftingStation: leftingStation.value
    };
    console.log(params);
    await InboundRequests(params);
    leftingStation.value = '';
    dialogFormInboundRequests.value = false;
  }
  catch (error) {
  }
}
// 出库 架子到吊装位
const queryOutbound = async (row) => {
  try {
    const params: OutboundRequestParams = {
      leftingStation: leftingStation.value,
      slot: slot.value,
      rawWidth: rawWidth.value,
      rawHeight: rawHeight.value,
      rawThickness: rawThickness.value,
    };
    // console.log(params);
    await outWarehousingRequest(params);
    leftingStation.value = '';
    slot.value = '';
    rawWidth.value = '';
    rawHeight.value = '';
    rawThickness.value = '';
    dialogFormOutbound.value = false;
  }
  catch (error) {
  }
}
// 回库
const queryOutboundRequests = async () => {
  try {
    const params: BackWarehousingParams = {
      leftingStation: leftingStation.value,
    };
    await backWarehousing(params);
    leftingStation.value = '';
    dialogFormOutboundRequests.value = false;
    dialogFormStorage.value = false;
  }
  catch (error) {
  }
}
// 解绑
const handleUnbind = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('productStock.unBindWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.yes'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      await rawGlassUnBind(row.id);
      fetchDetail();
    }
  } catch (error) {
  }
};
// 破损
const handleBreakage = async (row) => {
  try {
    const confirmResult = await ElMessageBox.prompt(
      t('Mounting.dilapidationWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.yes'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
        inputType: 'number',
        inputPlaceholder: t('film.quantityEnter'), // 输入框提示
      }
    );
    if (confirmResult.action === 'confirm') {
      const currentRemain = Number(row.remainQuantity) - Number(confirmResult.value);
      const params: UpdateRawGlassDetailsParams = {
        id: row.id,
        shelf: row.shelf,
        deviceId: row.deviceId,
        rawWidth: row.rawWidth,
        rawHeight: row.rawHeight,
        rawThickness: row.rawThickness,
        filmsId: row.filmsId,
        rawMargin: row.rawMargin,
        manufacturer: row.manufacturer,
        rawNumber: row.rawNumber,
        remainQuantity: currentRemain,
      };
      await updateRawGlassDetails(params);
      fetchDetail();
    }
  } catch (error) {
  }
};
// 删除
const deleteWareHousing = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('film.deleteWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.yes'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      await removeRawGlassDetails(row.id);
      fetchDetail();
    }
  } catch (error) {
  }
};
// 查询所有架子状态（是否禁用）
const toggleEnableState = async (row: any) => {
  if (!row.deviceId) {
    ElMessage.error(t('basicData.updatanull'));
    return;
  }
  const newState = row.enableState === 1 ? 0 : 1;
  try {
    await updateStorageCageState({
      enableState: newState,
      shelf: row.shelf
    });
    await fetchDetail();
    row.enableState = newState;
  } catch (error) {
    ElMessage.error(t('basicData.glassnull'));
  }
};
// 添加
const handleAdd = (row) => {
  Add.value = true;
  formData.id = row.id;
  formData.slot = row.slot;
  formData.id = row.id;
  formData.deviceId = row.deviceId;
  formData.shelf = row.shelf;
  queryUnBind()
  currentPrimaryRow.value = row;
}
// 查询未绑定原片详情
const queryUnBind = async () => {
  try {
    const data = await queryUnBindRawGlassList();
    tableDataQueryUnBind.value = data;
  }
  catch (error) {
  }
}
// 原片仓储详情--添加--处理添加行数据到表单
const handleAddRow = (row: any) => {
  // formData.id = row.id;
  formData.deviceId = formData.deviceId;
  formData.shelf = formData.shelf;
  formData.slot = formData.slot;
  formData.rawWidth = row.rawWidth;
  formData.rawHeight = row.rawHeight;
  formData.rawThickness = row.rawThickness;
  formData.filmsId = row.filmsId;
  formData.rawMargin = row.rawMargin;
  formData.remainQuantity = row.remainQuantity;
  formData.manufacturer = row.manufacturer;
  formData.rawNumber = row.rawNumber;
};
// 原片仓储详情--添加--左侧确认添加
const handleConfirm = async (apiType: string = 'insert') => {
  if (formData.deviceId === undefined || formData.deviceId === null || formData.deviceId === '') {
    ElMessage.error(t('searchOrder.deviceIDEnter'))
    return
  }
  if (formData.shelf === undefined || formData.shelf === null || formData.shelf === '') {
    ElMessage.error(t('film.shelfNumberEnter'))
    return
  }
  if (formData.rawWidth === undefined || formData.rawWidth === null || formData.rawWidth === '') {
    ElMessage.error(t('film.widthEnter'))
    return
  }
  if (formData.rawHeight === undefined || formData.rawHeight === null || formData.rawHeight === '') {
    ElMessage.error(t('film.heightEnter'))
    return
  }
  if (formData.rawThickness === undefined || formData.rawThickness === null || formData.rawThickness === '') {
    ElMessage.error(t('film.thicknessEnter'))
    return
  }
  if (formData.filmsId === undefined || formData.filmsId === null || formData.filmsId === '') {
    ElMessage.error(t('film.filmsEnter'))
    return
  }
  if (formData.rawMargin === undefined || formData.rawMargin === null || formData.rawMargin === '') {
    ElMessage.error(t('film.originalEdgeEnter'))
    return
  }
  if (formData.remainQuantity === undefined || formData.remainQuantity === null || formData.remainQuantity === '') {
    ElMessage.error(t('film.remainQuantityEnter'))
    return
  }
  if (formData.manufacturer === undefined || formData.manufacturer === null || formData.manufacturer === '') {
    ElMessage.error(t('film.manufacturerEnter'))
    return
  }
  if (formData.rawNumber === undefined || formData.rawNumber === null || formData.rawNumber === '') {
    ElMessage.error(t('film.originalFilmNumberEnter'))
    return
  }
  try {
    if (apiType === 'warehousing') {
      await glassWarehousing(formData);
    } else {
      await insertRawGlassDetails(formData);
    }
    Add.value = false;
    formData.slot = '';
    formData.rawWidth = '';
    formData.rawHeight = '';
    formData.rawThickness = '';
    formData.filmsId = '';
    formData.rawMargin = '';
    formData.remainQuantity = '';
    formData.manufacturer = '';
    formData.rawNumber = '';
    formData.deviceId = '';
    formData.shelf = '';
    fetchDetail();
  } catch (error) {
  }
};
// 修改
const handleEditLY = (row, index = 0) => {
  // 将数据设置到currentRowData中，传递给rawGlassDialog组件
  currentRowData.value = {
    deviceId: row.deviceId,
    shelf: row.shelf,
    slot: row.slot,
    manufacturer: row.manufacturer,
    rawNumber: row.rawNumber,
    rawWidth: row.rawWidth,
    rawHeight: row.rawHeight,
    rawThickness: row.rawThickness,
    filmsId: row.filmsId,
    rawMargin: row.rawMargin,
    remainQuantity: row.remainQuantity,
    id: row.id,
    isRightSide: index === 0 // 第一条数据为右侧，第二条为左侧
  };
  window.localStorage.setItem('id', row.id)
  queryUnBind()
  EditLY.value = true;
};
const handleEdit = (row) => {
  // 将数据设置到currentRowData中，传递给rawGlassDialog组件
  currentRowData.value = {
    deviceId: row.deviceId,
    shelf: row.shelf,
    slot: row.slot,
    manufacturer: row.manufacturer,
    rawNumber: row.rawNumber,
    rawWidth: row.rawWidth,
    rawHeight: row.rawHeight,
    rawThickness: row.rawThickness,
    filmsId: row.filmsId,
    rawMargin: row.rawMargin,
    remainQuantity: row.remainQuantity,
    id: row.id
  };
  window.localStorage.setItem('id', row.id)
  queryUnBind()
  Edit.value = true;
};
// 修改原片信息
const EditClick = async (formDataFromDialog?: any) => {
  // 检查参数是否为事件对象，如果是则忽略
  if (formDataFromDialog && formDataFromDialog.type === 'click') {
    formDataFromDialog = undefined;
  }
  // 从rawGlassDialog组件传递过来的数据，使用formDataFromDialog
  const dataToValidate = formDataFromDialog || {
    id: formData.id,
    deviceId: formData.deviceId,
    shelf: formData.shelf,
    slot: formData.slot,
    rawWidth: formData.rawWidth,
    rawHeight: formData.rawHeight,
    rawThickness: formData.rawThickness,
    filmsId: formData.filmsId,
    rawMargin: formData.rawMargin,
    remainQuantity: formData.remainQuantity,
    manufacturer: formData.manufacturer,
    rawNumber: formData.rawNumber,
  };
  if (dataToValidate.rawWidth === undefined || dataToValidate.rawWidth === null || dataToValidate.rawWidth === '') {
    ElMessage.error(t('film.widthEnter'))
    console.log('id号:', dataToValidate.id)
    return
  }
  if (dataToValidate.rawHeight === undefined || dataToValidate.rawHeight === null || dataToValidate.rawHeight === '') {
    ElMessage.error(t('film.heightEnter'))
    return
  }
  if (dataToValidate.rawThickness === undefined || dataToValidate.rawThickness === null || dataToValidate.rawThickness === '') {
    ElMessage.error(t('film.thicknessEnter'))
    return
  }
  if (dataToValidate.manufacturer === undefined || dataToValidate.manufacturer === null || dataToValidate.manufacturer === '') {
    ElMessage.error(t('film.manufacturerEnter'))
    return
  }
  if (dataToValidate.rawNumber === undefined || dataToValidate.rawNumber === null || dataToValidate.rawNumber === '') {
    ElMessage.error(t('film.originalFilmNumberEnter'))
    return
  }
  if (dataToValidate.remainQuantity === undefined || dataToValidate.remainQuantity === null || dataToValidate.remainQuantity === '') {
    ElMessage.error(t('film.quantityEnter'))
    return
  }
  try {
    const params = {
      id: dataToValidate.id,
      remainQuantity: dataToValidate.remainQuantity,
      rawMargin: dataToValidate.rawMargin,
      rawWidth: dataToValidate.rawWidth,
      rawHeight: dataToValidate.rawHeight,
      rawThickness: dataToValidate.rawThickness,
      filmsId: dataToValidate.filmsId,
      deviceId: dataToValidate.deviceId,
      shelf: dataToValidate.shelf,
      slot: dataToValidate.slot,
      manufacturer: dataToValidate.manufacturer,
      rawNumber: dataToValidate.rawNumber,
    };
    await updateRawGlassDetails(params);
    Edit.value = false;
    dialogFormStorage.value = false;
    // 清空表单数据
    if (!formDataFromDialog) {
      rawWidth.value = '';
      rawHeight.value = '';
      rawThickness.value = '';
      filmsId.value = '';
      remainQuantity.value = '';
      manufacturer.value = '';
      rawNumber.value = '';
    }
    fetchDetail();
  } catch (error) {
  }
};
const EditClickLY = async (formDataFromDialog?: any) => {
  // 检查参数是否为事件对象，如果是则忽略
  if (formDataFromDialog && formDataFromDialog.type === 'click') {
    formDataFromDialog = undefined;
  }
  // 从rawGlassDialog组件传递过来的数据，使用formDataFromDialog
  const dataToValidate = formDataFromDialog || {
    id: formData.id,
    deviceId: formData.deviceId,
    shelf: formData.shelf,
    slot: formData.slot,
    rawWidth: formData.rawWidth,
    rawHeight: formData.rawHeight,
    rawThickness: formData.rawThickness,
    filmsId: formData.filmsId,
    rawMargin: formData.rawMargin,
    remainQuantity: formData.remainQuantity,
    manufacturer: formData.manufacturer,
    rawNumber: formData.rawNumber,
  };
  if (dataToValidate.rawWidth === undefined || dataToValidate.rawWidth === null || dataToValidate.rawWidth === '') {
    ElMessage.error(t('film.widthEnter'))
    console.log('id号:', dataToValidate.id)
    return
  }
  if (dataToValidate.rawHeight === undefined || dataToValidate.rawHeight === null || dataToValidate.rawHeight === '') {
    ElMessage.error(t('film.heightEnter'))
    return
  }
  if (dataToValidate.rawThickness === undefined || dataToValidate.rawThickness === null || dataToValidate.rawThickness === '') {
    ElMessage.error(t('film.thicknessEnter'))
    return
  }
  if (dataToValidate.manufacturer === undefined || dataToValidate.manufacturer === null || dataToValidate.manufacturer === '') {
    ElMessage.error(t('film.manufacturerEnter'))
    return
  }
  if (dataToValidate.rawNumber === undefined || dataToValidate.rawNumber === null || dataToValidate.rawNumber === '') {
    ElMessage.error(t('film.originalFilmNumberEnter'))
    return
  }
  if (dataToValidate.remainQuantity === undefined || dataToValidate.remainQuantity === null || dataToValidate.remainQuantity === '') {
    ElMessage.error(t('film.quantityEnter'))
    return
  }
  try {
    const params = {
      id: dataToValidate.id,
      remainQuantity: dataToValidate.remainQuantity,
      rawMargin: dataToValidate.rawMargin,
      rawWidth: dataToValidate.rawWidth,
      rawHeight: dataToValidate.rawHeight,
      rawThickness: dataToValidate.rawThickness,
      filmsId: dataToValidate.filmsId,
      deviceId: dataToValidate.deviceId,
      shelf: dataToValidate.shelf,
      slot: dataToValidate.slot,
      manufacturer: dataToValidate.manufacturer,
      rawNumber: dataToValidate.rawNumber,
    };
    await updateRawGlassDetails(params);
    EditLY.value = false;
    dialogFormStorage.value = false;
    // 清空表单数据
    if (!formDataFromDialog) {
      rawWidth.value = '';
      rawHeight.value = '';
      rawThickness.value = '';
      filmsId.value = '';
      remainQuantity.value = '';
      manufacturer.value = '';
      rawNumber.value = '';
    }
    fetchDetail();
  } catch (error) {
  }
};
// 清空吊装位架子数据
const handleDelete = async () => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('film.clearShelfWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.yes'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      Object.keys(formData).forEach(key => {
        if (!['deviceId', 'shelf', 'slot'].includes(key)) {
          formData[key] = '';
        }
      });
      const { deviceId, shelf, slot } = currentRowData.value || {};
      currentRowData.value = {
        deviceId,
        shelf,
        slot,
        manufacturer: '',
        rawNumber: '',
        rawWidth: '',
        rawHeight: '',
        rawThickness: '',
        filmsId: '',
        rawMargin: '',
        remainQuantity: '',
      };
      ElMessage.success('数据已清空');
    }
  } catch (error) {
    // 用户取消操作，不做任何处理
  }
};
// 历史任务
const handleHistorical = (row) => {
  dialogHistory.value = true;
  iframeUrl.value = `${window.location.origin}/#/WarehousingCenters/rawhistory`;
};
// // 任务成功处理
// const handleSuccessfull = async(row) => {
//   try {
//     const confirmResult = await ElMessageBox.confirm(
//       t('searchOrder.successfullyProcessedWhether'),
//       t('workOrder.prompt'),
//       {
//         confirmButtonText: t('workOrder.yes'),
//         cancelButtonText: t('workOrder.cancel'),
//         type: 'warning',
//       }
//     );
//     if (confirmResult === 'confirm') {
//       await taskSuccess(row.id);
//     }
//   } catch (error) {
//   }
// };
// // 任务失败处理
// const handleTask = async(row) => {
//   try {
//     const confirmResult = await ElMessageBox.confirm(
//       t('searchOrder.taskFailureWhether'),
//       t('workOrder.prompt'),
//       {
//         confirmButtonText: t('workOrder.yes'),
//         cancelButtonText: t('workOrder.cancel'),
//         type: 'warning',
//       }
//     );
//     if (confirmResult === 'confirm') {
//       await taskError(row.id);
//     }
//   } catch (error) {
//   }
// };
// 仓储任务开关
const handleSteel = async () => {
  try {
    const result = await rawStorageSwitch(switchTask.value);
    switchTask.value = result;
  }
  catch (error) {
  }
}
// 重新下发任务
const handleReissueTask = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('searchOrder.taskResetWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.confirm'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      // 调用重新下发任务接口
      await reissueTask(row.id);
      // 重新加载数据
      const data = await queryAllMessage();
      tableDataTask.value = data.historyTaskList;
      switchTask.value = data.rawStorageSwitch;
    }
  } catch (error) {
  }
};
const handleDeleteTask = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('searchOrder.deleteMessageWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.confirm'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      // 调用重新下发任务接口
      await deleteTask(row.id);
      // 重新加载数据
      const data = await queryAllMessage();
      tableDataTask.value = data.historyTaskList;
      switchTask.value = data.rawStorageSwitch;
    }
  } catch (error) {
  }
};
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
function formatTimestamp(timestamp: number | Date): string {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
function getStatusTaskStatus(taskState) {
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
function getStatusTextStatus(taskState) {
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
const options = ref<Array<{ value: number; label: string }>>([])
// 获取吊装位选项
const fetchHoistingPositions = async () => {
  try {
    const response = await queryLeftingStation()
    if (response && Array.isArray(response)) {
      const hoistingSlots = response
        .filter(item => item.deviceId === 99)
        .map(item => ({
          value: item.slot,
          label: item.slot === 98 ? t('film.hoistingPositionOne') :
            item.slot === 99 ? t('film.hoistingPositionTwo') :
              `${t('film.hoistingPosition')} ${item.slot}`
        }))
      options.value = hoistingSlots
    } else {
      // 如果接口返回空，使用默认值
      options.value = [
        { value: 98, label: t('film.hoistingPositionOne') },
        { value: 99, label: t('film.hoistingPositionTwo') }
      ]
    }
  } catch (error) {
    // 接口失败时使用默认值
    options.value = [
      { value: 98, label: t('film.hoistingPositionOne') },
      { value: 99, label: t('film.hoistingPositionTwo') }
    ]
  }
}
 
onMounted(() => {
  parseAndSetTime();
  fetchHoistingPositions();
}); 
</script>
<template>
  <div style="height: 500px;">
    <div style="display: flex; flex-direction: row; align-items: center; margin-top: 20px;">
      <el-button style="margin-left: 20px;" id="searchButton" type="success" @click="handleDetail">
        {{ $t('film.rawFilmStorageDetails') }}
      </el-button>
      <el-button v-if="rawGlassPath !== 'rawGlassLayoutLY'" id="searchButton"
                 style="margin-left: 20px;"
                 type="success" @click="handleStorage">
        {{ $t('film.hoistingInformation') }}
      </el-button>
      <el-button style="margin-left: 20px;" id="searchButton" type="success" @click="handleHistorical">{{
        $t('Mounting.historicalTasks')
      }}
      </el-button>
      <el-switch style="margin-left: 20px;" v-model="switchTask" :inactive-text="$t('film.warehousingtaskswitch')"
        @change="handleSteel" />
    </div>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table height="200" ref="table" :data="tableDataTask"
          :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
          <el-table-column prop="id" align="center" :label="$t('ID')" />
          <!-- <el-table-column prop="rawSequence" align="center" :label="$t('Mounting.originalPreface')"/> -->
          <el-table-column prop="startSlot" align="center" :label="$t('film.startingGrid')" />
          <el-table-column prop="endSlot" align="center" :label="$t('film.targetGrid')" />
          <el-table-column prop="rawMargin" align="center" :label="$t('film.originalEdge')" width="160" />
          <el-table-column prop="rawGlassWidth" align="center" :label="$t('film.width')" />
          <el-table-column prop="rawGlassHeight" align="center" :label="$t('film.height')" />
          <el-table-column prop="rawGlassThickness" align="center" :label="$t('Mounting.thickness')" />
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" width="120" />
          <el-table-column prop="rawQuantity" align="center" :label="$t('film.originalFilmsNumber')" width="175" />
          <el-table-column prop="createTime" align="center" :label="$t('Mounting.creationTime')" width="160" />
          <el-table-column prop="updateTime" align="center" :label="$t('searchOrder.updateTime')" width="160" />
          <el-table-column align="center" :label="$t('Mounting.taskStatus')" prop="taskState">
            <template #default="scope">
              <el-tag :type="getStatusTaskStatus(scope.row.taskState)">
                {{ getStatusTextStatus(scope.row.taskState) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" min-width="150">
            <template #default="scope">
              <el-button type="text" @click="handleReissueTask(scope.row)">
                {{ $t('searchOrder.taskReset') }}
              </el-button>
              <el-button type="text" @click="handleDeleteTask(scope.row)">
                {{ $t('Mounting.delete') }}
              </el-button>
            </template>
          </el-table-column>
          <!-- <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center">
            <template #default="scope">
          <el-button :disabled="(scope.row.taskState !== 0)" type="text" plain @click="handleSuccessfull(scope.row)">{{ $t('searchOrder.successfullyProcessed') }}</el-button>
          <el-button :disabled="(scope.row.taskState !== 0)" type="text" plain @click="handleTask(scope.row)">{{ $t('searchOrder.taskFailure') }}</el-button>
            </template>
        </el-table-column> -->
        </el-table>
      </div>
    </el-card>
    <!-- 图片界面引用 -->
    <component :is="DynamicComponent" :raw-glass-count-vo-list="rawGlassCountVOList"
               :table-data-query-un-bind="tableDataQueryUnBind" @handle-la="handleVisualLA"
               @handle-storage="handleVisualStorage"/>
    <!-- 原片仓储详情 -->
    <el-dialog v-model="dialogFormDetail" top="5vh" width="85%">
      <el-table ref="table" style="margin-top: 20px; height: 700px;" :data="tableDataStation"
        :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }" row-key="projectNumber">
        <el-table-column>
          <template #default="props">
            <el-table :data="props.row.rawGlassDetailsList" style="width: 100%">
              <el-table-column prop="rawMargin" align="center" :label="$t('film.originalEdge')" min-width="80" />
              <el-table-column prop="rawWidth" align="center" :label="$t('Mounting.originalWidth')" min-width="80" />
              <el-table-column prop="rawHeight" align="center" :label="$t('film.height')" min-width="80" />
              <el-table-column prop="rawThickness" align="center" :label="$t('film.thickness')" min-width="80" />
              <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80" />
              <el-table-column prop="manufacturer" align="center" :label="$t('film.manufacturer')" min-width="80" />
              <el-table-column prop="rawNumber" align="center" :label="$t('film.originalFilmNumber')" min-width="80" />
              <el-table-column prop="remainQuantity" align="center" :label="$t('film.remainQuantity')" min-width="80" />
              <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="270">
                <template #default="scope">
                  <el-button type="text"  v-if="showOutboundButton" plain :disabled="scope.row.enableState !== 1 || scope.row.shelf == null"
                    @click="handleEdit(scope.row)">
                    {{ $t('productStock.exit') }}
                  </el-button>
                   <el-button type="text" v-if="showOutboundButtonLY" plain :disabled="scope.row.enableState !== 1 || scope.row.shelf == null"
                    @click="handleEditLY(scope.row, scope.$index)">
                    {{ $t('productStock.exit') }}
                  </el-button>
                  <el-button type="text" plain :disabled="scope.row.enableState !== 1 || scope.row.shelf == null"
                             @click="deleteWareHousing(scope.row)">
                    {{ $t('Mounting.delete') }}
                  </el-button>
                  <!-- 破损 -->
                  <el-button type="text" plain :disabled="scope.row.enableState !== 1 || scope.row.shelf == null"
                    @click="handleBreakage(scope.row)">
                    {{ $t('Mounting.dilapidation') }}
                  </el-button>
                  <!-- 出库 -->
                  <el-button type="text" plain :disabled="scope.row.enableState !== 1"
                             v-if="scope.row.shelf !== null && rawGlassPath !== 'rawGlassLayoutLY'"
                             @click="handleOutbound(scope.row)">
                    {{ $t('film.outboundShipments') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="deviceId" fixed align="center" :label="$t('searchOrder.deviceID')" min-width="10" /> -->
        <el-table-column prop="slot" fixed align="center" :label="$t('film.station')" min-width="10" />
        <el-table-column prop="shelf" fixed align="center" :label="$t('film.shelfNumber')" min-width="10" />
        <el-table-column align="center" fixed :label="$t('searchOrder.enabledStatus')" min-width="10"
          prop="enableState">
          <template #default="scope">
            <el-tag @click="toggleEnableState(scope.row)" :type="scope.row.enableState === 1 ? 'success' : 'warning'">
              {{ scope.row.enableState === 1 ? $t('searchOrder.enable') : $t('searchOrder.disable') }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column fixed="left" :label="$t('Mounting.operate')" align="center" width="70">
            <template #default="scope">
              <el-button type="text" v-if="scope.row.shelf !== null" plain @click="handleAdd(scope.row)">{{ $t('Mounting.add') }}</el-button>
            </template>
        </el-table-column> -->
      </el-table>
    </el-dialog>
    <!-- 历史任务 -->
    <el-dialog v-model="dialogHistory" top="5vh" width="95%" @close="iframeUrl = ''">
      <iframe :src="iframeUrl" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <!-- 吊装位信息管理 -->
    <el-dialog v-model="dialogFormStorage" top="7vh" width="95%" :title="$t('film.hoistingInformation')">
      <div style="margin-left: 960px;margin-top: -50px;font-size: large;margin-bottom: 10px;">{{
        $t('Mounting.originalQueueNotBound') }}</div>
      <div class="dialog-container">
        <!-- 左侧部分 -->
        <div class="left-side">
          <el-form class="glass-form" label-position="right" label-width="auto">
            <!-- 行1：吊装工位选择 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="24">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.stationColon')" :required="true">
                    <el-select v-model="formData.slot" :placeholder="$t('film.stationEnter')" autocomplete="off"
                               class="form-input" @change="handleSlotChange">
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
                    </el-select>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
 
            <!-- 行2：设备ID + 架子号 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('searchOrder.deviceIDColon')" :required="true">
                    <el-input v-model="formData.deviceId" :placeholder="$t('searchOrder.deviceIDEnter')" autocomplete="off"
                              class="form-input" disabled/>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.shelfNumberColon')" :required="true">
                    <el-input v-model="formData.shelf" :placeholder="$t('film.shelfNumberEnter')" autocomplete="off"
                              class="form-input" disabled/>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
 
            <!-- 行3：宽度 + 高度 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.widthColon')" :required="true">
                    <el-input v-model="formData.rawWidth" :placeholder="$t('film.widthEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.heightColon')" :required="true">
                    <el-input v-model="formData.rawHeight" :placeholder="$t('film.heightEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
 
            <!-- 行4：厚度 + 膜系 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.thicknessColon')" :required="true">
                    <el-input v-model="formData.rawThickness" :placeholder="$t('film.thicknessEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.filmsColon')" :required="true">
                    <el-input v-model="formData.filmsId" :placeholder="$t('film.filmsEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
 
            <!-- 行5：原片边距 + 剩余数量 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.originalEdgeColon')" :required="true">
                    <el-input v-model="formData.rawMargin" :placeholder="$t('film.originalEdgeEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.remainQuantityColon')" :required="true">
                    <el-input v-model="formData.remainQuantity" :placeholder="$t('film.remainQuantityEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
 
            <!-- 行6：生产商 + 原片编号 -->
            <el-row :gutter="20" class="form-row">
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.manufacturerColon')" :required="true">
                    <el-input v-model="formData.manufacturer" :placeholder="$t('film.manufacturerEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="form-item-wrapper">
                  <el-form-item :label="$t('film.originalFilmNumberColon')" :required="true">
                    <el-input v-model="formData.rawNumber" :placeholder="$t('film.originalFilmNumberEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
          </el-form>
 
          <div class="button-group">
            <el-button type="primary" @click="handleInboundRequests">
              {{ $t('film.emptyShelfPosition') }}
            </el-button>
            <el-button :disabled="!formData.shelf" type="primary" @click="handleOutboundRequests">
              {{ $t('film.warehouseReturn') }}
            </el-button>
            <el-button :disabled="!formData.shelf" type="primary" @click="EditClick">
              {{ $t('Mounting.confirm') }}
            </el-button>
          </div>
 
          <div class="single-button">
            <el-button :disabled="!formData.shelf" type="primary" @click="handleDelete">
              {{ $t('film.shelfEmpty') }}
            </el-button>
          </div>
        </div>
        <!-- 右侧部分 -->
        <div class="right-side">
          <el-table ref="table" style="width: 100%;height: 90%;" :data="tableDataQueryUnBind"
            :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
            <el-table-column prop="rawWidth" align="center" :label="$t('Mounting.originalWidth')" min-width="80" />
            <el-table-column prop="rawHeight" align="center" :label="$t('film.height')" min-width="80" />
            <el-table-column prop="rawThickness" align="center" :label="$t('film.thickness')" min-width="80" />
            <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80" />
            <!-- <el-table-column prop="rawMargin" align="center" :label="$t('film.originalEdge')" min-width="100" /> -->
            <el-table-column prop="manufacturer" align="center" :label="$t('film.manufacturer')" min-width="100" />
            <el-table-column prop="rawNumber" align="center" :label="$t('film.originalFilmNumber')" min-width="100" />
            <el-table-column prop="remainQuantity" align="center" :label="$t('film.remainQuantity')" min-width="100" />
            <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="100">
              <template #default="scope">
                <el-button type="text" plain @click="handleAddRow(scope.row)">{{ $t('Mounting.add') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
    <!-- 空架子到吊装位 -->
    <el-dialog v-model="dialogFormInboundRequests" top="24vh" width="30%" :title="$t('film.emptyShelfPosition')">
      <div style="margin-left: 50px;margin-bottom: 10px;">
        <el-form-item :label="$t('film.hoistingPositionColon')" :required="true">
          <el-select v-model="leftingStation" clearable :placeholder="$t('film.hoistingPositionColonSelection')"
            style="margin-left: 20px;">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="String(item.value)">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <template #footer>
        <div id="dialog-footer">
          <el-button type="primary" @click="queryInboundRequests">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="dialogFormInboundRequests = false">{{ $t('Mounting.cancel') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 出库 -->
    <el-dialog v-model="dialogFormOutbound" top="24vh" width="30%" :title="$t('film.outboundShipments')">
      <div style="margin-left: 50px;margin-bottom: 10px;">
        <el-form-item :label="$t('film.hoistingPositionColon')" :required="true">
          <el-select v-model="leftingStation" clearable :placeholder="$t('film.hoistingPositionColonSelection')"
            style="margin-left: 20px;">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="String(item.value)">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('film.shelfNumberColon')" :required="true">
          <el-input v-model="slot" clearable :placeholder="$t('film.shelfNumberEnter')"
            style="margin-left: 20px; width: 214px;">
          </el-input>
        </el-form-item>
      </div>
      <template #footer>
        <div id="dialog-footer">
          <el-button type="primary" @click="queryOutbound">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="dialogFormOutbound = false">{{ $t('Mounting.cancel') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 回库 -->
    <el-dialog v-model="dialogFormOutboundRequests" top="24vh" width="30%" :title="$t('film.warehouseReturn')">
      <div style="margin-left: 50px;margin-bottom: 10px;">
        <el-form-item :label="$t('film.hoistingPositionColon')" :required="true">
          <el-select v-model="leftingStation" clearable :placeholder="$t('film.hoistingPositionColonSelection')"
            style="margin-left: 20px;">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="String(item.value)">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <template #footer>
        <div id="dialog-footer">
          <el-button type="primary" @click="queryOutboundRequests">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="dialogFormOutboundRequests = false">{{ $t('Mounting.cancel') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 原片仓储详情--编辑 -->
    <rawGlassDialog v-model:visible="Edit" :row-data="currentRowData" :table-data-query-un-bind="tableDataQueryUnBind"
      :is-view-mode="true" @add-row="handleAddRow" @delete-row="deleteWareHousing" @confirm="EditClick"
      @clean="handleDelete" @close="Edit = false" />
    <!-- 洛阳原片仓储详情--编辑 -->
    <rawGlassDialogLY v-model:visible="EditLY" :row-data="currentRowData" :table-data-query-un-bind="tableDataQueryUnBind"
      :is-view-mode="true" @add-row="handleAddRow" @delete-row="deleteWareHousing" @confirm="EditClickLY"
      @clean="handleDelete" @close="EditLY = false" />
  </div>
</template>
<style scoped>
#dt {
  display: block;
  float: left;
  line-height: 20px;
  margin-left: 100px;
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
.dialog-container {
  display: flex;
  height: 70vh;
} 
.left-side {
  flex: 1;
  padding: 20px;
  border-right: 1px solid #ebeef5;
}
.right-side {
  flex: 1;
  padding: 20px;
}
.form-row {
  margin-bottom: 12px; /* 表单行间距 */
}
.glass-form {
  margin-left: 20px;
}
.glass-form :deep(.el-form-item__label) {
  /* max-width: 180px;  */
  margin-right: -10px;
}
.form-input {
  width: 200px !important;
}
.button-group {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin: 24px 0;
}
.single-button {
  text-align: center;
  margin-bottom: 16px;
}
</style>