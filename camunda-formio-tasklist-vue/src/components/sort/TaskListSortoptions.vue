<template>
  <b-nav-item-dropdown
    :no-caret="true"
    v-b-tooltip.hover
    title="Add New Field For Sorting"
  >
    <template slot="button-content">
      <i class="fa fa-plus fa-sm click-element cftf-add-sorting mt-1"/>
    </template>
    <b-dropdown-item-button
      v-for="s in sortOptions"
      :key="s.sortBy"
      @click="addSort(s)"
    >
      {{ s.label }}
    </b-dropdown-item-button>
  </b-nav-item-dropdown>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklist.scss";
import {
  Component, Emit, Prop, Vue 
} from "vue-property-decorator";
import {
  TaskListSortType 
} from "../../models";

@Component
export default class TaskSortOptions extends Vue {
  @Prop() private sortOptions!: TaskListSortType[];

  @Emit()
  addSort (sort: TaskListSortType) {
    return sort;
  }
}
</script>
