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
            :setDate="setDate"
            :searchValueItem="searchValueItem"
            :searchVariableValue="searchVariableValue"
            :showSearchstate="showSearchstate"
            :showVariableValue="showVariableValue"
            :operator="operator"
            @updatevariableinput="updatevariableinput"
            @show-VariableValue-Item="showVariableValueItem"
            @update-search-value="updatesearchinput"
            @show-search-value="showsearchValueItem"
          />
        </b-col>
      </div>
      <TaskListAddSearchIgnoreCase
        :queryList="queryList"
        :tasklength="tasklength"
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
  Component, Emit, Prop, Vue 
} from "vue-property-decorator";
import {
  FilterSearchTypes,
  getDeletedVariableIndex,
  getISODateTime,
  getVariableOperator,
  searchValueObject,
  taskSearchFilters,
} from "../../services";
import {
  SearchOptionPayload
} from "../../models";
import TaskListAddSearchIgnoreCase from "./TaskListAddSearchIgnoreCase.vue";
import TaskListSearchType from "./TaskListSearchType.vue";
import TaskSearchItem from "./TaskSearchItem.vue";

@Component({
  components: {
    TaskListAddSearchIgnoreCase,
    TaskListSearchType,
    TaskSearchItem,
  },
})
export default class TaskListSearch extends Vue {
  @Prop({
    default: 0 
  }) private tasklength!: number;

  private searchListElements: any = taskSearchFilters;
  private queryType: string = "ALL";
  private selectedSearchQueries: SearchOptionPayload[] = [];
  private searchValueItem: any = [];
  private searchVariableValue: any = [];
  private operator: Array<string> = [];
  private showSearchstate: Array<string> = []; //3 states - a, i, s
  private showVariableValue: Array<string> = [];
  private queryList: any = {
    taskVariables: [],
    processVariables: [],
  };
  private isVariableTypeInSelectedSearchQuery: boolean = false;
  private setDate: Array<string> = [];

  updateSearchQueryOperators (operator: any, index: number) {
    if (this.searchValueItem[index] || this.setDate[index]) {
      delete this.queryList[
        searchValueObject(
          this.selectedSearchQueries[index].key,
          this.selectedSearchQueries[index].operator
        )
      ];
      if (this.selectedSearchQueries[index].type === "date") {
        this.setSearchQueryValue(
          this.setDate[index],
          this.selectedSearchQueries[index],
          this.operator[index],
          index
        );
      } else {
        this.setSearchQueryValue(
          this.searchValueItem[index],
          this.selectedSearchQueries[index],
          this.operator[index],
          index
        );
      }
    }
  }

  updatesearchinput (index: number) {
    this.searchValueItem[index] = "";
    Vue.set(this.showSearchstate, index, "i");
  }

  showsearchValueItem (index: number) {
    Vue.set(this.showSearchstate, index, "s");
  }

  makeInputNull (index: number) {
    Vue.set(this.showSearchstate, index, "a");
  }

  updatevariableinput (index: number) {
    this.searchVariableValue[index] = "";
    Vue.set(this.showVariableValue, index, "i");
  }

  showVariableValueItem (index: number) {
    Vue.set(this.showVariableValue, index, "s");
  }

