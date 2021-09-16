<template>
<div class="card">
  <div class="tasklist-container">
    <TaskListSearch
      @update-task-list="onSearchUpdateTasklistResult"
      :tasklength="tasklength"
    />
    <b-list-group class="cft-list-container" v-if="tasks && tasks.length">
      <b-list-group-item
        button
        v-for="(task, idx) in tasks"
        v-bind:key="task.id"
        v-on:click="toggle(idx)"
        :class="{ 'cft-selected': task.id == getFormsFlowTaskId }"
      >
        <div @click="setselectedTask(task.id)" class="cft-select-task">
          <span class="cft-task-title font-weight-bold" data-title="Task Name">{{ task.name }}</span>
          <b-row>
            <b-col cols="7">
              <div
                class="cft-process-title"
                data-tile="Process Definition Name"
              >
                {{
                  getProcessDataFromList(
                    getProcessDefinitions,
                    task.processDefinitionId,
                    "name"
                  )
                }}
              </div>
            </b-col>
            <b-col cols="5">
              <div class="cft-task-assignee" data-title="Task assignee">
                {{ task.assignee }}
              </div>
            </b-col>
          </b-row>
          <b-row>
            <div class="created-details font-11 cft-task-details-assign">
              <b-col cols="9">
                <span
                  class="cft-due-date"
                  :data-title="getExactDate(task.due)"
                  v-if="task.due"
                >
                  Due {{ timedifference(task.due) }},
                </span>
                <span
                  class="cft-due-date"
                  :data-title="getExactDate(task.followUp)"
                  v-if="task.followUp"
                >
                  Follow-up {{ timedifference(task.followUp) }} ,
                </span>
                <span
                  class="cft-due-date"
                  :data-title="getExactDate(task.created)"
                  v-if="task.created"
                >
                  Created {{ timedifference(task.created) }}
                </span>
              </b-col>
              <b-col cols="3" class="cft-priority" data-title="priority">
                {{ task.priority }}
              </b-col>
            </div>
          </b-row>
        </div>
      </b-list-group-item>

      <b-pagination
        v-model="currentPage"
        :total-rows="tasklength"
        :per-page="perPage"
        class="cft-paginate"
      />
    </b-list-group>
    <b-list-group cols="3" v-else>
      <b-row class="cft-not-selected mt-2 ml-1 row">
        <i class="fa fa-circle-exclamation" scale="1"></i>
        <p>No tasks found in the list.</p>
      </b-row>
    </b-list-group>
  </div>
</div>
</template>

<script lang="ts">
import {
  Component, Mixins, Prop, Watch 
} from "vue-property-decorator";
import BaseMixin from "../mixins/BaseMixin.vue";
import {
  CamundaRest, getFormattedDateAndTime
} from "../../services";

import {
  Payload 
} from "../../models/Payload";
import TaskListSearch from "../search/TaskListSearch.vue";
import cloneDeep from "lodash/cloneDeep";
import isEqual from "lodash/isEqual";
import moment from "moment";
import {
  namespace 
} from "vuex-class";

const serviceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    TaskListSearch,
  },
})
export default class LeftSider extends Mixins(BaseMixin) {
  @Prop() private tasks!: Array<object>;
  @Prop() private tasklength!: number;
  @Prop() private perPage!: number;
  @Prop() private selectedfilterId!: string;
  @Prop() private payload!: Payload;

  @serviceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Getter("getFormsFlowTaskId")
  private getFormsFlowTaskId: any;
  @serviceFlowModule.Getter("getFormsFlowactiveIndex")
  private getFormsFlowactiveIndex: any;

  @serviceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Mutation("setFormsFlowTaskId")
  public setFormsFlowTaskId: any;
  @serviceFlowModule.Mutation("setFormsFlowactiveIndex")
  public setFormsFlowactiveIndex: any;

  private getProcessDefinitions: Array<object> = [];
  private processDefinitionId = "";
  private activeIndex = NaN;
  private currentPage = 1;

  @Watch("currentPage")
  onPageChange (newVal: number) {
    this.payload["firstResult"] = (newVal - 1) * this.perPage;
    this.payload["maxResults"] = this.perPage;
    if (this.currentPage !== this.getFormsFlowTaskCurrentPage) {
      this.activeIndex = NaN;
    }
    this.setFormsFlowTaskCurrentPage(this.currentPage);
    this.$root.$emit("call-fetchPaginatedTaskList", {
      filterId: this.selectedfilterId,
      requestData: this.payload,
      firstResult: (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      maxResults: this.perPage,
    });
  }

  timedifference (date: Date) {
    return moment(date).fromNow();
  }

  getProcessDataFromList (
    processList: any[],
    processId: string,
    dataKey: string
  ) {
    const process = processList.find((process) => process.id === processId);
    return process && process[dataKey];
  }

  setselectedTask (taskId: string) {
    this.setFormsFlowTaskId(taskId);
    this.$root.$emit("call-fetchData", {
      selectedTaskId: taskId 
    });
  }

  getExactDate (date: Date) {
    return getFormattedDateAndTime(date);
  }

  toggle (index: number) {
    // this.activeIndex = index;
    this.setFormsFlowactiveIndex(index);
  }

  onSearchUpdateTasklistResult (queryList: object) {
    const requiredParams = {
      ...{
        sorting: this.payload["sorting"] 
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

  mounted () {
    this.$root.$on("call-pagination", () => {
      this.resetPaginationStore();
    });
    this.$root.$on("update-pagination-currentpage", (para: any) => {
      this.currentPage = para.page;
    });
    this.$root.$on("update-activeIndex-pagination", (para: any) => {
      this.activeIndex = para.activeindex;
    });
    if (this.getFormsFlowactiveIndex > 0) {
      this.activeIndex = this.getFormsFlowactiveIndex;
    }
    this.currentPage = this.getFormsFlowTaskCurrentPage;
    this.$root.$emit("call-fetchData", {
      selectedTaskId: this.getFormsFlowTaskId,
    });

    CamundaRest.getProcessDefinitions(this.token, this.bpmApiUrl).then(
      (response) => {
        this.getProcessDefinitions = response.data;
      }
    );
  }

  resetPaginationStore () {
    if (this.getFormsFlowactiveIndex < 9) {
      this.setFormsFlowactiveIndex(this.getFormsFlowactiveIndex + 1);
      this.activeIndex = this.getFormsFlowactiveIndex;
    } else if (this.getFormsFlowactiveIndex === 9) {
      this.setFormsFlowactiveIndex(0);
      this.activeIndex = 0;
      this.setFormsFlowTaskCurrentPage(this.getFormsFlowTaskCurrentPage + 1);
      this.currentPage = this.getFormsFlowTaskCurrentPage;
    }
  }

  beforeDestroy () {
    this.$root.$off("call-pagination");
    this.$root.$off("update-pagination-currentpage");
    this.$root.$off("update-activeIndex-pagination");
  }
}
</script>
