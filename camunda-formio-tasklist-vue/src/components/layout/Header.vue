<template>
  <b-container fluid class="task-outer-container">
    <div class="main-filters my-2 mb-1">
      <div class="cft-filter-dropdown mx-2"> 
        <b-dropdown variant="primary">
            <template #button-content >
              <span><i class="fa fa-wpforms"> Filters</i></span>
            </template>
            <b-dropdown-item
              v-for="(filter, idx) in filterList"
              :key="filter.id"
              href="#"
              @click="togglefilter(filter, idx)"
              :active="idx === activefilter"
              :class="{ 'cft-filter-selected': idx === activefilter }"
            >
            <span class="font-weight-normal cft-filter-text">{{ filter.name }}</span>
            </b-dropdown-item>
          <b-dropdown-item v-if="!filterList.length">No Filters found</b-dropdown-item>
          </b-dropdown>
      </div>
      <FormListModal :token="token" :bpmApiUrl="bpmApiUrl" />
      <div class="cft-first">
        <div id="cftf-dpdown-container" class="ml-2">
          <div
            class="cftf-dpdown-box mr-2"
            v-for="(sort, idx) in sortList"
            :key="sort.sortBy"
          >
            <b-row>
              <b-col cols="1" v-b-tooltip.hover title="Remove Sorting Field">
                <span
                  v-if="sortList.length > 1"
                  class="cftf-exit-button"
                  @click="deleteSort(sort, idx)"
                >
                  <i class="fa fa-times"></i>
                </span>
              </b-col>
              <b-col cols="7">
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
              </b-col>
              <b-col cols="1" v-b-tooltip.hover title="Toggle Sort Order">
                <a
                  v-if="sort.sortOrder === 'asc'"
                  @click="toggleSort(idx)"
                  href="#"
                  title="Ascending"
                >
                  <i class="fa fa-chevron-up cftf-arrow" aria-hidden="true"></i>
                </a>
                <a v-else @click="toggleSort(idx)" href="#" title="Descending">
                  <i
                    class="fa fa-chevron-down cft-arrow"
                    aria-hidden="true"
                  ></i>
                </a>
              </b-col>
            </b-row>
          </div>
          <TaskSortOptions
            :sortOptions="sortOptions"
            :updateSortOptions="updateSortOptions"
            @add-sort="addSort"
          >
          </TaskSortOptions>
        </div>
      </div>
    </div>
  </b-container>
</template>

<script lang="ts">
import { Component, Mixins, Prop } from "vue-property-decorator";
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  sortingList,
} from "../../services/utils";
import BaseMixin from "../mixins/BaseMixin.vue";
import { FilterPayload } from "../../models/FilterPayload";
import FormListModal from "../FormListModal.vue";
import { Payload } from "../../models/Payload";
import TaskSortOptions from "../TaskListSortoptions.vue";
import { namespace } from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    FormListModal,
    TaskSortOptions,
  },
})
export default class Header extends Mixins(BaseMixin) {
  @Prop() private perPage!: number;
  @Prop() private filterList!: FilterPayload[];
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;

  private activefilter = NaN;
  private sortList = TASK_FILTER_LIST_DEFAULT_PARAM;
  private sortOptions: Array<object> = [];
  private userList: Array<object> = [];
  private updateSortOptions: Array<object> = [];
  private setupdateSortListDropdownindex = 0;

  togglefilter (filter: FilterPayload, index: number) {
    this.activefilter = index;
    this.$root.$emit("call-fetchTaskListCount", {
      filterId: filter.id,
      requestData: this.payload,
    });
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: filter.id,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

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

  getActivefilterId (selectedfilterId: string) {
    this.filterList.forEach((filter, index) => {
      if(filter.id=== selectedfilterId) {
        this.activefilter = index;
      }
    });
  }

  mounted () {
    this.sortOptions = this.getOptions(this.sortList);
  }

  updated () {
    if(this.selectedfilterId) {this.getActivefilterId(this.selectedfilterId); }
  }

}
</script>

<style scoped>
.cft-filter-text {
  font-size: 16px;
}
</style>
