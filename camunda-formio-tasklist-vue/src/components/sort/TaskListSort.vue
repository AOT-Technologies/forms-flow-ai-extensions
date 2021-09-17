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
  TASK_FILTER_LIST_DEFAULT_PARAM_CREATED,
  TASK_SORT_DEFAULT_ASSINGEE,
  TASK_SORT_DEFAULT_DUE_DATE,
  TASK_SORT_DEFAULT_FOLLOW_UP_DATE,
  TASK_SORT_DEFAULT_PARAM_NAME,
  TASK_SORT_DEFAULT_PRIORITY,
  TaskListSortType,
} from "../../models";
import {
  Payload 
} from "../../models/Payload";
import TaskSortOptions from "../sort/TaskListSortoptions.vue";
import {
  namespace 
} from "vuex-class";
import {
  sortingList 
} from "../../services";

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

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;
  private sortList: TaskListSortType[] = [];
  private sortOptions: TaskListSortType[] = [];
  private updateSortOptions: TaskListSortType[] = [];
  private setupdateSortListDropdownindex = 0;

  getOptions (options: TaskListSortType[]) {
    const optionsArray: {
      sortOrder: string;
      label: string;
      sortBy: string;
    }[] = [];

    sortingList.forEach((sortOption) => {
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


  addSort (sort: TaskListSortType) {
    this.sortList.push(sort);
    if (this.sortList.length === sortingList.length) {
      this.updateSortOptions = this.sortOptions;
      this.sortOptions = [];
    } else {
      this.sortOptions = this.getOptions(this.sortList);
    }
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

  updateSort (sort: TaskListSortType, index: number) {
    this.sortList[index].label = sort.label;
    this.sortList[index].sortBy = sort.sortBy;
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
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

  deleteSort (sort: TaskListSortType, index: number) {
    this.sortList.splice(index, 1);
    this.updateSortOptions = [];
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
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

  toggleSort (index: number) {
    if (this.sortList[index].sortOrder === "asc")
      this.sortList[index].sortOrder = "desc";
    else {
      this.sortList[index].sortOrder = "asc";
    }
    this.payload["sorting"] = this.sortList;
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
  getTaskSortOption () {
    if (TASK_SORT_DEFAULT_DUE_DATE.sortBy === this.taskSortBy){
      this.payload.sorting = [TASK_SORT_DEFAULT_DUE_DATE];
    }
    else if (TASK_SORT_DEFAULT_FOLLOW_UP_DATE.sortBy === this.taskSortBy){
      this.payload.sorting = [TASK_SORT_DEFAULT_FOLLOW_UP_DATE];
    }
    else if (TASK_SORT_DEFAULT_PARAM_NAME.sortBy === this.taskSortBy){
      this.payload.sorting = [TASK_SORT_DEFAULT_PARAM_NAME];
    }
    else if (TASK_SORT_DEFAULT_ASSINGEE.sortBy === this.taskSortBy){
      this.payload.sorting = [TASK_SORT_DEFAULT_ASSINGEE];
    }
    else if (TASK_SORT_DEFAULT_PRIORITY.sortBy === this.taskSortBy){
      this.payload.sorting = [TASK_SORT_DEFAULT_PRIORITY];
    } else 
    {
      // created is the default task sort
      this.payload.sorting = [TASK_FILTER_LIST_DEFAULT_PARAM_CREATED];
    }
    this.sortList = this.payload.sorting;
  }
  mounted () {
    this.getTaskSortOption();
    this.sortOptions = this.getOptions(this.sortList);
  }
}
</script>
