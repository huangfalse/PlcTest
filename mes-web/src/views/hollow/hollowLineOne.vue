<script lang="ts" setup>
import BaseUploadLine from './basicHollowLine.vue'
const props = {
  lineNumber: 930,
  webSocketPath: 'HollowGlassone',
  idleState: 'idleStateOneColon',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
    :idleState="props.idleState"
  />
</template>