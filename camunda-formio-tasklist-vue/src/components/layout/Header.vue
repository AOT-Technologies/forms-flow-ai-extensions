<template>
<<<<<<< HEAD
  <div class="header-container">
    <div class="dropdown mx-1">
      <button
        type="button"
        class="btn btn-primary dropdown-toggle"
        id="presetFiltersDropdownBtn"
        data-bs-toggle="dropdown"
        aria-expanded="false"
      >
        <i class="fa fa-wpforms mx-1"></i>
        Filters
      </button>
      <ul
        class="dropdown-menu"
        aria-labelledby="dropdownMenuLink"
      >
        <li>
          <a
            class="dropdown-item"
            v-for="(filter, idx) in filterList"
            :key="filter.id"
            href="#"
            @click="togglefilter(filter, idx)"
            :class="{'active': idx === activefilter}"
          >{{ filter.name }}</a>
        </li>
        <li v-if="!filterList.length">
          <a class="dropdown-item">No Filters found</a>
        </li>
      </ul>
=======
  <b-container fluid class="task-outer-container">
    <div class="main-filters my-2 mb-1">
      <div class="cft-filter-dropdown mx-2"> 
        <b-dropdown variant="primary" v-if="filterList.length>1 && disableComponents.filterList">
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
        </b-dropdown>
      </div>
      <FormListModal :token="token" :bpmApiUrl="bpmApiUrl" v-if="disableComponents.form" />
      <div>
        <TaskListSort  v-if="disableComponents.sort"
        :selectedfilterId="selectedfilterId"
        :perPage="perPage"
        :payload="payload"
        :defaultTaskSortBy="defaultTaskSortBy"
        :defaultTaskSortOrder="defaultTaskSortOrder"
        />
      </div>
>>>>>>> origin/disableprop
    </div>
    <FormListModal
      v-if="showFormsButton"
      class="mx-1"
      :token="token"
      :bpmApiUrl="bpmApiUrl"
    />
    <TaskListSort
      :selectedfilterId="selectedfilterId"
      :perPage="perPage"
      :payload="payload"
      :defaultTaskSortBy="defaultTaskSortBy"
      :defaultTaskSortOrder="defaultTaskSortOrder"
    />
  </div>
</template>

<script lang="ts">
import {
  Component, Mixins, Prop
} from "vue-property-decorator";
import {
  FilterPayload, Payload,SortPayload
} from "../../models";
import BaseMixin from "../../mixins/BaseMixin.vue";
import FormListModal from "../form/FormListModal.vue";
import TaskListSort from "../sort/TaskListSort.vue";
import {
  namespace
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    FormListModal,
    TaskListSort,
  },
})
export default class Header extends Mixins(BaseMixin) {
  @Prop() private perPage!: number;
  @Prop() private filterList!: FilterPayload[];
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;
  @Prop() private defaultTaskSortBy!: string
  @Prop() private defaultTaskSortOrder!: string ;
  @Prop ({
    type:Object,
    default:{
      filterList: true ,filterTask: true,form: true,sort: true
    }
  })private disableComponents!: SortPayload;

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;

  private activefilter = NaN;

  togglefilter(filter: FilterPayload, index: number) {
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
}
</script>

<style scoped>
.header-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  max-width: 100vw;
  background-color: white;
  font-family: inherit;
  margin-right: -4px;
  border-radius: 0.5rem;
  padding: 0.25rem 1rem;
}
</style>
