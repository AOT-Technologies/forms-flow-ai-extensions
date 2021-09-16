<template>
  <b-container fluid class="task-outer-container">
    <Header
      v-if="token && bpmApiUrl && maximize"
      :token="token"
      :bpmApiUrl="bpmApiUrl"
      :filterList="filterList"
      :perPage="perPage"
      :selectedfilterId="selectedfilterId"
      :payload="payload"
      :taskSortBy="taskSortBy"
    />
    <b-row class="cft-service-task-list mt-1">
      <b-col xl="3" lg="3" md="12" :class="containerHeight ? `cft-height-h${containerHeight}` : 'cft-height'" v-if="maximize">
        <LeftSider
          v-if="token && bpmApiUrl"
          :token="token"
          :formsflowaiApiUrl="formsflowaiApiUrl"
          :formIOApiUrl="formIOApiUrl"
          :bpmApiUrl="bpmApiUrl"
          :tasks="tasks"
          :Lentask="tasklength"
          :perPage="perPage"
          :selectedfilterId="selectedfilterId"
          :payload="payload"
        />
      </b-col>
      <b-col
        v-if="getFormsFlowTaskId && task"
        :lg="maximize ? 9 : 12"
        md="12"
        :class="containerHeight ? `cft-height-h${containerHeight}` : 'cft-height'"
      >
        <div class="cft-service-task-details">
          <b-row>
            <ExpandContract />
            <span
              class="ml-0 cft-task-header task-header-title"
              v-b-tooltip.hover.top
              title="Task Name"
              >{{ task.name }}
            </span>
          </b-row>
          <br />
          <b-row class="ml-0">
            <span
              class="cft-task-name"
              v-b-tooltip.hover
              title="Process Name"
              >{{ taskProcess }}</span
            >
          </b-row>
          <br />
          <b-row class="ml-0">
            <span
              class="cft-application-id"
              v-b-tooltip.hover
              title="Application Id"
              >Application ID # {{ applicationId }}</span
            >
          </b-row>
          <div class="cft-actionable-container">
            <b-row class="cft-actionable">
              <b-col cols="2" class="align-self-center">
                <span v-if="task.followUp">
                  <i class="fa fa-calendar"></i>
                  {{ timedifference(task.followUp) }}
                  <i
                    class="fa fa-times-circle"
                    @click="removeFollowupDate"
                    id="removeFollow"
                  ></i>
                  <b-tooltip target="removeFollow" triggers="hover">
                    Click to remove <b>FollowUp Date</b>
                  </b-tooltip>
                </span>
                <span v-else>
                  <DatePicker
                    type="datetime"
                    placeholder="Set Follow-up date"
                    v-model="
                      setFollowup[
                        (getFormsFlowTaskCurrentPage - 1) * perPage +
                          getFormsFlowactiveIndex
                      ]
                    "
                    @change="updateFollowUpDate"
                  ></DatePicker>
                </span>
              </b-col>
              <b-col cols="2" class="align-self-center">
                <span v-if="task.due">
                  <i class="fa fa-calendar"></i>
                  {{ timedifference(task.due) }}
                  <i
                    class="fa fa-times-circle"
                    @click="removeDueDate"
                    id="removeDueDate"
                  ></i>
                  <b-tooltip target="removeDueDate" triggers="hover">
                    Click to remove <b> Due Date</b>
                  </b-tooltip>
                </span>
                <span v-else>
                  <DatePicker
                    type="datetime"
                    placeholder="Set Due Date"
                    v-model="
                      setDue[
                        (getFormsFlowTaskCurrentPage - 1) * perPage +
                          getFormsFlowactiveIndex
                      ]
                    "
                    @change="updateDueDate"
                  ></DatePicker>
                </span>
              </b-col>
              <b-col cols="3" class="align-self-center">
                <div
                  id="groups"
                  v-b-modal.AddGroupModal
                  class="group-align word-break"
                >
                  <i class="fa fa-th mr-1"></i>
                  <span
                    class="cft-group-align cft-word-break"
                    v-if="groupListNames"
                  >
                    {{ String(groupListNames) }}
                  </span>
                  <span v-else>Add Groups</span>
                  <b-tooltip target="groups" triggers="hover">
                    <b>Groups</b>
                  </b-tooltip>
                </div>
                <b-modal id="AddGroupModal" ref="modal" :hide-footer="true">
                  <template #modal-header="{ close }">
                    <h5>MANAGE GROUPS</h5>
                    <b-button
                      size="sm"
                      variant="outline-danger"
                      @click="close()"
                    >
                      <h5>
                        Close
                        <i class="fa fa-times"></i>
                      </h5>
                    </b-button>
                  </template>
                  <div class="modal-text">
                    <i class="fa fa-exclamation-circle"></i>
                    You can add a group by typing a group ID into the input
                    field and afterwards clicking the button with the plus sign.
                    <b-row class="mt-3 mb-3">
                      <b-col>
                        <b-button
                          variant="primary"
                          @click="addGroup"
                          :disabled="!setGroup"
                        >
                          <span>Add a group</span>
                          <span>
                            <i class="ml-2 fa fa-plus"></i>
                          </span>
                        </b-button>
                      </b-col>
                      <b-col>
                        <b-form-input
                          type="text"
                          placeholder="Group ID"
                          v-model="setGroup"
                          v-on:keyup.enter="addGroup"
                        ></b-form-input>
                      </b-col>
                    </b-row>
                    <b-row>
                      <b-col v-if="groupList.length">
                        <ul v-for="g in groupList" :key="g.groupId">
                          <div class="mt-1">
                            <i
                              class="fa fa-times mr-2 click-element"
                              @click="deleteGroup(g.groupId)"
                            ></i>
                            <span>{{ g.groupId }}</span>
                          </div>
                        </ul>
                      </b-col>
                    </b-row>
                  </div>
                </b-modal>
              </b-col>
              <b-col class="align-self-center">
                <span v-if="task.assignee">
                  <div v-if="editAssignee" class="cft-user-edit">
                    <div class="cft-assignee-change-box">
                      <v-select
                        :label="selectSearchType"
                        :options="reviewerUsersList"
                        v-model="userSelected"
                        placeholder="Search by Lastname"
                        class="assignee-align float-left"
                      />
                      <i
                        @click="onSetassignee"
                        class="fa fa-check cft-assignee-tickmark-icon cft-icon-border"
                      ></i>
                      <i
                        @click="toggleassignee"
                        class="fa fa-times cft-assignee-cancel-icon cft-icon-border"
                      ></i>
                      <b-dropdown
                        id="dropdown-right"
                        class="ml-3 cft-select-generic-search"
                        variant="primary"
                      >
                        <template #button-content>
                          <span><i class="fa fa-filter cft-assignee-filter-icon" /></span>
                        </template>
                        <b-dd-item
                          v-for="(row, index) in UserSearchListLabel"
                          :key="row.id"
                          @click="
                            setSelectedUserSearchBy(row.searchType, index)
                          "
                          :active="index === activeUserSearchindex"
                        >
                          <span>{{ row.label }}</span>
                        </b-dd-item>
                      </b-dropdown>
                    </div>
                  </div>
                  <div v-else>
                    <span
                      id="setAssignee"
                      v-b-tooltip.hover
                      title="Click to change Assignee"
                    >
                      <i class="fa fa-user cft-person-fill" />
                      <span class="cft-user-span" @click="toggleassignee">
                        {{ task.assignee }}
                      </span>
                    </span>
                    <span
                      id="resetAssignee"
                      v-b-tooltip.hover
                      title="Reset Assignee"
                    >
                      <i class="fa fa-times ml-1" @click="onUnClaim" />
                    </span>
                  </div>
                </span>
                <span v-else>
                  <div
                    @click="onClaim"
                    v-b-tooltip.hover.left
                    title="Claim task"
                  >
                    <span id="claimAssignee">
                      <i class="fa fa-user" /> Claim
                    </span>
                  </div>
                </span>
              </b-col>
            </b-row>
            <div class="height-100">
              <b-tabs pills class="height-100" content-class="mt-3">
                <b-tab title="Form" active>
                  <div v-if="showForm" class="ml-4 mr-4 form-tab-conatiner">
                    <b-overlay
                      :show="task.assignee !== userName"
                      variant="light"
                      opacity="0.75"
                      spinner-type="none"
                      aria-busy="true"
                    >
                      <!-- don't remove form-render class -->
                      <formio
                        v-if="formioUrl"
                        :src="formioUrl"
                        :options="
                          task.assignee !== userName
                            ? readFormOptions
                            : editFormoptions
                        "
                        v-on:submit="onFormSubmitCallback"
                        v-on:customEvent="oncustomEventCallback"
                        class="form-render"
                      ></formio>
                    </b-overlay>
                  </div>
                </b-tab>
                <b-tab title="History">
                  <TaskHistory
                    :taskHistoryList="taskHistoryList"
                    :applicationId="applicationId"
                  />
                </b-tab>
                <b-tab
                  class="cft-diagram-container"
                  id="diagramContainer"
                  title="Diagram"
                >
                  <div class="diagram-full-screen" id="canvas"></div>
                </b-tab>
              </b-tabs>
            </div>
          </div>
        </div>
      </b-col>
      <b-col v-else>
        <b-row class="cft-not-selected mt-2 ml-1 row">
          <i
            class="fa fa-exclamation-circle-fill"
            variant="secondary"
            scale="1"
          ></i>
          <p>Select a task in the list.</p>
        </b-row>
      </b-col>
    </b-row>
  </b-container>
