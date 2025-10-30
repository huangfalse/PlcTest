<script setup>
import BaseUploadLine from './BasicEdging.vue'
const firstPageStatusConfig = {
  typeMapping: {
    0: 'info',
    1: 'success',
    2: 'danger', 
    8: 'danger'
  },
  textMapping: {
    0: 'workOrder.unEdging',
    1: 'workOrder.finishEdging',
    2: 'workOrder.finishEdging',
    8: 'workOrder.breakage'
  }
}
const props = {
  lineNumber: 1,
  webSocketPath: 'edgingTask',
  workingProcedure: '磨边',
  statusConfig: firstPageStatusConfig
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :webSocketPath="props.webSocketPath"
    :workingProcedure="props.workingProcedure"
    :statusConfig="props.statusConfig"
  />
</template>