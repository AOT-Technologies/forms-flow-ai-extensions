<template>
  <div class="d-flex align-items-center flex-wrap">
    <div
      class="d-flex align-items-center sort-by-tab m-2"
      v-for="(sort, idx) in sortList"
      :key="sort.sortBy"
    >
      <button
        v-if="sortList.length > 1"
        class="btn btn-outline-danger btn-sm"
        @click="deleteSort(sort, idx)"
        title="Remove Sorting Field"
      >
        <i
          class="fa fa-times"
          aria-hidden="true"
        ></i>
      </button>
      <div class="nav-item dropdown">
        <a
          class="nav-link dropdown-toggle px-2"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
          title="Click To Change Field for Sorting"
        >
          {{ sort.label }}
        </a>
        <ul class="dropdown-menu">
          <li v-if="sortOptions.length">
            <a
              class="dropdown-item"
              v-for="sortOption in sortOptions"
              :key="sortOption.sortBy"
              @click="updateSort(sortOption, idx)"
            >{{ sortOption.label }}</a>
          </li>
          <li v-else>
            <a class="dropdown-item">Sorry, no more fields to sort</a>
          </li>
        </ul>
      </div>
      <button
        class="btn btn-outline-primary btn-sm"
        v-if="sort.sortOrder === 'asc'"
        @click="toggleSort(idx)"
        title="Ascending"
      >
        <i
          class="fa fa-arrow-up"
          aria-hidden="true"
        ></i>
      </button>
      <button
        class="btn btn-outline-primary btn-sm"
        v-else
        @click="toggleSort(idx)"
        title="Descending"
      >
        <i
          class="fa fa-arrow-down"
          aria-hidden="true"
        ></i>
      </button>
    </div>
    <TaskSortOptions
      v-if="sortList.length !== fullTaskSortLength"
      :sortOptions="sortOptions"
      @add-sort="addSort"
    >
    </TaskSortOptions>
  </div>
</template>

<script lang="ts">
import {
  Component, Prop, Vue
} from "vue-property-decorator";
import {
  Payload,
  TaskListSortType,
} from "../../models";
import {
  SORT_ORDER,
  TASK_FILTER_LIST_DEFAULT_PARAM_CREATED,
  TASK_SORTING_FULL_LIST,
  TASK_SORT_DEFAULT_ASSINGEE,
  TASK_SORT_DEFAULT_DUE_DATE,
  TASK_SORT_DEFAULT_FOLLOW_UP_DATE,
  TASK_SORT_DEFAULT_PARAM_NAME,
  TASK_SORT_DEFAULT_PRIORITY,
} from "../../services";
import TaskSortOptions from "../sort/TaskListSortoptions.vue";
import {
  namespace
} from "vuex-class";


const serviceFlowModule = namespace("serviceFlowModule");
@Component({
  components: {
    TaskSortOptions,
  },
})
export default class TaskListSort extends Vue {
  @Prop() private perPage!: number;
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;
  @Prop() private defaultTaskSortBy!: string
  @Prop() private defaultTaskSortOrder!: string

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  private setFormsFlowTaskCurrentPage: any;
  private sortList: TaskListSortType[] = [];
  private sortOptions: TaskListSortType[] = [];
  private updateSortOptions: TaskListSortType[] = [];

  getOptions(options: TaskListSortType[]) {
    const optionsArray: TaskListSortType[] = [];
    TASK_SORTING_FULL_LIST.forEach((sortOption) => {
      if (
        !options.some(
          (option: { sortBy: string }) => option.sortBy === sortOption.sortBy
        )
      ) {
        optionsArray.push({
          ...sortOption
        });
      }
    });
    return optionsArray;
  }

  get fullTaskSortLength() {
    return TASK_SORTING_FULL_LIST.length;
  }

  updateOnSortAction() {
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }


  addSort(sort: TaskListSortType) {
    this.sortList.push(sort);
    if (this.sortList.length === TASK_SORTING_FULL_LIST.length) {
      this.updateSortOptions = this.sortOptions;
      this.sortOptions = [];
    } else {
      this.sortOptions = this.getOptions(this.sortList);
    }

    this.updateOnSortAction();
  }

  updateSort(sort: TaskListSortType, index: number) {
    if (this.sortList[index].sortOrder === SORT_ORDER.ASCENDING) {
      this.sortList[index] = sort;
      this.sortList[index].sortOrder = SORT_ORDER.ASCENDING;
    }
    else {
      this.sortList[index] = sort;
    }
    this.sortOptions = this.getOptions(this.sortList);
    this.payload.sorting = this.sortList;

    this.updateOnSortAction();
  }

  deleteSort(sort: TaskListSortType, index: number) {
    this.sortList.splice(index, 1);
    this.updateSortOptions = [];
    this.sortOptions = this.getOptions(this.sortList);
    this.payload.sorting = this.sortList;

    this.updateOnSortAction();
  }

  toggleSort(index: number) {
    if (this.sortList[index].sortOrder === SORT_ORDER.ASCENDING)
      this.sortList[index].sortOrder = SORT_ORDER.DESCENDING;
    else {
      this.sortList[index].sortOrder = SORT_ORDER.ASCENDING;
    }
    this.payload.sorting = this.sortList;

    this.updateOnSortAction();
  }

  getDefaultTaskSortOption() {
    /**
   * "created" is the default TaskSortBy and "desc" is the deafult TaskSortOrder
   */
    if (this.defaultTaskSortBy === TASK_SORT_DEFAULT_DUE_DATE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_DUE_DATE];
    }

    else if (this.defaultTaskSortBy === TASK_SORT_DEFAULT_FOLLOW_UP_DATE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.defaultTaskSortBy === TASK_SORT_DEFAULT_PARAM_NAME.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.defaultTaskSortBy === TASK_SORT_DEFAULT_ASSINGEE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.defaultTaskSortBy === TASK_SORT_DEFAULT_PRIORITY.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else {
      this.sortList = [TASK_FILTER_LIST_DEFAULT_PARAM_CREATED];
    }

    if (this.defaultTaskSortOrder === SORT_ORDER.ASCENDING) {
      this.sortList[0].sortOrder = SORT_ORDER.ASCENDING;
    }
    this.payload.sorting = this.sortList;
  }

  mounted() {
    this.getDefaultTaskSortOption();
    this.sortOptions = this.getOptions(this.sortList);
  }
}
</script>

<style lang="scss" scoped>
.sort-by-tab {
  background: #ebebeb;
  border-radius: 8px;
  padding: 0.25rem 0.75rem;
  .btn {
    min-height: unset;
    border-radius: 6px;
  }
}
</style>
