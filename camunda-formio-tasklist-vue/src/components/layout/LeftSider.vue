<template>
  <div class="cft-task-list-container rounded border">
    <TaskListSearch
      ref="taskListSearchRef"
      @update-task-list="onSearchUpdateTasklistResult"
       v-if="!disableOption.filterTask" 
       :filterList="filterList"
       :selectedfilterId="selectedfilterId"
    />
    <div
      class="cft-list-group"
      :style="{
          height: listHeight
        }"
    >
      <div
        v-if="taskLoading"
        class="d-flex justify-content-center text-primary"
      >
        <div
          class="spinner-border"
          role="status"
        >
          <span class="sr-only">Loading...</span>
        </div>
      </div>
      <template v-else-if="tasks && tasks.length">
        <div
          v-for="(task, idx) in tasks"
          :key="task.id"
          class="cft-list-group-item"
          aria-current="true"
         
          @click="
            toggle(idx);
            showTaskDetails(task.id);
          "
          :class="{ 'task-selected': task.id == getFormsFlowTaskId , 'cft-card-list-group-item':listItemCardStyle}"
        >
          <h6
            class="mb-1"
            data-bs-toggle="tooltip"
            title="Task Name"
          >{{ task.name }}</h6>
          <p
            class="cft-description mb-0"
            data-bs-toggle="tooltip"
            title="Process Name"
          >
            {{
              getProcessDataFromList(
                processDefinitions,
                task.processDefinitionId,
                "name"
              )
            }}
          </p>
          <div
            class="cft-task-details assignee mb-1"
            data-bs-toggle="tooltip"
            title="Task Assignee"
          >{{ task.assignee }}</div>
          <div class="d-flex w-100 cft-task-details mb-1">
            <div
              class="days-ago created-date"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.created)"
            >
              Created {{ timedifference(task.created) }}
            </div>
            <div
              class="days-ago"
              data-bs-toggle="tooltip"
              :title="'Priority'"
            >{{ task.priority }}</div>
          </div>
          <div class="d-flex w-100 task-details">
            <div
              class="days-ago due-date"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.due)"
              v-if="task.due"
            >Due {{ timedifference(task.due) }},</div>
            <div
              class="days-ago"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.followUp)"
              v-if="task.followUp"
            > &nbsp; Follow up {{ timedifference(task.followUp) }}</div>
          </div>

          <task-variable v-if="task._embedded&& task._embedded.variable"
           :variables="task._embedded.variable"
           :filterTaskVariable="selectedFilterTaskVariable"/>
 

        </div>
      </template>
      <div
        v-else
        class="d-flex justify-content-center align-items-center py-5 mt-5"
      >
        <i class="fa fa-exclamation-circle"></i>
        <h4 class="mt-0 mx-2">No tasks found in the list.</h4>
      </div>
    </div>
    <Pagination
      v-if="tasks && tasks.length"
      ref="taskListPaginationRef"
      class="py-2 px-3 cft-task-list-pagination"
      :perPage="perPage"
      :totalRecords="getFormsFlowTaskLength"
      :currentPageNumber="currentPage"
      @go-to-page="onPageChange"
    />
  </div>

</template>

<script lang="ts">
import {
  Component, Mixins, Prop , Watch
} from "vue-property-decorator";
import {
  DisableComponentPropPayload,Payload
} from "../../models";

import {
  cloneDeep, getFormattedDateAndTime, isEqual
} from "../../services";
import BaseMixin from "../../mixins/BaseMixin.vue";
import Pagination from "./Pagination.vue";

import TaskListSearch from "../search/TaskListSearch.vue";
import TaskVariable from "../taskVariable/TaskVariable.vue";
import moment from "moment";
import {
  namespace
} from "vuex-class";
const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    TaskListSearch,
    Pagination,
    TaskVariable,
  },
})
export default class LeftSider extends Mixins(BaseMixin) {
  @Prop() private tasks!: Array<any>;
  @Prop() private perPage!: number;
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;
  @Prop() private taskLoading!: boolean;
  @Prop({
    default: 0
  }) private containerHeight!: number;
  @Prop() private disableOption!: DisableComponentPropPayload;
 @Prop({
   default: () => []
 }) private processDefinitions;
  @Prop({
    default:()=>[]
  }) private filterList ;

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Getter("getFormsFlowTaskId")
  private getFormsFlowTaskId: any;
  @serviceFlowModule.Getter("getFormsFlowactiveIndex")
  private getFormsFlowactiveIndex: any;
  @serviceFlowModule.Getter("getFormsFlowTaskLength")
  private getFormsFlowTaskLength: any;

  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskId")
  public setFormsFlowTaskId: any;
  @serviceFlowModule.Mutation("setFormsFlowactiveIndex")
  public setFormsFlowactiveIndex: any;
  private selectedFilterTaskVariable: Array<any> = [];

  
  private processDefinitionId = "";
  private currentPage = 1;
  private listHeight: string = '360px';