</template>

<script lang="ts">

import "font-awesome/scss/font-awesome.scss";
import "formiojs/dist/formio.full.min.css";
import "vue2-datepicker/index.css";
import "semantic-ui-css/semantic.min.css";
import "../styles/user-styles.css";
import "../styles/camundaFormIOTasklist.scss";
import {
  ALL_FILTER,
  CamundaRest,
  SEARCH_USERS_BY,
  SocketIOService,
  TaskListSortType,
  authenticateFormio,
  findFilterKeyOfAllTask,
  getFormDetails,
  getISODateTime,
  getTaskFromList,
  getUserName,
  getformHistoryApi,
  reviewerGroup,
  sortByPriorityList
} from "../services";
import {
  Component, Mixins, Prop 
} from "vue-property-decorator";
import {
  CustomEventPayload, 
  FilterPayload, 
  FormRequestActionPayload, 
  FormRequestPayload, 
  GroupListPayload, 
  Payload, 
  TaskHistoryListPayload, 
  TaskPayload, 
  UserListPayload, 
  UserPayload, 
  UserSearchListLabelPayload 
} from "../models";
import BpmnViewer from "bpmn-js";
import DatePicker from "vue2-datepicker";
import ExpandContract from "./addons/ExpandContract.vue";
import {
  Form 
} from "vue-formio";
import Header from "./layout/Header.vue";
import LeftSider from "./layout/LeftSider.vue";
import TaskHistory from "../components/addons/TaskHistory.vue";
import TaskListMixin from "./mixins/TaskListMixin.vue";
import moment from "moment";
import {
  namespace 
} from "vuex-class";
import serviceFlowModule from "../store/modules/serviceFlow-module";
import vSelect from "vue-select";

const StoreServiceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    formio: Form,
    DatePicker,
    TaskHistory,
    Header,
    LeftSider,
    vSelect,
    ExpandContract,
    BpmnViewer,
  },
})
export default class Tasklist extends Mixins(TaskListMixin) {
  @Prop() private getTaskId!: string;
  @Prop() private containerHeight!: string;
  @Prop({
    default: "lastName"
  }) userListType!: string;
  @Prop({
    default: "created"
  }) public taskSortBy!: string

  @StoreServiceFlowModule.Getter("getFormsFlowTaskCurrentPage")
  private getFormsFlowTaskCurrentPage: any;
  @StoreServiceFlowModule.Getter("getFormsFlowTaskId")
  private getFormsFlowTaskId: any;
  @StoreServiceFlowModule.Getter("getFormsFlowactiveIndex")
  private getFormsFlowactiveIndex: any;

  @StoreServiceFlowModule.Mutation("setFormsFlowTaskCurrentPage")
  public setFormsFlowTaskCurrentPage: any;
  @StoreServiceFlowModule.Mutation("setFormsFlowTaskId")
  public setFormsFlowTaskId: any;
  @StoreServiceFlowModule.Mutation("setFormsFlowactiveIndex")
  public setFormsFlowactiveIndex: any;

  private tasks: TaskPayload[] = [];
  private fulltasks: TaskPayload[] = [];
  private taskProcess: string = "";
  private formId: string = "";
  private submissionId: string = "";
  private formioUrl: string = "";
  private task: TaskPayload = {
  };
  private setFollowup: Array<Date| null> = [];
  private setDue: Array<Date| null> = [];
  private setGroup = null;
  private userSelected: UserListPayload = {
  };
  public perPage: number = 10;
  private tasklength: number = 0;
  private editFormoptions: object = {
    noAlerts: false,
    i18n: {
      en: {
        error: "Please fix the errors before submitting again."
      },
    },
  };
  private readFormOptions: object = {
    readOnly: true
  };
  private filterList: FilterPayload[] = [];
  private editAssignee: boolean = false;
  private applicationId: string = "";
  private groupList: GroupListPayload[] = [];
  private groupListNames?: Array<string> = [];
  private groupListItems: string[] = [];
  private userEmail: string = "external";
  private selectedfilterId: string = "";
  private xmlData!: string;
  private payload: Payload = {
    active: true,
    sorting: [] as TaskListSortType[],
    firstResult: 0,
    maxResults: this.perPage,
  };
  private taskHistoryList: TaskHistoryListPayload[] = [];
  private reviewerUsersList: UserListPayload[] = [];
  private selectSearchType: string = "lastName";
  private taskIdValue: string = "";
  private taskId2: string = "";
  private taskIdWebsocket: string = "";
  private eventNameWebSocket: string = "";
  private showForm: boolean = false;
  private activeUserSearchindex = 1;
  private UserSearchListLabel: UserSearchListLabelPayload[] = SEARCH_USERS_BY;
  
