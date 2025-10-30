<script setup>
import request from "@/utils/request"
import {onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router"
import {useI18n} from 'vue-i18n'
import {ElMessage, ElMessageBox} from 'element-plus'

const { t } = useI18n()
const router = useRouter()
const addLevelOne = ref(false)
const addLevelTwo = ref(false)
const dialogLevelOne = ref(false)
const dialogLevelTwo = ref(false)
const slot = ref('')
const url = ref('')
const menuName = ref('')
const listSort = ref('')
const tableData = ref([])
const selectedProjectNoa = ref('') // 当前选中的角色 
const currentRow = reactive({}) // 当前行的数据 
const titleSelectJsona = ref({
  processType: [],
})
let language = ref(localStorage.getItem('lang') || 'zh')
request.get("/plcSend/sys/menu/nav").then((res) => {
 if (res.code == 200) {
 tableData.value = res.data.tree
 } else {
 ElMessage.warning(res.msg)
 }
 });
onMounted(() => {
  fetchOptionsa('');  
}); 
  // 角色下拉选  
  const fetchOptionsa = async () => {  
  try {  
    const response = await request.post('/plcSend/sys/role/queryRole',{
     key: ''
    });  
    if (response.code === 200) {  
    titleSelectJsona.value.processTypea = response.data;
    } else {  
      ElMessage.warning(response.data);  
    }  
  } catch (error) {  
    console.error('Error fetching options:', error);  
  }  
}; 
// 添加一级菜单
const getTableRowLevelOne = async () => {
   if (!menuName.value) {
    ElMessage.error(t('customer.menuEnter'))
    return
  }
   if (!url.value) {
    ElMessage.error(t('customer.linkEnter'))
    return
  }
   if (!listSort.value) {
    ElMessage.error(t('customer.sortEnter'))
    return
  }
  try {
    const response = await request.post('/plcSend/sys/menu/save', {
      menuName: menuName.value,
      listSort: listSort.value,
      url: url.value,
    }); 
 
    if (response.code === 200) {
      ElMessage.success(response.message);
      addLevelOne.value = false;
      tableData.value = response.data;
      menuName.value = '';
      listSort.value = '';
      url.value = '';
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    console.error(error);
  }
};
// 点击添加二级菜单
const handleLevelTwo = (row) => {
  currentRow.menuName  = row.menuName; // 直接设置响应式属性  
  currentRow.id  = row.id;
  addLevelTwo.value = true;
};
// 添加二级菜单
const getTableRowLevelTwo = async () => {
   if (!menuName.value) {
    ElMessage.error(t('customer.menuEnter'))
    return
  }
   if (!url.value) {
    ElMessage.error(t('customer.linkEnter'))
    return
  }
   if (!listSort.value) {
    ElMessage.error(t('customer.sortEnter'))
    return
  }
  try {
    const dataToSend = {  
      parentId: currentRow.id,
      menuName: menuName.value,
      listSort: listSort.value,
      url: url.value,
    };
    const response = await request.post('/plcSend/sys/menu/save',dataToSend ); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      addLevelTwo.value = false;
     tableData.value = response.data;
       menuName.value = '';
       listSort.value = '';
       url.value = '';
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    console.error(error);
  }
};
// 修改一级菜单
function handleEditLevelOne(row) {  
  listSort.value = row.listSort; 
  menuName.value = row.menuName; 
  url.value = row.url; 
  dialogLevelOne.value = true;
}
// 编辑一级菜单
const getExitLevelOne = async () => {
   if (!menuName.value) {
    ElMessage.error(t('customer.menuEnter'))
    return
  }
   if (!url.value) {
    ElMessage.error(t('customer.linkEnter'))
    return
  }
   if (!listSort.value) {
    ElMessage.error(t('customer.sortEnter'))
    return
  }
  try {
    const response = await request.post('/plcSend/sys/menu/updateMenu', {
      parentId: currentRow.id,
      menuName: menuName.value,
      listSort: listSort.value,
      url: url.value,
    }); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      dialogLevelOne.value = false;
      tableData.value = response.data;
      menuName.value = '';
      listSort.value = '';
      url.value = '';
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    console.error(error);
  }
};
// 修改二级菜单
function handleEditLevelTwo(row) {
  listSort.value = row.listSort; 
  menuName.value = row.menuName; 
  url.value = row.url; 
  dialogLevelTwo.value = true;
}
// 编辑二级菜单
const getExitLevelTwo = async () => {
   if (!menuName.value) {
    ElMessage.error(t('customer.menuEnter'))
    return
  }
   if (!url.value) {
    ElMessage.error(t('customer.linkEnter'))
    return
  }
   if (!listSort.value) {
    ElMessage.error(t('customer.sortEnter'))
    return
  }
  try {
    const response = await request.post('/plcSend/sys/menu/updateMenu', {
      menuName: menuName.value,
      listSort: listSort.value,
      url: url.value,
    }); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      dialogLevelTwo.value = false;
      tableData.value = response.data;
      menuName.value = '';
      listSort.value = '';
      url.value = '';
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    console.error(error);
  }
};
// 关闭--添加一级菜单
function closeDialogAddLevelOne(row) {
    addLevelOne.value = false;
    menuName.value = '';
    listSort.value = '';
    url.value = '';
}
// 关闭--编辑一级菜单
function closeDialogLevelOne(row) {
    dialogLevelOne.value = false;
    menuName.value = '';
    listSort.value = '';
    url.value = '';
}
// 关闭--添加二级菜单
function closeDialogAddLevelTwo(row) {
    addLevelTwo.value = false;
    menuName.value = '';
    listSort.value = '';
    url.value = '';
}
// 关闭--编辑二级菜单
function closeDialogLevelTwo(row) {
    dialogLevelTwo.value = false;
    menuName.value = '';
    listSort.value = '';
    url.value = '';
}
// 删除 
 const openDelete = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('customer.deleteMenuWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/plcSend/sys/menu/deleteMenu", {
        menuId: row.id,
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
</script>
<template>
  <div style="height: 600px;">
     <el-button type="primary" style="margin-top: 10px;margin-left: 10px;"  id="searchButton" @click="addLevelOne = true">{{ $t('customer.addFirstMenu') }}</el-button>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;max-height: 750px;">
        <el-table  
          :data="tableData" 
          height="750"
          @expand-change="handleExpandChange"  
          row-key="id" 
          :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" 
        >  
          <el-table-column type="expand">  
            <template #default="props">  
              <div v-if="props.row.children && props.row.children.length">  
                <el-table  
                  :data="props.row.children"  
                  border
                  style="width: 100%;margin-left: 120px;"  
                  row-key="id"  
                  :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}" 
                >  
          <el-table-column prop="menuName" :label="$t('customer.secondMenus')" align="center" min-width="140" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}"/>  
          <el-table-column prop="url" :label="$t('customer.link')" align="center" min-width="160"/>  
          <el-table-column prop="listSort" align="center" :label="$t('customer.sort')" min-width="140"/>
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center">
            <template #default="scope">
              <el-button type="text" plain @click="handleEditLevelTwo(scope.row)">{{ $t('productStock.exit') }}</el-button>
              <el-button type="text" plain @click="openDelete(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
      </el-table>  
     </div>  
     </template>  
     </el-table-column>  
          <el-table-column prop="menuName" :label="$t('customer.firstMenu')" align="center" min-width="120"  />  
          <el-table-column prop="url" :label="$t('customer.link')" align="center" min-width="200"/>  
          <el-table-column prop="listSort" align="center" :label="$t('customer.sort')" min-width="180" />
          <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="300">
            <template #default="scope">
              <el-button type="text" plain @click="handleLevelTwo(scope.row)">{{ $t('customer.addSecondMenus') }}</el-button>
              <el-button type="text" plain @click="handleEditLevelOne(scope.row)">{{ $t('productStock.exit') }}</el-button>
              <el-button type="text" plain @click="openDelete(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
  <!-- 添加一级菜单 -->
  <el-dialog v-model="addLevelOne" top="23vh" width="37%" :title="$t('customer.addFirstMenu')">
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form  label-width="150px">
      <el-form label-width="150px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div> 
          <el-form-item :label="$t('customer.firstMenuNameColon')" :required="true" style="width: 25vw">
                <el-input v-model="menuName" autocomplete="off" :placeholder="$t('customer.menuEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.linkColon')" :required="true" style="width: 25vw">
                <el-input v-model="url" autocomplete="off" :placeholder="$t('customer.linkEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.sortColon')" :required="true" style="width: 25vw">
                <el-input v-model="listSort" autocomplete="off" :placeholder="$t('customer.sortEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getTableRowLevelOne">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogAddLevelOne">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
  <!-- 添加二级菜单 -->
  <el-dialog v-model="addLevelTwo" top="23vh" width="37%" :title="$t('customer.addSecondMenus')">
    <div style="margin-bottom: 20px">
      <el-form>
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
              <el-form-item :label="$t('customer.firstMenuNameColon')"  style="width: 25vw">
            <el-input disabled v-model="currentRow.menuName"  autocomplete="off" style="width: 330px;"></el-input>
              </el-form-item>
              </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form  label-width="150px">
      <el-form label-width="150px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div> 
          <el-form-item :label="$t('customer.secondMenusNameColon')" :required="true" style="width: 25vw">
                <el-input v-model="menuName" autocomplete="off" :placeholder="$t('customer.menuEnter')" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.linkColon')" :required="true" style="width: 25vw">
                <el-input v-model="url" autocomplete="off" :placeholder="$t('customer.linkEnter')" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.sortColon')" :required="true" style="width: 25vw">
                <el-input v-model="listSort" autocomplete="off" :placeholder="$t('customer.sortEnter')" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getTableRowLevelTwo">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogAddLevelTwo">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
  <!-- 编辑一级菜单 -->
  <el-dialog v-model="dialogLevelOne" top="23vh" width="37%" :title="$t('customer.exitFirstMenu')">
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
     <el-form ref="formRef" label-width="150px">
      <el-form label-width="100px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div> 
          <el-form-item :label="$t('customer.menuColon')" :required="true" style="width: 25vw">
                <el-input v-model="menuName" autocomplete="off" :placeholder="$t('customer.menuEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.linkColon')" :required="true" style="width: 25vw">
                <el-input v-model="url" autocomplete="off" :placeholder="$t('customer.linkEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.sortColon')" :required="true" style="width: 25vw">
                <el-input v-model="listSort" autocomplete="off" :placeholder="$t('customer.sortEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getExitLevelOne">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogLevelOne">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
  <!-- 编辑二级菜单 -->
  <el-dialog v-model="dialogLevelTwo" top="23vh" width="37%" :title="$t('customer.exitSecondMenus')">
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
     <el-form ref="formRef" label-width="150px">
      <el-form label-width="100px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div> 
          <el-form-item :label="$t('customer.menuColon')" :required="true" style="width: 25vw">
                <el-input v-model="menuName" autocomplete="off" :placeholder="$t('customer.menuEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.linkColon')" :required="true" style="width: 25vw">
                <el-input v-model="url" autocomplete="off" :placeholder="$t('customer.linkEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('customer.sortColon')" :required="true" style="width: 25vw">
                <el-input v-model="listSort" autocomplete="off" :placeholder="$t('customer.sortEnter')"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getExitLevelTwo">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogLevelTwo">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
</template>
<style scoped>
#dt { display:block; float:left;line-height: 20px;margin-left: 100px;}
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
}
</style>