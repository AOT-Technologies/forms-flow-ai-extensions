<template>
  <div class="cft-input-search-filter px-2">
    <b-col class="cft-filter-container" xl="12" lg="12" md="12" sm="12">
      <div v-if="selectedSearchQueries.length">
        <TaskListSearchType />
        <b-col
          class="cftf-search-item-box mr-2"
          v-for="(query, index) in selectedSearchQueries"
          :key="query.label + index"
        >
          <TaskSearchItem
            :query="query"
            :index="index"
            :searchListElements="searchListElements"
            :showSearchstate="showSearchstate"
            :showVariableValue="showVariableValue"
            :operator="operator"
            :selectedSearchQueries="selectedSearchQueries"
            @updatevariableinput="updatevariableinput"
            @show-VariableValue-Item="showVariableValueItem"
            @update-search-value="updatesearchinput"
            @show-search-value="showsearchValueItem"
          />
        </b-col>
      </div>
      <TaskListAddSearchIgnoreCase
        :isVariableTypeInSelectedSearchQuery="
          isVariableTypeInSelectedSearchQuery
        "
        :searchListElements="searchListElements"
        @add-to-selected-search-query="addToSelectedSearchQuery"
      />
    </b-col>
  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Emit, Vue 
} from "vue-property-decorator";
import {
  FilterSearchTypes,
  SEARCH_BOX_STATE,
  getFormattedQueryListParams,
  getISODateTime,
  taskSearchFilters,
} from "../../services";
import {
  SearchOptionPayload,
  SearchQueryPayload,
} from "../../models";
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


export default class TaskListSearch extends Vue {

  private searchListElements: SearchOptionPayload[] = taskSearchFilters;
  private selectedSearchQueries: SearchOptionPayload[] = [];
  private operator: string[] = [];
  private showSearchstate: Array<string> = []; //3 states - a, i, s
  private showVariableValue: Array<string> = [];
  private queryList: SearchQueryPayload = {
    taskVariables: [],
    processVariables: [],
  };
  private isVariableTypeInSelectedSearchQuery: boolean = false;

  @serviceFlowModule.Getter("getFormsFlowTaskSearchType")
  private getFormsFlowTaskSearchType: any;
  @serviceFlowModule.Getter("getVariableNameIgnoreCase")
  private getVariableNameIgnoreCase: any;
  @serviceFlowModule.Getter("getVariableValueIgnoreCase")
  private getVariableValueIgnoreCase: any;

  updateSearchQueryOperators (operator: any, index: number) {
    this.selectedSearchQueries[index].operator = this.operator[index];
    this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
    this.onSearchUpdateTasklistResult();
  }

  updatesearchinput (index: number) {
    this.selectedSearchQueries[index].value = "";
    Vue.set(this.showSearchstate, index, SEARCH_BOX_STATE.INSERT);
  }

  showsearchValueItem (index: number) {
    Vue.set(this.showSearchstate, index, SEARCH_BOX_STATE.SHOW);
  }

  makeInputNull (index: number) {
    Vue.set(this.showSearchstate, index, SEARCH_BOX_STATE.START);
  }

  updatevariableinput (index: number) {
    Vue.set(this.showVariableValue, index, SEARCH_BOX_STATE.INSERT);
  }

  showVariableValueItem (index: number) {
    Vue.set(this.showVariableValue, index, SEARCH_BOX_STATE.SHOW);
  }

  addToSelectedSearchQuery (item: any) {
    this.selectedSearchQueries.push(item);
    if (this.selectedSearchQueries === []) {
      this.operator[0] = item.operator;
      this.showSearchstate[0] = SEARCH_BOX_STATE.START;
      this.showVariableValue[0] = SEARCH_BOX_STATE.START;
      if (item.type === FilterSearchTypes.VARIABLES) {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    } else {
      this.operator[this.selectedSearchQueries.length - 1] = item.operator;
      this.showSearchstate[this.selectedSearchQueries.length - 1] = SEARCH_BOX_STATE.START;
      this.showVariableValue[this.selectedSearchQueries.length - 1] = SEARCH_BOX_STATE.START;
      if (item.type === FilterSearchTypes.VARIABLES) {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    }
  }

  deleteSearchQueryElement (query: any, index: number) {
    this.selectedSearchQueries.splice(index, 1);
    this.operator.splice(index, 1);

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
    this.onSearchUpdateTasklistResult();
  }

  updateSearchQueryElement (searchitem: any, index: number) {
    if (
      searchitem.type === FilterSearchTypes.DATE
      && this.selectedSearchQueries[index].type !== FilterSearchTypes.DATE
    ) {
      this.makeInputNull(index);
    }
    searchitem.value = this.selectedSearchQueries[index].value;
    Vue.set(this.selectedSearchQueries, index, searchitem);
    this.operator[index] = this.selectedSearchQueries[index].operator;
    if (this.showSearchstate[index] !== SEARCH_BOX_STATE.START) {
      this.setSearchQueryValue(this.selectedSearchQueries, index);
    }
  }

  setSearchQueryValue (selectedSearchQueries: SearchOptionPayload[], index: number) {
    /*Dates should be in ISO format for request object*/
    if(this.selectedSearchQueries[index].type === FilterSearchTypes.DATE) {
      this.selectedSearchQueries[index].value = getISODateTime(this.selectedSearchQueries[index].value);
    }
    this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
    this.onSearchUpdateTasklistResult();
  }

  @Emit("update-task-list")
  onSearchUpdateTasklistResult () {
    return this.queryList;
  }

  mounted () {
    this.$root.$on("call-deleteSearchQueryElement", (para: any) => {
      this.deleteSearchQueryElement(para.query, para.index);
    });

    this.$root.$on("call-updateTaskList", () => {
      this.queryList = getFormattedQueryListParams(this.selectedSearchQueries, this.getFormsFlowTaskSearchType, this.getVariableNameIgnoreCase, this.getVariableValueIgnoreCase);
      this.onSearchUpdateTasklistResult();
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

  beforeDestroy () {
    this.$root.$off("call-deleteSearchQueryElement");
    this.$root.$off("call-updateSearchQueryElement");
    this.$root.$off("call-updateSearchQueryOperators");
    this.$root.$off("call-updateTaskList");
    this.$root.$off("call-setSearchQueryValue");
  }
}
</script>
