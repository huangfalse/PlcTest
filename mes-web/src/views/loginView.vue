<script lang="ts" setup>
import request from '@/utils/request'
import userInfo from '@/stores/userInfo'
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import type {FormInstance, FormRules} from 'element-plus'
let ruleFormRef = ref<FormInstance>()
let language = ref(localStorage.getItem('lang') || 'zh')
let loginLoadings = ref(false)
const store = userInfo()
const router = useRouter()
const route = useRoute()
const { t } = useI18n()
const changeLanguage = () =>{
  localStorage.setItem('lang',language.value)
  location.reload()
}
const userForm = reactive({
  userName: '',
  password: '',
})
if (typeof route.query.id != 'undefined') {
  userForm.userId = <string>route.query.id
}
const validateUser = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error(t('login.enterAccountNumber')))
  } else {
    callback()
  }
}
const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error(t('login.enterPassword')))
  } else {
    callback()
  }
}
const rules = reactive<FormRules<typeof userForm>>({
  userId: [{validator: validateUser, trigger: 'blur'}],
  pass: [{validator: validatePass, trigger: 'blur'}],
})
//登陆方法
const submitForm = (formEl: FormInstance | undefined) => {
  store.$patch({
              user: null,
              })
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      loginLoadings.value = true
      // 使用现有的登录接口地址
      request
          .post('/plcSend/sys/user/login', userForm)
          .then((res) => {
            if (res['code'] == 200) {
              store.$patch({user: res.data})
              router.push('/main')
              ElMessage.success(t('login.loginSuccessful'))
            } else {
              store.$patch({
              user: null,
              })
              loginLoadings.value = false
              return false
            }
          })
          .catch((error) => {
            ElMessage.error(t('main.connectErr'))
            store.$patch({
              user: null,
              })
            loginLoadings.value = false
            return false
          })
    }
  })
}
function register() {
  router.push({
    path: '/register',
  })
}
const keyDown = (e) => {
  // 回车则执行登录方法 enter键的ASCII是13
  if (e.keyCode == 13 || e.keyCode == 100) {
    submitForm(ruleFormRef.value)
  }
}
onMounted(() => {
  window.addEventListener('keydown', keyDown)
})
onUnmounted(() => {
  window.removeEventListener('keydown', keyDown)
})
</script>
<template>
  <div class="mainDiv">
    <div id="main-login">
      <div>
        <div style="position: absolute; left: 8vw; top: 6vw; ">
          <img src="/3.png">
        </div>
        <div style="position: absolute; left: 15vw; top: 22vw; font-size: 55px;color: rgba(29, 33, 41, 1);">
          {{ $t('NorthGlassMESSystem') }}
        </div>
      </div>
      <div id="div-login">
        <!--语言切换按钮-->
        <el-select
            @change="changeLanguage"
            v-model="language"
            placeholder=" "
            style="float: right;width: 9rem">
          <el-option value="zh"  label="中文" />
          <el-option value="py"  label="Русский язык" />
          <el-option value="en"  label="English" />
        </el-select>
        <el-form @submit.native.prevent
                 ref="ruleFormRef"
                 :model="userForm"
                 status-icon
                 :rules="rules">
          <div id="center">
            <div style="color: rgba(78, 89, 105, 1);margin-bottom: 10px;">{{ $t('login.account') }}</div>
            <el-form-item prop="userId">
              <el-input style="width: 340px;"
                        v-model="userForm.userName"
                        type="text"
                        autocomplete="off"
                        :prefix-icon="Avatar"
                        :placeholder="$t('login.enterAccountNumber')"/>
            </el-form-item>
            <div style="color: rgba(78, 89, 105, 1);margin-bottom: 10px;">{{ $t('login.password') }}</div>
            <el-form-item prop="pass">
              <el-input style="width: 340px;"
                        v-model="userForm.password"
                        type="password"
                        autocomplete="off"
                        :prefix-icon="Lock"
                        show-password
                        :placeholder="$t('login.enterPassword')"
                       />
            </el-form-item>
            <el-form-item id="submitForm">
              <el-button :loading="loginLoadings"
                         type="primary"
                         native-type="submit"
                         style="width: 80px;"
                         @click="submitForm(ruleFormRef)"
                         @keyup.enter.native="keyDown(e)"
                         plain>{{ $t('login.login') }}
              </el-button>
            </el-form-item>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>
<style scoped>
.mainDiv {
  overflow: hidden;
  min-width: 718px;
  background-image: url('/background.jpg');
}
#main-login {
  margin: 150px auto 0 auto;
  height: 70vh;
  width: 80vw;
}
#img-div {
  width: 55%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  float: left;
}
#img-pic {
  max-height: 90%;
  max-width: 100%;
}
#div-login {
  margin-top: 5%;
  background-color: #fff;
  float: right;
  width: 40%;
  height: 60%;
  min-width: 318px;
  border-radius: 4px;
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0), 0 6px 5px 0 rgba(0, 0, 0, 0.19);
}
#center {
  margin-top: -30px;
}
.el-form {
  width: 60%;
  margin: 20% auto auto;
}
#submitForm {
  display: flex;
  justify-content: space-evenly;
  margin-top: 2rem;
}
:deep(.el-form-item__content) {
  flex: unset;
}
</style>