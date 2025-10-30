<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from "vue";
import {ElMessage} from 'element-plus'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {useI18n} from 'vue-i18n'
import {useRouter} from 'vue-router'
import {fetchAllMessage, updateTempQueueState} from '@/api/tempering'

const { t } = useI18n()
const router = useRouter()
const adjustedTemper = ref([])
const dialogBaked = ref(false)
const cantakeBaked = ref(true)
const canSelectProjectOut = ref(true)
const currentRect = ref(null)
const currentGlassId = ref(null)
const currenttemperingFeedSequence = ref(null)
const activeRectId = ref(null) // 新增：专门跟踪激活的矩形 ID
// 获取数据
const fetchData = async () => {
  try {
    const result = await fetchAllMessage()
    const firstKey = Object.keys(result.data.tempBeforeQueueInfo)[0]
    if (firstKey && result.data.tempBeforeQueueInfo[firstKey]) {
      processTemperData(result.data.tempBeforeQueueInfo[firstKey])
    }
  } catch (error) {
  }
}
// 处理钢化数据
const processTemperData = (data) => {
  adjustedTemper.value = data.map(rect => {
    // 保留现有的 isActive 状态（如果存在）
    const existingRect = adjustedTemper.value.find(r => r.id === rect.id)
    const isActive = existingRect ? existingRect.isActive : false

    const scaleFactor = 1621.78 / 6000
    const scaleFactorY = 700 / 2800
    let adjustedWidth, adjustedHeight, widtha, heighta
    if (rect.width < rect.height) {
      widtha = rect.height
      heighta = rect.width
    } else {
      widtha = rect.width
      heighta = rect.height
    }
    // 计算位置和尺寸
    let newX = rect.ycoordinate
    if (rect.angle === 0) {
      adjustedWidth = widtha * scaleFactor
      adjustedHeight = heighta * scaleFactorY
      newX = 6000 - (rect.ycoordinate + widtha)
    } else {
      adjustedWidth = heighta * scaleFactor
      adjustedHeight = widtha * scaleFactorY
      newX = 6000 - (rect.ycoordinate + heighta)
    }
    return {
      ...rect,
      x: newX * scaleFactor,
      y: rect.xcoordinate * scaleFactorY,
      width: adjustedWidth,
      height: adjustedHeight,
      widtha: rect.width,
      heighta: rect.height,
      isActive: activeRectId.value === rect.id ? true : isActive // 从外部状态设置
    }
  })
}
// WebSocket 消息处理
const socketUrl = `ws://${window.ipConfig.serverUrl}/temperingGlass/webSocket/tempGlass`
const handleMessage = (data) => {
  if (data?.tempBeforeQueueInfo) {
    const firstKey = Object.keys(data.tempBeforeQueueInfo)[0]
    if (firstKey && data.tempBeforeQueueInfo[firstKey]) {
      processTemperData(data.tempBeforeQueueInfo[firstKey])
    }
  }
}
// 显示对话框
function showDialogOut(rect) {
  activeRectId.value = rect.id // 只存储 ID 而不是整个对象
  const index = adjustedTemper.value.findIndex(r => r.id === rect.id)
  if (index !== -1) {
    // 重置所有矩形的激活状态
    adjustedTemper.value.forEach(r => { r.isActive = false })
    adjustedTemper.value[index].isActive = true
    currentGlassId.value = rect.id
    currenttemperingFeedSequence.value = rect.temperingFeedSequence
    dialogBaked.value = true
    currentRect.value = rect
    // 更新按钮状态
    canSelectProjectOut.value = rect.state !== 8
  }
}
// 关闭对话框
const handleDialogCloseOut = () => {
  activeRectId.value = null
  adjustedTemper.value.forEach(rect => {  
    rect.isActive = false  
  })  
  dialogBaked.value = false 
}
// 破损处理
const handleDamageOut = async () => {
  if (!currentGlassId.value) {
    return
  }
  try {
    await updateTempQueueState(currentGlassId.value, 8)
    ElMessage.success('操作成功！')
    dialogBaked.value = false
    updateRectColorsOut()
  } catch (error) {
  }
}
onMounted(() => {
  fetchData()
  initializeWebSocket(socketUrl, handleMessage)
})
// 更新矩形颜色状态
function updateRectColorsOut() {
  adjustedTemper.value.forEach(rect => {  
    if (rect.id === currentGlassId.value) {  
      rect.state = 8
    }
  })  
}
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
onBeforeUnmount(() => {
  closeWebSocket()
})
</script>
<template>
  <div style="height: 870px;">
    <div style="margin-top: 10px; height: 100%; display: flex; flex-direction: column;">
      <el-card style="margin-left: 10px; margin-right: 10px; margin-top: 10px;">
        <div style="height: 100%;">
          <el-scrollbar height="800px" style="background-color: #e9e9eb; height: 100%;">
            <div style="position: relative; max-width: 1400px; height: 100%;">
              <div  
                v-for="(rect, index) in adjustedTemper"
                :key="index"  
                @click="showDialogOut(rect)"  
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
      </el-card>
      <el-dialog 
        v-model="dialogBaked" 
        top="30vh" 
        width="15%" 
        style="text-align: center;" 
        @close="handleDialogCloseOut"
      >
        <el-button 
          :disabled="!canSelectProjectOut" 
          type="warning" 
          plain 
          @click="handleDamageOut"
          style="width: 150px;margin-left: 10px;"
        >
          {{ $t('Mounting.dilapidation') }}
        </el-button>
      </el-dialog> 
    </div>
  </div>
</template>
<style scoped>
.rect {  
  border: 1px solid black;
  transition: background-color 0.3s ease;
} 
.rect:hover {
  opacity: 0.8;
}
.centered-text {
  display: flex;
  flex-direction: column;
  justify-content: center;  
  align-items: center; 
  height: 100%;
  text-align: center;
} 
</style>