<script lang="ts" setup>
import BaseUploadLine from './basicHollowLine.vue'
const props = {
  lineNumber: 931,
  webSocketPath: 'HollowGlassTwo',
  idleState: 'idleStateTwoColon',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
    :idleState="props.idleState"
  />
</template>