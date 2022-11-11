<template>
  <div class="row mx-0 task-list-search">
    <div class="col my-2">
      <div v-if="selectedSearchQueries.length">
        <TaskListSearchType />
        <div
          class="cftf-search-item-box mt-2"
          v-for="(query, index) in selectedSearchQueries"
          :key="query.label + index"
        >
          <TaskSearchItem
            :query="query"
            :index="index"
            :showSearchState="showSearchState"
            :showVariableValueState="showVariableValueState"
            :operator="operator"
            :selectedSearchQueries="selectedSearchQueries"
             :filterList="filterList"
             :selectedfilterId="selectedfilterId"
            @updateVariableInput="updateVariableInput"
            @show-VariableValue-Item="showVariableValueItem"
            @reject-Search-Value-Item="rejectSearchValueItem"
            @reject-Variable-Item="rejectVariableItem"
            @update-search-value="updateSearchInput"
            @show-search-value="showSearchValueItem"
          />
        </div>
      </div>
      <TaskListAddSearchIgnoreCase
        :isVariableTypeInSelectedSearchQuery="
          isVariableTypeInSelectedSearchQuery
        "
        @add-to-selected-search-query="addToSelectedSearchQuery"
      />
    </div>
  </div>
</template>


<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Emit, Mixins, Prop, Vue
} from "vue-property-decorator";
import {
  FilterSearchTypes,
  SEARCH_BOX_STATE,
  getFormattedQueryListParams,
  getISODateTime,
} from "../../services";
import {
  SearchOptionPayload,
  SearchQueryPayload,
} from "../../models";
import BaseMixin from "../../mixins/BaseMixin.vue";
import TaskListAddSearchIgnoreCase from "./TaskListAddSearchIgnoreCase.vue";
import TaskListSearchType from "./TaskListSearchType.vue";
import TaskSearchItem from "./TaskSearchItem.vue";
import {
  namespace
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    TaskListAddSearchIgnoreCase,
    TaskListSearchType,
    TaskSearchItem,
  },
})
export default class TaskListSearch extends Mixins(BaseMixin) {
  private selectedSearchQueries: SearchOptionPayload[] = [];
  private operator: string[] = [];
  private showSearchState: string[] = []; //States - a(?? state), i(enter input), s(show entered value)
  private showVariableValueState: string[] = [];
  private queryList: SearchQueryPayload = {
    taskVariables: [],
    processVariables: [],
  };
  private isVariableTypeInSelectedSearchQuery: boolean = false;
  @Prop() private  filterList: any;
  @Prop() private selectedfilterId ;
  @serviceFlowModule.Getter("getFormsFlowTaskSearchType")
  private getFormsFlowTaskSearchType: any;
  @serviceFlowModule.Getter("getVariableNameIgnoreCase")
  private getVariableNameIgnoreCase: any;
  @serviceFlowModule.Getter("getVariableValueIgnoreCase")
  private getVariableValueIgnoreCase: any;

  updateSearchQueryOperators(operator: any, index: number) {
    this.selectedSearchQueries[index].operator = this.operator[index];
    this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
    this.onSearchUpdateTaskListResult();
  }

  updateSearchInput(index: number) {
 
    Vue.set(this.showSearchState, index, SEARCH_BOX_STATE.INSERT);
  }

  showSearchValueItem(index: number) {
    Vue.set(this.showSearchState, index, SEARCH_BOX_STATE.SHOW);
  }

  rejectSearchValueItem(index: number) {
    Vue.set(this.showSearchState, index, SEARCH_BOX_STATE.START);
  }

  makeInputNull(index: number) {
    Vue.set(this.showSearchState, index, SEARCH_BOX_STATE.START);
  }

  updateVariableInput(index: number) {
    Vue.set(this.showVariableValueState, index, SEARCH_BOX_STATE.INSERT);
  }

  showVariableValueItem(index: number) {
    Vue.set(this.showVariableValueState, index, SEARCH_BOX_STATE.SHOW);
  }

