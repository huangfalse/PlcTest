<script setup>
import request from "@/utils/request"
import dayjs from 'dayjs';
import * as echarts from 'echarts';
import {useRouter} from "vue-router"
import {computed, nextTick, onBeforeUnmount, onMounted, onUnmounted, ref, watch} from "vue";
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {ElMessage} from 'element-plus'
import {useI18n} from 'vue-i18n'

const { t } = useI18n()
const load = ref(true)
const edg = ref(true)
const temp = ref(true)
const hollow = ref(true)
const router = useRouter()
const dialogProcessCard = ref(false)
const dialogDetailsFilm = ref(false)
const iframeUrl1 = ref('')
const iframe1 = ref(false)
const iframeUrl2 = ref('')
const iframe2 = ref(false)
const iframeUrl3 = ref('')
const iframe3 = ref(false)
const iframeUrl4 = ref('')
const iframe4 = ref(false)
const iframeUrl5 = ref('')
const iframe5 = ref(false)
const iframeUrl6 = ref('')
const iframe6 = ref(false)
const iframeUrl7 = ref('')
const iframe7 = ref(false)
const iframeUrl8 = ref('')
const iframe8 = ref(false)
const iframeUrl9 = ref('')
const iframe9 = ref(false)
const iframeUrl10 = ref('')
const iframe10 = ref(false)
const iframeUrl11 = ref('')
const iframe11 = ref(false)
const tableDatad = ref([])
const tableData = ref([])
const dynamicColumns = ref([])
const edgOneTasks = ref([])
const edgTwoTasks = ref([])
const engineeringOne = ref([])
const engineeringTwo = ref([])
const temperingTaskType = ref([])
const temperingGlassInfoList = ref([])
const temperingGlassInfoInList = ref([])
const bigStorageCageUsage = ref([])
const hollowBigStorageCageUsage = ref([])
const productionVO = ref([])
const orderDTOS = ref([])
const chartRef = ref(null)
const chartRef2 = ref(null)
const chartLoad = ref(null)
const chartEdg = ref(null)
const chartTemp = ref(null)
const chartHollow = ref(null)
const numBoxes = ref(0)
const numBoxes2 = ref(0)
const numBoxes3 = ref(0)
const numBoxes4 = ref(0)
const boxRefs = []
const boxRefs2 = []
const boxRefs3 = []
const boxRefs4 = []
const rawGlassStorageDetailList = ref([])
let categories = ref([]) // 获取日期作为 x 轴
let totalAreaOutOne = ref([])
let countIn = ref([])
let totalAreaOutTwo = ref([])
let totalAreaOut = ref([])
let countOut = ref([])
let hollowTotalAreaOutTwo = ref([])
let hollowCountOutOne = ref([])
let countOutTwo = ref([])
let hollowCountOutTwo = ref([])
let hollowTotalAreaOutOne = ref([])
let totalAreaIn = ref([])
let countOutOne = ref([])
let boxes = ref([])
let boxes2 = ref([])
let boxes3 = ref([])
let boxes4 = ref([])
let socket1 = null
let myChartLoad = null
let myChartEdg = null
let myChartTemp = null
let myChartHollow = null
let socket2 = null
let socket3 = null
let firstTime = null
let secondTime = null
let socket4 = null
let socket5 = null
let myChart = null
let myChart2 = null
let language = ref(localStorage.getItem('lang') || 'zh')
let socket = null
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/glassStorage/api/talk/largenScreen`;
const handleMessage = (data) => {
  rawGlassStorageDetailList.value = data.rawGlassStorageDetailList[0]
  orderDTOS.value = data.orderDTOS[0]
};
const socketUrl1 = `ws://${window.ipConfig.serverUrl}/api/cacheGlass/api/talk/largenScreen`;
const handleMessage1 = (data) => {
  edgOneTasks.value = data.edgOneTasks[0];
  edgTwoTasks.value = data.edgTwoTasks[0];
  engineeringOne.value = data.engineeringOne[0];
  engineeringTwo.value = data.engineeringTwo[0];
  if (numBoxes.value != edgOneTasks.value.length) {
    numBoxes.value = edgOneTasks.value.length;
    initBoxes(boxes, numBoxes, 'firstup');
  }
  if (numBoxes2.value != edgTwoTasks.value.length) {
    numBoxes2.value = edgTwoTasks.value.length;
    initBoxes(boxes2, numBoxes2, 'firstup');
  }
  timeAxisCreate("切割当天运行情况", chartLoad.value, myChartLoad, data.loadRunTimes[0]);
  timeAxisCreate("磨边当天运行情况", chartEdg.value, myChartEdg, data.loadRunTimes[0]);
};
const socketUrl2 = `ws://${window.ipConfig.serverUrl}/api/temperingGlass/api/talk/largenScreen`;
const handleMessage2 = (data) => {
  temperingTaskType.value = data.temperingTaskType[0];
  temperingGlassInfoList.value = data.temperingGlassInfoList[0];
  if (numBoxes3.value != temperingTaskType.value) {
    numBoxes3.value = temperingTaskType.value;
    initBoxes(boxes3, numBoxes3, 'secondleft');
  }
  if (temperingGlassInfoList.value >= 1) {
    if (numBoxes4.value != 1) {
      numBoxes4.value = 1;
      initBoxes(boxes4, numBoxes4, 'right');
    }
  } else {
    if (numBoxes4.value != 0) {
      numBoxes4.value = 0;
      initBoxes(boxes4, numBoxes4, 'right');
    }
  }
  temperingGlassInfoInList.value = data.temperingGlassInfoInList[0];
};
const socketUrl3 = `ws://${window.ipConfig.serverUrl}/api/cacheVerticalGlass/api/talk/largenScreen`;
const handleMessage3 = (data) => {
  bigStorageCageUsage.value = data.bigStorageCageUsage[0];
  timeAxisCreate("钢化当天运行情况", chartTemp.value, myChartTemp, data.tempRunTimes[0]);
};
const formatToTimeString = (datetime) => {
  const date = new Date(datetime);
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const seconds = date.getSeconds().toString().padStart(2, '0');
  return `${hours}:${minutes}:${seconds}`;
}
const timeAxisCreate = (title, chartDom, mychart, RunTimes) => {
  if (!chartDom) {
    console.error('图表容器未找到');
    return;
  }
  if (!mychart) {
    mychart = echarts.init(chartDom);
  }
  if (RunTimes.length == 0) {
    firstTime = "00-00-00 00:00:00";
    secondTime = "00-00-00 00:00:00";
  } else {
    firstTime = RunTimes[0].startTimestamp;
    secondTime = RunTimes[0].endTimestamp;
  }
  let datas = ref([])
  RunTimes.forEach((item, index) => {
    datas.value.push([item.firstTimestamp, -1, formatToTimeString(item.firstTimestamp)])
    datas.value.push([item.secondTimestamp, 1, formatToTimeString(item.secondTimestamp)])
  })
  // 构建线段数组
  const segments = RunTimes.map(item => ({
    type: 'line',
    symbol: 'none',
    data: [
      [item.firstTimestamp, 0],
      [item.secondTimestamp, 0]
    ],
    lineStyle: {
      color: 'red', // 支持不同颜色
      width: 4
    },
    z: 1
  }));
  const optionOne = {
    title: {
      text: title,
      left: 'center',
      top: '5%',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'line' },
      backgroundColor: 'rgba(50,50,50,0.7)',
      textStyle: { color: '#fff' }
    },
    grid: {
      top: '20%',
      left: '5%',
      right: '5%',
      bottom: '15%'
    },
    xAxis: {
      type: 'time',
      axisLabel: {
        formatter: function (value) {
          const date = new Date(value);
          return (
            date.getHours().toString().padStart(2, '0') + ':' +
            date.getMinutes().toString().padStart(2, '0') + ':' +
            date.getSeconds().toString().padStart(2, '0')
          );
        },
        fontSize: 10,
        color: '#666'
      },
      splitLine: { show: false },
      axisLine: { lineStyle: { color: '#aaa' } }
    },
    yAxis: {
      type: 'value',
      min: -1,
      max: 1,
      show: false
    },
    series: [
      // 主时间线（蓝色渐变，稍微加粗）
      {
        type: 'line',
        symbol: 'none',
        data: [
          [firstTime, 0],
          [secondTime, 0]
        ],
        lineStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
            { offset: 0, color: '#4facfe' },
            { offset: 1, color: '#00f2fe' }
          ]),
          width: 5,
          shadowColor: 'rgba(0,0,0,0.2)',
          shadowBlur: 8
        },
        z: 0
      },
      // 小段时间线（统一柔和的黄色）
      ...segments.map(seg => ({
        ...seg,
        lineStyle: {
          color: '#ffcc00',
          width: 3,
          shadowColor: 'rgba(255,204,0,0.5)',
          shadowBlur: 5
        }
      })),
      // 卡片事件（优化样式）
      {
        type: 'custom',
        renderItem: function (params, api) {
          const x = api.coord([api.value(0), api.value(1)])[0];
          const yBase = api.coord([api.value(0), 0])[1];
          const yOffset = api.value(1) > 0 ? -70 : 30;
          const cardWidth = 40;
          const cardHeight = 22;
          const cardX = x;
          const cardY = yBase + yOffset;
          const text = api.value(2);
          return {
            type: 'group',
            children: [
              {
                type: 'line',
                shape: {
                  x1: x,
                  y1: yBase,
                  x2: x,
                  y2: cardY + (api.value(1) > 0 ? cardHeight : 0)
                },
                style: {
                  stroke: '#888',
                  lineWidth: 1.5,
                  lineDash: [4, 2]
                }
              },
              {
                type: 'rect',
                shape: {
                  x: cardX - cardWidth / 2,
                  y: cardY,
                  width: cardWidth,
                  height: cardHeight,
                  r: 6 // 圆角
                },
                style: {
                  fill: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#ffffff' },
                    { offset: 1, color: '#e0f7fa' }
                  ]),
                  stroke: '#00acc1',
                  lineWidth: 1,
                  shadowColor: '#ccc',
                  shadowBlur: 6
                }
              },
              {
                type: 'text',
                style: {
                  text: text,
                  x: cardX,
                  y: cardY + cardHeight / 2,
                  textAlign: 'center',
                  textVerticalAlign: 'middle',
                  font: '12px sans-serif',
                  fill: '#006064'
                }
              }
            ]
          };
        },
        encode: {
          x: 0
        },
        data: datas.value,
        z: 10
      }
    ]
  };
  mychart.setOption(optionOne);
};
const socketUrl4 = `ws://${window.ipConfig.serverUrl}/api/hollowGlass/api/talk/largenScreen`;
const handleMessage4 = (data) => {
  hollowBigStorageCageUsage.value = data.hollowBigStorageCageUsage[0];
  timeAxisCreate("中空当天运行情况", chartHollow.value, myChartHollow, data.hollowRunTimes[0]);
};
const socketUrl5 = `ws://${window.ipConfig.serverUrl}/api/cacheGlass/api/talk/largenScreenProduction`;
const handleMessage5 = (data) => {
  productionVO.value = data.productionVO[0];
  // 将原始数据转换为 ECharts 图表所需格式
  categories = productionVO.value.map(item => item.date); // 获取日期作为 x 轴
  totalAreaOutOne = productionVO.value.map(item => item.totalAreaOutOne);
  countIn = productionVO.value.map(item => item.countIn);
  totalAreaOutTwo = productionVO.value.map(item => item.totalAreaOutTwo);
  totalAreaOut = productionVO.value.map(item => item.totalAreaOut);
  countOut = productionVO.value.map(item => item.countOut);
  hollowTotalAreaOutTwo = productionVO.value.map(item => item.hollowTotalAreaOutTwo);
  hollowCountOutOne = productionVO.value.map(item => item.hollowCountOutOne);
  countOutTwo = productionVO.value.map(item => item.countOutTwo);
  hollowCountOutTwo = productionVO.value.map(item => item.hollowCountOutTwo);
  hollowTotalAreaOutOne = productionVO.value.map(item => item.hollowTotalAreaOutOne);
  totalAreaIn = productionVO.value.map(item => item.totalAreaIn);
  countOutOne = productionVO.value.map(item => item.countOutOne);
  //图表
  nextTick() // 确保 DOM 加载完成
  const chartDom = chartRef.value;
  if (!chartDom) {
    console.error('图表容器未找到');
    return;
  }
  if (!myChart) {
    myChart = echarts.init(chartDom);
  }
  const option = {
    title: { text: '' },
    tooltip: { trigger: 'axis' },
    legend: {
      data: [
        t('large.countOutOne'),
        t('large.totalAreaOutOne'),
        t('large.countOutTwo'),
        t('large.totalAreaOutTwo'),
        t('large.countIn'),
        t('large.totalAreaIn')
      ]
    },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    toolbox: { feature: { saveAsImage: {} } },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: categories // 这是日期数据
    },
    yAxis: { type: 'value' },
    series: [
      { name: t('large.countOutOne'), type: 'bar', data: countOutOne },
      { name: t('large.totalAreaOutOne'), type: 'bar', data: totalAreaOutOne },
      { name: t('large.countOutTwo'), type: 'bar', data: countOutTwo },
      { name: t('large.totalAreaOutTwo'), type: 'bar', data: totalAreaOutTwo },
      { name: t('large.countIn'), type: 'bar', data: countIn },
      { name: t('large.totalAreaIn'), type: 'bar', data: totalAreaIn }
    ]
  };
  myChart.setOption(option);
  const chartDom2 = chartRef2.value;
  if (!chartDom2) {
    console.error('图表容器未找到');
    return;
  }
  if (!myChart2) {
    myChart2 = echarts.init(chartDom2);
  }
  const option2 = {
    title: { text: '' },
    tooltip: { trigger: 'axis' },
    legend: {
      data: [
        t('large.totalAreaOut'),
        t('large.temperedSteelQuantity'),
        t('large.hollowTotalAreaOutOne'),
        t('large.hollowCountOutOne'),
        t('large.hollowCountOutTwo'),
        t('large.hollowTotalAreaOutTwo')
      ]
    },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    toolbox: { feature: { saveAsImage: {} } },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: categories // 这是日期数据
    },
    yAxis: { type: 'value' },
    series: [
      { name: t('large.totalAreaOut'), type: 'bar', data: totalAreaOut },
      { name: t('large.countOut'), type: 'bar', data: countOut },
      { name: t('large.hollowTotalAreaOutOne'), type: 'bar', data: hollowTotalAreaOutOne },
      { name: t('large.hollowCountOutOne'), type: 'bar', data: hollowCountOutOne },
      { name: t('large.hollowCountOutTwo'), type: 'bar', data: hollowCountOutTwo },
      { name: t('large.hollowTotalAreaOutTwo'), type: 'bar', data: hollowTotalAreaOutTwo }
    ]
  };
  myChart2.setOption(option2);
};
// 将原始数据转换为 ECharts 图表所需格式
const patternUsage = async () => {
  try {
    const response = await request.post('/glassStorage/rawGlassStorageDetails/patternUsage?width=' + ptnusage.value.width + '&height=' + ptnusage.value.height + '&thickness=' + ptnusage.value.thickness + '&films=' + ptnusage.value.films);
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDatad.value = response.data;
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
    console.error(error);
  }
}
const exportToExcel = async () => {
  try {
    window.open('http://'+{WebSocketHost}+':10011/largenScreen/exportDailyProduction', '_blank');
  }
  catch (error) {
    console.error(error);
  }
}
// 流程卡进度
const flowCardDetail = async (orderId) => {
  dialogProcessCard.value = true;
  try {
    const response = await request.post('/glassStorage/flow_card/flowCardReportForms?orderId=' + orderId);
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableData.value = response.data;
      // 解析数据并生成动态列
      if (response.data.length > 0) {
        // 解析第一个对象的 reportWorkQuantity，确定动态列
        const firstItem = response.data[0];
        const reportData = JSON.parse(firstItem.reportWorkQuantity || "{}");
        dynamicColumns.value = Object.keys(reportData).map((key) => ({
          prop: key,
          label: key,
        }));
        // 解析数据并将 JSON 字符串转换为对象
        tableData.value = response.data.map((item) => {
          const reportData = JSON.parse(item.reportWorkQuantity || "{}");
          return {
            ...item,
            ...reportData, // 把 JSON 字段合并到平铺对象
          };
        });
      }
    } else {
      ElMessage.error(response.message);
    }
  }
  catch (error) {
    console.error(error);
  }
}
const ptnusage = ref({
  width: '',
  height: '',
  thickness: '',
  films: ''
})
// 点击下方弹窗
const handlehistorical = (row) => {
  dialogDetailsFilm.value = true;
  patternUsage()
};
const cleanupWebSocketData = (completeCleanup) => {
  if (completeCleanup) {
    // 完全清理
    rawGlassStorageDetailList.value = [];
    orderDTOS.value = [];
    // ...其他数组
  } else {
    // 部分清理 - 保留最近数据
    const MAX_ITEMS = 50;
    rawGlassStorageDetailList.value = rawGlassStorageDetailList.value.slice(-MAX_ITEMS);
    orderDTOS.value = orderDTOS.value.slice(-MAX_ITEMS);
  }
  // 清理图表
  [myChart, myChart2, myChartLoad, myChartEdg, myChartTemp, myChartHollow].forEach(chart => {
    if (chart) {
      chart.clear();
    }
  });
};
// 在组件挂载时设置默认时间范围
onMounted(async () => {
  socket = initializeWebSocket(socketUrl, handleMessage);
  socket1 = initializeWebSocket(socketUrl1, handleMessage1);
  socket2 = initializeWebSocket(socketUrl2, handleMessage2);
  socket3 = initializeWebSocket(socketUrl3, handleMessage3);
  socket4 = initializeWebSocket(socketUrl4, handleMessage4);
  socket5 = initializeWebSocket(socketUrl5, handleMessage5);
  const endTime = dayjs().startOf('minute'); // 当前时间，精确到分钟
  const startTime = endTime.subtract(1, 'day').startOf('minute'); // 当前时间的前一天，精确到分钟
  const cleanupInterval = setInterval(() => {
    cleanupWebSocketData(true); // 完全清理
  }, 3000);
  // 每次收到消息时检查数据量
  const originalHandleMessage = handleMessage;
  handleMessage = (data) => {
    originalHandleMessage(data);
    cleanupWebSocketData(false); // 部分清理
  };
  onUnmounted(() => {
    clearInterval(cleanupInterval);
    cleanupWebSocketData(true); // 组件卸载时完全清理
  });
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
    closeWebSocket(socket1);
    closeWebSocket(socket2);
    closeWebSocket(socket3);
    closeWebSocket(socket4);
    closeWebSocket(socket5);
  }
});
const speed = 0.1;
const delayFrames = 600;
const maxCoords = {
  box1: { x: 250, y: -165 },
  box2: { x: 190, y: -75 },
  box3: { x: 95, y: -85 },
  box4: { x: 60, y: 0 },
};
let rafId = null;
// ✅ 初始化 boxes，去除 style，保留 el
const initBoxes = (arr, countRef, direction) => {
  const count = countRef.value;
  const existing = arr.value;
  for (let i = 0; i < count; i++) {
    if (!existing[i]) {
      existing.push({
        x: 0,
        y: 0,
        direction,
        delay: i * delayFrames,
        frameCount: 0,
        el: null
      });
    } else {
    }
  }
  if (existing.length > count) {
    existing.length = count;
  }
};
// ✅ 动画函数
const animateBox = (box, maxX, maxY) => {
  if (box.delay > 0) {
    box.delay--;
    return;
  }
  switch (box.direction) {
    case 'firstup':
      box.y -= speed;
      if (box.y <= maxY) box.direction = 'firstleft';
      break;
    case 'firstleft':
      box.x -= speed;
      if (box.x <= -maxX) {
        box.x = 0;
        box.y = 0;
        box.direction = 'firstup';
      }
      break;
    case 'secondup':
      box.y -= speed;
      if (box.y <= maxY) {
        box.x = 0;
        box.y = 0;
        box.direction = 'secondleft';
      }
      break;
    case 'secondleft':
      box.x -= speed;
      if (box.x <= -maxX) {
        box.direction = 'secondup';
      }
      break;
    case 'right':
      box.x += speed;
      if (box.x >= maxX) {
        box.x = 0;
        box.y = 0;
      }
      break;
  }
  box.frameCount++;
  if (box.frameCount % 2 === 0 && box.el) {
    box.el.style.top = `${box.y}px`;
    if (box.x > 0) {
      
      if(box.direction=="right"){
        box.el.style.right = `-${box.x}px`;
        
      }else{
        box.el.style.right = `${box.x}px`;
      }
      box.el.style.left = '';
    } else {
      box.el.style.left = `${box.x}px`;
      box.el.style.right = '';
    }
  }
};
const animate = () => {
  boxes.value.forEach(box => animateBox(box, maxCoords.box1.x, maxCoords.box1.y));
  boxes2.value.forEach(box => animateBox(box, maxCoords.box2.x, maxCoords.box2.y));
  boxes3.value.forEach(box => animateBox(box, maxCoords.box3.x, maxCoords.box3.y));
  boxes4.value.forEach(box => animateBox(box, maxCoords.box4.x, maxCoords.box4.y));
  rafId = requestAnimationFrame(animate);
};
const boxStart = () => {
  initBoxes(boxes, numBoxes, 'firstup');
  initBoxes(boxes2, numBoxes2, 'firstup');
  initBoxes(boxes3, numBoxes3, 'secondleft');
  initBoxes(boxes4, numBoxes4, 'right');
};
watch(
  () => boxes.value,
  async (newVal) => {
    await nextTick();
    newVal.forEach((box, i) => {
      box.el = boxRefs[i];
    });
  },
  { deep: true, immediate: true }
);
watch(
  () => boxes2.value,
  async (newVal) => {
    await nextTick();
    newVal.forEach((box, i) => {
      box.el = boxRefs2[i];
    });
  },
  { deep: true, immediate: true }
);
watch(
  () => boxes3.value,
  async (newVal) => {
    await nextTick();
    newVal.forEach((box, i) => {
      box.el = boxRefs3[i];
    });
  },
  { deep: true, immediate: true }
);
watch(
  () => boxes4.value,
  async (newVal) => {
    await nextTick();
    newVal.forEach((box, i) => {
      box.el = boxRefs4[i];
    });
  },
  { deep: true, immediate: true }
);
onMounted(() => {
  boxStart();
  animate();
});
onBeforeUnmount(() => {
  cancelAnimationFrame(rafId);
  rafId = null;
  boxes.value.length = 0;
  boxes2.value.length = 0;
  boxes3.value.length = 0;
  boxes4.value.length = 0;
});
onUnmounted(() => {
  if (rafId) {
    cancelAnimationFrame(rafId); // 停止动画，防止内存泄漏
  }
});
//嵌入历史任务
const handlehistorical1 = () => {
  iframe1.value = true;
  iframeUrl1.value = `${window.location.origin}/#/GlassStorage/rawhistory`;
};
const handlehistorical2 = () => {
  iframe2.value = true;
  iframeUrl2.value = `${window.location.origin}/#/Returns/upreturnhistory`;;
};
const handlehistorical3 = () => {
  iframe3.value = true;
  iframeUrl3.value = `${window.location.origin}/#/Returns/upreturnhistory2`;
};
const handlehistorical4 = () => {
  iframe4.value = true;
  iframeUrl4.value = `${window.location.origin}/#/Caching/cachingbeforehistory`;
};
const handlehistorical5 = () => {
  iframe5.value = true;
  iframeUrl5.value = `${window.location.origin}/#/Caching/cachingunhistory`;
};
const handlehistorical6 = () => {
  iframe6.value = true;
  iframeUrl6.value = `${window.location.origin}/#/Slicecage/slicecagehistory`;
};
const handlehistorical7 = () => {
  iframe7.value = true;
  iframeUrl7.value = `${window.location.origin}/#/hollow/hellowquiphistory`;
};
const handlehistorical8 = () => {
  iframe8.value = true;
  iframeUrl8.value = `${window.location.origin}/#/hollow/hellowquiptwohistory`;
};
const handlehistorical9 = () => {
  iframe9.value = true;
  iframeUrl9.value = `${window.location.origin}/#/hollow/hellowslicecagehistory`;
};
const handlehistorical10 = () => {
  iframe10.value = true;
  iframeUrl10.value = `${window.location.origin}/#/largescreendisplay/Statistics`;
};
const handlehistorical11 = () => {
  iframe11.value = true;
  iframeUrl11.value = `${window.location.origin}/#/largescreendisplay/ProductionStatistics`;
};
const transposedData = computed(() => {
  const keys = Object.keys(fieldNames); // 按 fieldNames 的顺序
  return keys.map(key => {
    const row = { name: fieldNames[key] || key };
    productionVO.value.forEach((item, index) => {
      row[`value${index}`] = item[key] ?? ''; // 处理可能不存在的键
    });
    return row;
  });
});
const fieldNames = {
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
  hollowTotalAreaOutTwo: t('large.hollowTotalAreaOutTwo'),
};
</script>
<template>
  <div style="height: 500px;">
    <el-dialog v-model="iframe1" top="5vh" width="95%" @close="iframeUrl1 = ''">
      <iframe :src="iframeUrl1" marginwidth="2000px" marginheight="2000px" width="100%" height="700px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe2" top="5vh" width="95%" @close="iframeUrl2 = ''">
      <iframe :src="iframeUrl2" marginwidth="2000px" marginheight="2000px" width="100%" height="700px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe3" top="5vh" width="95%" @close="iframeUrl3 = ''">
      <iframe :src="iframeUrl3" marginwidth="2000px" marginheight="2000px" width="100%" height="700"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe4" top="5vh" width="95%" @close="iframeUrl4 = ''">
      <iframe :src="iframeUrl4" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe5" top="5vh" width="95%" @close="iframeUrl5 = ''">
      <iframe :src="iframeUrl5" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe6" top="5vh" width="95%" @close="iframeUrl6 = ''">
      <iframe :src="iframeUrl6" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe7" top="5vh" width="95%" @close="iframeUrl7 = ''">
      <iframe :src="iframeUrl7" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe8" top="5vh" width="95%" @close="iframeUrl8 = ''">
      <iframe :src="iframeUrl8" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe9" top="5vh" width="95%" @close="iframeUrl9 = ''">
      <iframe :src="iframeUrl9" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe10" top="5vh" width="95%" @close="iframeUrl10 = ''">
      <iframe :src="iframeUrl10" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <el-dialog v-model="iframe11" top="5vh" width="95%" @close="iframeUrl11 = ''">
      <iframe :src="iframeUrl11" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
        frameborder="0"></iframe>
    </el-dialog>
    <div class="awatch" style="display: flex;">
      <!-- 生产统计 -->
      <div style="height: 100px;width: 25%;float: right;">
        <div id="ss" style="width: 100%; display: flex; justify-content: center;">
          <div ref="chartRef" style="width: 600px; height: 400px;" @dblclick="handlehistorical10()"></div>
        </div>
        <div style="width: 100%;height: 245px;display: flex;">
          <div ref="chartLoad" style="width: 100%;"></div>
        </div>
        <div style="width: 100%;height: 245px;display: flex;">
          <div ref="chartEdg" style="width: 100%;"></div>
        </div>
      </div>
      <div style="width: 50%;">
        <div class="img-screen" alt="Screen">
          <div>
            <el-button type="primary" style="top:40px;left:670px;position: absolute;" @click="exportToExcel()">{{
              $t('large.productionStatistics') }}</el-button>
          </div>
          <!-- 钢化半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 315px;left: 130px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countOut : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaOut : 0 }}m²</div>
          </div>
          <!-- 中空一线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 235px;left: 450px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].hollowCountOutOne : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].hollowTotalAreaOutOne : 0 }}m²</div>
          </div>
          <!-- 中空二线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 130px;left: 450px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].hollowCountOutTwo : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].hollowTotalAreaOutTwo : 0 }}m²</div>
          </div>
          <!-- 切割二线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 542px;left: 410px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countOutOne : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaOutOne : 0 }}m²</div>
          </div>
          <!-- 切割一线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 490px;left: 410px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countOutTwo : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaOutTwo : 0 }}m²</div>
          </div>
          <!-- 磨边一线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 369px;left: 450px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countOutOne : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaOutOne : 0 }}m²</div>
          </div>
          <!-- 磨边二线半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 410px;left: 450px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countOutTwo : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaOutTwo : 0 }}m²</div>
          </div>
          <!-- 钢化前大理{{ $t('large.slice') }}半透明色块 -->
          <div
            style="width: 80px;height: 40px;top: 380px;left: 290px;position: absolute;background-color: rgba(0, 0, 0, 0.5);color: white;font-size: 12px;z-index: 999;">
            <div>{{ $t('large.quantity') }}:{{ productionVO.length > 0 ? productionVO[6].countIn : 0 }}{{
              $t('large.slice') }}</div>
            <div>{{ $t('large.are') }}:{{ productionVO.length > 0 ? productionVO[6].totalAreaIn : 0 }}m²</div>
          </div>
          <!-- 钢化色块 -->
          <div v-if="temperingGlassInfoInList > 0" class="tempering"
            style="width: 44px;height: 19px;top: 284px;left: 178px;position: absolute;background-color: #911005;"></div>
          <div v-if="temperingGlassInfoList > 0" class="tempering"
            style="width: 53px;height: 22px;top: 282px;left: 226px;position: absolute;background-color: #911005;"></div>
          <div v-if="temperingGlassInfoList > 1" class="tempering"
            style="width: 53px;height: 22px;top: 282px;left: 282px;position: absolute;background-color: #911005;"></div>
          <!-- 切割台色块 -->
          <div v-if="engineeringOne.length > 1" class="slicing"
            style="width: 45px;height: 20px;top: 550px;left: 496px;position: absolute;background-color: #911005;"></div>
          <div v-if="engineeringTwo.length > 1" class="slicing"
            style="width: 45px;height: 20px;top: 500px;left: 496px;position: absolute;background-color: #911005;"></div>
          <!-- 原片仓储色块 -->
          <div class="raw" style="width: 27px;height: 30px;top: 497px;left: 296px;position: absolute;">
            <div v-for="(item, index) in rawGlassStorageDetailList.slice(0, 7)" :key="index"
              :style='"width: " + (item.remainQuantity / 1.8) + "px;height: 5.6px;background-color: #911005;"'></div>
          </div>
          <div class="raw" style="width: 27px;height: 45px;top: 535px;left: 296px;position: absolute;">
            <div v-for="(item, index) in rawGlassStorageDetailList.slice(8, 17)" :key="index"
              :style='"width: " + (item.remainQuantity / 1.8) + "px;height: 5.6px;background-color: #911005;"'></div>
          </div>
          <div class="raw" style="width: 27px;height: 13px;top: 598px;left: 296px;position: absolute;">
            <div v-for="(item, index) in rawGlassStorageDetailList.slice(18, 20)" :key="index"
              :style='"width: " + (item.remainQuantity / 1.8) + "px;height: 5.6px;background-color: #911005;"'></div>
          </div>
          <div class="raw" style="width: 27px;height: 23px;top: 580px;left: 365px;position: absolute;">
            <div v-for="(item, index) in rawGlassStorageDetailList.slice(21, 25)" :key="index"
              :style='"width: " + (item.remainQuantity / 1.8) + "px;height: 5.6px;background-color: #911005;"'></div>
          </div>
          <!-- 磨边色块 -->
          <div class="container"
            style="position: relative;width: 15px;height: 15px;top: 500px;left: 638px;position: absolute;">
            <div v-for="(box, i) in boxes2" :key="`b1-${i}`" class="moving-rect" :ref="el => boxRefs2[i] = el" />
          </div>
          <div class="container"
            style="position: relative;width: 15px;height: 15px;top: 548px;left: 690px;position: absolute;">
            <div v-for="(box, i) in boxes" :key="`b1-${i}`" class="moving-rect" :ref="el => boxRefs[i] = el" />
          </div>
          <!-- 钢化运动色块 -->
          <div class="container"
            style="position: relative;width: 15px;height: 15px;top: 370px;left: 195px;position: absolute;">
            <div v-for="(box, i) in boxes3" :key="`b1-${i}`" class="moving-rect" :ref="el => boxRefs3[i] = el" />
          </div>
          <div class="container"
            style="position: relative;width: 15px;height: 15px;top: 284px;left: 489px;position: absolute;">
            <div v-for="(box, i) in boxes4" :key="`b1-${i}`" class="moving-rect" :ref="el => boxRefs4[i] = el" />
          </div>
          <!-- 钢化前大理片色块 -->
          <div class="container" v-for="(item, index) in bigStorageCageUsage.slice(0, 1)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 374px;left: 309px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in bigStorageCageUsage.slice(1, 2)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 390px;left: 309px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in bigStorageCageUsage.slice(2, 3)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 406px;left: 309px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in bigStorageCageUsage.slice(3, 4)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 422px;left: 309px;position: absolute;background-color: #911005;"'>
          </div>
          <!-- 中空前大理片色块 -->
          <div class="container" v-for="(item, index) in hollowBigStorageCageUsage.slice(0, 1)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 265px;left: 662px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in hollowBigStorageCageUsage.slice(1, 2)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 281px;left: 662px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in hollowBigStorageCageUsage.slice(2, 3)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 297px;left: 662px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in hollowBigStorageCageUsage.slice(3, 4)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 313px;left: 662px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="container" v-for="(item, index) in hollowBigStorageCageUsage.slice(4, 5)" :key="index"
            :style='"position: relative;width: " + (37 * item.percentage / 100) + "px;height: 13px;top: 329px;left: 662px;position: absolute;background-color: #911005;"'>
          </div>
          <div class="clickable-area" @click="handlehistorical"
            style="position: relative;width: 108px;height: 134px;top: 478px;left: 290px;"></div>
        </div>
        <div style="height: 175px;width: 100%;float: right;">
          <el-table height="175" ref="table" :data="orderDTOS"
            :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
            <el-table-column prop="orderId" align="center" :label="$t('large.orderId')" min-width="50" />
            <el-table-column prop="customerName" align="center" :show-overflow-tooltip="true"
              :label="$t('large.customerName')" min-width="90" />
            <el-table-column prop="project" align="center" :label="$t('large.projectName')" min-width="50" />
            <el-table-column prop="area" align="center" :label="$t('large.are')" min-width="50" />
            <el-table-column prop="quantity" align="center" :label="$t('large.quantity')" min-width="50" />
            <el-table-column prop="percent" align="center" :label="$t('large.progressPercentage')" min-width="50" />
            <el-table-column align="center" :label="$t('large.operate')" min-width="50">
              <template #default="scope">
                <el-button type="text" style="margin-left: 10px;margin-bottom: 10px;"
                  @click="flowCardDetail(scope.row.orderId)">{{ $t('') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div style="height: 100px;width: 25%;float: right;">
        <div id="ss" style="width: 100%; display: flex; justify-content: center;">
          <div ref="chartRef2" style="width: 600px; height: 400px;" @dblclick="handlehistorical10()"></div>
        </div>
        <div style="width: 100%;height: 245px;display: flex;">
          <div ref="chartTemp" style="width: 100%;"></div>
        </div>
        <div style="width: 100%;height: 245px;display: flex;">
          <div ref="chartHollow" style="width: 100%;"></div>
        </div>
      </div>
    </div>
  </div>
  <!-- 原片使用详情 -->
  <el-dialog v-model="dialogDetailsFilm" top="10vh" width="90%">
    <div style="display: flex;">
      <el-input v-model="ptnusage.width" style="margin-left: 10px;margin-bottom: 10px;width: 270px;" clearable
        :placeholder="$t('screendisplay.pwidth')" />
      <el-input v-model="ptnusage.height" style="margin-left: 10px;margin-bottom: 10px;width: 270px;" clearable
        :placeholder="$t('screendisplay.pheight')" />
      <el-input v-model="ptnusage.thickness" style="margin-left: 10px;margin-bottom: 10px;width: 270px;" clearable
        :placeholder="$t('searchOrder.thicknessEnter')" />
      <el-input v-model="ptnusage.films" style="margin-left: 10px;margin-bottom: 10px;width: 270px;" clearable
        :placeholder="$t('film.filmsEnter')" />
      <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="patternUsage()">{{
        $t('report.inquire')
      }}</el-button>
    </div>
    <el-table ref="table" style="margin-top: 20px;height: 650px;" :data="tableDatad"
      :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
      <el-table-column prop="patternWidth" align="center" :label="$t('basicData.width')" min-width="80" />
      <el-table-column prop="patternHeight" align="center" :label="$t('basicData.height')" min-width="80" />
      <el-table-column prop="patternThickness" align="center" :label="$t('basicData.thickness')" min-width="80" />
      <el-table-column prop="filmsId" align="center" :label="$t('basicData.coatingtypes')" min-width="120" />
      <el-table-column prop="totalCount" align="center" :label="$t('searchOrder.totalQuantity')" min-width="120" />
      <el-table-column prop="count" align="center" :label="$t('searchOrder.cagesNumber')" min-width="120" />
      <el-table-column prop="finishCount" align="center" :label="$t('screendisplay.upnumber')" min-width="120" />
      <el-table-column prop="damageCount" align="center" :label="$t('hollow.damageNumber')" min-width="120" />
    </el-table>
  </el-dialog>
  <!-- 流程卡进度 -->
  <el-dialog v-model="dialogProcessCard" top="5vh" width="85%">
    <el-table height="650" ref="table" :data="tableData"
      :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
      <el-table-column prop="processId" fixed align="center" :label="$t('searchOrder.processCardNumber')" min-width="150" />
      <el-table-column prop="glassChild" align="center" :label="$t('hollow.productName')" min-width="150" />
      <el-table-column prop="orderNumber" align="center" :label="$t('large.orderNumber')" min-width="110" />
      <el-table-column prop="technologyNumber" align="center" :label="$t('large.slicemarker')" min-width="110" />
      <el-table-column prop="width" align="center" :label="$t('large.width')" min-width="110" />
      <el-table-column prop="height" align="center" :label="$t('large.height')" min-width="110" />
      <el-table-column prop="quantity" align="center" :label="$t('large.quantity')" min-width="110" />
      <el-table-column prop="brokenNum" align="center" :label="$t('large.quantityBroken')" min-width="100" />
      <el-table-column prop="inventory" align="center" :label="$t('large.inventory')" min-width="100" />
      <el-table-column prop="inventoryArea" align="center" :label="$t('large.inventoryArea')" min-width="100" />
      <el-table-column prop="shippedQuantity" align="center" :label="$t('large.shippedQuantity')" min-width="110" />
      <!-- 动态生成的 reportWorkQuantity 列 -->
      <el-table-column v-for="column in dynamicColumns" :key="column.prop" :prop="column.prop" :label="column.label"
        align="center" min-width="110" />
    </el-table>
  </el-dialog>
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
  height: 460px;
}
.img-screen {
  position: relative;
  background-image: url('/screen.png');
  width: 795px;
  height: 700px;
  background-size: contain;
  background-repeat: no-repeat;
}
.clickable-area {
  cursor: pointer;
  /* 指示这是一个可点击的区域 */
  text-align: center;
  /* 如果需要，可以在div中添加文本并居中 */
  line-height: 95px;
  /* 如果需要，使文本垂直居中 */
}
.box {
  transition: transform 0.016s linear;
}
.lipiana {
  width: 15px;
  height: 9px;
  background-color: #529b2e;
  top: 190px;
  right: 530px;
  transform: translateX(-50%);
  animation: move-lipiana 1s infinite;
}
@keyframes move-lipiana {
  0% {
    right: 530px;
  }
  100% {
    right: calc(100% - 350px);
  }
}
.moving-rect {
  width: 15px;
  height: 15px;
  background-color: #911005;
  position: absolute;
}
</style>