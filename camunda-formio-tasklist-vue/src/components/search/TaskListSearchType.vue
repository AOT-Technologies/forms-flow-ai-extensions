<template>
  <div class="cft-search-criteria">
    <b-button squared variant="primary" @click="changeQueryType">
      {{ getFormsFlowTaskSearchType }}
    </b-button>
    <span class="cft-search-item-criteria"> of the criteria are met.</span>
  </div>
</template>

<script lang="ts">
import {
  Component, Vue 
} from "vue-property-decorator";
import {
  namespace 
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component
export default class TaskListSearchType extends Vue {
  @serviceFlowModule.Getter("getFormsFlowTaskSearchType")
  private getFormsFlowTaskSearchType: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskSearchType")
  public setFormsFlowTaskSearchType: any;

  changeQueryType () {
    this.setFormsFlowTaskSearchType();
    this.$root.$emit("call-updateSearchQueryType", {
    });
  }
}
</script>
