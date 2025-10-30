<script setup>
import request from "@/utils/request"
import { Search } from "@element-plus/icons-vue";
import { ref, onMounted,reactive } from "vue";
import { useRouter } from "vue-router"
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
const router = useRouter()
const tableData = ref([])
const currentPage = ref(1)
const totalRecords = ref(0)
const isEnable = ref('')
const configName = ref('')
const configCode = ref('')
const configValue = ref('')
const isEnablesearch = ref('')
const configNamesearch = ref('')
const configCodesearch = ref('')
const configValuesearch = ref('')
const selectedProjectNoa = ref(null)
const dialogExit = ref(false)
let language = ref(localStorage.getItem('lang') || 'zh')
const listByUserName = async () => {  
  try {
  const response = await request.post('/plcSend/sys/sysConfig/queryConfigPage',{
      pageNo: 1,
      pageSize: 10,
      configName: "",
      configCode: "",
      configValue: "",
      isEnable: -1
     });  
    if (response.code === 200) {  
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableData.value = formattedData;
      totalRecords.value = response.data.pages*10
      } else {
      ElMessage.warning(response.data);  
    }  
  } catch (error) {
  }  
};
onMounted(() => {
  listByUserName('');
});
// 查询
  const handleClick = async () => {
  try {
    const response = await request.post('/plcSend/sys/sysConfig/queryConfigPage',{
      pageNo: 1,
      pageSize: 10,
      configName: configNamesearch.value,
      configCode: configCodesearch.value,
      configValue: configValuesearch.value,
      isEnable: isEnablesearch.value !== '' ? isEnablesearch.value : -1, 
   
     });  
    if (response.code === 200) {  
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableData.value = formattedData;
      totalRecords.value = response.data.pages*10
      } else {
      ElMessage.warning(response.data);  
    }  
  } catch (error) {
  }  
};
// 点击分页
const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  window.localStorage.setItem('pagenumber', currentPage.value)
  historicala(currentPage.value);
};
const historicala = async (page) => {
  try { 
  let page = window.localStorage.getItem('pagenumber')
  const response = await request.post('/plcSend/sys/sysConfig/queryConfigPage',{
      pageNo: page,
      pageSize: 10,
      configName: "",
      configCode: "",
      configValue: "",
      isEnable: -1
     });  
    if (response.code === 200) {  
        const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }));
      tableData.value = formattedData;
      totalRecords.value = response.data.pages*10
      } else {
      ElMessage.warning(response.data);  
    }  
  } catch (error) {
  }  
};
// 处理编辑按钮点击  
function handleEdit(row) {
  dialogExit.value = true;
  configValue.value = row.configValue
  isEnable.value = row.isEnable
  configName.value = row.configName
  window.localStorage.setItem('id', row.id)
  window.localStorage.setItem('configName', row.configName)
  window.localStorage.setItem('configCode', row.configCode)
}
// 编辑
const getTableRow = async () => {
  let configName = window.localStorage.getItem('configName')
  let configCode = window.localStorage.getItem('configCode')
  let id = window.localStorage.getItem('id')
  let page = window.localStorage.getItem('pagenumber')
  if (!configValue.value) {
    ElMessage.error(t('productStock.configValueEnter'))
    return
  }
  if (!isEnable.value) {
    ElMessage.error(t('searchOrder.enabledStatusSelection'))
    return
  }
  try {
    const response = await request.post('/plcSend/sys/sysConfig/updateConfig',{
      id: id,
      configName: configName,
      configCode: configCode,
      configValue: configValue.value,
      isEnable: isEnable.value
  }
  ); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      dialogExit.value = false;
      historicala(page)
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
const options = [
  {
    value: 0,
    label: t('sorter.disable'),
  },
  {
    value: 1,
    label: t('sorter.start'),
  }
]
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
</script>
<template>
  <div>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;" >
    <div style="display: flex;width: 1770px;">
    <el-input v-model="configNamesearch" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" :placeholder="$t('productStock.configName')" />
    <el-input v-model="configCodesearch" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputa" :placeholder="$t('productStock.configCode')" />
    <el-input v-model="configValuesearch" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" @input="handleInputb" :placeholder="$t('productStock.configValue')" />
    <el-select v-model="isEnablesearch" clearable style="margin-left: 10px;margin-bottom: 10px;width: 200px;" :placeholder="$t('searchOrder.enabledStatus')">
            <el-option :label="$t('film.start')" value="1"></el-option>
            <el-option :label="$t('film.disable')" value="0"></el-option>
          </el-select>
          <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="handleClick()">{{$t('order.inquire')}}</el-button>
     </div>
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
          <el-table height="700" ref="table" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
            :data="tableData">
            <el-table-column prop="configName" align="center" :label="$t('productStock.configName')"/>
          <el-table-column prop="configCode" align="center" :label="$t('productStock.configCode')"/>
          <el-table-column prop="configValue" align="center" :label="$t('productStock.configValue')"/>
          <el-table-column
            align="center"
            :label="$t('searchOrder.enabledStatus')"
            prop="isEnable"
          >
          <template #default="scope">
            <el-tag
              :type="scope.row.isEnable === 1 ? 'success' : 'warning'"
            >
              {{ scope.row.isEnable === 1 ? $t('film.start') : $t('film.disable') }}
            </el-tag>
          </template>
      </el-table-column>
          <el-table-column prop="formattedCreateTime" align="center" :label="$t('Mounting.creationTime')"/>
          <el-table-column prop="formattedUpdateTime" align="center" :label="$t('searchOrder.updateTime')" />
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="270">
            <template #default="scope">
              <el-button type="text" plain @click="handleEdit(scope.row)">{{ $t('productStock.exit') }}</el-button>
            </template>
        </el-table-column>
          </el-table>
        </div>
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
    </el-card>
  </div>
  <el-dialog v-model="dialogExit" top="23vh" width="37%" >
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form ref="formRef" label-width="150px">
      <el-form label-width="120px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('productStock.configNameColon')" :required="true" style="width: 25vw">
                <el-input :placeholder="$t('productStock.configName')" disabled v-model="configName" autocomplete="off" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('productStock.configValueColon')" :required="true" style="width: 25vw">
                <el-input :placeholder="$t('productStock.configValue')" v-model="configValue" autocomplete="off" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('searchOrder.enabledStatusColon')" :required="true" style="width: 25vw;">
                <el-select 
            :placeholder="$t('searchOrder.enabledStatus')"
             clearable
             style="width: 350px;"  
             v-model="isEnable">
               <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
          </el-select>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getTableRow">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="dialogExit = false">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
</template>
<style scoped>
#dt { display:block; float:left;line-height: 20px;margin-left: 150px;}
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
  height: 460px;
  /* margin-top: -60px; */
}
</style>