<script lang="ts" setup>
import rawGlassDialog from '../rawGlassDialog.vue'
import request from "@/utils/request"
import {defineEmits, defineProps, onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
// import { closeWebSocket, initializeWebSocket } from '@/utils/WebSocketService'
import WebSocketStorage from '@/utils/WebSocketStorage'
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
const rawGlassCountVOList = ref([]); // 存储 WebSocket 返回的数据
const props = defineProps({
  cardStates: {
    type: Object,
    required: true
  }
})
const websocketService = WebSocketStorage.getInstance()
// 查询未绑定原片详情
const queryUnBind = async () => {
  try {
    const response = await request.post("/glassStorage/rawGlassStorageDetails/queryUnBindRawGlassList", {})
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataQueryUnBind.value = response.data;
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
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
});
const emit = defineEmits(['card-click'])
const handleCardClick = (cardNumber: number) => {
  emit('card-click', cardNumber)
}
const handleLA = (event: MouseEvent, rowIndex: number, isLeft: boolean) => {
  event.stopPropagation()
  const rowData = rawGlassCountVOList.value[rowIndex]
  if (!rowData) return
  if (rowData.rawCount === 2) {
    return
  }
  currentRowData.value = rowData
  dialogFormLA.value = true
  queryUnBind()
}
</script>
<template>
  <div class="img-dlpl">
    <div class="img-car1" style="z-index:999;top:150px;left:662px;position:absolute;"></div>
    <div class="img-lj" style="z-index:999;top:0px;left:430px;position:absolute;"></div>
    <!-- 右侧半部分 -->
    <div style="width:150px;height: 20px; z-index:999;top:32px;left:240px;position:absolute; "
         @click="handleLA($event, 0, true)">
      <div v-show="showL1Left" class="LA-left"></div>
    </div>
    <div v-show="showL1Right" class="LA-right" style="z-index:999;top:32px;left:316px;position:absolute;"
         @click="handleLA($event, 0, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:60px;left:240px;position:absolute; "
         @click="handleLA($event, 1, true)">
      <div v-show="showA21Left" class="LA-left" style="top: 12px;"></div>
    </div>
    <div v-show="showA21Right" class="LA-right" style="z-index:999;top:72px;left:316px;position:absolute;"
         @click="handleLA($event, 1, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:85px;left:240px;position:absolute; "
         @click="handleLA($event, 2, true)">
      <div v-show="showA22Left" class="LA-left"></div>
    </div>
    <div v-show="showA22Right" class="LA-right" style="z-index:999;top:85px;left:316px;position:absolute"
         @click="handleLA($event, 2, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:111px;left:240px;position:absolute; "
         @click="handleLA($event, 3, true)">
      <div v-show="showA31Left" class="LA-left" style="top: 13px;"></div>
    </div>
    <div v-show="showA31Right" class="LA-right" style="z-index:999;top:124px;left:316px;position:absolute;"
         @click="handleLA($event, 3, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:139px;left:240px;position:absolute; "
         @click="handleLA($event, 4, true)">
      <div v-show="showA32Left" class="LA-left"></div>
    </div>
    <div v-show="showA32Right" class="LA-right" style="z-index:999;top:139px;left:316px;position:absolute;"
         @click="handleLA($event, 4, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:170px;left:240px;position:absolute; "
         @click="handleLA($event, 5, true)">
      <div v-show="showA41Left" class="LA-left" style="top: 12px;"></div>
    </div>
    <div v-show="showA41Right" class="LA-right" style="z-index:999;top:182px;left:316px;position:absolute;"
         @click="handleLA($event, 5, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:195px;left:240px;position:absolute; "
         @click="handleLA($event, 6, true)">
      <div v-show="showA42Left" class="LA-left"></div>
    </div>
    <div v-show="showA42Right" class="LA-right" style="z-index:999;top:195px;left:316px;position:absolute;"
         @click="handleLA($event, 6, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:220px;left:240px;position:absolute; "
         @click="handleLA($event, 7, true)">
      <div v-show="showA51Left" class="LA-left" style="top: 10px;"></div>
    </div>
    <div v-show="showA51Right" class="LA-right" style="z-index:999;top:231px;left:316px;position:absolute;"
         @click="handleLA($event, 7, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:245px;left:240px;position:absolute; "
         @click="handleLA($event, 8, true)">
      <div v-show="showA52Left" class="LA-left"></div>
    </div>
    <div v-show="showA52Right" class="LA-right" style="z-index:999;top:245px;left:316px;position:absolute;"
         @click="handleLA($event, 8, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:272px;left:240px;position:absolute; "
         @click="handleLA($event, 9, true)">
      <div v-show="showA61Left" class="LA-left" style="top: 12px;"></div>
    </div>
    <div v-show="showA61Right" class="LA-right" style="z-index:999;top:284px;left:316px;position:absolute;"
         @click="handleLA($event, 9, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:299px;left:240px;position:absolute; "
         @click="handleLA($event, 10, true)">
      <div v-show="showA62Left" class="LA-left"></div>
    </div>
    <div v-show="showA62Right" class="LA-right" style="z-index:999;top:299px;left:316px;position:absolute;"
         @click="handleLA($event, 10, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:329px;left:240px;position:absolute; "
         @click="handleLA($event, 11, true)">
      <div v-show="showA71Left" class="LA-left" style="top: 12px;"></div>
    </div>
    <div v-show="showA71Right" class="LA-right" style="z-index:999;top:341px;left:316px;position:absolute;"
         @click="handleLA($event, 11, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:355px;left:240px;position:absolute; "
         @click="handleLA($event, 12, true)">
      <div v-show="showA72Left" class="LA-left"></div>
    </div>
    <div v-show="showA72Right" class="LA-right" style="z-index:999;top:355px;left:316px;position:absolute;"
         @click="handleLA($event, 12, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:387px;left:240px;position:absolute; "
         @click="handleLA($event, 13, true)">
      <div v-show="showA81Left" class="LA-left" style="top: 12px;"></div>
    </div>
    <div v-show="showA81Right" class="LA-right" style="z-index:999;top:399px;left:316px;position:absolute;"
         @click="handleLA($event, 13, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:413px;left:240px;position:absolute; "
         @click="handleLA($event, 14, true)">
      <div v-show="showA82Left" class="LA-left"></div>
    </div>
    <div v-show="showA82Right" class="LA-right" style="z-index:999;top:413px;left:316px;position:absolute;"
         @click="handleLA($event, 14, false)"></div>
    <div style="width:150px;height: 20px; z-index:999;top:448px;left:240px;position:absolute; "
         @click="handleLA($event, 15, true)">
      <div v-show="showL9Left" class="LA-left" style="top: 9px;"></div>
    </div>
    <div v-show="showL9Right" class="LA-right" style="z-index:999;top:457px;left:316px;position:absolute;"
         @click="handleLA($event, 15, false)"></div>
    <!-- 左侧半部分 -->
    <div style="width:150px;height: 20px; z-index:999;top:32px;left:57px;position:absolute; "
         @click="handleLA($event, 16, true)">
      <div v-show="showL10Left" class="LA-left"></div>
    </div>
    <div v-show="showL10Right" class="LA-right" style="z-index:999;top:32px;left:133px;position:absolute;"
         @click="handleLA($event, 16, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:58px;left:57px;position:absolute; "
         @click="handleLA($event, 17, true)">
      <div v-show="showA111Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA111Right" class="LA-right" style="z-index:999;top:69px;left:133px;position:absolute;"
         @click="handleLA($event, 17, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:85px;left:57px;position:absolute; "
         @click="handleLA($event, 18, true)">
      <div v-show="showA112Left" class="LA-left"></div>
    </div>
    <div v-show="showA112Right" class="LA-right" style="z-index:999;top:85px;left:133px;position:absolute;"
         @click="handleLA($event, 18, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:113px;left:57px;position:absolute; "
         @click="handleLA($event, 19, true)">
      <div v-show="showA121Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA121Right" class="LA-right" style="z-index:999;top:124px;left:133px;position:absolute;"
         @click="handleLA($event, 19, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:139px;left:57px;position:absolute; "
         @click="handleLA($event, 20, true)">
      <div v-show="showA122Left" class="LA-left"></div>
    </div>
    <div v-show="showA122Right" class="LA-right" style="z-index:999;top:139px;left:133px;position:absolute;"
         @click="handleLA($event, 20, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:170px;left:57px;position:absolute; "
         @click="handleLA($event, 21, true)">
      <div v-show="showA131Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA131Right" class="LA-right" style="z-index:999;top:181px;left:133px;position:absolute;"
         @click="handleLA($event, 21, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:195px;left:57px;position:absolute; "
         @click="handleLA($event, 22, true)">
      <div v-show="showA132Left" class="LA-left"></div>
    </div>
    <div v-show="showA132Right" class="LA-right" style="z-index:999;top:195px;left:133px;position:absolute;"
         @click="handleLA($event, 22, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:220px;left:57px;position:absolute; "
         @click="handleLA($event, 23, true)">
      <div v-show="showA141Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA141Right" class="LA-right" style="z-index:999;top:231px;left:133px;position:absolute;"
         @click="handleLA($event, 23, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:245px;left:57px;position:absolute; "
         @click="handleLA($event, 24, true)">
      <div v-show="showA142Left" class="LA-left"></div>
    </div>
    <div v-show="showA142Right" class="LA-right" style="z-index:999;top:245px;left:133px;position:absolute;"
         @click="handleLA($event, 24, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:272px;left:57px;position:absolute; "
         @click="handleLA($event, 25, true)">
      <div v-show="showA151Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA151Right" class="LA-right" style="z-index:999;top:283px;left:133px;position:absolute;"
         @click="handleLA($event, 25, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:299px;left:57px;position:absolute; "
         @click="handleLA($event, 26, true)">
      <div v-show="showA152Left" class="LA-left"></div>
    </div>
    <div v-show="showA152Right" class="LA-right" style="z-index:999;top:299px;left:133px;position:absolute;"
         @click="handleLA($event, 26, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:330px;left:57px;position:absolute; "
         @click="handleLA($event, 27, true)">
      <div v-show="showA161Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA161Right" class="LA-right" style="z-index:999;top:341px;left:133px;position:absolute;"
         @click="handleLA($event, 27, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:355px;left:57px;position:absolute; "
         @click="handleLA($event, 28, true)">
      <div v-show="showA162Left" class="LA-left"></div>
    </div>
    <div v-show="showA162Right" class="LA-right" style="z-index:999;top:355px;left:133px;position:absolute;"
         @click="handleLA($event, 28, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:386px;left:57px;position:absolute; "
         @click="handleLA($event, 29, true)">
      <div v-show="showA171Left" class="LA-left" style="top: 11px;"></div>
    </div>
    <div v-show="showA171Right" class="LA-right" style="z-index:999;top:397px;left:133px;position:absolute;"
         @click="handleLA($event, 29, false)"></div>
    <div style="width:145px;height: 20px; z-index:999;top:411px;left:57px;position:absolute; "
         @click="handleLA($event, 30, true)">
      <div v-show="showA172Left" class="LA-left"></div>
    </div>
    <div v-show="showA172Right" class="LA-right" style="z-index:999;top:411px;left:133px;position:absolute;"
         @click="handleLA($event, 30, false)"></div>
    <div style="width:150px;height: 20px; z-index:999;top:448px;left:57px;position:absolute; "
         @click="handleLA($event, 31, true)">
      <div v-show="showL18Left" class="LA-left" style="top: 9px;"></div>
    </div>
    <div v-show="showL18Right" class="LA-right" style="z-index:999;top:457px;left:133px;position:absolute;"
         @click="handleLA($event, 31, false)"></div>
    <!-- 最下方两个小车 -->
    <div style="width:150px;height: 20px; z-index:999;top:445px;left:453px;position:absolute; "
         @click="handleLA($event, 32, true)">
      <div v-show="showL19Left" class="LA-left" style="top: 9px;"></div>
    </div>
    <div v-show="showL19Right" class="LA-right" style="z-index:999;top:454px;left:528px;position:absolute;"
         @click="handleLA($event, 32, false)"></div>
    <div style="width:150px;height: 20px; z-index:999;top:445px;left:661px;position:absolute; "
         @click="handleLA($event, 33, true)">
      <div v-show="showL20Left" class="LA-left" style="top: 9px;"></div>
    </div>
    <div v-show="showL20Right" class="LA-right" style="z-index:999;top:454px;left:736px;position:absolute;"
         @click="handleLA($event, 33, false)"></div>
  </div>
  <!-- 图片点击弹窗 -->
  <rawGlassDialog
      v-model:visible="dialogFormLA"
      :is-view-mode="true"
      :row-data="currentRowData"
      :table-data-query-un-bind="tableDataQueryUnBind"
      @close="dialogFormLA = false"
      @confirm="handleDialogConfirm"
      @add-row="handleAddRow"
      @delete-row="deleteWareHousing"
  />
</template>
<style scoped>
.img-dlpl {
  margin-left: 330px;
  margin-top: 110px;
  background-image: url('/ypcc.png');
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 450px;
  width: 800px;
  max-width: 100%;
  background-size: 800px 450px;
  overflow: hidden;
  position: relative;
  margin-left: 30%
}
.img-car1 {
  display: flex;
  background-image: url('/C1.png');
  position: absolute;
  background-repeat: no-repeat;
  background-attachment: local;
  min-height: 150px;
  width: 70px;
  max-width: 100%;
  background-size: 70px 150px;
  overflow: hidden;
  position: relative
}
.dialog-container {
  display: flex;
  height: 70vh;
  /* 根据需要调整高度 */
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