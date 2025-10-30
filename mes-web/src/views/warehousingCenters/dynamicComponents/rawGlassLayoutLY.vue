<script lang="ts" setup>
import rawGlassDialogLY from '../rawGlassDialogLY.vue'
import request from "@/utils/request"
import {defineEmits, defineProps, onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
// import { closeWebSocket, initializeWebSocket } from '@/utils/WebSocketService'
import WebSocketStorage from '@/utils/WebSocketStorage'
 const shelfIdMapping = ref<{[key: string]: number[]}>({})
const tableDataQueryUnBind = ref<any[]>([])
const currentRowData = ref<any>({})
const dialogFormLA = ref(false)
const showL1Left = ref(false)
const showL1Right = ref(false)
const showA21Left = ref(false)
const showA21Right = ref(false)
const showA22Left = ref(false)
const showA22Right = ref(false)
const showA31Left = ref(false)
const showA31Right = ref(false)
const showA32Left = ref(false)
const showA32Right = ref(false)
const showA41Left = ref(false)
const showA41Right = ref(false)
const showA42Left = ref(false)
const showA42Right = ref(false)
const showA51Left = ref(false)
const showA51Right = ref(false)
const showA52Left = ref(false)
const showA52Right = ref(false)
const showA61Left = ref(false)
const showA61Right = ref(false)
const showA62Left = ref(false)
const showA62Right = ref(false)
const showA71Left = ref(false)
const showA71Right = ref(false)
const showA72Left = ref(false)
const showA72Right = ref(false)
const showA81Left = ref(false)
const showA81Right = ref(false)
const showA82Left = ref(false)
const showA82Right = ref(false)
const showL9Left = ref(false)
const showL9Right = ref(false)
const showL10Left = ref(false)
const showL10Right = ref(false)
const showA111Left = ref(false)
const showA111Right = ref(false)
const showA112Left = ref(false)
const showA112Right = ref(false)
const showA121Left = ref(false)
const showA121Right = ref(false)
const showA122Left = ref(false)
const showA122Right = ref(false)
const showA131Left = ref(false)
const showA131Right = ref(false)
const showA132Left = ref(false)
const showA132Right = ref(false)
const showA141Left = ref(false)
const showA141Right = ref(false)
const showA142Left = ref(false)
const showA142Right = ref(false)
const showA151Left = ref(false)
const showA151Right = ref(false)
const showA152Left = ref(false)
const showA152Right = ref(false)
const showA161Left = ref(false)
const showA161Right = ref(false)
const showA162Left = ref(false)
const showA162Right = ref(false)
const showA171Left = ref(false)
const showA171Right = ref(false)
const showA172Left = ref(false)
const showA172Right = ref(false)
const showL18Left = ref(false)
const showL18Right = ref(false)
const showL19Left = ref(false)
const showL19Right = ref(false)
const showL20Left = ref(false)
const showL20Right = ref(false)
const dialogHistory = ref(false)
const currentShelfId = ref(0) // 当前点击的架子ID
const isLeftSide = ref(true) // 标记点击的是左侧还是右侧
const rawGlassCountVOList = ref([]); // 存储 WebSocket 返回的数据
const props = defineProps({
  cardStates: {
    type: Object,
    required: true
  }
})
const websocketService = WebSocketStorage.getInstance()
onMounted(() => {
const handleMessage = (data) => {
  if (data.rawGlassCountVOList != null && data.rawGlassCountVOList.length > 0) {
    rawGlassCountVOList.value = data.rawGlassCountVOList; // 存储数据
    showL1Left.value = data.rawGlassCountVOList[0].rawCount >= 2;
    showL1Right.value = data.rawGlassCountVOList[0].rawCount >= 1;
    showA21Left.value = data.rawGlassCountVOList[1].rawCount >= 2;
    showA21Right.value = data.rawGlassCountVOList[1].rawCount >= 1;
    showA22Left.value = data.rawGlassCountVOList[2].rawCount >= 2;
    showA22Right.value = data.rawGlassCountVOList[2].rawCount >= 1;
    showA31Left.value = data.rawGlassCountVOList[3].rawCount >= 2;
    showA31Right.value = data.rawGlassCountVOList[3].rawCount >= 1;
    showA32Left.value = data.rawGlassCountVOList[4].rawCount >= 2;
    showA32Right.value = data.rawGlassCountVOList[4].rawCount >= 1;
    showA41Left.value = data.rawGlassCountVOList[5].rawCount >= 2;
    showA41Right.value = data.rawGlassCountVOList[5].rawCount >= 1;
    showA42Left.value = data.rawGlassCountVOList[6].rawCount >= 2;
    showA42Right.value = data.rawGlassCountVOList[6].rawCount >= 1;
    showA51Left.value = data.rawGlassCountVOList[7].rawCount >= 2;
    showA51Right.value = data.rawGlassCountVOList[7].rawCount >= 1;
    showA52Left.value = data.rawGlassCountVOList[8].rawCount >= 2;
    showA52Right.value = data.rawGlassCountVOList[8].rawCount >= 1;
    showA61Left.value = data.rawGlassCountVOList[9].rawCount >= 2;
    showA61Right.value = data.rawGlassCountVOList[9].rawCount >= 1;
    showA62Left.value = data.rawGlassCountVOList[10].rawCount >= 2;
    showA62Right.value = data.rawGlassCountVOList[10].rawCount >= 1;
    showA71Left.value = data.rawGlassCountVOList[11].rawCount >= 2;
    showA71Right.value = data.rawGlassCountVOList[11].rawCount >= 1;
    showA72Left.value = data.rawGlassCountVOList[12].rawCount >= 2;
    showA72Right.value = data.rawGlassCountVOList[12].rawCount >= 1;
    showA81Left.value = data.rawGlassCountVOList[13].rawCount >= 2;
    showA81Right.value = data.rawGlassCountVOList[13].rawCount >= 1;
    showA82Left.value = data.rawGlassCountVOList[14].rawCount >= 2;
    showA82Right.value = data.rawGlassCountVOList[14].rawCount >= 1;
    showL9Left.value = data.rawGlassCountVOList[15].rawCount >= 2;
    showL9Right.value = data.rawGlassCountVOList[15].rawCount >= 1;
    showL10Left.value = data.rawGlassCountVOList[16].rawCount >= 2;
    showL10Right.value = data.rawGlassCountVOList[16].rawCount >= 1;
    showA111Left.value = data.rawGlassCountVOList[17].rawCount >= 2;
    showA111Right.value = data.rawGlassCountVOList[17].rawCount >= 1;
    showA112Left.value = data.rawGlassCountVOList[18].rawCount >= 2;
    showA112Right.value = data.rawGlassCountVOList[18].rawCount >= 1;
    showA121Left.value = data.rawGlassCountVOList[19].rawCount >= 2;
    showA121Right.value = data.rawGlassCountVOList[19].rawCount >= 1;
    showA122Left.value = data.rawGlassCountVOList[20].rawCount >= 2;
    showA122Right.value = data.rawGlassCountVOList[20].rawCount >= 1;
    showA131Left.value = data.rawGlassCountVOList[21].rawCount >= 2;
    showA131Right.value = data.rawGlassCountVOList[21].rawCount >= 1;
    showA132Left.value = data.rawGlassCountVOList[22].rawCount >= 2;
    showA132Right.value = data.rawGlassCountVOList[22].rawCount >= 1;
    showA141Left.value = data.rawGlassCountVOList[23].rawCount >= 2;
    showA141Right.value = data.rawGlassCountVOList[23].rawCount >= 1;
    showA142Left.value = data.rawGlassCountVOList[24].rawCount >= 2;
    showA142Right.value = data.rawGlassCountVOList[24].rawCount >= 1;
    showA151Left.value = data.rawGlassCountVOList[25].rawCount >= 2;
    showA151Right.value = data.rawGlassCountVOList[25].rawCount >= 1;
    showA152Left.value = data.rawGlassCountVOList[26].rawCount >= 2;
    showA152Right.value = data.rawGlassCountVOList[26].rawCount >= 1;
    showA161Left.value = data.rawGlassCountVOList[27].rawCount >= 2;
    showA161Right.value = data.rawGlassCountVOList[27].rawCount >= 1;
    showA162Left.value = data.rawGlassCountVOList[28].rawCount >= 2;
    showA162Right.value = data.rawGlassCountVOList[28].rawCount >= 1;
    showA171Left.value = data.rawGlassCountVOList[29].rawCount >= 2;
    showA171Right.value = data.rawGlassCountVOList[29].rawCount >= 1;
    showA172Left.value = data.rawGlassCountVOList[30].rawCount >= 2;
    showA172Right.value = data.rawGlassCountVOList[30].rawCount >= 1;
    showL18Left.value = data.rawGlassCountVOList[31].rawCount >= 2;
    showL18Right.value = data.rawGlassCountVOList[31].rawCount >= 1;
    showL19Left.value = data.rawGlassCountVOList[32].rawCount >= 2;
    showL19Right.value = data.rawGlassCountVOList[32].rawCount >= 1;
    showL20Left.value = data.rawGlassCountVOList[33].rawCount >= 2;
    showL20Right.value = data.rawGlassCountVOList[33].rawCount >= 1;
  }
}
  websocketService.addListener(handleMessage)
  // 清理函数
  return () => {
    websocketService.removeListener(handleMessage)
  }
})
const emit = defineEmits(['card-click'])
const handleCardClick = (cardNumber: number) => {
  emit('card-click', cardNumber)
}
// 点击处理函数
const handleLA = async (event: MouseEvent, shelfKey: string, isRight: boolean) => {
  event.stopPropagation()
  console.log('点击架子:', shelfKey, 'isRight:', isRight, 'shelfIdMapping:', shelfIdMapping.value)
  // 检查架子数据是否存在
  if (!shelfIdMapping.value[shelfKey]) {
    ElMessage.warning('该架子数据未加载，请稍后重试')
    return
  }
  const shelfData = shelfIdMapping.value[shelfKey]
  if (!shelfData || shelfData.length < 2) {
    ElMessage.warning('该架子数据不完整')
    return
  }
  // 根据点击的是右侧还是左侧选择对应的ID
  // 右侧取 [0]，左侧取 [1]
  const detailId = isRight ? shelfData[0] : shelfData[1]
  console.log('架子', shelfKey, isRight ? '右侧' : '左侧', '选择的detailId:', detailId)
  try {
    // 调用查询接口 - 直接传递数字
    const response = await request.post("/glassStorage/rawGlassStorageDetails/queryRawDetailsById", 
      detailId
    )
    if (response.code === 200) {
       currentRowData.value = {
        ...response.data,
        slot: response.data.slot, // 确保字段名匹配
        shelf: response.data.shelf,
        rawWidth: response.data.rawWidth,
        rawHeight: response.data.rawHeight,
        rawThickness: response.data.rawThickness,
        filmsId: response.data.filmsId,
        rawMargin: response.data.rawMargin,
        remainQuantity: response.data.remainQuantity,
        manufacturer: response.data.manufacturer,
        rawNumber: response.data.rawNumber,
        shelfKey: shelfKey,
        isRightSide: isRight,
        detailId: detailId
      }
      dialogFormLA.value = true
      queryUnBind()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error('查询原片详情失败')
  }
}
  const handleDialogConfirm = async (formData: any) => {
  try {
    const response = await request.post("glassStorage/rawGlassStorageDetails/updateRawGlassDetails", formData);
    if (response.code === 200) {
      ElMessage.success(response.message);
      // 可选：刷新数据或关闭弹窗
      dialogFormLA.value = false;
      // 如果需要刷新 WebSocket 数据，可以重新初始化连接或触发刷新
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
// 查询未绑定原片详情
const queryUnBind = async () => {
  try {
    const response = await request.post("/glassStorage/rawGlassStorageDetails/queryUnBindRawGlassList", { })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataQueryUnBind.value = response.data;
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
    console.error(error);
  }
}
// 查询工位内的原片详情id
const queryRawDetailsIds = async () => {
  try {
    const response = await request.get("/glassStorage/rawGlassStorageDetails/queryRawDetailsIds")
    if (response.code == 200) {
      shelfIdMapping.value = response.data
      const shelfNumbers = Object.keys(shelfIdMapping.value)
    showL1Left.value =    shelfIdMapping.value['1'][1]
    showL1Right.value =   shelfIdMapping.value['1'][0]
    showA21Left.value =   shelfIdMapping.value['2'][1]
    showA21Right.value =  shelfIdMapping.value['2'][0]
    showA22Left.value =   shelfIdMapping.value['3'][1]
    showA22Right.value =  shelfIdMapping.value['3'][0]
    showA31Left.value =   shelfIdMapping.value['4'][1]
    showA31Right.value =  shelfIdMapping.value['4'][0]
    showA32Left.value =   shelfIdMapping.value['5'][1]
    showA32Right.value =  shelfIdMapping.value['5'][0]
    showA41Left.value =   shelfIdMapping.value['6'][1]
    showA41Right.value =  shelfIdMapping.value['6'][0]
    showA42Left.value =   shelfIdMapping.value['7'][1]
    showA42Right.value =  shelfIdMapping.value['7'][0]
    showA51Left.value =   shelfIdMapping.value['8'][1]
    showA51Right.value =  shelfIdMapping.value['8'][0]
    showA52Left.value =   shelfIdMapping.value['9'][1]
    showA52Right.value =  shelfIdMapping.value['9'][0]
    showA61Left.value =   shelfIdMapping.value['10'][1]
    showA61Right.value =  shelfIdMapping.value['10'][0]
    showA62Left.value =   shelfIdMapping.value['11'][1]
    showA62Right.value =  shelfIdMapping.value['11'][0]
    showA71Left.value =   shelfIdMapping.value['12'][1]
    showA71Right.value =  shelfIdMapping.value['12'][0]
    showA72Left.value =   shelfIdMapping.value['13'][1]
    showA72Right.value =  shelfIdMapping.value['13'][0]
    showA81Left.value =   shelfIdMapping.value['14'][1]
    showA81Right.value =  shelfIdMapping.value['14'][0]
    showA82Left.value =   shelfIdMapping.value['15'][1]
    showA82Right.value =  shelfIdMapping.value['15'][0]
    showL9Left.value =    shelfIdMapping.value['16'][1]
    showL9Right.value =   shelfIdMapping.value['16'][0]
    showL10Left.value =   shelfIdMapping.value['17'][1]
    showL10Right.value =  shelfIdMapping.value['17'][0]
    showA111Left.value =  shelfIdMapping.value['18'][1]
    showA111Right.value = shelfIdMapping.value['18'][0]
    showA112Left.value =  shelfIdMapping.value['19'][1]
    showA112Right.value = shelfIdMapping.value['19'][0]
    showA121Left.value =  shelfIdMapping.value['20'][1]
    showA121Right.value = shelfIdMapping.value['20'][0]
    showA122Left.value =  shelfIdMapping.value['21'][1]
    showA122Right.value = shelfIdMapping.value['21'][0]
    showA131Left.value =  shelfIdMapping.value['22'][1]
    showA131Right.value = shelfIdMapping.value['22'][0]
    showA132Left.value =  shelfIdMapping.value['23'][1]
    showA132Right.value = shelfIdMapping.value['23'][0]
    showA141Left.value =  shelfIdMapping.value['24'][1]
    showA141Right.value = shelfIdMapping.value['24'][0]
    showA142Left.value =  shelfIdMapping.value['25'][1]
    showA142Right.value = shelfIdMapping.value['25'][0]
    showA151Left.value =  shelfIdMapping.value['26'][1]
    showA151Right.value = shelfIdMapping.value['26'][0]
    showA152Left.value =  shelfIdMapping.value['27'][1]
    showA152Right.value = shelfIdMapping.value['27'][0]
    showA161Left.value =  shelfIdMapping.value['28'][1]
    showA161Right.value = shelfIdMapping.value['28'][0]
    showA162Left.value =  shelfIdMapping.value['29'][1]
    showA162Right.value = shelfIdMapping.value['29'][0]
    showA171Left.value =  shelfIdMapping.value['30'][1]
    showA171Right.value = shelfIdMapping.value['30'][0]
    showA172Left.value =  shelfIdMapping.value['31'][1]
    showA172Right.value = shelfIdMapping.value['31'][0]
    showL18Left.value =   shelfIdMapping.value['32'][1]
    showL18Right.value =  shelfIdMapping.value['32'][0]
    showL19Left.value =   shelfIdMapping.value['33'][1]
    showL19Right.value =  shelfIdMapping.value['33'][0]
    showL20Left.value =   shelfIdMapping.value['34'][1]
    showL20Right.value =  shelfIdMapping.value['34'][0]
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
    console.error(error);
  }
}
onMounted(() => {
  queryRawDetailsIds();
}); 
</script>
<template>
  <div class="img-dlpl">
      <div class="img-car1" style="z-index:999;top:220px;left:395px;position:absolute;"></div>
      <div class="img-lj" style="z-index:999;top:0px;left:430px;position:absolute;"></div>
      <!-- 右侧半部分 -->
      <!-- L1 -->
      <div style="width:70px;height: 20px; z-index:999;top:32px;left:240px;position:absolute;" @click="handleLA($event, '1', false)">
        <div class="LA-left" v-show="showL1Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:32px;left:318px;position:absolute;" @click="handleLA($event, '1', true)">
        <div class="LA-right" v-show="showL1Right"></div>
      </div>

      <!-- A21 -->
      <div style="width:70px;height: 20px; z-index:999;top:60px;left:240px;position:absolute; " @click="handleLA($event, '2', false)">
       <div class="LA-left" v-show="showA21Left" style="top: 12px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:72px;left:318px;position:absolute;" @click="handleLA($event, '2', true)">
      <div class="LA-right" v-show="showA21Right"></div>
     </div>
     
      <!-- A22 -->
      <div style="width:70px;height: 20px; z-index:999;top:85px;left:240px;position:absolute; " @click="handleLA($event, '3', false)">
      <div class="LA-left" v-show="showA22Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:85px;left:318px;position:absolute;" @click="handleLA($event, '3', true)">
      <div class="LA-right" v-show="showA22Right" ></div>
      </div>

      <!-- A31 -->
      <div style="width:70px;height: 20px; z-index:999;top:111px;left:240px;position:absolute; " @click="handleLA($event, '4', false)">
      <div class="LA-left" v-show="showA31Left" style="top: 13px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:124px;left:318px;position:absolute;" @click="handleLA($event, '4', true)">
      <div class="LA-right" v-show="showA31Right"></div>
      </div>

      <!-- A32 -->
      <div style="width:70px;height: 20px; z-index:999;top:139px;left:240px;position:absolute; " @click="handleLA($event, '5', false)">
      <div class="LA-left" v-show="showA32Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:139px;left:318px;position:absolute;" @click="handleLA($event, '5', true)">
      <div class="LA-right" v-show="showA32Right"></div>
      </div>

      <!-- A41 -->
      <div style="width:70px;height: 20px; z-index:999;top:170px;left:240px;position:absolute; " @click="handleLA($event, '6', false)">
      <div class="LA-left" v-show="showA41Left" style="top: 12px;"></div>
     </div>
      <div style="width:70px;height: 20px; z-index:999;top:182px;left:318px;position:absolute;" @click="handleLA($event, '6', true)">
      <div class="LA-right" v-show="showA41Right"></div>
      </div>

      <!-- A42 -->
      <div style="width:70px;height: 20px; z-index:999;top:195px;left:240px;position:absolute; " @click="handleLA($event, '7', false)">
      <div class="LA-left" v-show="showA42Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:195px;left:318px;position:absolute;" @click="handleLA($event, '7', true)">
      <div class="LA-right" v-show="showA42Right"></div>
      </div>

      <!-- A51 -->
      <div style="width:70px;height: 20px; z-index:999;top:220px;left:240px;position:absolute; " @click="handleLA($event, '8', false)">
      <div class="LA-left" v-show="showA51Left" style="top: 10px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:231px;left:318px;position:absolute;" @click="handleLA($event, '8', true)">
      <div class="LA-right" v-show="showA51Right"></div>
      </div>

      <!-- A52 -->
      <div style="width:70px;height: 20px; z-index:999;top:245px;left:240px;position:absolute; " @click="handleLA($event, '9', false)">
      <div class="LA-left" v-show="showA52Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:245px;left:318px;position:absolute;" @click="handleLA($event, '9', true)">
      <div class="LA-right" v-show="showA52Right"></div>
      </div>

      <!-- A61 -->
      <div style="width:70px;height: 20px; z-index:999;top:272px;left:240px;position:absolute; " @click="handleLA($event, '10', false)">
      <div class="LA-left" v-show="showA61Left" style="top: 12px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:284px;left:318px;position:absolute;" @click="handleLA($event, '10', true)">
      <div class="LA-right" v-show="showA61Right"></div>
      </div>

      <!-- A62 -->
      <div style="width:70px;height: 20px; z-index:999;top:299px;left:240px;position:absolute; " @click="handleLA($event, '11', false)">
      <div class="LA-left" v-show="showA62Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:299px;left:318px;position:absolute;" @click="handleLA($event, '11', true)">
      <div class="LA-right" v-show="showA62Right"></div>
      </div>

      <!-- A71 -->
      <div style="width:70px;height: 20px; z-index:999;top:329px;left:240px;position:absolute; " @click="handleLA($event, '12', false)">
      <div class="LA-left" v-show="showA71Left" style="top: 12px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:341px;left:318px;position:absolute;" @click="handleLA($event, '12', true)">
      <div class="LA-right" v-show="showA71Right"></div>
      </div>

      <!-- A72 -->
      <div style="width:70px;height: 20px; z-index:999;top:355px;left:240px;position:absolute; " @click="handleLA($event, '13', false)">
      <div class="LA-left" v-show="showA72Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:355px;left:318px;position:absolute;" @click="handleLA($event, '13', true)">
      <div class="LA-right" v-show="showA72Right"></div>
      </div>

      <!-- A81 -->
      <div style="width:70px;height: 20px; z-index:999;top:387px;left:240px;position:absolute; " @click="handleLA($event, '14', false)">
      <div class="LA-left" v-show="showA81Left" style="top: 12px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:399px;left:318px;position:absolute;" @click="handleLA($event, '14', true)">
      <div class="LA-right" v-show="showA81Right"></div>
      </div>

      <!-- A82 -->
      <div style="width:70px;height: 20px; z-index:999;top:413px;left:240px;position:absolute; " @click="handleLA($event, '15', false)">
      <div class="LA-left" v-show="showA82Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:413px;left:318px;position:absolute;" @click="handleLA($event, '15', true)">
      <div class="LA-right" v-show="showA82Right"></div>
      </div>

      <!-- L9 -->
      <div style="width:70px;height: 20px; z-index:999;top:448px;left:240px;position:absolute;" @click="handleLA($event, '16', false)">
      <div class="LA-left" v-show="showL9Left" style="top: 9px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:457px;left:318px;position:absolute;" @click="handleLA($event, '16', true)">
      <div class="LA-right" v-show="showL9Right"></div>
      </div>

      <!-- 左侧半部分 -->
      <!-- L10 -->
      <div style="width:70px;height: 20px; z-index:999;top:32px;left:57px;position:absolute;" @click="handleLA($event, '17', false)">
        <div class="LA-left" v-show="showL10Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:32px;left:133px;position:absolute;" @click="handleLA($event, '17', true)">
      <div class="LA-right" v-show="showL10Right"></div>
      </div>

      <!-- A111 -->
      <div style="width:70px;height: 20px; z-index:999;top:58px;left:57px;position:absolute; " @click="handleLA($event, '18', false)">
        <div class="LA-left" v-show="showA111Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:69px;left:133px;position:absolute;" @click="handleLA($event, '18', true)">
      <div class="LA-right" v-show="showA111Right"></div>
      </div>

      <!-- A112 -->
      <div style="width:70px;height: 20px; z-index:999;top:85px;left:57px;position:absolute; " @click="handleLA($event, '19', false)">
      <div class="LA-left" v-show="showA112Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:85px;left:133px;position:absolute;" @click="handleLA($event, '19', true)">
      <div class="LA-right" v-show="showA112Right"></div>
      </div>

      <!-- A121 -->
      <div style="width:70px;height: 20px; z-index:999;top:113px;left:57px;position:absolute; " @click="handleLA($event, '20', false)">
      <div class="LA-left" v-show="showA121Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:124px;left:133px;position:absolute;" @click="handleLA($event, '20', true)">
      <div class="LA-right" v-show="showA121Right"></div>
      </div>

      <!-- A122 -->
      <div style="width:70px;height: 20px; z-index:999;top:139px;left:57px;position:absolute; " @click="handleLA($event, '21', false)">
      <div class="LA-left" v-show="showA122Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:139px;left:133px;position:absolute;" @click="handleLA($event, '21', true)">
      <div class="LA-right" v-show="showA122Right"></div>
      </div>

      <!-- A131 -->
      <div style="width:70px;height: 20px; z-index:999;top:170px;left:57px;position:absolute; " @click="handleLA($event, '22', false)">
      <div class="LA-left" v-show="showA131Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:181px;left:133px;position:absolute;" @click="handleLA($event, '22', true)">
      <div class="LA-right" v-show="showA131Right"></div>
      </div>

      <!-- A132 -->
      <div style="width:70px;height: 20px; z-index:999;top:195px;left:57px;position:absolute; " @click="handleLA($event, '23', false)">
      <div class="LA-left" v-show="showA132Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:195px;left:133px;position:absolute;" @click="handleLA($event, '23', true)">
      <div class="LA-right" v-show="showA132Right"></div>
      </div>

      <!-- A141 -->
      <div style="width:70px;height: 20px; z-index:999;top:220px;left:57px;position:absolute; " @click="handleLA($event, '24', false)">
      <div class="LA-left" v-show="showA141Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:231px;left:133px;position:absolute;" @click="handleLA($event, '24', true)">
      <div class="LA-right" v-show="showA141Right"></div>
      </div>

      <!-- A142 -->
      <div style="width:70px;height: 20px; z-index:999;top:245px;left:57px;position:absolute; " @click="handleLA($event, '25', false)">
      <div class="LA-left" v-show="showA142Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:245px;left:133px;position:absolute;" @click="handleLA($event, '25', true)">
      <div class="LA-right" v-show="showA142Right"></div>
      </div>

      <!-- A151 -->
      <div style="width:70px;height: 20px; z-index:999;top:272px;left:57px;position:absolute; " @click="handleLA($event, '26', false)">
      <div class="LA-left" v-show="showA151Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:283px;left:133px;position:absolute;" @click="handleLA($event, '26', true)">
      <div class="LA-right" v-show="showA151Right"></div>
      </div>

      <!-- A152 -->
      <div style="width:70px;height: 20px; z-index:999;top:299px;left:57px;position:absolute; " @click="handleLA($event, '27', false)">
      <div class="LA-left" v-show="showA152Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:299px;left:133px;position:absolute;" @click="handleLA($event, '27', true)">
      <div class="LA-right" v-show="showA152Right"></div>
      </div>

      <!-- A161 -->
      <div style="width:70px;height: 20px; z-index:999;top:330px;left:57px;position:absolute; " @click="handleLA($event, '28', false)">
      <div class="LA-left" v-show="showA161Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:341px;left:133px;position:absolute;" @click="handleLA($event, '28', true)">
      <div class="LA-right" v-show="showA161Right"></div>
      </div>

      <!-- A162 -->
      <div style="width:70px;height: 20px; z-index:999;top:355px;left:57px;position:absolute; " @click="handleLA($event, '29', false)">
      <div class="LA-left" v-show="showA162Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:355px;left:133px;position:absolute;" @click="handleLA($event, '29', true)">
      <div class="LA-right" v-show="showA162Right"></div>
      </div>

      <!-- A171 -->
      <div style="width:70px;height: 20px; z-index:999;top:386px;left:57px;position:absolute; " @click="handleLA($event, '30', false)">
      <div class="LA-left" v-show="showA171Left" style="top: 11px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:397px;left:133px;position:absolute;" @click="handleLA($event, '30', true)">
      <div class="LA-right" v-show="showA171Right"></div>
      </div>

      <!-- A172 -->
      <div style="width:70px;height: 20px; z-index:999;top:411px;left:57px;position:absolute; " @click="handleLA($event, '31', false)">
      <div class="LA-left" v-show="showA172Left"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:411px;left:133px;position:absolute;" @click="handleLA($event, '31', true)">
      <div class="LA-right" v-show="showA172Right"></div>
      </div>
      <!-- L18 -->
      <div style="width:70px;height: 20px; z-index:999;top:448px;left:57px;position:absolute;" @click="handleLA($event, '32', false)">
      <div class="LA-left" v-show="showL18Left" style="top: 9px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:457px;left:133px;position:absolute;" @click="handleLA($event, '32', true)">
      <div class="LA-right" v-show="showL18Right"></div>
      </div>
      <!-- 最下方两个小车 -->
      <!-- L19 -->
      <div style="width:70px;height: 20px; z-index:999;top:445px;left:453px;position:absolute;" @click="handleLA($event, '33', false)">
      <div class="LA-left" v-show="showL19Left" style="top: 9px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:454px;left:528px;position:absolute;" @click="handleLA($event, '33', true)">
      <div class="LA-right" v-show="showL19Right"></div>
      </div>
      <!-- L20 -->
      <div style="width:70px;height: 20px; z-index:999;top:445px;left:661px;position:absolute; " @click="handleLA($event, '34', false)">
      <div class="LA-left" v-show="showL20Left" style="top: 9px;"></div>
      </div>
      <div style="width:70px;height: 20px; z-index:999;top:454px;left:736px;position:absolute;" @click="handleLA($event, '34', true)">
      <div class="LA-right" v-show="showL20Right"></div>
      </div>
    </div>
    <!-- 图片点击弹窗 -->
   <rawGlassDialogLY
      v-model:visible="dialogFormLA"
      :row-data="currentRowData"
      :table-data-query-un-bind="tableDataQueryUnBind"
      :is-view-mode="true"
      @confirm="handleDialogConfirm"
      @add-row="handleAddRow"
      @delete-row="deleteWareHousing"
      @close="dialogFormLA = false"
    />
</template>
<style scoped>
.img-dlpl{
  margin-left: 330px;
  margin-top: 50px;
  background-image:url('/ccly.png');
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 500px;
  width: 850px;
  max-width: 100%;
  background-size: 850px 500px;
  overflow: hidden;
  position:relative;
  margin-left: 28%
}
.img-car1{
  display: flex;
  background-image:url('/car.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 70px;
  width: 243px;
  max-width: 100%;
  background-size: 243px 70px;
  overflow: hidden;
  position:relative
}
.img-lj{
  display: flex;
  background-image:url('/lj.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 495px;
  width: 180px;
  max-width: 100%;
  background-size: 180px 495px;
  overflow: hidden;
  position:relative
}
.LA-left {
  width: 70px;
  height: 8px;
  background-color: rgb(51, 126, 204);
  position: absolute;
}
.LA-right {
  width: 70px;
  height: 8px;
  background-color: rgb(51, 126, 204);
  position: absolute;
}
.dialog-container {
  display: flex;
  height: 70vh; /* 根据需要调整高度 */
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
</style>