<template>
  <div class="task-list-container">
    <TaskListSearch @update-task-list="onSearchUpdateTasklistResult" />
    <template v-if="tasks && tasks.length">
      <div class="list-group">
        <div
          v-for="(task, idx) in tasks"
          :key="task.id"
          class="list-group-item"
          aria-current="true"
          @click="
            toggle(idx);
            showTaskDetails(task.id);
          "
          :class="{ 'task-selected': task.id == getFormsFlowTaskId }"
        >
          <h6
            class="mb-1"
            data-bs-toggle="tooltip"
            title="Task Name"
          >{{ task.name }}</h6>
          <p
            class="description mb-0"
            data-bs-toggle="tooltip"
            title="Process Name"
          >
            {{
              getProcessDataFromList(
                getProcessDefinitions,
                task.processDefinitionId,
                "name"
              )
            }}
          </p>
          <div
            class="task-details assignee mb-1"
            data-bs-toggle="tooltip"
            title="Task Assignee"
          >{{ task.assignee }}</div>
          <div class="d-flex w-100 task-details mb-1">
            <div
              class="days-ago created-date"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.created)"
            >
              Created {{ timedifference(task.created) }}
            </div>
          </div>
          <div class="d-flex w-100 task-details">
            <div
              class="days-ago due-date"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.due)"
              v-if="task.due"
            >Due in {{ timedifference(task.due) }}</div>
            <div
              class="days-ago"
              data-bs-toggle="tooltip"
              :title="getExactDate(task.followUp)"
              v-if="task.followUp"
            >Follow up in {{ timedifference(task.followUp) }}</div>
          </div>
        </div>
      </div>
      <Pagination
        class="mt-2 px-3"
        :perPage="perPage"
        :totalRecords="getFormsFlowTaskLength"
        @go-to-page="onPageChange"
      />
    </template>
    <div
      class="d-flex justify-content-center align-items-center p-4"
      v-else
    >
      <i class="far fa-exclamation-circle"></i>
      <h4 class="mt-0 mx-2">No tasks found in the list.</h4>
    </div>
  </div>
</template>

<script lang="ts">
import {
  CamundaRest, cloneDeep, getFormattedDateAndTime, isEqual
} from "../../services";
import {
  Component, Mixins, Prop
} from "vue-property-decorator";
import BaseMixin from "../../mixins/BaseMixin.vue";
import Pagination from "./Pagination.vue";
import {
  Payload
} from "../../models/Payload";
import TaskListSearch from "../search/TaskListSearch.vue";
import moment from "moment";
import {
  namespace
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    TaskListSearch,
    Pagination,
  },
})
export default class LeftSider extends Mixins(BaseMixin) {
  @Prop() private tasks!: Array<any>;
  @Prop() private perPage!: number;
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;

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

  private getProcessDefinitions: Array<any> = [];
  private processDefinitionId = "";
  private currentPage = 1;

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

  mounted() {
    this.$root.$on("call-pagination", () => {
      this.resetPaginationStore();
    });
    this.$root.$on("update-pagination-currentpage", (para: any) => {
      this.currentPage = para.page;
    });

    this.currentPage = this.getFormsFlowTaskCurrentPage;

    CamundaRest.getProcessDefinitions(this.token, this.bpmApiUrl).then(
      (response) => {
        this.getProcessDefinitions = response.data;
      }
    );
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
.task-list-container {
  background: #fff;
  padding: .5rem 0;
  .list-group {
    height: calc(calc(100vh - 168px) - 108px); // TODO: replace
    overflow-y: auto;
    .list-group-item {
      padding: 0.75rem 1rem;
      &.task-selected {
        background: #eff8ff;
        border-left: 4px solid #2185d0;
        padding-left: 0.75rem;
      }
    }
  }
  .description {
    opacity: 0.6;
    font-size: 0.9rem;
  }
  .task-details {
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
