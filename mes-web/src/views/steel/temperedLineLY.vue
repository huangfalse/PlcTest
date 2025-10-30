<script lang="ts" setup>
import request from "@/utils/request"
import {onBeforeUnmount, onMounted, reactive, ref, onUnmounted, watch, nextTick} from "vue"
import {useRouter} from "vue-router"
import {ElMessage, ElMessageBox} from 'element-plus'
import {useI18n} from 'vue-i18n'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService'
const {t} = useI18n()
const router = useRouter()
const tableDataCard = ref([])
const tableDataStove = ref([])
const totalTempering = ref(0)
const totalTemperingAll = ref(0)
const loadingRate = ref(0)
const allID = ref('')
const currentTempering = ref(0)
const loadWidth = ref(2750)
const loadLength = ref(6000)
const maxLoadPct = ref(0.9)
const chaos_pct = ref(0.5)
const xspace = ref(60)
const yspace = ref(70)
const switchSteel = ref(false)
const temperingSwitch = ref(false)
const rateUpper = ref(0)
const degreeMix = ref(0)
const adjustedTemper = ref([])
const tableStove = ref(null)
const scrollHeight = ref(0)
const actualyHeight = ref(0)
const selectedRows = ref<any[]>([])
const selectedRowsStove = ref<any[]>([])
const divsData = ref<Array<{ groupName: string; items: any[] }>>([])
const thicknessOptions = ref<{ value: number, label: string }[]>([])
const filmsOptions = ref<{ value: string, label: string }[]>([])
const selectedThickness = ref<number>()
const selectedFilmsId = ref<string>()
const isLoading = ref(true)
const curtainWall = ref(false)
const rotateMode = ref(false)
const allowMissing = ref(false)
const dialogALLTemperedMap = ref(false)
const dialogTemperedMap = ref(false)
const dialogAllMap = ref(false)
const mergeFields = ['flowCardId', 'layer', 'thickness', 'filmsId', 'selectionKey']
const spanArr = ref<{ index: number; rowspan: number }[]>([]);
const isLoadingMap = ref(false)
// 上方表格
const fetchData = async () => {
  try {
    const response = await request.post("/verticalGlass/temperingComputeController/queryNeedTemperingInfo", {
      filmsId: '',
      thickness: -1
    })
    if (response.code === 200) {
      tableDataCard.value = response.data || [];
      // 执行合并计算
      nextTick(() => {
        getSpan(tableDataCard.value, mergeFields);
      });
      // 提取厚度和膜系数据
      const thicknessSet = new Set<number>()
      const filmsSet = new Set<string>()
      response.data.forEach(item => {
        thicknessSet.add(item.thickness)
        filmsSet.add(item.filmsId)
      })
      thicknessOptions.value = Array.from(thicknessSet).map(thickness => ({
        value: thickness,
        label: `${thickness}mm`
      }))
      filmsOptions.value = Array.from(filmsSet).map(filmsId => ({
        value: filmsId,
        label: filmsId
      }))
    }
  } catch (error) {
  }
}
// 合并逻辑
const getSpan = (data: any[], mergeFields: string[]) => {
  // 添加空值检查
  if (!data || data.length === 0) return [];
  const result: { index: number; rowspan: number }[] = [];
  let pos = 0;
  result.push({ index: 0, rowspan: 1 });
  for (let i = 1; i < data.length; i++) {
    const currentKey = mergeFields.map(f => String(data[i][f])).join('|');
    const prevKey = mergeFields.map(f => String(data[i-1][f])).join('|');
    if (currentKey === prevKey) {
      result[pos].rowspan += 1;
      result.push({ index: pos, rowspan: 0 });
    } else {
      result.push({ index: i, rowspan: 1 });
      pos = i;
    }
  }
  return result;
};
// 表格合并方法
const getSpanMethod = ({ row, column, rowIndex }: any) => {
  const field = column.property;
  if (!mergeFields.includes(field)) return { rowspan: 1, colspan: 1 };
  //添加空值检查
  if (!spanArr.value || spanArr.value.length <= rowIndex) {
    return { rowspan: 1, colspan: 1 };
  }
  const target = spanArr.value[rowIndex];
  return target?.rowspan > 0 
    ? { rowspan: target.rowspan, colspan: 1 } 
    : { rowspan: 0, colspan: 0 };
};
// 数据更新时重新计算合并
watch(tableDataCard, (newVal) => {
  if (newVal && newVal.length > 0) {
    nextTick(() => {
      spanArr.value = getSpan(newVal, mergeFields);
    });
  }
}, { deep: true });
// 验证表单
const validateForm = () => {  
  if (!loadWidth.value) {  
    ElMessage.error(t('order.loadWidthEnter'))
    return false  
  }  
  if (!loadLength.value) {  
    ElMessage.error(t('order.loadLengthEnter'))
    return false  
  } 
  if (!xspace.value) {  
    ElMessage.error(t('order.xspaceEnter'))
    return false  
  }  
  if (!yspace.value) {  
    ElMessage.error(t('order.yspaceEnter'))
    return false  
  } 
  if (!selectedThickness.value) {  
    ElMessage.error(t('order.thicknessSelection'))
    return false  
  }  
  if (!selectedFilmsId.value) {
    ElMessage.error(t('order.filmsIdSelection'))
    return false  
  }  
  if (tableDataCard.value.length === 0) {
    ElMessage.error(t('order.tableEmpty'))
    return false
  }
  if (selectedRows.value.length === 0) {
    ElMessage.error(t('order.messageSelection'))
    return false
  }
  return true
}
// 预排炉操作
const handleFurnace = async () => {  
  // 下方表格存在数据时禁止预排炉
  if (tableDataStove.value && tableDataStove.value.length > 0) {
    ElMessage.error(t('report.existingTemperingTask'))
    return
  }
  if (!validateForm()) return  
  const temperingFlowCardIdRequestList = selectedRows.value.map(row => ({
    curtainWall: row.curtainWall || false,
    rotateMode: row.rotateMode || false,
    flowCardId: row.flowCardId,
    layer: row.layer,
    productSortNumber: row.productSortNumber
  }))
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('hollow.clickWhether'),
      t('workOrder.prompt'),
      {
        confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      isLoading.value = true;
      const response = await request.post("/verticalGlass/temperingComputeController/beforehandTemperingLayout", {
        allowMissing: allowMissing.value, 
        maxLoadPct: maxLoadPct.value || 0.5,
        chaos_pct: chaos_pct.value || 0,
        loadWidth: loadWidth.value,  
        loadLength: loadLength.value,  
        xspace: xspace.value,  
        yspace: yspace.value,  
        thickness: selectedThickness.value,  
        filmsId: selectedFilmsId.value,
        cageFree: 0.5,
        heat_mode: 0,
        max_area: 1,
        max_qty: 1,
        temperingFlowCardIdRequestList
      })
      if (response.code === 200) {
        window.localStorage.setItem('actualWidth', response.data.furnaceWidth)
        window.localStorage.setItem('actualHeight', response.data.furnaceHeight)
        ElMessage.success(response.message);
        // 先更新上方表格
        await fetchData();
        // 重置并刷新下方表格
        await resetAndRefreshStoveTable();
        dialogAllMap.value = true;
        loadingRate.value = (response.data.loadingRate || 0) * 100;
        console.log(response.data.loadingRate);
        console.log(loadingRate.value);
        totalTemperingAll.value = response.data.totalTempering || 0;
        window.localStorage.setItem('allID', response.data.id || '');
      }
    }
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
}
// 格式化函数：保留 2 位小数（四舍五入）
const formatNum = (value) => {
  return Math.round(value * 100) / 100; // 100 对应保留 2 位，如需 3 位则用 1000
};
// 下方表格
const fetchStovePageChange = async (page?: any) => {
  try {
    let pageNum = page || window.localStorage.getItem('pagenumber') || 1;
    const response = await request.post('/verticalGlass/TemperingTask/queryComputeTemperingTask', {
      pageNo: pageNum,
      pageSize: 20,
    });
    if (response.code == 200) {
      // 安全地更新表格数据
      tableDataStove.value = Array.isArray(response.data.temperingTaskList) 
        ? response.data.temperingTaskList 
        : [];
      // 确保DOM更新
      await nextTick();
      if (response.data.record) {
        window.localStorage.setItem('ID', response.data.record.id || '');
        window.localStorage.setItem('temperingEngineerId', response.data.record.temperingEngineerId || '');
        window.localStorage.setItem('furnaceHeight', response.data.record.furnaceHeight || '');
        window.localStorage.setItem('furnaceWidth', response.data.record.furnaceWidth || '');
      }
    } else {
      tableDataStove.value = [];
    }
  } catch (error) {
    tableDataStove.value = [];
  }
};
// 重置并刷新下方表格
const resetAndRefreshStoveTable = async () => {
  try {
    // 先重置表格数据
    tableDataStove.value = [];
    selectedRowsStove.value = [];
    selectedRowIds.value = new Set();
    // 确保DOM更新
    await nextTick();
    // 重新获取数据
    await fetchStovePageChange();
    // 如果WebSocket没有及时更新，设置重试机制
    let retryCount = 0;
    const maxRetries = 3;
    const checkAndRetry = async () => {
      if (tableDataStove.value.length === 0 && retryCount < maxRetries) {
        retryCount++;
        await new Promise(resolve => setTimeout(resolve, 500));
        await fetchStovePageChange();
        await checkAndRetry();
      }
    };
    await checkAndRetry();
  } catch (error) {
  }
};
// 指定一炉
const handleSpecifyFurnace = async(row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('hollow.clickWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {  
      const response = await request.post("/verticalGlass/TemperingTask/appointComputeTemperingTask",{
        id: row.id,
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
      }
    }  
  } catch (error) {
  }  
}; 
// 预排炉-返回，即清空任务
const deleteMakeTask = async () => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('hollow.clickWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {  
      const response = await request.post("/verticalGlass/temperingComputeRecord/removeComputeTemperingTask");
      if (response.code === 200) {
        ElMessage.success(response.message);
        dialogAllMap.value = false;
        fetchData();
        fetchStovePageChange();
        // 清空下方表格数据
        tableDataStove.value = [];
        selectedRowsStove.value = [];
        selectedRowIds.value = new Set();
      }
    }  
  } catch (error) {
  }  
}; 
// 查看全部版图
const handleALLLayoutView = (row) => {
  dialogALLTemperedMap.value = true;
  queryALLUnreported();
};
const getAdjustedRectsForGroup = (group: any[]) => {
  if (!group || !Array.isArray(group)) return [];
  let actualWidth = parseInt(window.localStorage.getItem('actualWidth') || '2750');
  let actualHeight = parseInt(window.localStorage.getItem('actualHeight') || '6000');
  // 计算缩放比例
  const containerWidth = 1609; // 容器固定宽度
  const scaleFactor = containerWidth / actualWidth;
  const scaledHeight = scaleFactor * actualHeight;
  // 更新实际高度
  actualyHeight.value = Math.min(scaledHeight, 800); // 限制最大高度
  return group.map(rect => {
    let adjustedWidth, adjustedHeight, widtha, heighta;
    let newX = rect.ycoordinate || 0;
    if (rect.width < rect.height) {
      widtha = rect.height || 0;
      heighta = rect.width || 0;
    } else {
      widtha = rect.width || 0;
      heighta = rect.height || 0;
    }
    if (rect.angle === 0) {
      adjustedWidth = widtha * scaleFactor;
      adjustedHeight = heighta * scaleFactor;
      newX = actualWidth - ((rect.ycoordinate || 0) + widtha);
    } else {
      adjustedWidth = heighta * scaleFactor;
      adjustedHeight = widtha * scaleFactor;
      newX = actualWidth - ((rect.ycoordinate || 0) + heighta);
    }
    return {
      ...rect,
      x_axis: newX * scaleFactor,
      y_axis: (rect.xcoordinate || 0) * scaleFactor,
      width: adjustedWidth,
      height: adjustedHeight,
      widtha: rect.width,
      heighta: rect.height,
    }
  });
}
const queryALLUnreported = async () => {
  let allID = window.localStorage.getItem('allID');
  if (!allID) return;
  try {
    isLoadingMap.value = true;
    const response = await request.post('/verticalGlass/temperingComputeController/queryAllTemperingByRecord', {
      id: allID,
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      divsData.value = (response.data.tempIndexMessageVOList || []).map(group => ({
        groupName: `${group.engineerId || ''}-${group.temperingLayoutId || ''}`,
        items: group.temperingGlassInfoList || []
      }))
      // 处理每个组的数据以计算正确的高度
      nextTick(() => {
        divsData.value.forEach((group, index) => {
          getAdjustedRectsForGroup(group.items);
        });
      });
    }
  } catch (error) {
  } finally {
    isLoadingMap.value = false;
  }
};
// 更新完整版图滚动区域高度
const updateActualyHeight = () => {
  try {
    let actualyHeight = window.localStorage.getItem('actualyHeight');
    actualyHeight.value = actualyHeight ? parseInt(actualyHeight) : 700;
    console.log(actualyHeight.value);
    
  } catch (error) {
    actualyHeight.value = 700; 
  }
};
// 更新下方滚动区域高度
const updateScrollHeight = () => {
  try {
    let realHeight = window.localStorage.getItem('realHeight');
    scrollHeight.value = realHeight ? parseInt(realHeight) : 400;
  } catch (error) {
    scrollHeight.value = 400;
  }
};
// 查看版图
const handleLayoutView = (row) => {
  dialogTemperedMap.value = true;
  try {
    queryUnreported(row.id);
  } catch (error) {
    isLoadingMap.value = false;
  }
};
const queryUnreported = async (id: string) => {
  if (!id) return;
  try {
    const response = await request.post('/verticalGlass/TemperingTask/queryTemperingComputeGlassList', {
      id: id,
    });
    if (response.code == 200) {
      let furnaceWidth = parseInt(window.localStorage.getItem('furnaceWidth') || '0');
      let furnaceHeight = parseInt(window.localStorage.getItem('furnaceHeight') || '0');
      if (!furnaceWidth) furnaceWidth = 2750;
      if (!furnaceHeight) furnaceHeight = 6000;
      ElMessage.success(response.message);
      const rawRects = response.data || [];
      adjustedTemper.value = rawRects.map(rect => {
        const scaleFactor = 1496 / furnaceWidth;
        const realHeight = scaleFactor * furnaceHeight;
        window.localStorage.setItem('realHeight', realHeight.toString());
        const scaleFactorY = realHeight / furnaceHeight;
        let adjustedWidth, adjustedHeight, widtha, heighta;
        if (rect.width < rect.height) {
          widtha = rect.height || 0;
          heighta = rect.width || 0;
        } else {
          widtha = rect.width || 0;
          heighta = rect.height || 0;
        }
        // 计算位置和尺寸
        let newX = rect.ycoordinate || 0;
        if (rect.angle === 0) {
          adjustedWidth = widtha * scaleFactor;
          adjustedHeight = heighta * scaleFactorY;
          newX = furnaceWidth - ((rect.ycoordinate || 0) + widtha);
        } else {
          adjustedWidth = heighta * scaleFactor;
          adjustedHeight = widtha * scaleFactorY;
          newX = furnaceWidth - ((rect.ycoordinate || 0) + heighta);
        }
        return {
          ...rect,
          x: newX * scaleFactor,
          y: (rect.xcoordinate || 0) * scaleFactorY,
          width: adjustedWidth,
          height: adjustedHeight,
          widtha: rect.width,
          heighta: rect.height,
        }
      });
      // 更新滚动高度
      updateScrollHeight();
    }
  } catch (error) {
  }
};
// 钢化自动出片
const handleSteel = async () => {
  let ID = window.localStorage.getItem('ID');
  if (!ID) return;
  try {
    const stateValue = switchSteel.value ? 1 : 0;
    const response = await request.post('/verticalGlass/temperingComputeController/updateTemperingComputeRecordState',{
        id: ID,
        state: stateValue
      }
    );
    if (response.code === 200) {
      ElMessage.success(response.message);
      fetchData();
      fetchStovePageChange();
    }
  } catch (error) {
  }
};
// 钢化开关
const handleSwitchSteel = async () => {
  try {
    var url="/verticalGlass/VerticalJobController/temperingSwitch?flag="+temperingSwitch.value;
    const response = await request.post(url);
    if (response.code == 200) {
      ElMessage.success(response.message);
      temperingSwitch.value = response.data;
    }
  } catch (error) {
  }
}
// 在data中新增选中行ID的存储
const selectedRowIds = ref<Set<string>>(new Set());
// 下方表格选中处理逻辑
const handleStoveSelectionChange = (rows: any[]) => {
  if (!rows) {
    selectedRowsStove.value = [];
    selectedRowIds.value = new Set();
    return;
  }
  selectedRowsStove.value = rows;
  selectedRowIds.value = new Set(rows.map(row => row.id));
};
// 上方表格选中处理
const handleSelectionChange = (rows: any[]) => {
  if (!rows) {
    selectedRows.value = [];
    return;
  }
  selectedRows.value = rows;
  // 获取所有合并行的完整数据
  const allMergedRows = [];
  const processedKeys = new Set();
  rows.forEach(row => {
    const key = mergeFields.map(f => String(row[f])).join('|');
    if (!processedKeys.has(key)) {
      processedKeys.add(key);
      // 找到所有具有相同合并键的行
      const mergedRows = (tableDataCard.value || []).filter(item => 
        mergeFields.map(f => String(item[f])).join('|') === key
      );
      allMergedRows.push(...mergedRows);
    }
  });
  selectedRows.value = allMergedRows;
};
// 选择控制函数
const isSelectable = (row: any) => {
  return row.state === 0;
};
// 删除排炉结果
const handleResultsDelete = async() => {
  if (!selectedRowsStove.value || selectedRowsStove.value.length === 0) {
    ElMessage.error(t('order.messageSelection'));
    return;
  }
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('searchOrder.deleteMessageWhether'), 
      t('workOrder.prompt'),  
      {  
        confirmButtonText: t('workOrder.yes'), 
        cancelButtonText: t('workOrder.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      const ids = selectedRowsStove.value.map(row => row.id);
      let temperingEngineerId = window.localStorage.getItem('temperingEngineerId');
      const response = await request.post('/verticalGlass/TemperingTask/removeTemperingById',{
        idList: ids,
        engineerId: temperingEngineerId
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
        // 清空选中状态
        selectedRowsStove.value = [];
        selectedRowIds.value = new Set();
        // 强制刷新表格数据
        await resetAndRefreshStoveTable();
        // 确保表格选中状态被清空
        nextTick(() => {
          if (tableStove.value) {
            tableStove.value.clearSelection();
          }
        });
        fetchData();
      }
    }
  } catch (error) {
  }
}
let socket = null;
// 初始化WebSocket
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/verticalGlass/webSocket/tempingCompute`;
const handleMessage = (data: any) => {
  isLoading.value = true;
  // 保存当前选中ID
  const currentSelectedIds = new Set(
    selectedRowsStove.value ? selectedRowsStove.value.map(row => row.id) : []
  );
  if(data.temperingTaskList != null){
    isLoading.value = false;
    tableDataStove.value = Array.isArray(data.temperingTaskList) 
      ? data.temperingTaskList 
      : [];
    // 数据更新后恢复选中状态
    nextTick(() => {
      const tableRef = tableStove.value;
      if (tableRef && tableDataStove.value && tableDataStove.value.length > 0) {
        // 清空当前选中
        tableRef.clearSelection();
        // 重新选中之前选中的行
        tableDataStove.value.forEach(row => {
          if (currentSelectedIds.has(row.id)) {
            tableRef.toggleRowSelection(row, true);
          }
        });
      }
    });
  } else {
    isLoading.value = false;
    tableDataStove.value = [];
  }
  if(data.record != null){
    totalTempering.value = data.record.totalTempering || 0;
  } else {
    totalTempering.value = 0;
  }
  if(data.currentTempering != null){
    currentTempering.value = data.currentTempering || 0;
  } else {
    currentTempering.value = 0;
  }
  if(data.record != null){
    switchSteel.value = data.record.state === 1;
  } else {
    switchSteel.value = false;
  }
  // 钢化开关
  if(data.temperingSwitch != null){
    temperingSwitch.value = data.temperingSwitch;
  } else {
    temperingSwitch.value = false;
  }
  if(data.record != null){
    window.localStorage.setItem('ID', data.record.id || '');
    window.localStorage.setItem('temperingEngineerId', data.record.temperingEngineerId || '');
    window.localStorage.setItem('furnaceHeight', data.record.furnaceHeight || '');
    window.localStorage.setItem('furnaceWidth', data.record.furnaceWidth || '');
  }
};
// 获取矩形颜色
function getRectColorOut(state) {  
  switch (state) {  
    case 3: return '#eebe77'  
    case 4: return '#CD6090'
    case 8: return '#911005' 
    case 9: return '#4682B4' 
    default: return '#CDAF95' 
  }  
}
function getStateType(state) {
  switch (state) {
    case 0:
      return 'primary';
    case 1:
      return 'warning';
    case 2:
      return 'success';
    case 3:
      return 'danger';
    default:
      return 'info';
  }
}
function getStateText(state) {
  switch (state) {
    case 0:
      return t('Mounting.notStarted');
    case 1:
      return t('Mounting.inAction');
    case 2:
      return t('film.finish');
    case 3:
      return t('Mounting.deleteFinish');
    default:
      return t('Mounting.unknown');
  }
}
// 页面刷新时恢复选中状态
onMounted(() => {
  const savedIds = localStorage.getItem('selectedRowIds');
  if (savedIds) {
    selectedRowIds.value = new Set(JSON.parse(savedIds));
  }
  fetchData();
  socket = initializeWebSocket(socketUrl, handleMessage);
  fetchStovePageChange();
});
// 窗口关闭时保存状态
window.addEventListener('beforeunload', () => {
  localStorage.setItem('selectedRowIds', JSON.stringify(Array.from(selectedRowIds.value)));
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
  }
});
onBeforeUnmount(() => {
  closeWebSocket();
});
const formatSliderValue = (value: number) => {
  return `${(value * 100).toFixed(0)}%`;
}
</script>
<template>
  <div style="height: 500px;">
    <div style="display: flex;margin-top: 10px; width: 100%;">
    <span style="margin-left: 70px; margin-right: 20px; font-size: 20px; font-weight: bold; margin-top: 5px;">
      {{ $t('report.steelOne') }}
    </span>
    <div style="max-width: 400px; flex: 1; display: flex; margin-top: 6px;">
      <span style="margin-right: 10px;">{{ $t('report.rateUpperLimitColon') }}</span>
      <el-slider 
        v-model="maxLoadPct"
        :min="0.50"
        :max="1.00"
        :step="0.01"
        :format-tooltip="formatSliderValue"
        style="flex: 0 0 60%;"
      />
    </div>
    <div style="max-width: 400px; flex: 1; display: flex; margin-top: 6px;">
      <span style="margin-right: 10px;">{{ $t('report.degreeMixingColon') }}</span>
      <el-slider
        v-model="chaos_pct"
        :min="0"
        :max="1"
        :step="0.01"
        :format-tooltip="formatSliderValue"
        style="flex: 0 0 60%;"
      />
    </div>
      <!-- 炉长 -->
      <span style="margin-top: 6px;">{{ $t('report.furnaceLongColon') }}</span>
       <el-input v-model="loadLength" style="margin-left: 10px; width: 80px;" />
       <!-- 炉宽 -->
      <span style="margin-top: 6px;margin-left: 10px;">{{ $t('report.furnaceWidthColon') }}</span>
       <el-input v-model="loadWidth" style="margin-left: 10px; width: 80px;" />
       <!-- 左右间距 -->
      <span style="margin-top: 6px;margin-left: 10px;">{{ $t('report.marginsWidthColon') }}</span>
       <el-input v-model="xspace" style="margin-left: 10px; width: 80px;" />
       <!-- 前后间距 -->
      <span style="margin-top: 6px;margin-left: 10px;">{{ $t('report.marginsLongColon') }}</span>
       <el-input v-model="yspace" style="margin-left: 10px; width: 80px;" />
    </div>
    <span style="margin-top: 6px;margin-left: 50px;">{{ $t('Mounting.thicknessColon') }}</span>
  <el-select 
    v-model="selectedThickness"
    clearable
    style="width: 100px"
    @change="fetchData()"
  >
    <el-option
      v-for="option in thicknessOptions"
      :key="option.value"
      :label="option.label"
      :value="option.value"
    />
  </el-select>
  <span style="margin-top: 6px;margin-left: 30px;">{{ $t('film.filmsColon') }}</span>
  <!-- 膜系选择 -->
  <el-select 
    v-model="selectedFilmsId"
    clearable
    style="width: 100px; margin-left: 10px;"
    @change="fetchData()"
  >
    <el-option
      v-for="option in filmsOptions"
      :key="option.value"
      :label="option.label"
      :value="option.value"
    />
  </el-select>
  <el-button style="margin-left: 30px;" @click="fetchData" >{{ $t('order.inquire') }}</el-button>
  <!-- 预排炉 -->
  <el-button style="margin-left: 30px;width: 100px;" @click="handleFurnace" >{{ $t('report.PreArrangedFurnace') }}</el-button>
  <!-- 忽略缺片 -->
  <el-checkbox v-model="allowMissing" style="margin-left: 40px;margin-top: 5px;" :label="$t('report.ignoreMissingPieces')" size="large" />
      <el-card style="flex: 1;margin-left: 10px;margin-top: 15px;margin-bottom: 10px;">
        <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
          <el-table height="300" ref="table" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }" 
           @selection-change="handleSelectionChange"
             :data="tableDataCard"  :span-method="getSpanMethod">
            <el-table-column prop="selectionKey" type="selection" width="50"/>
            <el-table-column prop="flowCardId" :label="$t('searchOrder.processCardNumber')" align="center"/>
            <el-table-column prop="layer" :label="$t('searchOrder.layer')" width="120" align="center"/>
            <el-table-column prop="thickness" :label="$t('Mounting.thickness')" width="120" align="center"/>
            <el-table-column prop="filmsId" :label="$t('searchOrder.membraneSystem')" width="120" align="center"/>
            <el-table-column prop="productSortNumber" :label="$t('large.orderNumber')" width="120" align="center"/>
            <el-table-column prop="width" :label="$t('Mounting.width')" width="120" align="center"/>
            <el-table-column prop="height" :label="$t('Mounting.height')" width="120" align="center"/>
            <el-table-column prop="needTemperingCount" :label="$t('report.furnacesLinedNumber')" width="150" align="center"/>
            <el-table-column prop="realCount" :label="$t('report.alreadyCageQuantity')" width="150" align="center"/>
            <el-table-column prop="count" :label="$t('report.priority')" width="120" align="center"/>
            <el-table-column :label="$t('report.curtainWallMode')" width="120" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.curtainWall" size="large" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('report.placedHorizontally')" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.rotateMode" size="large" />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
       <span style="margin-top: 6px;margin-left: 50px;">{{ $t('report.furnacesArrangedNumberColon') }}{{ totalTempering }}</span>
       <span style="margin-top: 6px;margin-left: 50px;">{{ $t('report.filmBatchNumberColon') }}{{ currentTempering }}</span>
       <el-button style="margin-left: 50px;width: 100px;">{{ $t('report.filmOutputTempered') }}</el-button>
       <el-switch style="margin-left: 10px;" v-model="switchSteel" class="mb-2" @change="handleSteel" />
       <el-switch style="margin-top: 7px;margin-left: 10px;" v-model="temperingSwitch" class="mb-2" :inactive-text="$t('searchOrder.temperedSwitch')" @change="handleSwitchSteel" />
       <el-button style="margin-left: 30px;width: 100px;" @click="handleResultsDelete">{{ $t('report.furnaceResultsDelete') }}</el-button>
      <el-card style="flex: 1;margin-left: 10px;margin-top: 15px;">
  <div v-loading="isLoading" class="loading-container" :element-loading-text="$t('searchOrder.loadingText')" >
        <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
          <el-table height="300" ref="tableStove" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
          @selection-change="handleStoveSelectionChange" reserve-selection row-key="id"
             :data="tableDataStove">
            <el-table-column type="selection" width="50" :selectable="isSelectable" reserve-selection/>
            <el-table-column prop="temperingLayoutId" :label="$t('order.heatNumber')" align="center"/>
            <el-table-column align="center" :label="$t('Mounting.state')" prop="state">
            <template #default="scope">  
              <el-tag :type="getStateType(scope.row.state)">  
                {{ getStateText(scope.row.state) }}  
              </el-tag>  
            </template> 
          </el-table-column>
            <el-table-column prop="ratio" :label="$t('report.rateLimit')" align="center"/>
            <el-table-column prop="thickness" :label="$t('Mounting.thickness')" align="center"/>
            <el-table-column prop="filmsId" :label="$t('searchOrder.membraneSystem')" align="center"/>
            <el-table-column prop="glassTotal" :label="$t('report.glassNumber')" align="center"/>
            <el-table-column prop="realCount" :label="$t('report.alreadyCageQuantity')" align="center"/>
            <el-table-column prop="curtainWall" :label="$t('report.curtainWallMode')" align="center"/>
            <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="170">
            <template #default="scope">
              <el-button type="text" plain @click="handleSpecifyFurnace(scope.row)">{{ $t('searchOrder.specifyFurnace') }}</el-button>
              <el-button type="text" plain @click="handleLayoutView(scope.row)">{{ $t('report.layoutView') }}</el-button>
            </template>
        </el-table-column>
          </el-table>
        </div>
        </div>
      </el-card>
    </div>
  <!-- 预排炉后弹窗 -->
  <el-dialog v-model="dialogAllMap" top="30vh" width="35%" >
    <div style="margin-left: 30%;">
    <span style="margin-right: 40px;">{{ $t('searchOrder.totalFurnaces') }}：{{ totalTemperingAll }}</span>
    {{ $t('report.rateLimitAverage') }}：{{ formatNum(loadingRate) }}%
    <el-button style="margin-left: 70px;" plain @click="handleALLLayoutView()">{{ $t('searchOrder.temperedLayout') }}</el-button>
    </div>
    <div style="margin-left: 40%;margin-top: 50px;">
    <el-button plain @click="dialogAllMap = false">{{ $t('report.save') }}</el-button>
    <el-button plain @click="deleteMakeTask">{{ $t('report.return') }}</el-button>
    </div>
  </el-dialog>
<!-- 查看完整版图 -->
 <el-dialog v-model="dialogALLTemperedMap" top="2vh" width="90%" class="full-layout-dialog">
    <div style="height: 83vh; display: flex; flex-direction: column;">
      <!-- 内容区域 - 使用弹性布局 -->
      <div style="flex: 1; overflow-y: auto; padding: 10px;">
        <div
          v-for="(group, groupIndex) in divsData" 
          :key="groupIndex" 
          style="margin-bottom: 30px;"
        >
          <el-card style="width: 100%;">
            <div class="card-header">
              <span style="font-size: 16px; font-weight: bold;">{{ group.groupName }}</span>
            </div>
            <!-- 滚动区域 - 使用动态高度 -->
            <div class="scrollbar-wrapper" :style="{ maxHeight: actualyHeight + 50 + 'px' }">
              <el-scrollbar>
                <div 
                  class="content-container" 
                  :style="{ 
                    position: 'relative', 
                    width: '1609px', 
                    height: actualyHeight + 'px',
                    backgroundColor: '#e9e9eb'
                  }"
                >
                  <div
                    v-for="(rect, index) in getAdjustedRectsForGroup(group.items)"
                    :key="index"
                    class="rect"
                    :style="{
                      position: 'absolute',
                      top: `${rect.y_axis}px`,
                      left: `${rect.x_axis}px`,
                      width: `${rect.width}px`,
                      height: `${rect.height}px`,
                      backgroundColor: 'lightblue',
                      border: '1px solid #333',
                      boxSizing: 'border-box'
                    }"
                  >
                    <div class="centered-text">
                      <div style="font-size: 10px; font-weight: bold;">{{ rect.glassId }}</div>  
                      <div style="font-size: 10px; font-weight: bold;">{{ rect.flowCardId }}</div>  
                      <div style="font-size: 12px; font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>
                    </div>
                  </div>
                </div>
              </el-scrollbar>
            </div>
          </el-card>
        </div>
      </div>
      <!-- 底部按钮 -->
      <div class="dialog-footer-fixed">
        <el-button @click="dialogALLTemperedMap = false">{{ $t('Mounting.close') }}</el-button>
      </div>
    </div>
  </el-dialog>
<!-- 下方查看版图 -->
  <el-dialog v-model="dialogTemperedMap" top="7vh" width="80%" >
    <div style="height: 100%;">
          <el-scrollbar :style="{ height: scrollHeight + 'px' }" style="background-color: #e9e9eb; ">
            <div style="position: relative; max-width: 1500px; height: 100%;">
              <div  
                v-for="(rect, index) in adjustedTemper"
                :key="index"  
                class="rect"  
                :style="{ 
                  position: 'absolute',
                  top: `${rect.y}px`,
                  left: `${rect.x}px`,
                  width: `${rect.width}px`, 
                  height: `${rect.height}px`,
                  backgroundColor: rect.isActive ? '#ADFF2F' : getRectColorOut(rect.state),
                  cursor: 'pointer'
                }">
                <div class="centered-text">
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.glassId }}</div>  
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.flowCardId }}</div>  
                  <div style="font-size: 15px;font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>
                </div>
              </div> 
            </div>
          </el-scrollbar>
        </div>
  </el-dialog>
</template>
<style scoped>
  .rect {  
  border: 1px solid black; /* 设置矩形的边框 */
  cursor: pointer;
} 
.scrollbar-container {
  background-color: #e9e9eb;
  width: 100%;
  height: 100%;
}
.content-container {
  position: relative;
  width: 100%;
  height: 750px;
}
.centered-text {
  text-align: center;
  width: 100%;
}
.card-header {
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
  font-size: 16px;
}
.loading-container {
  position: relative;
  height: 100%;
}
.dialog-footer-fixed {
  position: sticky;
  bottom: 0;
  background: white;
  padding: 15px 0;
  border-top: 1px solid #ebeef5;
  z-index: 1000;
  text-align: center;
  width: 100%;
}
</style>