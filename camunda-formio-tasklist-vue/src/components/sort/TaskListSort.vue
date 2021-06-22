<template>
  <b-col id="cftf-dpdown-container" class="d-flex flex-row">
    <b-row
      class="cftf-dpdown-box mr-2 pr-3"
      v-for="(sort, idx) in sortList"
      :key="sort.sortBy"
    >
        <span
          v-if="sortList.length > 1"
          class="cftf-exit-button"
          @click="deleteSort(sort, idx)"
        >
          <i class="fa fa-times"></i>
        </span>
        <b-nav-item-dropdown
          :text="sortList[idx].label"
          v-b-tooltip.hover
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
          <i class="fa fa-chevron-up cftf-arrow" aria-hidden="true"></i>
        </a>
        <a v-else @click="toggleSort(idx)" href="#" title="Descending">
          <i class="fa fa-chevron-down cft-arrow" aria-hidden="true"></i>
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
import { Component, Prop, Vue } from "vue-property-decorator";
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  sortingList,
} from "../../services/utils";
import { Payload } from "../../services/TasklistTypes";
import TaskSortOptions from "../TaskListSortoptions.vue";
import { namespace } from "vuex-class";

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

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;

  private sortList = TASK_FILTER_LIST_DEFAULT_PARAM;
  private sortOptions: Array<object> = [];
  private updateSortOptions: Array<object> = [];
  private setupdateSortListDropdownindex = 0;

  getOptions (options: any) {
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
        optionsArray.push({ ...sortOption });
      }
    });
    return optionsArray;
  }

  addSort (sort: any) {
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

  updateSort (sort: any, index: number) {
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

  deleteSort (sort: any, index: number) {
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

  mounted () {
    this.sortOptions = this.getOptions(this.sortList);
  }
}
</script>
