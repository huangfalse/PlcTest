<script lang="ts" setup>
import BaseUploadLine from './basicBreakPiece.vue'
const props = {
  lineNumber: 2,
  webSocketPath: 'currentCutDrawingTwo',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
  />
</template>