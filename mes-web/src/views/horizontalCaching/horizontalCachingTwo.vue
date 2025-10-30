<script lang="ts" setup>
import BaseUploadLine from './basicCaching.vue'

const props = {
  lineNumber: 2,
  webSocketPath: 'horizontalTask',
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
  />
</template>