  checkforTaskID () {
    if (this.getTaskId) {
      this.taskIdValue = this.getTaskId;
    }
    if (!this.getTaskId) {
      const routeparams = this.$route?.query?.taskId;
      if (typeof routeparams === "string" && this.$route.query.taskId) {
        this.taskIdValue = routeparams;
      }
    }
    this.userName = getUserName();
  }

  timedifference (date: Date) {
    return moment(date).fromNow();
  }

  toggleassignee () {
    this.editAssignee = !this.editAssignee;
    this.userSelected["code"] = this.task.assignee;
  }

  setSelectedUserSearchBy (searchby: string, index: number) {
    this.selectSearchType = searchby;
    this.activeUserSearchindex = index;
  }

  async onFormSubmitCallback (actionType = "") {
    if (this.task.id!==null) {
      await this.onBPMTaskFormSubmit(this.task.id!, actionType);
      await this.reloadTasks();
    }
  }

  async addGroup () {
    await CamundaRest.createTaskGroupByID(
      this.token,
      this.task.id!,
      this.bpmApiUrl,
      {
        userId: null,
        groupId: this.setGroup,
        type: "candidate",
      }
    ).then(async () => {
      await this.getGroupDetails();
      await this.reloadCurrentTask();
      this.setGroup = null;
    });
  }

  async getGroupDetails () {
    const grouplist = await CamundaRest.getTaskGroupByID(
      this.token,
      this.task.id!,
      this.bpmApiUrl
    );
    this.groupList = grouplist.data;
    this.groupListItems = [];
    this.groupListNames = undefined;
    for (const group of grouplist.data) {
      this.groupListItems.push(group.groupId);
    }
    if (this.groupListItems.length) {
      this.groupListNames = this.groupListItems;
    }
  }

  async deleteGroup (groupid: string) {
    await CamundaRest.deleteTaskGroupByID(
      this.token,
      this.task.id!,
      this.bpmApiUrl,
      {
        groupId: groupid,
        type: "candidate",
      }
    ).then(async () => {
      await this.getGroupDetails();
      await this.reloadCurrentTask();
    });
  }

  async onBPMTaskFormSubmit (taskId: string, actionType: string) {
    let formRequestFormat: FormRequestPayload = {
      variables: {
        formUrl: {
          value: this.formioUrl,
        },
        applicationId: {
          value: this.applicationId,
        },
      },
    };
    if (actionType !== "") {
      const newformRequestFormat: FormRequestActionPayload = Object.assign(
        formRequestFormat.variables,
        {
          action: {
            value: actionType
          }
        }
      );
      formRequestFormat = {
        variables: newformRequestFormat
      };
    }

    await CamundaRest.formTaskSubmit(
      this.token,
      taskId,
      formRequestFormat,
      this.bpmApiUrl
    ).then(async () => {
      await this.reloadCurrentTask();
    });
  }

