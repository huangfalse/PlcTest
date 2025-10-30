<script lang="ts" setup>
import BaseUploadLine from './basicRawFilmStorage.vue'
const props = {
  rawGlassPath: 'rawGlassLayoutLY',
}
</script>
<template>
  <BaseUploadLine
    :rawGlassPath="props.rawGlassPath"
    :showOutboundButton="false"
    :showOutboundButtonLY="true"
  />
</template>