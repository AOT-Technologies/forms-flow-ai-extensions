<template>
  <div class="task-search-filter-item">
    <div class="d-flex mb-2 justify-content-between">
      <div class="d-flex">
        <button
          class="btn btn-outline-danger btn-sm"
          @click="deleteSearchQueryElement(query, index)"
          title="Remove this filter"
        >
          <i
            class="fa fa-trash-o"
            aria-hidden="true"
          ></i>
        </button>
        <div class="dropdown mx-2">
          <button
            class="btn btn-light dropdown-toggle"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            {{ query.label }}
          </button>
          <ul
            class="dropdown-menu"
            aria-labelledby="dropdownMenuButton1"
          >
            <li
              v-for="updateSearch in searchListElements"
              :key="updateSearch.label"
              @click="updateSearchQueryElement(updateSearch, index)"
            >
              <a class="dropdown-item">{{ updateSearch.label }}</a>
            </li>
          </ul>
        </div>
      </div>
      <button
        v-if="(query.type === 'variables') && (showVariableValueState[index] === 'a')"
        class="btn btn-outline-primary btn-sm"
        @click="updateVariableInput(index)"
        title="Property"
      >
        ??
      </button>
    </div>
    <div
      class="d-flex"
      v-if="query.type === 'variables'"
    >
      <div
        class="d-flex mb-2"
        v-if="showVariableValueState[index] === 'i'"
      >
        <input
          class="form-control"
          type="text"
          v-model="selectedSearchQueries[index].variable"
          v-on:keyup.enter="showVariableValueItem(index)"
        >
        <button
          class="btn btn-outline-success btn-sm mx-2"
          @click="showVariableValueItem(index)"
        ><i class="fa fa-check"></i>
        </button>
        <button
          class="btn btn-outline-danger btn-sm"
          @click="rejectVariableItem(index)"
        ><i class="fa fa-times"></i>
        </button>
      </div>
      <p
        class="cft-search-cursor"
        v-if="showVariableValueState[index] === 's'"
        @click="updateVariableInput(index)"
      >
        {{ selectedSearchQueries[index].variable }}
      </p>
    </div>
    <div class="d-flex mb-2">
      <v-select
        :filterable="false"
        :clearable="false"
        v-model="operator[index]"
        @input="updateSearchQueryOperators(operator[index], index)"
        :options="query.compares"
        class="select-search-query"
      />
      <button
        v-if="showSearchState[index] === 'a'"
        @click="updateSearchInput(index)"
        class="btn btn-outline-primary btn-sm"
      >
        ??
      </button>
      <v-date-picker
        v-if="showSearchState[index] === 'i' && query.type === 'date'"
        v-model="selectedSearchQueries[index].value"
        :popover="{ visibility: 'click' }"
         @input="
                setSearchQueryValue(selectedSearchQueries, index);
                showSearchValueItem(index);
        "
      >
        <template v-slot="{ inputValue, inputEvents }">
          <div class="input-group">
            <input
              class="form-control"
              :value="inputValue"
              v-on="inputEvents"
             
              placeholder="dd/mm/yyyy"
            />
          </div>
        </template>
      </v-date-picker>
      <div
        v-if="showSearchState[index] === 's' && query.type === 'date'"
        @click="updateSearchInput(index)"
      >
        {{ formatDate(selectedSearchQueries[index].value) }}
      </div>
      <template v-if="showSearchState[index] === 'i' && query.type !== 'date'">
        <input
          class="form-control"
          type="text"
          v-model="selectedSearchQueries[index].value"
          v-on:keyup.enter="
                setSearchQueryValue(selectedSearchQueries, index);
                showSearchValueItem(index);"
        >
        <button
          class="btn btn-outline-success btn-sm mx-2"
          @click="
                  setSearchQueryValue(selectedSearchQueries, index);
                  showSearchValueItem(index);"
        ><i class="fa fa-check"></i>
        </button>
        <button
          class="btn btn-outline-danger btn-sm"
          @click="rejectSearchValueItem(index)"
        ><i class="fa fa-times"></i>
        </button>
      </template>
      <div
        v-if="showSearchState[index] === 's' && query.type !== 'date'"
        @click="updateSearchInput(index)"
      >
        {{ selectedSearchQueries[index].value }}
      </div>
    </div>

  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Emit, Prop, Vue
} from "vue-property-decorator";
import {
  getFormattedDateAndTime, taskSearchFilters
} from "../../services";
import DatePicker from "v-calendar/lib/components/date-picker.umd";
import {
  SearchOptionPayload
} from "../../models";
import vSelect from "vue-select";

@Component({
  components: {
    vSelect,
    DatePicker,
    VDatePicker: DatePicker,
  }
})
export default class TaskSearchItem extends Vue {
  @Prop({
  }) private query!: SearchOptionPayload;
  @Prop({
  }) private index!: number;

  @Prop({
    default: [],
  })
  private showSearchState!: string[];
  @Prop({
    default: [],
  })
  private selectedSearchQueries!: SearchOptionPayload[];
  @Prop({
    default: [],
  })
  private showVariableValueState!: string[];
  @Prop({
    default: [],
  })
  private operator!: string[];

  private searchListElements: SearchOptionPayload[] = taskSearchFilters;

  deleteSearchQueryElement(query: SearchOptionPayload, index: number) {
    this.$root.$emit("call-deleteSearchQueryElement", {
      query: query,
      index: index,
    });
  }
  updateSearchQueryElement(updateSearch: SearchOptionPayload, index: number) {
    this.$root.$emit("call-updateSearchQueryElement", {
      updateSearch: updateSearch,
      index: index,
    });
  }

  @Emit("updateVariableInput")
  updateVariableInput(index: number) {
    return index;
  }

  @Emit("show-VariableValue-Item")
  showVariableValueItem(index: number) {
    return index;
  }

  @Emit("reject-Variable-Item")
  rejectVariableItem(index: number) {
    return index;
  }

  @Emit("reject-Search-Value-Item")
  rejectSearchValueItem(index: number) {
    return index;
  }

  @Emit("update-search-value")
  updateSearchInput(index: number) {
    return index;
  }

  @Emit("show-search-value")
  showSearchValueItem(index: number) {
    return index;
  }

  updateSearchQueryOperators(operator: string, index: number) {
    console.log("reached here")
    this.$root.$emit("call-updateSearchQueryOperators", {
      operator: operator,
      index: index,
    });
  }

  setSearchQueryValue(item: SearchOptionPayload[], index: number) {
    this.$root.$emit("call-setSearchQueryValue", {
      item: item,
      index: index,
    });
  }

  formatDate(date: Date) {
    return getFormattedDateAndTime(date);
  }
}
</script>

<style lang="scss" scoped>
.task-search-filter-item {
  padding: 0.35rem 0.5rem;
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  .form-control,
  .btn {
    height: 40px;
    min-height: 40px;
    border-radius: 0.4rem;
  }
  .select-search-query {
    margin-right: 0.5rem;
  }
}
</style>
