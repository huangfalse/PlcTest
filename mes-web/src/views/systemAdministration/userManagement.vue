<script setup>
import request from "@/utils/request"
import { Search } from "@element-plus/icons-vue";
import { ref, onMounted, reactive } from "vue";
import { useRouter } from "vue-router"
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
const router = useRouter()
const slot = ref('')
const userName = ref('')
const password = ref('')
const tableData = ref([])
const dialogAdd = ref(false)
const dialogEdit = ref(false)
const selectedProjectNoa = ref(null)
const titleSelectJsona = ref({ processType: [] })
let language = ref(localStorage.getItem('lang') || 'zh')
const listByUserName = async () => {  
  try {
    const response = await request.post('/plcSend/sys/user/listByUserName',{
      key: ''
     });  
    if (response.code === 200) {  
      tableData.value = response.data
    } else {  
      ElMessage.warning(response.data);  
    }  
  } catch (error) {
  }  
}; 
// 重置密码
 const resetPassword = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('productStock.resetPasswordWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      var url="/plcSend/sys/user/resetPassword?userId="+row.id;
      const response = await request.post(url, {
        esdId: row.esdId
    })
    if (response.code === 200) {
      ElMessage.success(response.message);
      } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
};
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
  }  
};
// 处理编辑按钮点击 
function handleEdit(row) {
  userName.value = row.userName;
  if (row.roleList && row.roleList.length > 0) {  
    const firstRole = row.roleList[0];  
    selectedProjectNoa.value = firstRole.id; // 设置为ID，而不是name  
  }  
  dialogEdit.value = true;
  window.localStorage.setItem('id', row.id)
}
// 关闭页面后清空输入框内容
function closeDialogAdd(row) {
     dialogAdd.value = false;
     userName.value = '';
     nickName.value = '';
     password.value = '';
     selectedProjectNoa.value = '';
}
function closeDialogEdit(row) {
     dialogEdit.value = false;
     userName.value = '';
     nickName.value = '';
     password.value = '';
}
// 添加 
const confirmAdd = async () => {
   if (!userName.value) {
    ElMessage.error(t('productStock.userNameEnter'))
    return
  }
   if (!selectedProjectNoa.value) {
    ElMessage.error(t('productStock.roleSelection'))
    return
  }
   if (!password.value) {
    ElMessage.error(t('productStock.passwordEnter'))
    return
  }
  try {
    const response = await request.post('/plcSend/sys/user/saveUser', {
      userName: userName.value,
      nickName: userName.value,
      password: password.value,
      roleList: [{id: selectedProjectNoa.value}]
    }); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      dialogAdd.value = false;
      listByUserName('');
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
// 编辑
const confirmEdit = async () => {
   if (!userName.value) {
    ElMessage.error(t('productStock.userNameEnter'))
    return
  }
   if (!selectedProjectNoa.value) {
    ElMessage.error(t('productStock.roleSelection'))
    return
  }
   if (!password.value) {
    ElMessage.error(t('productStock.passwordEnter'))
    return
  }
  let id = window.localStorage.getItem('id')
  try {
    const response = await request.post('/plcSend/sys/user/updateUser', {
      id: id,
      userName: userName.value,
      roleList: [{id: selectedProjectNoa.value}]
  }
  ); 
    if (response.code === 200) {
      ElMessage.success(response.message);
      dialogEdit.value = false;
      listByUserName('');
      tableData.value = response.data;
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
};
// 删除 
 const confirmDelete = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('productStock.deleteUserWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/plcSend/sys/user/deleteUser", [row.id])
    if (response.code === 200) {
      ElMessage.success(response.message);
      listByUserName('');
    } else {
      ElMessage.error(response.msg);
      }  
    }  
  } catch (error) {
  }  
};
onMounted(() => {
  fetchOptionsa('');
  listByUserName('');
}); 
</script>
<template>
  <div>
     <el-button type="primary" style="margin-top: 10px;margin-left: 10px;"  id="searchButton" @click="dialogAdd = true">{{ $t('productStock.addUsers') }}</el-button>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;" >
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table height="750" ref="table" 
        :data="tableData" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
          <el-table-column prop="userName" align="center" :label="$t('productStock.userName')" min-width="180" />
          <el-table-column align="center" :label="$t('delivery.role')" min-width="80">
            <template #default="scope">  
             <el-tag v-for="role in scope.row.roleList">  
            {{role.name}}  
           </el-tag>   
         </template> 
            </el-table-column>
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="270">
            <template #default="scope">
              <el-button type="text" plain @click="resetPassword(scope.row)">{{ $t('productStock.resetPassword') }}</el-button>
              <el-button type="text" plain @click="handleEdit(scope.row)">{{ $t('productStock.exit') }}</el-button>
              <el-button type="text" plain @click="confirmDelete(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
  <!-- 添加用户 -->
  <el-dialog v-model="dialogAdd" top="23vh" width="37%" :title="$t('productStock.addUsers')">
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form  label-width="150px">
      <el-form label-width="105px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('productStock.userNameColon')" :required="true" style="width: 25vw">
                <el-input v-model="userName" autocomplete="off" style="width: 350px;" :placeholder="$t('productStock.userNameEnter')" />
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('delivery.roleColon')" :required="true" style="width: 25vw;">
                <el-select
               v-model="selectedProjectNoa"
               filterable
               clearable
               :placeholder="$t('productStock.roleSelection')"
               style="width: 350px;"
             >
               <el-option
            v-for="item in titleSelectJsona['processTypea']"  
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('productStock.passwordColon')" :required="true" style="width: 25vw;">
                <el-input style="width: 350px;"
                        v-model="password"
                        :placeholder="$t('productStock.passwordEnter')"
                        autocomplete="off"
                        :prefix-icon="Lock"
                        show-password/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="confirmAdd">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogAdd">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
  <!-- 编辑用户 -->
  <el-dialog v-model="dialogEdit" top="23vh" width="37%" :title="$t('productStock.modifyUser')" >
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form ref="formRef" label-width="150px">
      <el-form label-width="105px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('productStock.userNameColon')" :required="true" style="width: 25vw">
                <el-input :placeholder="$t('productStock.userNameEnter')" v-model="userName" autocomplete="off" style="width: 350px;"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('delivery.roleColon')" :required="true" style="width: 25vw;">
                <el-select
               v-model="selectedProjectNoa"
               filterable
               clearable
               :placeholder="$t('productStock.roleSelection')"
               style="width: 350px;"
             >
               <el-option
         v-for="item in titleSelectJsona['processTypea']"
            :key="item.id"
            :label="item.name"
            :value="item.id"
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
        <el-button type="primary" @click="confirmEdit">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogEdit">{{ $t('Mounting.cancel') }}</el-button>
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