<template>
  <div class="cft-input-search-filter px-2">
    <b-col class="cft-filter-container" xl="12" lg="12" md="12" sm="12">
      <div>
        <TaskListSearchType />
      </div>
      <TaskListHeader/>
    </b-col>
  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOTasklistSearch.scss";
import {
  Component, Emit, Prop, Vue 
} from "vue-property-decorator";
import TaskListSearchType from "./TaskListSearchType.vue";
import TaskListHeader from "./TaskListHeader.vue";

@Component({
  components: {
    TaskListHeader,
    TaskListSearchType
  }
})

export default class TaskListSearch extends Vue {

}
</script>