  async getBPMTaskDetail (taskId: string) {
    await CamundaRest.getTaskById(this.token, taskId, this.bpmApiUrl).then(
      async (result) => {
        this.task = result.data;
        await CamundaRest.getProcessDefinitionById(
          this.token,
          this.task.processDefinitionId!,
          this.bpmApiUrl
        ).then((res) => {
          this.taskProcess = res.data.name;
        });
      }
    );
    await this.getGroupDetails();
  }

  async getTaskFormIODetails (taskId: string) {
    if (this.taskIdWebsocket === taskId) {
      this.showForm = false;
      this.formioUrl = "";
    }
    CamundaRest.getVariablesByTaskId(this.token, taskId, this.bpmApiUrl).then(
      (result) => {
        if (result.data["formUrl"]?.value) {
          const formUrlPattern = result.data["formUrl"]?.value;
          const {
            formioUrl, formId, submissionId 
          } = getFormDetails(
            formUrlPattern,
            this.formIOApiUrl
          );

          this.formioUrl = formioUrl;
          this.submissionId = submissionId;
          this.formId = formId;
        }
        this.showForm = true;
      }
    );
  }

  async getTaskHistoryDetails (taskId: string) {
    this.taskHistoryList = [];
    const result = await CamundaRest.getVariablesByTaskId(
      this.token,
      taskId,
      this.bpmApiUrl
    );

    if (result.data && result.data["applicationId"]?.value) {
      this.applicationId = result.data["applicationId"].value;
      const applicationHistoryList = await getformHistoryApi(
        this.formsflowaiApiUrl,
        result.data["applicationId"].value,
        this.token
      );
      this.taskHistoryList = applicationHistoryList.applications;
    }
  }

  async getTaskProcessDiagramDetails (task: TaskPayload) {
    await CamundaRest.getProcessDiagramXML(
      this.token,
      task.processDefinitionId!,
      this.bpmApiUrl
    ).then(async (res) => {
      this.xmlData = res.data.bpmn20Xml;
      const div = document.getElementById("canvas");
      if (div) {
        div.innerHTML = "";
      }
      const viewer = new BpmnViewer({
        container: "#canvas",
      });

      try {
        await viewer.importXML(this.xmlData);
        viewer.get("canvas").zoom("fit-viewport");
      } catch (err) {
        console.error("error rendering process diagram", err); // eslint-disable-line no-console
      }
    });
  }

  oncustomEventCallback = async (customEvent: CustomEventPayload) => {
    switch (customEvent.type) {
    case "reloadTasks":
      await this.reloadTasks();
      break;
    case "reloadCurrentTask":
      await this.reloadCurrentTask();
      break;
    case "actionComplete":
      this.onFormSubmitCallback(customEvent.actionType);
      break;
    }
  };

