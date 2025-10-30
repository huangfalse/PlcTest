<script lang="ts" setup>
import BaseUploadLine from './basicBreakPiece.vue'
const props = {
  lineNumber: 1,
  webSocketPath: 'currentCutDrawingOne',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
  />
</template>