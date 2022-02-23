<template>
  <div class="d-flex">
    <div class="task-list-container">
      <div>Filter</div>
      <div class="list-group">
        <div
          v-for="task in tasks"
          :key="task.id"
          class="list-group-item list-group-item-action"
          aria-current="true"
        >
          <h6 class="mb-1">{{ task.name }}</h6>
          <p class="description mb-1">Some placeholder content in a paragraph.</p>
          <div class="d-flex w-100 task-details">
            <div class="assignee">{{ task.assignee }}</div>
            <div
              class="days-ago created-date"
              v-b-tooltip.hover
              title="Tooltip directive content"
            >{{ timedifference(task.created) }}</div>
          </div>
          <!-- <div class="d-flex w-100 task-details">
            <div
              class="days-ago due-date"
              v-b-tooltip.hover.right="getExactDate(task.followUp)"
              v-if="task.followUp"
            >Due: {{ timedifference(task.due) }}</div>
            <div
              class="days-ago"
              v-b-tooltip.hover.right="getExactDate(task.followUp)"
              v-if="task.followUp"
            >Follow-up: {{ timedifference(task.followUp) }}</div>
          </div> -->
        </div>
      </div>
      <div class="mt-2">
        <ul class="pagination mb-0">
          <li class="page-item">
            <a
              class="page-link"
              href="#"
              aria-label="Previous"
            >
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <li class="page-item"><a
              class="page-link"
              href="#"
            >1</a></li>
          <li class="page-item"><a
              class="page-link"
              href="#"
            >2</a></li>
          <li class="page-item"><a
              class="page-link"
              href="#"
            >3</a></li>
          <li class="page-item">
            <a
              class="page-link"
              href="#"
              aria-label="Next"
            >
              <span aria-hidden="true">&raquo;</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
    <div class="task-form-container">Form</div>
  </div>
</template>

<script lang="ts">
/* eslint-disable object-curly-newline */
import { ALL_FILTER, CamundaRest, findFilterKeyOfAllTask, getFormattedDateAndTime, sortByPriorityList } from "../services";
import { Component, Mixins } from 'vue-property-decorator';
import { Payload, TaskListSortType, TaskPayload } from "../models";
import TaskListMixin from "../mixins/TaskListMixin.vue";
import moment from "moment";
import { namespace } from "vuex-class";
import serviceFlowModule from "../store/modules/serviceFlow-module";

const StoreServiceFlowModule = namespace("serviceFlowModule");

@Component({
})
export default class TaftTaskList extends Mixins(TaskListMixin) {
  @StoreServiceFlowModule.Mutation("setFormsFlowTaskLength")
  public setFormsFlowTaskLength: any;
  private tasks: TaskPayload[] = this.getStaticList();
  public perPage: number = 10;
  private payload: Payload = {
    sorting: [] as TaskListSortType[],
    firstResult: 0,
    maxResults: this.perPage,
  };

  beforeCreate() {
    if (!this.$store.hasModule("serviceFlowModule")) {
      this.$store.registerModule("serviceFlowModule", serviceFlowModule);
    }
  }

  async mounted() {
    // eslint-disable-next-line no-console
    console.log('inside compo');
    // const filterListResult = await CamundaRest.filterList(
    //   this.token,
    //   this.bpmApiUrl
    // );
    // this.filterList = sortByPriorityList(filterListResult.data);
    // // this.selectedfilterId = findFilterKeyOfAllTask(this.filterList, ALL_FILTER);
    // this.selectedfilterId = 'b7d61066-eff4-11eb-9e0f-0a580a614207';

    // await this.fetchPaginatedTaskList(this.selectedfilterId, {}, 1, 1);
  }

  async fetchPaginatedTaskList(
    filterId: string,
    requestData: any,
    first: number,
    max: number,
    taskIdToRemove?: string
  ) {
    // this.selectedfilterId = filterId;
    console.log(this.token,
      filterId,
      requestData,
      first,
      max,
      this.bpmApiUrl);
    const paginatedTaskResults = await CamundaRest.filterTaskListPagination(
      this.token,
      filterId,
      requestData,
      first,
      max,
      this.bpmApiUrl
    );
    const responseData = paginatedTaskResults.data;
    const _embedded = responseData._embedded; // data._embedded.task is where the task list is.
    // this.tasks = _embedded.task;
    this.tasks = this.getStaticList();
    this.setFormsFlowTaskLength(responseData.count);

    if (taskIdToRemove) {
      //if the list has the task with taskIdToRemove remove that task and decrement
      if (this.tasks.find((task: TaskPayload) => task.id === taskIdToRemove)) {
        this.tasks = _embedded.task.filter((task: TaskPayload) => task.id !== taskIdToRemove);
        this.setFormsFlowTaskLength(responseData.count--); // Count has to be decreased since one task id is removed.
      }
    }
  }

  timedifference(date: Date) {
    return moment(date).fromNow();
  }

  getExactDate(date: Date) {
    return getFormattedDateAndTime(date);
  }