  addToSelectedSearchQuery (item: any) {
    this.selectedSearchQueries.push(item);
    if (this.selectedSearchQueries === []) {
      this.operator[0] = item["compares"][0];
      this.showSearchstate[0] = "a";
      this.showVariableValue[0] = "a";
      if (item.type === "variables") {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    } else {
      this.operator[this.selectedSearchQueries.length - 1]
        = item["compares"][0];
      this.showSearchstate[this.selectedSearchQueries.length - 1] = "a";
      this.showVariableValue[this.selectedSearchQueries.length - 1] = "a";
      if (item.type === "variables") {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    }
  }

  deleteSearchQueryElement (query: any, index: number) {
    this.searchValueItem.splice(index, 1);
    this.setDate.splice(index, 1);
    if (query.type === "variables") {
      this.searchVariableValue.splice(index, 1);
      this.queryList = getDeletedVariableIndex(
        query,
        this.selectedSearchQueries,
        this.selectedSearchQueries[index]["key"],
        this.queryList
      );
    } else {
      delete this.queryList[
        searchValueObject(
          this.selectedSearchQueries[index].key,
          this.operator[index]
        )
      ];
    }
    this.selectedSearchQueries.splice(index, 1);
    this.operator.splice(index, 1);

    this.isVariableTypeInSelectedSearchQuery = false;
    for (const idx in this.selectedSearchQueries) {
      if (this.selectedSearchQueries[idx]["type"] === "variables") {
        this.isVariableTypeInSelectedSearchQuery = true;
      }
    }
    this.onSearchUpdateTasklistResult();
  }

  updateSearchQueryElement (searchitem: any, index: number) {
    if (
      searchitem.type === "date"
      && this.selectedSearchQueries[index].type !== "date"
    ) {
      this.makeInputNull(index);
    }
    if (
      this.selectedSearchQueries[index].type === "date"
      && searchitem.type !== "date"
    ) {
      this.makeInputNull(index);
    }
    if (this.selectedSearchQueries[index].type === "variables") {
      this.queryList = getDeletedVariableIndex(
        this.selectedSearchQueries[index],
        this.selectedSearchQueries,
        this.selectedSearchQueries[index]["key"],
        this.queryList
      );
    } else {
      delete this.queryList[
        searchValueObject(
          this.selectedSearchQueries[index].key,
          this.operator[index]
        )
      ];
    }
    Vue.set(this.selectedSearchQueries, index, searchitem);
    this.operator[index] = this.selectedSearchQueries[index].compares[0];
    if (this.showSearchstate[index] !== "a") {
      if (this.selectedSearchQueries[index].type === "date") {
        this.setSearchQueryValue(
          this.setDate[index],
          this.selectedSearchQueries[index],
          this.operator[index],
          index
        );
      } else {
        this.setSearchQueryValue(
          this.searchValueItem[index],
          this.selectedSearchQueries[index],
          this.operator[index],
          index
        );
      }
    }
  }

  setSearchQueryValue (item: any, query: any, operator: string, idx: number) {
    const Vindex = searchValueObject(
      this.selectedSearchQueries[idx].key,
      this.operator[idx]
    );
    this.selectedSearchQueries[idx]["name"] = item;
    this.selectedSearchQueries[idx].operator = this.operator[idx];
    switch (query.type) {
    case FilterSearchTypes.VARIABLES: {
      this.selectedSearchQueries[idx]["name"] = this.searchVariableValue[idx];
      this.selectedSearchQueries[idx]["variable"] = this.searchValueItem[idx];
      if (this.searchValueItem[idx] && this.searchVariableValue[idx]) {
        this.queryList[query.key].push({
          name: this.searchVariableValue[idx],
          operator: getVariableOperator(operator),
          value: this.searchValueItem[idx],
        });
        this.onSearchUpdateTasklistResult();
      }
      break;
    }
    case FilterSearchTypes.DATE: {
      this.queryList[Vindex] = getISODateTime(item);
      this.onSearchUpdateTasklistResult();
      break;
    }
    case FilterSearchTypes.STRING:
    case FilterSearchTypes.NORMAL: {
      if (this.operator[idx] === "like") {
        this.queryList[Vindex] = "%" + item + "%";
      } else {
        this.queryList[Vindex] = item;
      }
      this.onSearchUpdateTasklistResult();
      break;
    }
    default:
    }
  }

  @Emit("update-task-list")
  onSearchUpdateTasklistResult () {
    if (this.queryType === "ALL") {
      return this.queryList;
    } else {
      return {
        orQueries: [this.queryList] 
      };
    }
  }

  mounted () {
    this.$root.$on("call-deleteSearchQueryElement", (para: any) => {
      this.deleteSearchQueryElement(para.query, para.index);
    });

    this.$root.$on("call-updateSearchQueryType", (para: any) => {
      this.queryType = para.queryType;
      this.onSearchUpdateTasklistResult();
    });

    this.$root.$on("call-updateTaskList", (para: any) => {
      this.queryList = para.queryList;
      this.onSearchUpdateTasklistResult();
    });

    this.$root.$on("call-updateSearchQueryElement", (para: any) => {
      this.updateSearchQueryElement(para.updateSearch, para.index);
    });

    this.$root.$on("call-updateSearchQueryOperators", (para: any) => {
      this.updateSearchQueryOperators(para.operator, para.index);
    });

    this.$root.$on("call-setSearchQueryValue", (para: any) => {
      this.setSearchQueryValue(para.item, para.query, para.operator, para.idx);
    });
  }

  beforeDestroy () {
    this.$root.$off("call-deleteSearchQueryElement");
    this.$root.$off("call-updateSearchQueryElement");
    this.$root.$off("call-updateSearchQueryType");
    this.$root.$off("call-updateSearchQueryOperators");
    this.$root.$off("call-updateTaskList");
    this.$root.$off("call-setSearchQueryValue");
  }
}
</script>
