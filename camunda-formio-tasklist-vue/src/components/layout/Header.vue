<template>
  <b-container fluid class="task-outer-container">
    <div class="main-filters my-2 mb-1">
      <div class="cft-filter-dropdown mx-2">
        <b-nav-item-dropdown class="cft-nav-backgroup mr-0" :no-caret="true">
          <template slot="button-content">
          <span>
            <h4 class="cft-filter-header"><i class="fa fa-wpforms"/> Filters</h4>
          </span>
          </template>
          <span v-if="filterList.length">
            <b-dropdown-item
              v-for="(filter, idx) in filterList"
              :key="filter.id"
              href="#"
              @click="togglefilter(filter, idx)"
              :class="{ 'cft-filter-selected': idx == activefilter }"
            >
              {{ filter.name }}
            </b-dropdown-item>
          </span>
          <b-dropdown-item v-else>No Filters found</b-dropdown-item>
        </b-nav-item-dropdown>
      </div>
      <FormListModal :token="token" :bpmApiUrl="bpmApiUrl" />
    </div>
  </b-container>
</template>

<script lang="ts">
import { Component, Mixins, Prop } from "vue-property-decorator";
import BaseMixin from "../mixins/BaseMixin.vue";
import FormListModal from "../FormListModal.vue";
import { Payload } from "../../services/TasklistTypes";
import { namespace } from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    FormListModal,
  },
})
export default class Header extends Mixins(BaseMixin) {
  @Prop() private perPage!: number;
  @Prop() private filterList!: Array<string>;
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;

  private activefilter = 0;

  togglefilter (filter: any, index: number) {
    this.activefilter = index;
    this.$root.$emit("call-fetchTaskList", {
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
