<script lang="ts" setup>
import BaseUploadLine from './basicFilmCamera.vue'
const props = {
  lineNumber: 2,
  shelfNumber: 3,
  loadGlass: 'two',
  webSocketPath: 'loadGlassTwo',
  historicalTaskUrl: 'loadHistory',
  loaderPicture: 'lyspj'
}
</script>
<template>
  <BaseUploadLine 
    :lineNumber="props.lineNumber"
    :shelfNumber="props.shelfNumber"
    :loadGlass="props.loadGlass"
    :webSocketPath="props.webSocketPath"
    :historicalTaskUrl="props.historicalTaskUrl"
    :loaderPicture="props.loaderPicture"
  />
</template>