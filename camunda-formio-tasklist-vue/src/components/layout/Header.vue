<template>
  <div class="ctf-header-container">
    <div class="dropdown mx-1">
      <button
         v-if="!disableOption.filterList"
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
    </div>
    <FormListModal
      v-if="!disableOption.form"
      class="mx-1"
      :token="token"
      :bpmApiUrl="bpmApiUrl"
    />
    <TaskListSort
      v-if="!disableOption.sort"
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
  DisableComponentPropPayload, FilterPayload, Payload,
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
  @Prop() private disableOption!: DisableComponentPropPayload;

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
.ctf-header-container {
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
