<script lang="ts" setup>
import BaseUploadLine from './basicRawFilmStorage.vue'
const props = {
  rawGlassPath: 'rawGlassLayout',
}
</script>
<template>
  <BaseUploadLine
    :rawGlassPath="props.rawGlassPath"
    :showOutboundButton="true"
    :showOutboundButtonLY="false"
  />
</template>