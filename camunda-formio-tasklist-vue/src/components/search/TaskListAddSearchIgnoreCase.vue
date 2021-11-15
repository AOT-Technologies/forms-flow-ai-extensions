<template>
  <div>
    <b-row>
      <b-col cols="9">
        <b-nav-item-dropdown
          split
          text="Filter Tasks"
          class="cft-search-item-nav"
        >
          <b-dropdown-item-button
            v-for="(s, idx) in searchList"
            :key="s.label"
            @click="
              addToSelectedSearchQuery(s);
              setActiveSearchItem(idx);
            "
            :class="{ 'cft-search-item-selected': idx == activeSearchItem }"
          >
            {{ s.label }}
          </b-dropdown-item-button>
        </b-nav-item-dropdown>
      </b-col>
      <b-col cols="3">
        <span class="cft-search-total" title="Total number of tasks.">
          {{ getFormsFlowTaskLength }}
        </span>
      </b-col>
    </b-row>
    <span v-if="isVariableTypeInSelectedSearchQuery">
      <span>
          <span class="cft-name-value-container"
            >For Variables, ignore case of 
            <b-form-checkbox
              v-model="getVariableNameIgnoreCase"
              @change="updateNameCase"
              >name</b-form-checkbox
            >
            <b-form-checkbox
              v-model="getVariableValueIgnoreCase"
              @change="updateValueCase"
              >value.</b-form-checkbox
            >
          </span>
      </span>
    </span>
  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Emit, Prop, Vue 
} from "vue-property-decorator";
import {
  namespace 
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component
export default class TaskListAddSearchIgnoreCase extends Vue {
  @Prop() private isVariableTypeInSelectedSearchQuery!: string;
  @Prop() private searchListElements!: any;

  @serviceFlowModule.Getter("getFormsFlowTaskLength")
  private getFormsFlowTaskLength: any;
  @serviceFlowModule.Getter("getVariableNameIgnoreCase")
  private getVariableNameIgnoreCase: any;
  @serviceFlowModule.Getter("getVariableValueIgnoreCase")
  private getVariableValueIgnoreCase: any;

  @serviceFlowModule.Mutation("setVariableNameIgnoreCase")
  public setVariableNameIgnoreCase: any;
  @serviceFlowModule.Mutation("setVariableValueIgnoreCase")
  public setVariableValueIgnoreCase: any;

  private activeSearchItem: number = 0;
  private searchList: any = this.searchListElements;

  setActiveSearchItem (index: number) {
    this.activeSearchItem = index;
  }

  @Emit()
  addToSelectedSearchQuery (item: any) {
    return item;
  }

  updateNameCase() {
    this.setVariableNameIgnoreCase();
    this.$root.$emit("call-updateTaskList");
  }

  updateValueCase() {
    this.setVariableValueIgnoreCase();
    this.$root.$emit("call-updateTaskList");
  }
}
</script>
