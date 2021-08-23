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
      <div>
        <TaskListSort
        :selectedfilterId="selectedfilterId"
        :perPage="perPage"
        :payload="payload"
        />
      </div>
    </div>
  </b-container>
</template>

<script lang="ts">
import { Component, Mixins, Prop } from "vue-property-decorator";
import BaseMixin from "../mixins/BaseMixin.vue";
import { FilterPayload } from "../../models/FilterPayload";
import FormListModal from "../form/FormListModal.vue";
import { Payload } from "../../models/Payload";
import TaskListSort from "../sort/TaskListSort.vue";
import { namespace } from "vuex-class";

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

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;

  private activefilter = NaN;

  togglefilter (filter: any, index: number) {
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
.cft-filter-text {
  font-size: 16px;
}
</style>
