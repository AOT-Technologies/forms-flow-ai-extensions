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
            @click="updatevariableinput(index)"
            title="Property"
          >
            ??
          </span>
          <span v-if="showVariableValue[index] === 'i'" title="Property">
            <span>
              <span @click="showVariableValueItem(index)">
                <i class="bi bi-check cft-approve-box"></i>
              </span>
              <i
                class="bi bi-x cft-reject-box"
                @click="showVariableValueItem(index)"
              ></i
            ></span>
            <b-form-input
              v-model="searchVariableValue[index]"
              v-on:keyup.enter="showVariableValueItem(index)"
            />
          </span>
        </span>
        <span
          class="cft-search-cursor"
          v-if="showVariableValue[index] === 's' && query.type === 'variables'"
          @click="updatevariableinput(index)"
        >
          {{ searchVariableValue[index] }}
        </span>
      </b-col>
    </b-row>

    <b-row align-h="end">
      <b-col cols="2">
        <b-nav-item-dropdown :text="operator[index]">
          <b-dropdown-item-button
            v-for="x in query.compares"
            :key="x"
            @click="updateSearchQueryOperators(x, index)"
          >
            {{ x }}
          </b-dropdown-item-button>
        </b-nav-item-dropdown>
      </b-col>
      <b-col cols="9">
        <div class="cft-rhs-container">
          <span
            v-if="showSearchstate[index] === 'a'"
            @click="updatesearchinput(index)"
            class="cft-search-cursor"
            >??</span
          >
          <span v-if="showSearchstate[index] === 'i' && query.type === 'date'">
            <b-form-datepicker
              size="sm"
              v-model="setDate[index]"
              @input="
                setSearchQueryValue(
                  setDate[index],
                  query,
                  operator[index],
                  index
                );
                showsearchValueItem(index);
              "
            >
            </b-form-datepicker>
          </span>
          <span
            v-if="showSearchstate[index] === 's' && query.type === 'date'"
            @click="updatesearchinput(index)"
          >
            {{ formatDate(setDate[index]) }}
          </span>
          <span v-if="showSearchstate[index] === 'i' && query.type !== 'date'">
            <span class="cft-icon-actions">
              <span
                @click="
                  setSearchQueryValue(
                    searchValueItem[index],
                    query,
                    operator[index],
                    index
                  );
                  showsearchValueItem(index);
                "
              >
                <i class="bi bi-check cft-approve-box"></i>
              </span>
              <i
                class="bi bi-x cft-reject-box"
                @click="showsearchValueItem(index)"
              ></i
            ></span>
            <b-form-input
              v-model="searchValueItem[index]"
              v-on:keyup.enter="
                setSearchQueryValue(
                  searchValueItem[index],
                  query,
                  operator[index],
                  index
                );
                showsearchValueItem(index);
              "
            />
          </span>
          <span
            v-if="showSearchstate[index] === 's' && query.type !== 'date'"
            @click="updatesearchinput(index)"
          >
            {{ searchValueItem[index] }}
          </span>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import { Component, Emit, Prop, Vue } from "vue-property-decorator";
import { getFormattedDateAndTime } from "../../services/format-time";
import { taskSearchFilters } from "../../services/search-constants";

@Component
export default class TaskSearchItem extends Vue {
  @Prop({}) private query!: any;
  @Prop({}) private index!: number;
  @Prop({ default: taskSearchFilters }) private searchListElements!: any;
  @Prop({ default: [] }) private searchVariableValue!: any;
  @Prop({ default: [] }) private searchValueItem!: any;
  @Prop({ default: [] }) private setDate!: Array<string>;
  @Prop({ default: [] }) private showSearchstate!: Array<string>;
  @Prop({ default: [] }) private showVariableValue!: Array<string>;
  @Prop({ default: [] }) private operator!: Array<string>;

  deleteSearchQueryElement (query: any, index: number) {
    this.$root.$emit("call-deleteSearchQueryElement", {
      query: query,
      index: index,
    });
  }
  updateSearchQueryElement (updateSearch: any, index: number) {
    this.$root.$emit("call-updateSearchQueryElement", {
      updateSearch: updateSearch,
      index: index,
    });
  }

  @Emit("updatevariableinput")
  updatevariableinput (index: number) {
    return index;
  }

  @Emit("show-VariableValue-Item")
  showVariableValueItem (index: number) {
    return index;
  }

  @Emit("update-search-value")
  updatesearchinput (index: number) {
    return index;
  }

  @Emit("show-search-value")
  showsearchValueItem (index: number) {
    return index;
  }

  updateSearchQueryOperators (operator: string, index: number) {
    this.$root.$emit("call-updateSearchQueryOperators", {
      operator: operator,
      index: index,
    });
  }

  setSearchQueryValue (item: any, query: any, operator: string, idx: number) {
    this.$root.$emit("call-setSearchQueryValue", {
      item: item,
      query: query,
      operator: operator,
      idx: idx,
    });
  }

  formatDate (date: Date) {
    return getFormattedDateAndTime(date);
  }
}
</script>