  getStaticList(): TaskPayload[] {
    return JSON.parse(JSON.stringify([
      {
        "id": "439d91b6-8df0-11ec-acd6-0a580a61289b",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2022-02-14T23:46:00.770+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "403d9d8f-8df0-11ec-acd6-0a580a61289b",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:2:e5d20767-82d3-11ec-a012-0a580a612677",
        "processInstanceId": "403d9d8f-8df0-11ec-acd6-0a580a61289b",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "1a12c33c-8475-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-reviewer",
        "created": "2022-02-02T22:11:42.512+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "18d07ff5-8475-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:2:e5d20767-82d3-11ec-a012-0a580a612677",
        "processInstanceId": "18d07ff5-8475-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "e82fadf8-8458-11ec-a012-0a580a612677",
        "name": "Twinkle's task",
        "assignee": undefined,
        "created": "2022-02-02T18:49:52.907+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "c27ec8c2-8458-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "TimerTestProcessTwinkle:1:9a224b90-8458-11ec-a012-0a580a612677",
        "processInstanceId": "c27ec8c2-8458-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Task_05zc8dg",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "979c8b0c-8458-11ec-a012-0a580a612677",
        "name": "Twinkle's task",
        "assignee": undefined,
        "created": "2022-02-02T18:47:37.725+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "6c07c62a-8458-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "Process_0xt9bix:3:6274c138-8458-11ec-a012-0a580a612677",
        "processInstanceId": "6c07c62a-8458-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Task_05zc8dg",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": true,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "716517a6-82d8-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-designer",
        "created": "2022-01-31T20:57:46.863+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "70781f4f-82d8-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:2:e5d20767-82d3-11ec-a012-0a580a612677",
        "processInstanceId": "70781f4f-82d8-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "db510144-82d0-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-reviewer",
        "created": "2022-01-31T20:03:28.597+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "da31120e-82d0-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "da31120e-82d0-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "e7189ceb-807d-11ec-a012-0a580a612677",
        "name": "Director level approval",
        "assignee": undefined,
        "created": "2022-01-28T21:04:37.806+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "8d23ea0a-8076-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "8d23ea0a-8076-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Director",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "e05ef236-77d9-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2022-01-17T21:10:19.757+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "df4c496e-77d9-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "df4c496e-77d9-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "2ea69046-756f-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2022-01-14T19:21:32.613+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "2da1ca2e-756f-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "2da1ca2e-756f-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "a6c41ee6-74c6-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-designer",
        "created": "2022-01-13T23:15:09.186+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "a5ba01a1-74c6-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "a5ba01a1-74c6-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "0c89afce-7276-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2022-01-11T00:33:08.370+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "0b445e49-7276-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "0b445e49-7276-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "0dd19ffe-629a-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-21T20:10:33.797+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "0c94d9f9-629a-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "0c94d9f9-629a-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "a9496bfc-61c7-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-20T19:04:30.819+0000",
        "due": "2022-01-01T08:00:00.000+0000",
        "followUp": "2021-12-30T08:00:00.000+0000",
        "delegationState": undefined,
        "description": undefined,
        "executionId": "a77d3737-61c7-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "a77d3737-61c7-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "c498c6fb-5c48-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-13T19:13:34.739+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "c327aa86-5c48-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "c327aa86-5c48-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "f552e4ac-5c42-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-13T18:31:59.509+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "f3d4cfe7-5c42-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "f3d4cfe7-5c42-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "fa76e330-5925-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-09T19:26:59.240+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "f95e951b-5925-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "f95e951b-5925-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "7f0cf578-587b-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": undefined,
        "created": "2021-12-08T23:06:37.742+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "7d02a645-587b-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "7d02a645-587b-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "52939004-52c5-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-designer",
        "created": "2021-12-01T16:39:58.908+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "5116187f-52c5-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "5116187f-52c5-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "c51dad2c-526a-11ec-a012-0a580a612677",
        "name": "Approve/send for director level approval",
        "assignee": "aot-designer",
        "created": "2021-12-01T05:51:46.871+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "c2d0fdc7-526a-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "AdvancedPurchase:1:abecdb9e-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "c2d0fdc7-526a-11ec-a012-0a580a612677",
        "taskDefinitionKey": "Approver",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      },
      {
        "id": "9ddba064-526a-11ec-a012-0a580a612677",
        "name": "Review Submission",
        "assignee": "aot-designer",
        "created": "2021-12-01T05:50:41.007+0000",
        "due": undefined,
        "followUp": undefined,
        "delegationState": undefined,
        "description": undefined,
        "executionId": "9cba51db-526a-11ec-a012-0a580a612677",
        "owner": undefined,
        "parentTaskId": undefined,
        "priority": 50,
        "processDefinitionId": "onestepapproval:1:abecb48d-1b6b-11ec-9e2d-0a580a6125e1",
        "processInstanceId": "9cba51db-526a-11ec-a012-0a580a612677",
        "taskDefinitionKey": "reviewer",
        "caseExecutionId": undefined,
        "caseInstanceId": undefined,
        "caseDefinitionId": undefined,
        "suspended": false,
        "formKey": undefined,
        "tenantId": undefined
      }
    ]));
  }
}
</script>

<style lang="scss" scoped>
.task-list-container {
  flex: 0.33;
  .list-group {
    height: calc(calc(100vh - 168px) - 66px); // TODO: replace
    overflow-y: auto;
    .list-group-item {
      padding: 0.75rem 1rem;
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
    }
    .created-date {
      width: 100px;
      text-align: end;
    }
    .due-date {
      color: red;
    }
    .assignee {
      width: calc(100% - 100px);
      font-weight: 600;
      font-size: 0.85rem;
    }
  }
}
.task-form-container {
  flex: 1;
}
</style>