  async reloadTasks () {
    this.setFormsFlowTaskId("");
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage
    );
  }

  async reloadCurrentTask () {
    await this.getBPMTaskDetail(this.task.id!);
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage
    );
  }

  async reloadLHSTaskList () {
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage
    );
  }

  async onClaim () {
    CamundaRest.claim(
      this.token,
      this.task.id!,
      {
        userId: this.userName
      },
      this.bpmApiUrl
    )
      .then(() => {
        if (!SocketIOService.isConnected()) {
          this.fetchTaskData(this.getFormsFlowTaskId);
          this.reloadLHSTaskList();
        }
      })
      .catch((error) => {
        console.error("Error", error); // eslint-disable-line no-console
      });
  }

  async onUnClaim () {
    await CamundaRest.unclaim(this.token, this.task.id!, this.bpmApiUrl)
      .then(async () => {
        if (!SocketIOService.isConnected()) {
          this.fetchTaskData(this.getFormsFlowTaskId);
          this.reloadLHSTaskList();
        }
      })
      .catch((error) => {
        console.error("Error", error); // eslint-disable-line no-console
      });
  }

  async onSetassignee () {
    await CamundaRest.setassignee(
      this.token,
      this.task.id!,
      {
        userId: this.userSelected?.code
      },
      this.bpmApiUrl
    )
      .then(async () => {
        this.fetchTaskData(this.getFormsFlowTaskId);
      })
      .catch((error) => {
        console.error("Error", error); // eslint-disable-line no-console
      });
    await this.toggleassignee();
    await this.reloadLHSTaskList();
  }

  async fetchFullTaskList (filterId: string, requestData: Payload) {
    await CamundaRest.filterTaskList(
      this.token,
      filterId,
      requestData,
      this.bpmApiUrl
    ).then((result) => {
      this.fulltasks = result.data;
    });
  }

  async fetchTaskListCount (filterId: string, requestData: Payload) {
    await CamundaRest.filterTaskListCount(
      this.token,
      filterId,
      requestData,
      this.bpmApiUrl
    ).then((result) => {
      this.tasklength = result.data.count;
    });
  }

  async fetchPaginatedTaskList (
    filterId: string,
    requestData: object,
    first: number,
    max: number
  ) {
    this.selectedfilterId = filterId;
    await CamundaRest.filterTaskListPagination(
      this.token,
      filterId,
      requestData,
      first,
      max,
      this.bpmApiUrl
    ).then((result) => {
      this.tasks = result.data;
    });
  }

  async updateTaskDatedetails (taskId: string, task: TaskPayload) {
    await CamundaRest.updateTasksByID(this.token, taskId, this.bpmApiUrl, task)
      .then(async () => {
        if (!SocketIOService.isConnected()) {
          await this.reloadCurrentTask();
        }
      })
      .catch((error) => {
        console.error("Error", error); // eslint-disable-line no-console
      });
  }

  async updateFollowUpDate () {
    const referenceobject = this.task;
    try {
      referenceobject["followUp"] = getISODateTime(
        this.setFollowup[
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage
            + this.getFormsFlowactiveIndex
        ]!
      );
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Follow date error"); // eslint-disable-line no-console
    }
  }

  async updateDueDate () {
    const referenceobject = this.task;
    try {
      referenceobject["due"] = getISODateTime(
        this.setDue[
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage
            + this.getFormsFlowactiveIndex
        ]!
      );
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Due date error"); // eslint-disable-line no-console
    }
  }

  async removeDueDate () {
    const referenceobject = this.task;
    try {
      this.setDue[
        (this.getFormsFlowTaskCurrentPage - 1) * this.perPage
          + this.getFormsFlowactiveIndex
      ] = null;
      referenceobject["due"] = null;
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Due date error"); // eslint-disable-line no-console
    }
  }

  async removeFollowupDate () {
    const referenceobject = this.task;
    try {
      referenceobject["followUp"] = null;
      this.setFollowup[
        (this.getFormsFlowTaskCurrentPage - 1) * this.perPage
          + this.getFormsFlowactiveIndex
      ] = null;
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Follow up date error"); // eslint-disable-line no-console
    }
  }

  async fetchTaskData (taskId: string) {
    const res = await CamundaRest.getTaskById(
      this.token,
      taskId,
      this.bpmApiUrl
    );
    this.task = res.data;
    if (this.task) {
      await this.getBPMTaskDetail(taskId);
      await CamundaRest.getVariablesByProcessId(
        this.token,
        this.task.processInstanceId!,
        this.bpmApiUrl
      );
      await this.getTaskFormIODetails(taskId);
      await this.getTaskHistoryDetails(taskId);
      await this.getTaskProcessDiagramDetails(this.task);
    }
  }

  async findTaskIdDetailsFromURLrouter (taskId: string, fulltasks: TaskPayload[]) {
    this.task = getTaskFromList(fulltasks, taskId);
    this.setFormsFlowTaskId(this.taskIdValue);
    const pos = fulltasks
      .map(function (e: TaskPayload) {
        return e.id;
      })
      .indexOf(this.taskIdValue);
    this.setFormsFlowactiveIndex(pos % this.perPage);
    this.$root.$emit("update-activeIndex-pagination", {
      activeindex: this.getFormsFlowactiveIndex,
    });
    this.setFormsFlowTaskCurrentPage(Math.floor(pos / this.perPage) + 1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
  }

  async mounted () {
    this.setFormsFlowTaskCurrentPage(1);
    this.setFormsFlowTaskId("");
    this.setFormsFlowactiveIndex(NaN);
    this.$root.$on("call-fetchData", async (para: any) => {
      this.editAssignee = false;
      this.setFormsFlowTaskId(para.selectedTaskId);
      await this.fetchTaskData(this.getFormsFlowTaskId);
    });

    this.$root.$on("call-fetchPaginatedTaskList", async (para: any) => {
      this.selectedfilterId = para.filterId;
      this.payload = para.requestData;
      await this.fetchPaginatedTaskList(
        para.filterId,
        para.requestData,
        para.firstResult,
        para.maxResults
      );
    });

    this.$root.$on("call-fetchTaskListCount", async (para: any) => {
      await this.fetchTaskListCount(para.filterId, para.requestData);
    });

    this.$root.$on("call-managerScreen", (para: any) => {
      this.maximize = para.maxi;
    });

    this.checkProps();
    this.checkforTaskID();
    authenticateFormio(
      this.formIOResourceId,
      this.formIOReviewerId,
      this.formIOReviewer,
      this.userEmail,
      this.formIOUserRoles
    );
    await CamundaRest.filterList(this.token, this.bpmApiUrl).then(
      async (response) => {
        this.filterList = sortByPriorityList(response.data);
        this.selectedfilterId = findFilterKeyOfAllTask(
          this.filterList,
          ALL_FILTER
        );
        await this.fetchTaskListCount(this.selectedfilterId, this.payload);
        await this.reloadLHSTaskList();
      }
    );

    if (SocketIOService.isConnected()) {
      SocketIOService.disconnect();
    }

    SocketIOService.connect(
      this.webSocketEncryptkey,
      (refreshedTaskId: string, eventName: string, error: string) => {
        this.taskIdWebsocket = refreshedTaskId;
        if (error) {
          this.$bvToast.toast(
            `WebSocket is not connected which will cause
            some inconsistency. System is trying to reconnect,
            if you see this message for more than 10sec,
            please refresh the page and try`,
            {
              title: "Websocket Alert",
              autoHideDelay: 10000,
              variant: "warning",
              solid: true,
            }
          );
        }

        if (eventName === "create") {
          this.fetchTaskListCount(this.selectedfilterId, this.payload);
          this.reloadLHSTaskList();
          this.$root.$emit("call-pagination");
        } else {
          if (
            this.selectedfilterId
            || (this.getFormsFlowTaskId
              && refreshedTaskId === this.getFormsFlowTaskId)
          ) {
            if (this.task.assignee === this.userName) {
              this.getBPMTaskDetail(this.task.id!);
            } else {
              this.fetchTaskData(this.getFormsFlowTaskId);
            }
          }
          if (this.selectedfilterId) {
            this.reloadLHSTaskList();
          }
        }
      }
    );

    await CamundaRest.getUsersByMemberGroups(
      this.token,
      this.bpmApiUrl,
      reviewerGroup
    ).then((response) => {
      this.reviewerUsersList = [];
      response.data.forEach((user: UserPayload) => {
        this.reviewerUsersList.push({
          code: user.id,
          email: user.email!,
          firstName: `${user.firstName!} ${user.lastName!}`,
          lastName: `${user.lastName!} ${user.firstName!}`,
        });
      });
    });

    //We used two variables - taskId2 and taskIdValue because the router value from gettaskId
    // is always constant,so after calling the required task details from router to use other
    // tasks in list we need to set taskId2 value as ''
    if (this.taskId2 !== this.taskIdValue) {
      this.taskId2 = this.taskIdValue;
    } else {
      this.taskId2 = "";
    }
    if (this.taskId2 !== "") {
      await this.fetchFullTaskList(this.selectedfilterId, this.payload);
      await this.findTaskIdDetailsFromURLrouter(this.taskId2, this.fulltasks);
      await this.fetchTaskData(this.taskId2);
      this.taskId2 = "";
    }
  }

  beforeDestroy () {
    SocketIOService.disconnect();
    this.$root.$off("call-fetchData");
    this.$root.$off("call-fetchPaginatedTaskList");
    this.$root.$off("call-fetchTaskListCount");
    this.$root.$off("call-managerScreen");
    if (this.$store.hasModule("serviceFlowModule")) {
      this.$store.unregisterModule("serviceFlowModule");
    }
  }
  beforeCreate () {
    if (!this.$store.hasModule("serviceFlowModule")) {
      this.$store.registerModule("serviceFlowModule", serviceFlowModule);
    }
  }
}
</script>
