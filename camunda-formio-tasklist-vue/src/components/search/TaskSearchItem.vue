<template>
  <div>
    <b-row>
      <b-col cols="1">
        <i
          class="fa fa-times cftf-x"
          @click="deleteSearchQueryElement(query, index)"
        ></i>
      </b-col>
      <b-col cols="5">
        <b-nav-item-dropdown :text="query.label">
          <b-dropdown-item-button
            v-for="updateSearch in searchListElements"
            :key="updateSearch.label"
            @click="updateSearchQueryElement(updateSearch, index)"
          >
            {{ updateSearch.label }}
          </b-dropdown-item-button>
        </b-nav-item-dropdown>
      </b-col>
      <b-col cols="5">
        <span v-if="query.type === 'variables'">
          <span>: </span>
          <span
            v-if="showVariableValue[index] === 'a'"
            @click="updateVariableInput(index)"
            title="Property"
          >
            ??
          </span>
          <span v-if="showVariableValue[index] === 'i'" title="Property">
            <span>
              <span @click="showVariableValueItem(index)">
                <i class="fa fa-check cft-approve-box"></i>
              </span>
              <i
                class="fa fa-times cft-reject-box"
                @click="showVariableValueItem(index)"
              ></i
            ></span>
            <b-form-input
              v-model="selectedSearchQueries[index].variable"
              v-on:keyup.enter="showVariableValueItem(index)"
            />
          </span>
        </span>
        <span
          class="cft-search-cursor"
          v-if="showVariableValue[index] === 's' && query.type === 'variables'"
          @click="updateVariableInput(index)"
        >
          {{ selectedSearchQueries[index].variable }}
        </span>
      </b-col>
    </b-row>

    <b-row>
      <b-col cols="4">
        <b-form-select
          v-model="operator[index]"
          @change="updateSearchQueryOperators(operator[index], index)"
          :options="query.compares"
          plain
          size='sm'
        />
      </b-col>
      <b-col cols="8">
        <div class="cft-rhs-container">
          <span
            v-if="showSearchState[index] === 'a'"
            @click="updateSearchInput(index)"
            class="cft-search-cursor"
            >??</span
          >
          <span v-if="showSearchState[index] === 'i' && query.type === 'date'">
            <b-form-datepicker
              size="sm"
              v-model="selectedSearchQueries[index].value"
              @input="
                setSearchQueryValue(
                  selectedSearchQueries,
                  index
                );
                showSearchValueItem(index);
              "
            >
            </b-form-datepicker>
          </span>
          <span
            v-if="showSearchState[index] === 's' && query.type === 'date'"
            @click="updateSearchInput(index)"
          >
            {{ formatDate(selectedSearchQueries[index].value) }}
          </span>
          <span v-if="showSearchState[index] === 'i' && query.type !== 'date'">
            <span class="cft-icon-actions">
              <span
                @click="
                  setSearchQueryValue(selectedSearchQueries, index);
                  showSearchValueItem(index);
                "
              >
                <i class="fa fa-check cft-approve-box"></i>
              </span>
              <i
                class="fa fa-times cft-reject-box"
                @click="showSearchValueItem(index)"
              ></i
            ></span>
            <b-form-input
              v-model="selectedSearchQueries[index].value"
              v-on:keyup.enter="
                setSearchQueryValue(
                  selectedSearchQueries, index
                );
                showSearchValueItem(index);
              "
            />
          </span>
          <span
            v-if="showSearchState[index] === 's' && query.type !== 'date'"
            @click="updateSearchInput(index)"
          >
            {{ selectedSearchQueries[index].value }}
          </span>
        </div>
      </b-col>
    </b-row>
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
import {
  SearchOptionPayload,
} from "../../models";


@Component
export default class TaskSearchItem extends Vue {
  @Prop({
  }) private query!: SearchOptionPayload;
  @Prop({
  }) private index!: number;
  @Prop({
    default: taskSearchFilters 
  }) private searchListElements!: SearchOptionPayload[];
  @Prop({
    default: [] 
  }) private showSearchState!: string[];
  @Prop({
    default: [] 
  }) private selectedSearchQueries!: SearchOptionPayload[];
  @Prop({
    default: [] 
  }) private showVariableValue!: string[];
  @Prop({
    default: [] 
  }) private operator!: string[];

  deleteSearchQueryElement (query: SearchOptionPayload, index: number) {
    this.$root.$emit("call-deleteSearchQueryElement", {
      query: query,
      index: index,
    });
  }
  updateSearchQueryElement (updateSearch: SearchOptionPayload, index: number) {
    this.$root.$emit("call-updateSearchQueryElement", {
      updateSearch: updateSearch,
      index: index,
    });
  }

  @Emit("updateVariableInput")
  updateVariableInput (index: number) {
    return index;
  }

  @Emit("show-VariableValue-Item")
  showVariableValueItem (index: number) {
    return index;
  }

  @Emit("update-search-value")
  updateSearchInput (index: number) {
    return index;
  }

  @Emit("show-search-value")
  showSearchValueItem (index: number) {
    return index;
  }

  updateSearchQueryOperators (operator: string, index: number) {
    this.$root.$emit("call-updateSearchQueryOperators", {
      operator: operator,
      index: index,
    });
  }

  setSearchQueryValue (item: SearchOptionPayload[], index: number) {
    this.$root.$emit("call-setSearchQueryValue", {
      item: item,
      index: index
    });
  }

  formatDate (date: Date) {
    return getFormattedDateAndTime(date);
  }
}
</script>
