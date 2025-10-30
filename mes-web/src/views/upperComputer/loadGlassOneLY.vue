<script lang="ts" setup>
import BaseUploadLine from './basicFilmCamera.vue'

const props = {
  workLine: 1,
  shelfNumber: 1,
  loadGlass: 'one',
  webSocketPath: 'loadGlass',
  historicalTaskUrl: 'loadHistory',
  loaderPicture: 'lyspj'
}
</script>
<template>
  <BaseUploadLine 
    :workLine="props.workLine"
    :shelfNumber="props.shelfNumber"
    :loadGlass="props.loadGlass"
    :webSocketPath="props.webSocketPath"
    :loaderPicture="props.loaderPicture"
    :historicalTaskUrl="props.historicalTaskUrl"
    :showOutboundButton="false"
  />
</template>