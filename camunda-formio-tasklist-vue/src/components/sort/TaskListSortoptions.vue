<template>
  <div class="nav-item dropdown">
    <a
      class="nav-link dropdown-toggle px-2"
      role="button"
      data-bs-toggle="dropdown"
      aria-expanded="false"
      title="Add New Field For Sorting"
    >
      <i class="fa fa-plus" />
    </a>
    <ul class="dropdown-menu">
      <li v-if="sortOptions.length">
        <a
          class="dropdown-item"
          v-for="s in sortOptions"
          :key="s.sortBy"
          @click="addSort(s)"
        >{{ s.label }}</a>
      </li>
      <li v-else>
        <a class="dropdown-item">Sorry, no more fields to sort</a>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
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
  addSort(sort: TaskListSortType) {
    return sort;
  }
}
</script>
