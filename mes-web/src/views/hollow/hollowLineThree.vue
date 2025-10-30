<script lang="ts" setup>
import BaseUploadLine from './basicHollowLine.vue'
const props = {
  lineNumber: 932,
  webSocketPath: 'HollowGlassThree',
  idleState: 'idleStateThreeColon',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
    :idleState="props.idleState"
  />
</template>