  // @Watch("currentPage")
  onPageChange(newVal: number) {
    this.currentPage = newVal;
    this.payload["firstResult"] = (newVal - 1) * this.perPage;
    this.payload["maxResults"] = this.perPage;
    this.setFormsFlowTaskCurrentPage(this.currentPage);
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

  timedifference(date: Date) {
    return moment(date).fromNow();
  }

  getExactDate(date: Date) {
    return getFormattedDateAndTime(date);
  }


  toggle(index: number) {
    this.setFormsFlowactiveIndex(index);
  }

  getProcessDataFromList(
    processList: any[],
    processId: string,
    dataKey: string
  ) {
    const process = processList.find((process) => process.id === processId);
    return process && process[dataKey];
  }

  showTaskDetails(taskId: string) {
    this.setFormsFlowTaskId(taskId);
    this.$root.$emit("call-fetchTaskDetails", {
      selectedTaskId: taskId
    });
  }

  onSearchUpdateTasklistResult(queryList: any) {
    const requiredParams = {
      ...{
        sorting: this.payload?.sorting
      },
      ...queryList,
    };

    if (!isEqual(this.payload, requiredParams)) {
      this.$root.$emit("call-fetchTaskListCount", {
        filterId: this.selectedfilterId,
        requestData: cloneDeep(requiredParams),
      });

      this.$root.$emit("call-fetchPaginatedTaskList", {
        filterId: this.selectedfilterId,
        requestData: cloneDeep(requiredParams),
        firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
        maxResults: this.perPage,
      });
    }
  }

  updated() {
    this.calculateViewHeights();
  }
  @Watch("selectedfilterId")
  settingTaskVariable (){
    if(this.filterList.length&&this.selectedfilterId){
      this.filterList.forEach(filterListItem=>{
        if(filterListItem.id===this.selectedfilterId){
          this.selectedFilterTaskVariable= filterListItem.properties?.variables || [];
        }
      });
    }
  }

  /*** to calculate the height and handling scroll views accordingly */
  calculateViewHeights() {
    const searchHeight = (this.$refs.taskListSearchRef as any)?.$el?.offsetHeight || 0;
    // const paginationHeight = (this.$refs.taskListPaginationRef as any)?.$el?.offsetHeight || 0;
    if (this.containerHeight > 250) {
      this.listHeight = `${this.containerHeight - (searchHeight + 45)}px`;
    }
  }

  mounted() {
    this.$root.$on("call-pagination", () => {
      this.resetPaginationStore();
    });
    this.$root.$on("update-pagination-currentpage", (para: any) => {
      this.currentPage = para.page;
    });

    this.currentPage = this.getFormsFlowTaskCurrentPage;
  }

  resetPaginationStore() {
    if (this.getFormsFlowactiveIndex < 9) {
      this.setFormsFlowactiveIndex(this.getFormsFlowactiveIndex + 1);
      // this.activeIndex = this.getFormsFlowactiveIndex;
    } else if (this.getFormsFlowactiveIndex === 9) {
      this.setFormsFlowactiveIndex(0);
      // this.activeIndex = 0;
      this.setFormsFlowTaskCurrentPage(this.getFormsFlowTaskCurrentPage + 1);
      this.currentPage = this.getFormsFlowTaskCurrentPage;
    }
  }

  beforeDestroy() {
    this.$root.$off("call-pagination");
    this.$root.$off("update-pagination-currentpage");
  }

}
</script>

<style lang="scss" scoped>
.cft-task-list-container {
  background: #fff;
  .cft-list-group {
    height: 100px;
    overflow-y: auto;
    border-radius: 0;
     .cft-card-list-group-item{
       box-shadow: 0 0 3px #b4b4b4;
       margin: 0.50rem 1rem;
     }
    .cft-list-group-item {
      padding: 0.75rem 1rem;
      border-top: 1px solid #eee;
      cursor: pointer;
      &.task-selected {
        background: #eff8ff;
        border-left: 4px solid #2185d0;
        padding-left: 0.75rem;
      }
      &:first-child,
      &:last-child {
        border-radius: 0;
      }
    }
  }
  .cft-task-list-pagination {
    border-top: 1px solid #eee;
  }
  .cft-description {
    opacity: 0.6;
    font-size: 0.9rem;
  }
  .cft-task-details {
    justify-content: space-between;
    align-items: center;
    .days-ago {
      font-size: 0.75rem;
      line-height: normal;
    }
    .due-date {
      color: red;
    }
    &.assignee {
      font-weight: 600;
      font-size: 0.85rem;
    }
  }
}
</style>
