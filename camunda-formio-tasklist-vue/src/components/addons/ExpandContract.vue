<template> 
<b-navbar >
    <b-nav-form>
    <b-button @click="clickMaxi" variant="light" size="sm"> 
        <i v-if="maximize" class="bi bi-arrows-angle-expand"></i>
        <i v-else class="bi bi-arrows-angle-contract"></i>
    </b-button>
    </b-nav-form>
</b-navbar>
</template>

<script lang="ts">
import { Component, Mixins } from 'vue-property-decorator';
import TaskListMixin from  '../mixins/TaskListMixin.vue';

@Component
export default class ExpandContract  extends Mixins(TaskListMixin) {

  clickMaxi () {
    this.maximize = !this.maximize
    this.$root.$emit('call-managerScreen', {maxi: this.maximize});
  }
}
</script>
