<template>
  <button
    @click="clickMaxi"
    class="btn btn-secondary expand-btn"
    :class="{expanded: !maximize}"
  >
    <i
      v-if="maximize"
      class="fa fa-chevron-left"
      v-b-tooltip.hover
      title="Click to expand the form"
    ></i>
    <i
      v-else
      class="fa fa-chevron-right"
      v-b-tooltip.hover
      title="Click to see the task list"
    ></i>
  </button>
</template>

<script lang="ts">
import {
  Component, Mixins
} from "vue-property-decorator";
import TaskListMixin from "../../mixins/TaskListMixin.vue";

@Component
export default class ExpandContract extends Mixins(TaskListMixin) {
  clickMaxi() {
    this.maximize = !this.maximize;
    this.$root.$emit("call-managerScreen", {
      maxi: this.maximize
    });
  }
}
</script>

<style lang="scss" scoped>
.expand-btn {
  position: absolute;
  margin-top: 12px;
  z-index: 9;
  margin-left: -32px;
  border-radius: 50rem;
  min-height: 32px;
  &.expanded {
    margin-left: 0px;
  }
}
</style>
