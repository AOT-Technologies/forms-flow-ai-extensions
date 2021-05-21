<template> 
  <b-container fluid class="task-outer-container">
    <div class="main-filters my-2 mb-1">
      <div class="cft-filter-dropdown mx-2">
          <b-nav-item-dropdown class="cft-nav-backgroup mr-0">
            <template slot="button-content">
              <i class="bi bi-filter-square"/>
          </template>
            <span v-if="filterList.length">
              <b-dropdown-item v-for="(filter, idx) in filterList" 
                :key="filter.id" 
                href="#"
                @click="togglefilter(filter, idx)"
                :class="{'cft-filter-selected': idx == activefilter}"
                >
                {{filter.name}}
              </b-dropdown-item>
            </span>
            <b-dropdown-item v-else >No Filters found</b-dropdown-item>
          </b-nav-item-dropdown>
        </div>
        <FormListModal :token="token" :bpmApiUrl="bpmApiUrl"/>
        <div class="cft-first">
        <div id="cftf-dpdown-container" class="mx-2">
          <div class="cftf-dpdown-box mr-2" v-for="(sort, idx) in sortList" :key="sort.sortBy">
            <b-row>
              <b-col cols="1">
                <span v-if="sortList.length>1"
                  class="cftf-exit-button"
                  title="Remove Sorting" 
                  @click="deleteSort(sort, idx)"
                >
                    <i class="bi bi-x"></i>
                </span>
              </b-col>
              <b-col cols="7">
                <b-nav-item-dropdown :text=sortList[idx].label>
                  <span v-if="sortOptions.length">
                    <b-dropdown-item-button
                      v-for="s in sortOptions"
                      :key="s.sortBy"
                      @click="updateSort(s,idx)"
                    >{{s.label}}
                    </b-dropdown-item-button>
                  </span>
                  <b-dropdown-item-button v-else>Sorry, no more fields to sortBy.</b-dropdown-item-button>
                </b-nav-item-dropdown>
              </b-col>
              <b-col cols="1">
                  <a v-if="sort.sortOrder==='asc'" @click="toggleSort(idx)" href="#" title="Ascending">
                    <i class="bi bi-chevron-up cftf-arrow"></i>
                  </a>
                  <a v-else @click="toggleSort(idx)"  href="#" title="Descending">
                    <i class="bi bi-chevron-down cftf-arrow"></i>
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
import { Component, Mixins, Prop} from 'vue-property-decorator'
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  sortingList,
} from '../../services/utils';
import BaseMixin from '../mixins/BaseMixin.vue';
import FormListModal from '../FormListModal.vue';
import {Payload} from '../../services/TasklistTypes';
import TaskSortOptions from '../TaskListSortoptions.vue';
import { namespace } from 'vuex-class';

const serviceFlowModule = namespace('serviceFlowModule')

@Component({
  components: {
    FormListModal,
    TaskSortOptions
  }
})
export default class Header extends Mixins(BaseMixin) {
  @Prop() private perPage !: number;
  @Prop() private filterList !: Array<string>;
  @Prop() private selectedfilterId !: string;
  @Prop() private payload !: Payload;

  
  @serviceFlowModule.Getter('getFormsFlowTaskCurrentPage') private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation('setFormsFlowTaskCurrentPage') public setFormsFlowTaskCurrentPage: any;

  private activefilter = 0;
  private sortList = TASK_FILTER_LIST_DEFAULT_PARAM;
  private sortOptions: Array<object> = [];
  private userList: Array<object> = [];
  private updateSortOptions: Array<object> = [];
  private setupdateSortListDropdownindex = 0;


  togglefilter (filter: any, index: number) {
    this.activefilter = index;
    this.$root.$emit('call-fetchTaskList', 
      {filterId: filter.id, requestData: this.payload}
    );
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    this.$root.$emit('call-fetchPaginatedTaskList', {
      filterId: filter.id,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage-1)*this.perPage,
      maxResults: this.perPage
    })
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
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    this.$root.$emit('call-fetchPaginatedTaskList', {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage-1)*this.perPage,
      maxResults: this.perPage
    })
  }

  updateSort (sort: any, index: number) {
    this.sortList[index].label = sort.label;
    this.sortList[index].sortBy = sort.sortBy;

    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    this.$root.$emit('call-fetchPaginatedTaskList', {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage-1)*this.perPage,
      maxResults: this.perPage
    })
  }

  deleteSort (sort: any, index: number) {
    this.sortList.splice(index, 1);
    this.updateSortOptions = [];
    this.sortOptions = this.getOptions(this.sortList);
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    this.$root.$emit('call-fetchPaginatedTaskList', {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage-1)*this.perPage,
      maxResults: this.perPage
    })
  }

  toggleSort (index: number) {
    if (this.sortList[index].sortOrder === "asc")
      this.sortList[index].sortOrder = "desc";
  
    else {
      this.sortList[index].sortOrder = "asc";
    }
    this.payload["sorting"] = this.sortList;
    this.setFormsFlowTaskCurrentPage(1);
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    this.$root.$emit('call-fetchPaginatedTaskList', {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage-1)*this.perPage,
      maxResults: this.perPage
    })
  }

  mounted () {
    this.sortOptions = this.getOptions(this.sortList);
  }

}
</script>
