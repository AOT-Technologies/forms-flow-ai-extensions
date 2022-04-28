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
      > <div>
        <input
          class="form-control"
          type="text"
          v-model="selectedSearchQueries[index].variable"
          v-on:keyup="filterTaskVariableArray(index)"
          v-on:keyup.enter="showVariableValueItem(index)"
        >
        <div class="filter-items variable-filter-item"  >
         <div
         v-for="(variable,idx) of filteredVariable"
           :key="idx"
           class="clickable p-0 mb-2 text-truncate"
           @click="setTaskvariableValue(variable.name,index)"
          data-bs-toggle="tooltip" data-bs-placement="top" :title="`${variable.name}  (${variable.label})`"
                  >
 
                 <span>{{variable.name}} <span class="text-muted"> ({{variable.label}})</span></span> 
                 </div>           

       </div>
      </div>
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
   
      <div
        class="cft-search-cursor text-truncate"
        v-if="showVariableValueState[index] === 's'"
        @click="updateVariableInput(index)"
      >
       <span  data-bs-toggle="tooltip" data-bs-placement="top" :title="selectedSearchQueries[index].variable "> {{ selectedSearchQueries[index].variable }}</span>
      </div>
        
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
        <div>
          <input 
          class="form-control"
          :type="showCalender?'datetime-local':'text'"
          v-model="selectedSearchQueries[index].value"
          v-on:keyup.enter="
                setSearchQueryValue(selectedSearchQueries, index);
                showSearchValueItem(index);"
        >
        </div>
        <div>
          <button
           class="btn btn-outline-success btn-sm "
           @click="
                  setSearchQueryValue(selectedSearchQueries, index);
                  showSearchValueItem(index);"
         ><i class="fa fa-check" aria-hidden="true" ></i>
         </button>
         <button class="btn btn-outline-secondary btn-sm" v-if="query.type === 'variables'" 
         @click="showCalender = true">
          <i  class="fa fa-calendar" aria-hidden="true"  />
         </button>
         <button
          class="btn btn-outline-danger btn-sm"
          @click="rejectSearchValueItem(index)"
         ><i class="fa fa-times" aria-hidden="true" ></i>
        </button>
        </div>
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
  Component, Emit, Prop, Vue, Watch
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

  @Prop({
    default:()=>[]
  }) private  filterList: any;
 
  @Prop() private selectedfilterId ;

  private  taskVariableArray: any = [];
  private filteredVariable =[];
  private searchListElements: SearchOptionPayload[] = taskSearchFilters;
  private showCalender: boolean =false

  @Watch("selectedfilterId")
  settingTaskVariable (){
    if(this.filterList.length&&this.selectedfilterId){
      this.filterList.forEach(filterListItem=>{
        if(filterListItem.id===this.selectedfilterId){
          this.filteredVariable = filterListItem?.properties?.variables ||[];
          this.taskVariableArray= filterListItem?.properties?.variables ||[];
        }
      });
    }
  }
  mounted(){
    this.settingTaskVariable();
    
  }

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
    this.$root.$emit("call-updateSearchQueryOperators", {
      operator: operator,
      index: index,
    });
  }

  setSearchQueryValue(item: SearchOptionPayload[], index: number) {
    this.showCalender=false;
    this.$root.$emit("call-setSearchQueryValue", {
      item: item,
      index: index,
    });
  }

  filterTaskVariableArray(index: any){
    const value = this.selectedSearchQueries[index].variable;
    this.filteredVariable = this.taskVariableArray.filter(item=> item.name.includes(value));
  }

  setTaskvariableValue(value, index){
    this.selectedSearchQueries[index].variable= value;
    this.showVariableValueItem(index);
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
.filter-items {
  max-width: 200px;
  max-height: 250px;
  overflow: auto;
  margin: 5px;
  border: 1px solid rgb(185, 185, 185);
  border-radius: 5px;
  background-color: #fff;
  background-clip: padding-box;
    border-radius: 0px;
    margin-top: 0px;
    padding:0 5px;
}
.clickable {
  cursor: pointer;
  margin-bottom: 10px !important;
  padding: 10px;
  border-left: 2px solid transparent;
}
.clickable:hover {
  border-left: 2px solid #155cb5;
}

</style>

<style>
::-webkit-datetime-edit-year-field:not([aria-valuenow]),
::-webkit-datetime-edit-month-field:not([aria-valuenow]),
::-webkit-datetime-edit-day-field:not([aria-valuenow]) {
    color: transparent;
}
</style>
