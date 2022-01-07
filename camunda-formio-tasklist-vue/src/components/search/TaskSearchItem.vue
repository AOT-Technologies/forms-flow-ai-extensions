<template>
  <div class="shadow-sm mb-3 bg-light">
    <b-row class="  align-items-center justify-content-between py-3 px-2">
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
      <b-col cols="5" >
        <span v-if="query.type === 'variables'" class="d-flex align-items-center text-right ">
          <span class="mr-2">: </span>
          <span
            v-if="showVariableValueState[index] === 'a'"
            @click="updateVariableInput(index)"
            title="Property"
          >
            ??
          </span>
          <!-- input field for typing  -->
          <span   v-if="showVariableValueState[index] === 'i'" title="Property">
              <span >
              <span @click="showVariableValueItem(index)" class=" task-list-button-hover p-1">
                <i class="fa fa-check  "></i> 
              </span>
              <span class="p-1 task-list-button-hover  "> <i
                class="fa fa-times  "
                @click="showVariableValueItem(index)"
              ></i
            ></span>
            
            </span>
             <b-form-input
              
              v-model="selectedSearchQueries[index].variable"
              v-on:keyup.enter="showVariableValueItem(index)"
              class= "mr-2"
            />
          </span>
        </span>
        <span
          class="cft-search-cursor"
          v-if="showVariableValueState[index] === 's' && query.type === 'variables'"
          @click="updateVariableInput(index)"
        >
          {{ selectedSearchQueries[index].variable }}
        </span>
      </b-col>
    </b-row>
<!-- next section -->
    <b-row  class=" align-items-center py-3 px-2 ">
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
        <div >
          <span
            v-if="showSearchState[index] === 'a'"
            @click="updateSearchInput(index)"
            class="cft-search-cursor"
            >: ??</span
          >
          <!-- next input section -->
          <div  class="text-right ">
            <span   v-if="showSearchState[index] === 'i' && query.type === 'date'">
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
            <span >
              <span
                @click="
                  setSearchQueryValue(selectedSearchQueries, index);
                  showSearchValueItem(index);
                "
                class="p-1 task-list-button-hover "
              >
                <i class="fa fa-check  "></i>
              </span>
              <span class="p-1 task-list-button-hover ">
                 <i
                class="fa fa-times  "
                @click="showSearchValueItem(index)"
              ></i
            >
              </span>
             </span>
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
          </div>
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
  }) private showVariableValueState!: string[];
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