  rejectVariableItem(index: number) {
    Vue.set(this.showVariableValueState, index, SEARCH_BOX_STATE.START);
  }

  addToSelectedSearchQuery(item: SearchOptionPayload) {
    item.index = this.selectedSearchQueries.length + 1;
    this.selectedSearchQueries = [...this.selectedSearchQueries, {
      ...item
    }];
    if (this.selectedSearchQueries === []) {
      this.operator[0] = item.operator;
      this.showSearchState[0] = SEARCH_BOX_STATE.START;
      this.showVariableValueState[0] = SEARCH_BOX_STATE.START;
      if (item.type === FilterSearchTypes.VARIABLES) {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    } else {
      this.operator[this.selectedSearchQueries.length - 1] = item.operator;
      this.showSearchState[this.selectedSearchQueries.length - 1] = SEARCH_BOX_STATE.START;
      this.showVariableValueState[this.selectedSearchQueries.length - 1] = SEARCH_BOX_STATE.START;
      if (item.type === FilterSearchTypes.VARIABLES) {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    }
  }

  deleteSearchQueryElement(query: SearchOptionPayload, index: number) {
    this.selectedSearchQueries.splice(index, 1);
    this.operator.splice(index, 1);
    this.showSearchState.splice(index, 1);
    this.showVariableValueState.splice(index, 1);

    /*To check if anymore selectedSearchSearchQueries with Variable type exists. If yes,
    show the variable type dropdown else hide it*/
    this.isVariableTypeInSelectedSearchQuery = false;
    for (const idx in this.selectedSearchQueries) {
      if (this.selectedSearchQueries[idx]?.type === FilterSearchTypes.VARIABLES) {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    }

    this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType,
      this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
    this.onSearchUpdateTaskListResult();
  }

  updateSearchQueryElement(searchitem: SearchOptionPayload, index: number) {
    if (
      searchitem.type === FilterSearchTypes.DATE
      && this.selectedSearchQueries[index].type !== FilterSearchTypes.DATE
    ) {
      this.makeInputNull(index);
    }
    searchitem.value = this.selectedSearchQueries[index].value;
    Vue.set(this.selectedSearchQueries, index, searchitem);
    this.operator[index] = this.selectedSearchQueries[index].operator;
    if (this.showSearchState[index] !== SEARCH_BOX_STATE.START) {
      this.setSearchQueryValue(this.selectedSearchQueries, index);
    }
  }

  setSearchQueryValue(selectedSearchQueries: SearchOptionPayload[], index: number) {
    /*Dates should be in ISO format for request object*/
    if (this.selectedSearchQueries[index].type === FilterSearchTypes.DATE) {
      this.selectedSearchQueries[index].value = getISODateTime(this.selectedSearchQueries[index].value);
    }
    this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
    this.onSearchUpdateTaskListResult();
  }

  @Emit("update-task-list")
  onSearchUpdateTaskListResult() {
    return this.queryList;
  }

  mounted() {
    this.$root.$on("call-deleteSearchQueryElement", (para: any) => {
      this.deleteSearchQueryElement(para.query, para.index);
    });

    this.$root.$on("call-updateTaskList", () => {
      this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
      this.onSearchUpdateTaskListResult();
    });

    this.$root.$on("call-updateSearchQueryElement", (para: any) => {
      this.updateSearchQueryElement(para.updateSearch, para.index);
    });

    this.$root.$on("call-updateSearchQueryOperators", (para: any) => {
      this.updateSearchQueryOperators(para.operator, para.index);
    });

    this.$root.$on("call-setSearchQueryValue", (para: any) => {
      this.setSearchQueryValue(para.item, para.index);
    });
  }

  beforeDestroy() {
    this.$root.$off("call-deleteSearchQueryElement");
    this.$root.$off("call-updateSearchQueryElement");
    this.$root.$off("call-updateSearchQueryOperators");
    this.$root.$off("call-updateTaskList");
    this.$root.$off("call-setSearchQueryValue");
  }
}
</script>

<style lang="scss" scoped>
.task-list-search {
  border-bottom: 1px solid #eee;
}
</style>
