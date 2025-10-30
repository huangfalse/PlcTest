<template>
  <el-dialog v-model="dialogVisible" top="7vh" width="95%" :title="$t('film.originalInformation')" @close="handleClose">
    <div style="margin-left: 750px;margin-top: 10px;font-size: large;margin-bottom: 10px;">
      {{ $t('Mounting.originalQueueNotBound') }}
    </div>
    <div class="dialog-container">
      <!-- 左侧部分 -->
      <div class="left-side">
        <el-form class="glass-form" label-position="right" label-width="auto">

          <el-row class="form-row" :gutter="20">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.stationColon')" :required="true" style="width: 17vw;">
                    <el-input :disabled="isViewMode" :placeholder="$t('film.stationEnter')" v-model="formData.slot"
                      autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <!-- <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('searchOrder.deviceIDColon')" :required="true" style="width: 17vw;">
                    <el-input :disabled="isViewMode" :placeholder="$t('searchOrder.deviceIDEnter')"
                      v-model="formData.deviceId" autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col> -->
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.shelfNumberColon')" :required="true" style="width: 17vw;">
                    <el-input :disabled="isViewMode" :placeholder="$t('film.shelfNumberEnter')" v-model="formData.shelf"
                      autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row class="form-row" :gutter="20">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.widthColon')" :required="true" style="width: 17vw;">
                    <el-input :placeholder="$t('film.widthEnter')" v-model="formData.rawWidth" autocomplete="off"
                      class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.heightColon')" :required="true" style="width: 17vw;">
                    <el-input :placeholder="$t('film.heightEnter')" v-model="formData.rawHeight" autocomplete="off"
                      class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row class="form-row" :gutter="20">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.thicknessColon')" :required="true" style="width: 17vw;">
                    <el-input :placeholder="$t('film.thicknessEnter')" v-model="formData.rawThickness"
                      autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.filmsColon')" :required="true" style="width: 17vw;">
                    <el-input :placeholder="$t('film.filmsEnter')" v-model="formData.filmsId" autocomplete="off"
                      class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row class="form-row" :gutter="20">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.originalEdgeColon')" style="width: 17vw;">
                    <el-input :placeholder="$t('film.originalEdgeEnter')" v-model="formData.rawMargin"
                      autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.remainQuantityColon')" :required="true" style="width: 17vw;">
                    <el-input :placeholder="$t('film.remainQuantityEnter')" v-model="formData.remainQuantity"
                      autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row class="form-row" :gutter="20">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.manufacturerColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.manufacturer" autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.originalFilmNumberColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.rawNumber" autocomplete="off" class="form-input" />
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>

        </el-form>
        <div id="dialog-footer" style="margin-top: 20px;">
          <el-button type="primary" @click="EditClick">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="handleClose">{{ $t('Mounting.cancel') }}</el-button>
        </div>
        <el-button type="primary" style="display: block; margin: 20px auto;" @click="handleDelete">{{
          $t('film.shelfEmpty') }}
        </el-button>
      </div>
      <!-- 右侧部分 -->
      <div class="right-side">
        <div style="display: flex;">
          <el-input v-model="rawWidth" style="margin-left: 10px;margin-bottom: 10px;width: 240px;"
            :placeholder="$t('film.widthEnter')" />
          <el-input v-model="rawHeight" style="margin-left: 10px;margin-bottom: 10px;width: 240px;"
            :placeholder="$t('film.heightEnter')" />
          <el-input v-model="rawThickness" style="margin-left: 10px;margin-bottom: 10px;width: 240px;"
            :placeholder="$t('film.thicknessEnter')" />
          <el-input v-model="filmsId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;"
            :placeholder="$t('film.filmsEnter')" />
          <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="fetchFlowCardSearch">
            {{ $t('order.inquire') }}</el-button>
        </div>
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
</template>
<script lang="ts" setup>
import request from "@/utils/request"
import { ref, watch } from 'vue'
import type { PropType } from 'vue'
import {useRouter} from "vue-router"
import {useI18n} from 'vue-i18n'
const {t} = useI18n()
const loading = ref(false)
const router = useRouter()
const rawWidth = ref('')
const rawHeight = ref('')
const rawThickness = ref('')
const filmsId = ref('')
const tableData = ref<any[]>([])

interface FormData {
  slot?: string
  deviceId?: string
  shelf?: string
  rawWidth?: string
  rawHeight?: string
  rawThickness?: string
  filmsId?: string
  rawMargin?: string
  remainQuantity?: string
  manufacturer?: string
  rawNumber?: string
}
interface TableRow {
  rawWidth?: string
  rawHeight?: string
  rawThickness?: string
  filmsId?: string
  rawMargin?: string
  manufacturer?: string
  rawNumber?: string
  remainQuantity?: string
  [key: string]: any
}
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  rowData: {
    type: Object as PropType<any>,
    default: () => ({})
  },
  tableDataQueryUnBind: {
    type: Array as PropType<TableRow[]>,
    default: () => []
  },
  isViewMode: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['update:visible', 'confirm', 'add-row', 'delete-row', 'close', 'clean'])
const dialogVisible = ref(props.visible)
const formData = ref<FormData>({})
// 监听props.visible变化
watch(() => props.visible, (val) => {
  dialogVisible.value = val
})
// 监听dialogVisible变化，同步到父组件
watch(dialogVisible, (val) => {
  emit('update:visible', val)
})
// 监听rowData变化
watch(() => props.rowData, (val) => {
  if (val) {
    formData.value = {
      slot: val.slot,
      deviceId: val.deviceId,
      shelf: val.shelf,
      rawWidth: val.rawWidth,
      rawHeight: val.rawHeight,
      rawThickness: val.rawThickness,
      filmsId: val.filmsId,
      rawMargin: val.rawMargin,
      remainQuantity: val.remainQuantity,
      manufacturer: val.manufacturer,
      rawNumber: val.rawNumber,
      id: val.id
    }
  }
}, { immediate: true })
const handleAddRow = (row: TableRow) => {
  // 将右侧表格行数据合并到表单中，但保留slot/deviceId/shelf字段
  formData.value = {
    ...formData.value,
    rawWidth: row.rawWidth,
    rawHeight: row.rawHeight,
    rawThickness: row.rawThickness,
    filmsId: row.filmsId,
    rawMargin: row.rawMargin,
    remainQuantity: row.remainQuantity,
    manufacturer: row.manufacturer,
    rawNumber: row.rawNumber,
  }
}
const EditClick = () => {
  emit('confirm', formData.value)
}
const deleteWareHousing = (row: TableRow) => {
  emit('delete-row', row)
}
const handleClose = () => {
  emit('close')
  const { slot, deviceId, shelf } = formData.value
  formData.value = { slot, deviceId, shelf }
}
const handleDelete = () => {
  emit('clean')
 
}

const fetchFlowCardSearch = async () => {
  try {
    const response = await request.post('glassStorage/rawGlassStorageDetails/queryUnBindRawGlassList',{
      rawWidth: rawWidth.value,
      rawHeight: rawHeight.value,
      rawThickness: rawThickness.value,
      filmsId: filmsId.value
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableData.value = response.data;
    } else {
      ElMessage.error();
    }
  } catch (error) {
  }
};

</script>
<style scoped>
.dialog-container {
  display: flex;
  height: 70vh;
}
.left-side {
  width: 40%;
  padding: 0 10px;
  overflow-y: auto;
}
.right-side {
  width: 60%;
  padding: 0 10px;
}
#dialog-footer {
  text-align: center;
  margin-top: -15px;
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
</style>