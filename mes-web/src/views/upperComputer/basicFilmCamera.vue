<script lang="ts" setup>
import request from "@/utils/request"
import {onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue"
import {useRouter} from "vue-router"
import {ElMessage, ElMessageBox} from 'element-plus'
import {useI18n} from 'vue-i18n'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService'

const {t} = useI18n()
const router = useRouter()
const selectValuesa = reactive([])
const selectedProjectNo = ref('')
const iframeUrl = ref('')
// const filmRemove = ref('')
const stationCell = ref('1');
const dialogFormVisible = ref(false)
const dialogStartfilm = ref(false)
const blindPause = ref(false)
const blindEndproject = ref(false)
const blindStorageHint = ref(false)
const blindHistoricaltasks = ref(false)
const ReportData = ref(true)
const canSelectProject = ref(true)
const canSelectStartloading = ref(true)
const canSelectStop = ref(false)
const hideFooter = ref(false)
const isLacking = ref(false)
const currentPage = ref(1)
const totalRecords = ref(0)
const isOpenPaging = ref(false); // 是否加载中（控制分页是否禁用）
const pageSize = ref(10); // 每页条数
const dataEngineering = ref([])
const beginEngineering = ref(null);
const tableDataEngineering = ref([])
const tableDataLoadStationDetails = ref([])

const tableDataEndproject = ref([])
const tableDataStorageInfo = ref([])
const tableDatWorkstation = ref<any[]>([])
const markingMachineStatus = ref('#911005')
const cuttingMachineStatus = ref('#911005')
const cuttingMachineStatusColor = ref('#911005')
const isLackingStatusColor = ref('#911005')
const upstatus = ref(t('Mounting.loaderOnlineStatus'))
const titleSelectJson = ref({
  engineerId: [],
})
let socket = null;
const props = defineProps({
  showOutboundButton: {
    type: Boolean,
    default: true // 默认显示
  },
  // 线路
  workLine: {
    type: Number,
    required: true
  },
  // 架子号
  shelfNumber: {
    type: Number,
    required: true
  },
  // 下拉选线路
  loadGlass: {
    type: String,
    required: true
  },
  // websockect
  webSocketPath: {
    type: String,
    required: true
  },
  // 历史任务页面
  historicalTaskUrl: {
    type: String,
    required: true
  },
  // 上片机图片
  loaderPicture: {
    type: String,
    required: true
  },
});
// 更健壮的图片路径处理
const getImageUrl = (name: string) => {
  return `/${name}.png`;
};
// 历史任务
const handleHistorical = (row) => {
  blindHistoricaltasks.value = true;
  iframeUrl.value = `${window.location.origin}/#/upperComputer/${props.historicalTaskUrl}`;
  console.log(iframeUrl.value);
};
// 打标机就绪状态
const confirmMarkingMachine = () => {
  markingMachineStatus.value = 'green';
};
// 切割机就绪状态
const confirmCuttingMachine = () => {
  cuttingMachineStatus.value = 'green';
};
request.post("/loadGlass/loadJob/queryAllMessageByStationCell", {
  stationCell: props.workLine
}).then((response) => {
  if (response.code == 200) {
    console.log(response.data);
    cuttingMachineStatusColor.value = response.data.onlineState === 1 ? 'green' : '#911005';
    if (response.data.engineering != null) {
      dataEngineering.value=response.data.engineering;
      tableDataEngineering.value = response.data.engineeringRawQueueList;
      canSelectProject.value = false;
      canSelectStartloading.value = false;
      canSelectStop.value = true;
    }
    if (response.data.loadStationDetailsList != null) {
      tableDataLoadStationDetails.value = response.data.loadStationDetailsList;
    }

  } else {
    canSelectProject.value = true;
    canSelectStartloading.value = true;
    canSelectStop.value = false;
  }
})
// 初始化WebSocket
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/loadGlass/webSocket/${props.webSocketPath}`;
const handleMessage = (data: any) => {
  if (data.engineering != null) {
    window.localStorage.setItem('engineeringIda', data.engineering.engineerId)
  }
  dataEngineering.value=data.engineering;
  tableDataEngineering.value = data.engineeringRawQueueList;
  tableDataLoadStationDetails.value = data.loadStationDetailsList;
  // 工程表
  if (data.engineering != null) {
    canSelectProject.value = false; // 工程表不为空，工程按钮不可选
    canSelectStartloading.value = false;
    canSelectStop.value = true;
    markingMachineStatus.value = 'green';
    cuttingMachineStatus.value = 'green';
  } else {
    canSelectProject.value = true;
    canSelectStartloading.value = true;
    canSelectStop.value = false;
    tableDataEngineering.value = [];
    window.localStorage.setItem('engineeringIda', '')
  }

};
// 选择预览工程
const selectproject = () => {
  dialogFormVisible.value = true;
  selectGong();
};
const selectGong = async () => {
  try {
    const response = await request.post('/loadGlass/engineering/queryNotFinishEngineerIdList');
    if (response.code == 200) {
      titleSelectJson.value.engineerId = response.data.map((projectNo, index) => ({
        id: index,       // 用索引或可以生成唯一ID
        projectNo: projectNo
      }));
    }
  } catch (error) {
    console.error(error);
  }
};
// 确认选择预览工程
const handlesure = async () => {
  if (!selectedProjectNo.value) {
    ElMessage.error(t('Mounting.selectProject'))
    return
  }
  try {
    const response = await request.post('/loadGlass/engineering/queryEngineeringRawQueueList', {
      engineerId: selectedProjectNo.value,
      // filmRemove: parseInt(filmRemove.value, 10),
    })
    beginEngineering.value=selectedProjectNo.value;
    window.localStorage.setItem('engineeringIda', selectedProjectNo.value);
    if (response.code == 200) {
      ElMessage.success(response.message);
      closeWebSocket(socket);
      tableDataEngineering.value = response.data;
      dialogFormVisible.value = false;
      markingMachineStatus.value = '#911005';
      cuttingMachineStatus.value = '#911005';
      ReportData.value = false;
      //window.localStorage.setItem('engineeringId', response.data[0].engineeringId);
      window.localStorage.setItem('engineeringId', selectedProjectNo.value);
      // selectedProjectNo.value = '';
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// 切换状态
const toggleState = async (row: any) => {
  row.state = (row.state + 1) % 5; // 0→1→2→3→4→0
  try {
    const response = await request.post('/loadGlass/engineeringRawQueue/updateEngineeringRawQueue', {
      id: row.id,
      state: row.state
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
};
// 开始上片
const handleStartloading = async(row) => {
  dialogStartfilm.value = true;
  hideFooter.value=true;
  try {
    //获取工程单号
    let engineeringId = selectedProjectNo.value;
    const response = await request.post('glassStorage/rawGlassStorageDetails/queryEngineeringDetails', {
      engineeringId: engineeringId
    })
    if (response.code == 200) {
      tableDataStorageInfo.value=response.data;
      //判断是否缺片
      isLacking.value=tableDataStorageInfo.value.some(item => {
        return item && item.shortageQuantity > 0;
      });
      if(isLacking.value){
        isLackingStatusColor.value='#911005';
      }else{
        isLackingStatusColor.value='green';
      }

      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
};
// 查看缺片信息
const storageInfo = async () => {
  dialogStartfilm.value = true;
  hideFooter.value=false;
  const response = await request.post('glassStorage/rawGlassStorageDetails/queryEngineeringDetails', {
    engineeringId: null
  })
  if (response.code == 200) {
    tableDataStorageInfo.value = response.data;
    //判断是否缺片
    console.log(tableDataStorageInfo.value);
    isLacking.value=tableDataStorageInfo.value.some(item => {
      return item && item.shortageQuantity > 0;
    });
    if(isLacking.value){
      isLackingStatusColor.value='#911005';
    }else{
      isLackingStatusColor.value='green';
    }
  }
}
// 确认开始上片
const handleon = async () => {
  dialogStartfilm.value = false;

  //获取工程单号
  let engineeringId = beginEngineering.value;
  if (markingMachineStatus.value === 'green' && cuttingMachineStatus.value === 'green') {
    try {
      const response = await request.post('/loadGlass/engineering/startTask', {
        workLine: props.workLine,
        engineerId: engineeringId,
        state: 1
      })
      if (response.code == 200) {
        ElMessage.success(response.message);
        canSelectProject.value = false;
        canSelectStartloading.value = false;
        canSelectStop.value = true;
        socket = initializeWebSocket(socketUrl, handleMessage);
        ReportData.value = true;
      } else {
        ElMessage.error(response.message);
      }
    } catch (error) {
      console.error(error);
    }
  } else if (markingMachineStatus.value === '#911005' || cuttingMachineStatus.value === '#911005') {
    ElMessage.warning(t('Mounting.confirmMachine'));
  }
}

// 暂停
const handleStop = (row) => {
  blindPause.value = true;
};
// 确认暂停
const handleup = async () => {
  try {
    let engineeringId = dataEngineering.value["engineerId"];
    if (engineeringId !== '') {
      const response = await request.post('/loadGlass/engineering/pauseTask', {
        workLine: props.workLine,
        engineerId: engineeringId
      })
      if (response.code == 200) {
        ElMessage.success(response.message);
        blindPause.value = false;
        markingMachineStatus.value = '#911005';
        cuttingMachineStatus.value = '#911005';
      } else {
        ElMessage.error(response.message);
      }
    } else {
      ElMessage({
        type: 'info',
        message: t('Mounting.projectNotEmpty'),
      })
    }
  } catch (error) {
    console.error(error);
  }
  window.location.reload();
}
// 破损
const opendilapidation = async (row) => {
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
      const response = await request.post('/loadGlass/damage/saveDamage', {
        glassId: row.glassId,
        line: props.workLine,
        workingProcedure: `上片`,
        remark: "上片人工点击破损"
      })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
    console.error('发生错误:', error);
  }
};
const deleteTask = () => {
  blindEndproject.value = true;
  queryUnreported()
};
// 结束工程
const deleteMakeTask = async () => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('Mounting.endProjectWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      let engineeringId = dataEngineering.value["engineerId"];
      if (engineeringId !== '') {
        const response = await request.post('/loadGlass/engineering/finishTask', {
          engineerId: engineeringId,
          workLine: props.workLine
        })
        if (response.code == 200) {
          ElMessage.success(response.message);
          canSelectProject.value = true;
          canSelectStartloading.value = true;
          canSelectStop.value = false;
          blindEndproject.value = false;
          markingMachineStatus.value = '#911005';
          cuttingMachineStatus.value = '#911005';
          window.location.reload();
        } else {
          ElMessage.error(response.message);
        }
      }
    }
  } catch (error) {
    console.error(error);
  }
};
const queryUnreported = async () => {
  try {
    let SettingsPageNo = 1;
    let engineeringId = dataEngineering.value["engineerId"];
    const response = await request.post('/loadGlass/glassInfo/queryNotReport', {
      engineerId: engineeringId,
      pageNo: SettingsPageNo,
      pageSize: pageSize.value
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataEndproject.value = response.data.records;
      totalRecords.value = response.data.total || 0; // 计算总页数|| 0;
    }
  } catch (error) {
  }
};
// 页码变化时触发
const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage.value)
  historicalTwo(currentPage.value);
};
const historicalTwo = async (page) => {
  try {
    let engineeringId = dataEngineering.value["engineerId"];
    let page = currentPage.value;
    const response = await request.post("/loadGlass/glassInfo/queryNotReport", {
      engineerId: engineeringId,
      pageNo: page,
      pageSize: pageSize.value,
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableDataEndproject.value = response.data.records
      totalRecords.value = response.data.total || 0;
      console.log("  :" + totalRecords.value)
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}
// 根据状态返回不同的Tag类型（可选）
const getStateTagType = (state: number) => {
  const tagTypes = {
    0: 'info',
    1: 'success',
    2: 'warning',
    3: 'danger',
    4: 'primary'
  };
  return tagTypes[state];
};
// 根据状态返回显示文本（可选）
const getStateLabel = (state: number) => {
  const stateLabels = {
    0: t('Mounting.notScheduled'),//未调度
    1: t('Mounting.schedulingComplete'),//调度完成
    2: t('Mounting.loadingComplete'),//上片完成
    3: t('Mounting.sent'),//已发送
    4: t('Mounting.cuttingCompleted')//切割完成
  };
  return stateLabels[state];
};
// 格式化后端时间并计算一周前的时间
const defaultTime = ref<[Date, Date]>([new Date(), new Date()]);
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

// 生命周期钩子
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
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
  <div style="height: 500px;">
    <div>
      <div id="dotClass">
        <div id="infoContent" style="min-width: 1200px">
          <div class="float-left">
            <div class="float-left">{{ $t('Mounting.markingMachineReadiness') }}</div>
            <i :style="{ marginTop: '4px', backgroundColor: markingMachineStatus, width: '18px', height: '18px', borderRadius: '50%', display: 'inline-block'}"></i>
            <el-button style="margin-left: 30px;margin-top: -12px;" @click="confirmMarkingMachine">
              {{ $t('Mounting.confirm') }}
            </el-button>
          </div>
          <div class="float-left">
            <div class="float-left" style="margin-left: 70px;">{{ $t('Mounting.cutterReadiness') }}</div>
            <i :style="{ marginTop: '4px', backgroundColor: cuttingMachineStatus, width: '18px', height: '18px', borderRadius: '50%', display: 'inline-block' }"></i>
            <el-button style="margin-left: 30px;margin-top: -12px;" @click="confirmCuttingMachine">
              {{ $t('Mounting.confirm') }}
            </el-button>
          </div>
          <div class="float-left" v-if="showOutboundButton">
            <div class="float-left" style="margin-left: 70px;">{{ $t('workOrder.prompt') }}：</div>
            <i :style="{ marginTop: '4px', backgroundColor: isLackingStatusColor, width: '18px', height: '18px', borderRadius: '50%', display: 'inline-block' }"></i>
            <el-button style="margin-left: 30px;margin-top: -12px;" @click="storageInfo">{{ $t('Mounting.confirm') }}
            </el-button>
          </div>
          <div class="float-left">
            <div class="float-left" style="margin-left: 70px;">{{ upstatus }}</div>
            <i :style="{ marginTop: '4px', backgroundColor: cuttingMachineStatusColor, width: '18px', height: '18px', borderRadius: '50%', display: 'inline-block' }"></i>
          </div>
        </div>
        <!-- 线路显示 -->
        <div style="float: right;margin-right: 30px;margin-top: -12px;">
          <el-icon>
            <Reading/>
          </el-icon>
          <div style="margin-left: 30px; margin-top: -30px;font-size: large;">{{
              $t(`Mounting.${loadGlass}LoadingLine`)
            }}
          </div>
        </div>

        <div style="clear: both;"></div>
      </div>
      <el-button :disabled="!canSelectProject" style="margin-top: 5px;margin-left: 15px;" type="primary"
                 @click="selectproject">{{ $t('Mounting.selectPreviewProject') }}
      </el-button>
      <el-button :disabled="!canSelectStartloading" style="margin-top: 5px;margin-left: 20px;" type="success"
                 @click="handleStartloading">{{ $t('Mounting.startLoading') }}
      </el-button>
      <el-button :disabled="!canSelectStop" style="margin-top: 5px;margin-left: 20px;" id="searchButton" type="warning"
                 @click="handleStop">{{ $t('Mounting.paused') }}
      </el-button>
      <el-button :disabled="!canSelectStop" style="margin-top: 5px;margin-left: 20px;" id="searchButton" type="danger"
                 @click="deleteTask">{{ $t('Mounting.endProject') }}
      </el-button>
      <el-button style="margin-top: 5px;margin-left: 20px;" id="searchButton" type="info" @click="handleHistorical">{{
          $t('Mounting.historicalTasks')
        }}
      </el-button>
      <el-card style="flex: 1;margin-left: 10px;margin-top: 15px;">
        <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
          <el-table ref="table" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }" height="300"
                    :data="tableDataEngineering">
            <el-table-column prop="rawSequence" :label="$t('Mounting.serialNumber')" align="center"/>
            <el-table-column prop="engineeringId" :label="$t('Mounting.projectNumber')" width="120" align="center"/>
            <el-table-column prop="rawGlassWidth" :label="$t('Mounting.originalWidth')" width="200" align="center"/>
            <el-table-column prop="rawGlassHeight" :label="$t('Mounting.originalHeight')" align="center"/>
            <el-table-column prop="rawGlassThickness" :label="$t('Mounting.thickness')" align="center"/>
            <el-table-column prop="filmsId" :label="$t('searchOrder.membraneSystem')" align="center"/>
            <!-- <el-table-column prop="createTime" align="center" :label="$t('Mounting.creationTime')" width="160"/> -->
            <!-- <el-table-column prop="updateTime" align="center" :label="$t('searchOrder.updateTime')" width="160"/> -->
            <el-table-column align="center" :label="$t('Mounting.state')" min-width="80" prop="state">
              <template #default="scope">
                <el-tag :type="getStateTagType(scope.row.state)"
                        @click="canSelectProject ? toggleState(scope.row) : null"
                        :style="canSelectProject ? 'cursor: pointer;' : 'cursor: not-allowed;'">
                  {{ getStateLabel(scope.row.state) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
      <div style="margin-left:5%;margin-top: 2%;">
        <div style="float: left;width: 50%">
          <img :src="getImageUrl(loaderPicture)" alt="" style="max-width: 100%;max-height: 100%;margin-left: 1%;margin-top: 1%;">
        </div>
        <!-- 表格区域 -->
        <div style="float: right;width: 48%; margin-right: 20px;"> <!-- 使用flex布局让表格和图片并排显示 -->
          <el-table ref="table" :data="tableDataLoadStationDetails" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129'}"
                    style="margin-top: 20px;width:100%">
            <!-- 表格列定义，根据你的业务需求修改 -->
            <el-table-column :label="$t('report.serialNumber')" align="center" fixed prop="deviceId"/>
            <el-table-column :label="$t('Mounting.upperPositionNumber')" align="center" prop="slot"/>
            <el-table-column :label="$t('Mounting.originalWidth')" align="center" prop="rawWidth"/>
            <el-table-column :label="$t('Mounting.originalHeight')" align="center" prop="rawHeight"/>
            <el-table-column :label="$t('Mounting.originalThick')" align="center" prop="rawThickness"/>
            <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" prop="filmsId"/>
            <el-table-column :label="$t('film.remainQuantity')" align="center" prop="remainQuantity"/>
            <!--            <el-table-column prop="operation" align="center" :label="$t('Mounting.operate')"></el-table-column>-->
          </el-table>
        </div>
        <div class="clearfix" style="clear: both;height: 0; overflow: hidden;"></div>
      </div>
      <!-- 开始上片 -->
      <el-dialog v-model="dialogStartfilm" top="10vh" width="80%" :title="$t('Mounting.startLoadingWhether')">
        <el-table ref="table" style="margin-top: 20px;height: 550px;" :data="tableDataStorageInfo" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
          <el-table-column prop="engineeringId" fixed align="center" :label="$t('Mounting.projectNumber')"/>
          <el-table-column prop="rawGlassWidth" align="center" :label="$t('Mounting.originalWidth')"/>
          <el-table-column prop="rawGlassHeight" align="center" :label="$t('Mounting.originalHeight')"/>
          <el-table-column prop="rawGlassThickness" align="center" :label="$t('Mounting.thickness')"/>
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')"/>
          <el-table-column prop="totalRequired" align="center" :label="$t('Mounting.totalRequired')"/>
          <el-table-column prop="totalStock" align="center" :label="$t('Mounting.totalStock')"/>
          <el-table-column prop="shortageQuantity" align="center" :label="$t('Mounting.shortageQuantity')"/>
        </el-table>
        <template #footer>
          <div id="dialog-footer" v-show="hideFooter">
            <el-button type="primary" @click="handleon">
              {{ $t('Mounting.confirm') }}
            </el-button>
            <el-button @click="dialogStartfilm = false">{{ $t('Mounting.cancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
      <!-- 结束工程 -->
      <el-dialog v-model="blindEndproject" top="10vh" width="80%">
        <el-table ref="table" :data="tableDataEndproject" style="margin-top: 20px;height: 540px;"
                  :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">
          <el-table-column prop="engineerId" fixed align="center" :label="$t('Mounting.serialNumber')"/>
          <el-table-column prop="glassId" fixed align="center" :label="$t('Mounting.glassID')"/>
          <el-table-column prop="temperingLayoutId" fixed align="center" :label="$t('Mounting.temperedLayoutID')"/>
          <el-table-column prop="temperingFeedSequence" align="center" :label="$t('Mounting.temperedLayoutSequence')"/>
          <el-table-column prop="width" align="center" :label="$t('Mounting.originalWidth')"/>
          <el-table-column prop="height" align="center" :label="$t('Mounting.originalHeight')"/>
          <el-table-column prop="thickness" align="center" :label="$t('Mounting.thickness')"/>
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')"/>
          <el-table-column prop="flowCardId" align="center" :label="$t('Mounting.processCards')"/>
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="200">
            <template #default="scope">
              <el-button type="text" plain @click="opendilapidation(scope.row)">{{
                  $t('Mounting.dilapidation')
                }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :size="'large'"
            :disabled="isOpenPaging"
            layout="prev, pager, next, jumper, total"
            :total="totalRecords"
            @current-change="handlePageChange"
            style="margin-top: 10px; margin-left: 40%;"
        />
        <!-- <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :size="'large'" :disabled="isOpenPaging"
          layout="prev, pager, next, jumper" :total="totalRecords" @current-change="handlePageChange"
          style="margin-top: 10px;margin-left: 40%;" /> -->
        <template #footer>
          <div id="dialog-footer">
            <el-button type="primary" @click="deleteMakeTask">
              {{ $t('Mounting.endProject') }}
            </el-button>
          </div>
        </template>
      </el-dialog>
      <!-- 暂停 -->
      <el-dialog v-model="blindPause" top="24vh" width="30%" :title="$t('Mounting.pausedWhether')">
        <div style="margin-left: 50px;margin-bottom: 10px;">
          <el-form-item :label="$t('Mounting.loadingLineColon')" :required="true">
            <el-select disabled v-model="stationCell" clearable :placeholder="$t('Mounting.loadingLineSelection')"
                       style="margin-left: 20px;">
              <el-option :label="$t('Mounting.selectAll')" value="0"></el-option>
              <el-option :label="$t('Mounting.oneLoadingLine')" value="1"></el-option>
              <el-option :label="$t('Mounting.twoLoadingLine')" value="2"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <template #footer>
          <div id="dialog-footer">
            <el-button type="primary" @click="handleup">
              {{ $t('Mounting.confirm') }}
            </el-button>
            <el-button @click="blindPause = false">{{ $t('Mounting.cancel') }}</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <!-- 选择预览工程 -->
    <el-dialog v-model="dialogFormVisible" top="24vh" width="50%">
      <div class="flex-container" style="margin-left: 100px;margin-bottom: 10px;">
        <el-form-item :label="$t('Mounting.loadingLineColon')" :required="true">
          <el-select disabled v-model="stationCell" clearable :placeholder="$t('Mounting.loadingLineSelection')">
            <el-option :label="$t('Mounting.oneLoadingLine')" value="1"></el-option>
            <el-option :label="$t('Mounting.twoLoadingLine')" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('Mounting.projectNumberColon')" :required="true">
          <el-select v-model="selectedProjectNo" filterable clearable :placeholder="$t('Mounting.selectProject')"
                     style="width: 220px" @input="handleInputChange">
            <el-option v-for="item in titleSelectJson['engineerId']" :key="item.id" :label="item.projectNo"
                       :value="item.projectNo"/>
          </el-select>
        </el-form-item>
      </div>
      <template #footer>
        <div id="dialog-footer">
          <el-button type="primary" @click="handlesure">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="dialogFormVisible = false">{{ $t('Mounting.cancel') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 历史任务 -->
    <el-dialog v-model="blindHistoricaltasks" top="5vh" width="95%" @close="iframeUrl = ''">
      <iframe :src="iframeUrl" marginwidth="2000px" marginheight="2000px" width="100%" height="750px"
              frameborder="0"></iframe>
    </el-dialog>
  </div>
</template>
<style scoped>
.flex-container {
  display: flex;
  align-items: center;
}
.flex-container .el-form-item {
  flex: 1;
  margin-right: 0;
}
.flex-container .el-form-item:last-child {
  margin-left: 20px;
}
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
#dotClass {
  margin-top: 20px;
  margin-bottom: 10px;
}

.float-left {
  float: left;
  margin-left: 8px;
}
</style>