<template>
  <b-navbar>
    <b-nav-form>
      <b-button @click="clickMaxi" variant="light" size="sm">
        <i v-if="maximize" class="fa fa-maximize"></i>
        <i v-else class="fa fa-down-left-and-up-right-to-center"></i>
      </b-button>
    </b-nav-form>
  </b-navbar>
</template>

<script lang="ts">
import { Component, Mixins } from "vue-property-decorator";
import TaskListMixin from "../mixins/TaskListMixin.vue";

@Component
export default class ExpandContract extends Mixins(TaskListMixin) {
  clickMaxi () {
    this.maximize = !this.maximize;
    this.$root.$emit("call-managerScreen", { maxi: this.maximize });
  }
}
</script>
