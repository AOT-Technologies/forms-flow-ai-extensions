<template>
    <b-row>
      <b-col cols="9">
        <b-nav-item-dropdown
          split
          text="Filter Tasks"
          class="cft-search-item-nav"
        >
          <b-dropdown-item-button
            v-for="(s, idx) in searchListElements"
            :key="s.label"
            @click="
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
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Vue 
} from "vue-property-decorator";
import {
  taskSearchFilters,
} from "../../services";
import {
  namespace 
} from "vuex-class";
import serviceFlowModule from "../../store/modules/serviceFlow-module";

const StoreServiceFlowModule = namespace("serviceFlowModule");

@Component
export default class TaskListHeader extends Vue {
  private searchListElements: any = taskSearchFilters;
  private activeSearchItem: number = 0;

  @StoreServiceFlowModule.Getter("getFormsFlowTaskLength")
  private getFormsFlowTaskLength: any;

  setActiveSearchItem (index: number) {
    this.activeSearchItem = index;
  }
  
}

</script>
