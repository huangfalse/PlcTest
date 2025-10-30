<script setup>
import {ArrowLeftBold, ArrowRight, Search} from "@element-plus/icons-vue"
import {useRouter} from "vue-router";
let indexFlag=$ref(1)
function changeRouter(index){
  indexFlag=index
}
</script>
<template>
    <div id="main-body">
      <router-view />
    </div>
</template>
<style scoped>
#main-div{
  width: 100%;
  height: 100%;
}
#div-title{
  height: 2%;
  width: 100%;
}
#searchButton{
  margin-top: -5px;
  margin-left: 1rem;
}
#main-body{
  width: 100%;
  height: 95%;
  /* margin-top: 1%; */
}
#select{
  margin-left:0.5rem;
}
:deep(.indexTag .el-breadcrumb__inner){
  color: #5CADFE !important;
}
</style>