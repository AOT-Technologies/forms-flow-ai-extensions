<template>
  <b-col id="cftf-dpdown-container" class="d-flex flex-row pl-2">
    <b-row
      class="cftf-dpdown-box mx-1 border"
      v-for="(sort, idx) in sortList"
      :key="sort.sortBy"
    >
      <span
        v-if="sortList.length > 1"
        class="cftf-exit-button ml-1"
        @click="deleteSort(sort, idx)"
        v-b-tooltip.hover title="Remove Sorting Field"
      >
        <i class="fa fa-times"></i>
      </span>
      <b-nav-item-dropdown
        :text="sortList[idx].label"
        v-b-tooltip.hover
         class="pl-1"
        title="Click To Change Field for Sorting"
        :no-caret="true"
      >
        <span v-if="sortOptions.length">
          <b-dropdown-item-button
            v-for="sort in sortOptions"
            :key="sort.sortBy"
            @click="updateSort(sort, idx)"
            >{{ sort.label }}
          </b-dropdown-item-button>
        </span>
        <b-dropdown-item-button v-else
          >Sorry, no more fields to sortBy.</b-dropdown-item-button
        >
      </b-nav-item-dropdown>
      <a
        v-if="sort.sortOrder === 'asc'"
        @click="toggleSort(idx)"
        href="#"
        title="Ascending"
      >
        <i class="fa fa-chevron-up cftf-arrow ml-2" aria-hidden="true"></i>
      </a>
      <a v-else @click="toggleSort(idx)" href="#" title="Descending">
        <i class="fa fa-chevron-down cft-arrow ml-2" aria-hidden="true"></i>
      </a>
    </b-row>
    <TaskSortOptions
      :sortOptions="sortOptions"
      :updateSortOptions="updateSortOptions"
      @add-sort="addSort"
    >
    </TaskSortOptions>
  </b-col>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklist.scss";
import {
  Component, Prop, Vue 
} from "vue-property-decorator";
import {
  Payload,
  TASK_FILTER_LIST_DEFAULT_PARAM_CREATED,
  TASK_SORT_DEFAULT_ASSINGEE,
  TASK_SORT_DEFAULT_DUE_DATE,
  TASK_SORT_DEFAULT_FOLLOW_UP_DATE,
  TASK_SORT_DEFAULT_PARAM_NAME,
  TASK_SORT_DEFAULT_PRIORITY,
  TASK_SORTING_FULL_LIST,
  TaskListSortType,
  SORT_ORDER
} from "../../models";
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
  @Prop() private taskSortBy!: string
  @Prop() private taskSortOrder!: string

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  private setFormsFlowTaskCurrentPage: any;
  private sortList: TaskListSortType[] = [];
  private sortOptions: TaskListSortType[] = [];
  private updateSortOptions: TaskListSortType[] = [];

  getOptions (options: TaskListSortType[]) {
    const optionsArray: {
      sortOrder: string;
      label: string;
      sortBy: string;
    }[] = [];

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

  afterSortApiCalls() {
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


  addSort (sort: TaskListSortType) {
    this.sortList.push(sort);
    if (this.sortList.length === TASK_SORTING_FULL_LIST.length) {
      this.updateSortOptions = this.sortOptions;
      this.sortOptions = [];
    } else {
      this.sortOptions = this.getOptions(this.sortList);
    }
    
    this.afterSortApiCalls();
  }

  updateSort (sort: TaskListSortType, index: number) {
    this.sortList[index].label = sort.label;
    this.sortList[index].sortBy = sort.sortBy;
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    
    this.afterSortApiCalls();
  }

  deleteSort (sort: TaskListSortType, index: number) {
    this.sortList.splice(index, 1);
    this.updateSortOptions = [];
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    
    this.afterSortApiCalls();
  }

  toggleSort (index: number) {
    if (this.sortList[index].sortOrder === "asc")
      this.sortList[index].sortOrder = "desc";
    else {
      this.sortList[index].sortOrder = "asc";
    }
    this.payload["sorting"] = this.sortList;
    
    this.afterSortApiCalls();
  }

  getDefaultTaskSortOption () {
    /**
   * "created" is the default TaskSortBy and "desc" is the deafult TaskSortOrder
   */

    if (this.taskSortBy === TASK_SORT_DEFAULT_DUE_DATE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_DUE_DATE];
    }

    else if (this.taskSortBy === TASK_SORT_DEFAULT_FOLLOW_UP_DATE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.taskSortBy === TASK_SORT_DEFAULT_PARAM_NAME.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.taskSortBy === TASK_SORT_DEFAULT_ASSINGEE.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else if (this.taskSortBy === TASK_SORT_DEFAULT_PRIORITY.sortBy) {
      this.sortList = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }

    else {
      this.sortList = [TASK_FILTER_LIST_DEFAULT_PARAM_CREATED];
    }

    if (this.taskSortOrder === SORT_ORDER.ASCENDING){
      this.sortList[0].sortOrder = SORT_ORDER.ASCENDING;
    }
    this.payload.sorting = this.sortList;
  }

  mounted () {
    this.getDefaultTaskSortOption();
    this.sortOptions = this.getOptions(this.sortList);
  }
}
</script>
