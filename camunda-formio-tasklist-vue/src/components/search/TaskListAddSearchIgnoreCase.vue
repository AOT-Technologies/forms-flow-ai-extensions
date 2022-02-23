<template>
  <div>
    <div class="d-flex justify-content-between align-items-center">
      <div class="dropdown">
        <button
          class="btn btn-sm dropdown-toggle"
          type="button"
          id="filterTasks"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          Filter Tasks
        </button>
        <ul
          class="dropdown-menu"
          aria-labelledby="filterTasks"
        >
          <li
            class="dropdown-item"
            v-for="(s, idx) in searchList"
            :key="s.label"
            @click="
              addToSelectedSearchQuery(s);
              setActiveSearchItem(idx);
            "
            :class="{ 'active': idx == activeSearchItem }"
          >
            {{ s.label }}
          </li>
        </ul>
      </div>
      <div>
        <div class="badge rounded-pill bg-primary">{{ getFormsFlowTaskLength }}</div>
      </div>
    </div>
    <div v-if="isVariableTypeInSelectedSearchQuery">
      <div class="cft-name-value-container">For Variables, ignore case of
        <b-form-checkbox
          v-model="getVariableNameIgnoreCase"
          @change="updateNameCase"
        >name</b-form-checkbox>
        <b-form-checkbox
          v-model="getVariableValueIgnoreCase"
          @change="updateValueCase"
        >value.</b-form-checkbox>
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
  SearchOptionPayload,
} from "../../models";
import {
  namespace
} from "vuex-class";
import {
  taskSearchFilters
} from "../../services";


const serviceFlowModule = namespace("serviceFlowModule");

@Component({
})
export default class TaskListAddSearchIgnoreCase extends Vue {
  @Prop() private isVariableTypeInSelectedSearchQuery!: string;


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
  private searchList: SearchOptionPayload[] = taskSearchFilters;

  setActiveSearchItem(index: number) {
    this.activeSearchItem = index;
  }

  @Emit()
  addToSelectedSearchQuery(item: SearchOptionPayload) {
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

<style lang="scss" scoped>
.dropdown {
  .btn {
    line-height: 1;
    min-height: 36px;
  }
  .dropdown-item {
    padding: 0.5rem 0.75rem;
  }
}
</style>
