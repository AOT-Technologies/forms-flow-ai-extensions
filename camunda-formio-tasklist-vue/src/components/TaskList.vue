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
    />
    <b-row class="cft-service-task-list mt-1">
      <b-col xl="3" lg="3" md="12" class="cft-first" v-if="maximize">
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
      <b-col v-if="getFormsFlowTaskId && task" :lg="maximize ? 9 : 12" md="12">
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
              <b-col
                class="align-self-center"
                v-if="task.followUp"
                cols="12"
                md="3"
              >
                <span>
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
              </b-col>
              <b-col class="d-flex align-items-end" v-else cols="12" md="3">
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
              </b-col>
              <b-col
                class="d-flex align-items-end"
                v-if="task.due"
                cols="12"
                md="3"
              >
                <span>
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
              </b-col>
              <b-col class="d-flex align-items-end" v-else cols="12" md="3">
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
              </b-col>
              <b-col class="d-flex align-items-end" cols="12" md="3">
                <div id="groups" v-b-modal.AddGroupModal>
                  <i class="fa fa-th mr-1"></i>
                  <span class="group-align word-break" v-if="groupListNames">
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
              <b-col
                :class="task.assignee ? 'd-flex align-items-end' : ''"
                v-if="task.assignee"
              >
                <div v-if="editAssignee" class="cft-user-edit">
                  <div class="cft-assignee-change-box">
                    <div class="d-flex justify-content-end row">
                      <i
                        @click="onSetassignee"
                        class="fa fa-check assignee-tickmark-icon icon-border"
                      ></i>
                      <i
                        @click="toggleassignee"
                        class="fa fa-times assignee-cancel-icon icon-border"
                      ></i>
                    </div>
                    <div class="row">
                      <v-select
                        @search="fetchOptions"
                        :options="autoUserList"
                        v-model="userSelected"
                        full-width
                        placeholder="Search by Lastname"
                        class="d-flex align-items-end"
                      />
                    </div>
                  </div>
                </div>
                <div class="cft-user-details" v-else>
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
              </b-col>
              <b-col
                :class="task.assignee ? '' : 'd-flex align-items-end'"
                v-else
              >
                <div @click="onClaim" v-b-tooltip.hover.left title="Claim task">
                  <span id="claimAssignee">
                    <i class="fa fa-user" /> Claim
                  </span>
                </div>
              </b-col>
            </b-row>
            <div class="height-100">
              <b-tabs pills class="height-100" content-class="mt-3">
                <b-tab title="Form" active>
                  <div class="ml-4 mr-4 form-tab-conatiner">
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
import { Component, Mixins, Prop } from "vue-property-decorator";
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  findFilterKeyOfAllTask,
  getTaskFromList,
  getUserName,
} from "../services/utils";
import BpmnViewer from "bpmn-js";
import CamundaRest from "../services/camunda-rest";
import DatePicker from "vue2-datepicker";
import ExpandContract from "./addons/ExpandContract.vue";
import { Form } from "vue-formio";
import Header from "./layout/Header.vue";
import LeftSider from "./layout/LeftSider.vue";
import { Payload } from "../services/TasklistTypes";
import SocketIOService from "../services/SocketIOServices";
import TaskHistory from "../components/TaskHistory.vue";
import TaskListMixin from "./mixins/TaskListMixin.vue";
import { authenticateFormio } from "../services/formio-token";
import { getFormDetails } from "../services/get-formio";
import { getISODateTime } from "../services/format-time";
import { getformHistoryApi } from "../services/formsflowai-api";
import moment from "moment";
import { namespace } from "vuex-class";
import { reviewerGroup } from "../services/constants";
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
  @Prop({ default: "lastName" }) userListType!: string;

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

  private tasks: Array<object> = [];
  private fulltasks: Array<object> = [];
  private taskProcess: string | null = null;
  private formId: string = "";
  private submissionId: string = "";
  private formioUrl: string = "";
  private task: any = {};
  private setFollowup: any = [];
  private setDue: any = [];
  private setGroup = null;
  private userSelected: any = {};
  public perPage: number = 10;
  private tasklength: number = 0;
  private editFormoptions: object = {
    noAlerts: false,
    i18n: {
      en: { error: "Please fix the errors before submitting again." },
    },
  };
  private readFormOptions: object = { readOnly: true };
  private filterList: Array<object> = [];
  private editAssignee: boolean = false;
  private applicationId: string = "";
  private groupList: Array<object> = [];
  private groupListNames: Array<string> | null = null;
  private groupListItems: string[] = [];
  private userEmail: string = "external";
  private selectedfilterId: string = "";
  private xmlData!: string;
  private payload: Payload = {
    active: true,
    sorting: TASK_FILTER_LIST_DEFAULT_PARAM,
    firstResult: 0,
    maxResults: this.perPage,
  };
  private taskHistoryList: Array<object> = [];
  private autoUserList: any = [];
  private taskIdValue: string = "";
  private taskId2: string = "";
  // private showForm: boolean = false
  private refresedTaskFromWebSocket: string = ""
  private eventNameWebSocket: string = ""

  checkPropsIsPassedAndSetValue () {
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

  async onFormSubmitCallback () {
    if (this.task.id) {
      await this.onBPMTaskFormSubmit(this.task.id);
      await this.reloadTasks();
    }
  }

  async addGroup () {
    await CamundaRest.createTaskGroupByID(this.token, this.task.id, this.bpmApiUrl, {
      userId: null,
      groupId: this.setGroup,
      type: "candidate",
    }).then(async () => {
      await this.getGroupDetails();
      await this.reloadCurrentTask();
      this.setGroup = null;
    });
  }

  async getGroupDetails () {
    const grouplist = await CamundaRest.getTaskGroupByID(
      this.token,
      this.task.id,
      this.bpmApiUrl,
    );
    this.groupList = grouplist.data;
    this.groupListItems = [];
    this.groupListNames = null;
    for (const group of grouplist.data) {
      this.groupListItems.push(group.groupId);
    }
    if (this.groupListItems.length) {
      this.groupListNames = this.groupListItems;
    }
  }

  async deleteGroup (groupid: string) {
    await CamundaRest.deleteTaskGroupByID(this.token, this.task.id, this.bpmApiUrl, {
      groupId: groupid,
      type: "candidate",
    }).then(async () => {
      await this.getGroupDetails();
      await this.reloadCurrentTask();
    });
  }

  async onBPMTaskFormSubmit (taskId: string) {
    const formRequestFormat = {
      variables: {
        formUrl: {
          value: this.formioUrl,
        },
        applicationId: {
          value: this.applicationId,
        },
      },
    };
    await CamundaRest.formTaskSubmit(
      this.token,
      taskId,
      formRequestFormat,
      this.bpmApiUrl,
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
          this.task.processDefinitionId,
          this.bpmApiUrl,
        ).then((res) => {
          this.taskProcess = res.data.name;
        });
      },
    );
    await this.getGroupDetails();
  }

  async getTaskFormIODetails (taskId: string) {
    if (this.eventNameWebSocket !== "create"){
      console.log('hereeeeeeeeeeeeeeeee');
      if (this.refresedTaskFromWebSocket === taskId){
        this.formioUrl = "";
      }
      await CamundaRest.getVariablesByTaskId(this.token, taskId, this.bpmApiUrl).then(
        async (result) => {
          if (result.data["formUrl"]?.value) {
            const formioUrlPattern = result.data["formUrl"]?.value;
            const { formioUrl, formId, submissionId } = getFormDetails(
              formioUrlPattern,
              this.formIOApiUrl,
            );
            this.formioUrl = formioUrl;
            this.submissionId = submissionId;
            this.formId = formId;
          }
        });
    }
  }

  async getTaskHistoryDetails (taskId: string) {
    this.taskHistoryList = [];
    await CamundaRest.getVariablesByTaskId(this.token, taskId, this.bpmApiUrl).then(
      (result) => {
        if (result.data && result.data["applicationId"]?.value) {
          this.applicationId = result.data["applicationId"].value;
          getformHistoryApi(
            this.formsflowaiApiUrl,
            result.data["applicationId"].value,
            this.token,
          ).then((r) => {
            this.taskHistoryList = r.data.applications;
          });
        }
      },
    );
  }

  async getTaskProcessDiagramDetails (task: any) {
    await CamundaRest.getProcessDiagramXML(
      this.token,
      task.processDefinitionId,
      this.bpmApiUrl,
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
        const { warnings } = await viewer.importXML(this.xmlData);
        viewer.get("canvas").zoom("fit-viewport");
        console.log(warnings);
      } catch (err) {
        console.error("error rendering process diagram", err);
      }
    });
  }

  oncustomEventCallback = async (customEvent: any) => {
    switch (customEvent.type) {
    case "reloadTasks":
      await this.reloadTasks();
      break;
    case "reloadCurrentTask":
      await this.reloadCurrentTask();
      break;
    }
  }

  async reloadTasks () {
    this.setFormsFlowTaskId("");
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage,
    );
  }

  async reloadCurrentTask () {
    await this.getBPMTaskDetail(this.task.id);
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage,
    );
  }

  async onClaim () {
    await CamundaRest.claim(
      this.token,
      this.task.id,
      { userId: this.userName },
      this.bpmApiUrl,
    )
      .then(async () => {
        if (!SocketIOService.isConnected()) {
          console.log('fetch onClaim');
          await this.fetchTaskData(this.getFormsFlowTaskId);
        }
        await this.fetchPaginatedTaskList(
          this.selectedfilterId,
          this.payload,
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
          this.perPage,
        );
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  async onUnClaim () {
    await CamundaRest.unclaim(this.token, this.task.id, this.bpmApiUrl)
      .then(async () => {
        await this.reloadCurrentTask();
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  async onSetassignee () {
    await CamundaRest.setassignee(
      this.token,
      this.task.id,
      { userId: this.userSelected?.code },
      this.bpmApiUrl,
    )
      .then(async () => {
        
        console.log('fetch taskonSetassignee');
        await this.fetchTaskData(this.getFormsFlowTaskId);
        await this.fetchPaginatedTaskList(
          this.selectedfilterId,
          this.payload,
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
          this.perPage,
        );
      })
      .catch((error) => {
        console.error("Error", error);
      });
    this.toggleassignee();
  }

  async fetchTaskList (filterId: string, requestData: object) {
    await CamundaRest.filterTaskList(
      this.token,
      filterId,
      requestData,
      this.bpmApiUrl,
    ).then((result) => {
      this.fulltasks = result.data;
      this.tasklength = result.data.length;
    });
  }

  async fetchPaginatedTaskList (
    filterId: string,
    requestData: object,
    first: number,
    max: number,
  ) {
    this.selectedfilterId = filterId;
    await CamundaRest.filterTaskListPagination(
      this.token,
      filterId,
      requestData,
      first,
      max,
      this.bpmApiUrl,
    ).then((result) => {
      this.tasks = result.data;
    });
  }

  async updateTaskDatedetails (taskId: string, task: any) {
    await CamundaRest.updateTasksByID(this.token, taskId, this.bpmApiUrl, task)
      .then(async () => {
        await this.reloadCurrentTask();
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  async updateFollowUpDate () {
    const referenceobject = this.task;
    try {
      referenceobject["followUp"] = getISODateTime(
        this.setFollowup[
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage +
            this.getFormsFlowactiveIndex
        ],
      );
      await this.updateTaskDatedetails(this.task.id, referenceobject);
    } catch {
      console.warn("Follow date error");
    }
  }

  async updateDueDate () {
    const referenceobject = this.task;
    try {
      referenceobject["due"] = getISODateTime(
        this.setDue[
          (this.getFormsFlowTaskCurrentPage - 1) * this.perPage +
            this.getFormsFlowactiveIndex
        ],
      );
      await this.updateTaskDatedetails(this.task.id, referenceobject);
    } catch {
      console.warn("Due date error");
    }
  }

  async removeDueDate () {
    const referenceobject = this.task;
    try {
      this.setDue[
        (this.getFormsFlowTaskCurrentPage - 1) * this.perPage +
          this.getFormsFlowactiveIndex
      ] = null;
      referenceobject["due"] = null;
      await this.updateTaskDatedetails(this.task.id, referenceobject);
    } catch {
      console.warn("Due date error");
    }
  }

  async removeFollowupDate () {
    const referenceobject = this.task;
    try {
      referenceobject["followUp"] = null;
      this.setFollowup[
        (this.getFormsFlowTaskCurrentPage - 1) * this.perPage +
          this.getFormsFlowactiveIndex
      ] = null;
      await this.updateTaskDatedetails(this.task.id, referenceobject);
    } catch {
      console.warn("Follow up date error");
    }
  }

  async fetchTaskData (taskId: string) {
    console.log('fetchTaskData');
    this.task = getTaskFromList(this.tasks, taskId);
    if (this.task) {
      await this.getBPMTaskDetail(taskId);
      await CamundaRest.getVariablesByProcessId(
        this.token,
        this.task.processInstanceId,
        this.bpmApiUrl,
      );
      await this.getTaskFormIODetails(taskId);
      await this.getTaskHistoryDetails(taskId);
      await this.getTaskProcessDiagramDetails(this.task);
    }
  }

  async mounted () {
    this.setFormsFlowTaskCurrentPage(1);
    this.setFormsFlowTaskId("");
    this.setFormsFlowactiveIndex(NaN);
    this.$root.$on("call-fetchData", async (para: any) => {
      this.editAssignee = false;
      this.setFormsFlowTaskId(para.selectedTaskId);
      console.log('fetch task mounted call emit bus-444');
      await this.fetchTaskData(this.getFormsFlowTaskId);
    });

    this.$root.$on("call-fetchPaginatedTaskList", async (para: any) => {
      this.selectedfilterId = para.filterId;
      this.payload = para.requestData;
      await this.fetchPaginatedTaskList(
        para.filterId,
        para.requestData,
        para.firstResult,
        para.maxResults,
      );
    });

    this.$root.$on("call-fetchTaskList", async (para: any) => {
      await this.fetchTaskList(para.filterId, para.requestData);
    });

    this.$root.$on("call-managerScreen", (para: any) => {
      this.maximize = para.maxi;
    });

    this.checkProps();
    this.checkPropsIsPassedAndSetValue();
    authenticateFormio(
      this.formIOResourceId,
      this.formIOReviewerId,
      this.formIOReviewer,
      this.userEmail,
      this.formIOUserRoles,
    );
    await CamundaRest.filterList(this.token, this.bpmApiUrl).then(async (response) => {
      this.filterList = response.data;
      this.selectedfilterId = findFilterKeyOfAllTask(
        this.filterList,
        "name",
        "All tasks",
      );
      await this.fetchTaskList(this.selectedfilterId, this.payload);
      await this.fetchPaginatedTaskList(
        this.selectedfilterId,
        this.payload,
        (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
        this.perPage,
      );
    });

    if (SocketIOService.isConnected()) {
      SocketIOService.disconnect();
    }

    SocketIOService.connect(
      this.webSocketEncryptkey,
      async (refreshedTaskId: any, eventName: any, error: any) => {
        console.log(refreshedTaskId, eventName, error,'+++++++++++++websocket');
        this.refresedTaskFromWebSocket = "";
        this.eventNameWebSocket ="";
        if (eventName === "update"){
          this.refresedTaskFromWebSocket = refreshedTaskId;
        }
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
            },
          );
        }
        if (this.selectedfilterId) {
          await this.fetchPaginatedTaskList(
            this.selectedfilterId,
            this.payload,
            (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
            this.perPage,
          );
          // console.log('fetch task mounted call-111')
          // await this.fetchTaskData(this.getFormsFlowTaskId);
          if (eventName === "create") {
            this.eventNameWebSocket = "create";
            this.$root.$emit("call-pagination");
            await this.fetchTaskList(this.selectedfilterId, this.payload);
          }
        }
        if (
          this.getFormsFlowTaskId &&
          refreshedTaskId === this.getFormsFlowTaskId
        ) {
          // console.log('fetch task mounted call-22')
          // await this.fetchTaskData(this.getFormsFlowTaskId);
          await this.reloadCurrentTask();
        }
        if (
          (this.getFormsFlowTaskId &&
          refreshedTaskId === this.getFormsFlowTaskId) || (this.selectedfilterId)
        ){
          console.log('fetch task mounted call-88888888888888');
          await this.fetchTaskData(this.getFormsFlowTaskId);
        }
      },
    );

    await CamundaRest.getUsersByMemberGroups(
      this.token,
      this.bpmApiUrl,
      reviewerGroup,
    ).then((response) => {
      this.autoUserList = [];
      response.data.forEach((element: any) => {
        this.autoUserList.push({
          code: element.id,
          email: element.email,
          fullName: `${element.firstName} ${element.lastName}`,
          lastNameSearch: `${element.lastName} (${element.id})`,
          label: `${element.lastName} ${element.firstName} `,
        });
      });
    });
    //We used two variables - taskId2 and taskIdValue because the router value from gettaskId is always constant,so after calling the required task details from router to use other tasks in list we need to set taskId2 value as ''
    if (this.taskId2 !== this.taskIdValue) {
      this.taskId2 = this.taskIdValue;
    } else {
      this.taskId2 = "";
    }
  }

  async fetchOptions (search: any) {
    await CamundaRest.getUsersByLastNameGroups(
      this.token,
      this.bpmApiUrl,
      search,
      reviewerGroup,
    ).then((response) => {
      this.autoUserList = [];
      response.data.forEach((element: any) => {
        this.autoUserList.push({
          code: element.id,
          label: `${element.lastName} ${element.firstName} `,
        });
      });
    });
  }

  findTaskIdDetailsFromURLrouter (taskId: string, tasks: any) {
    this.task = getTaskFromList(tasks, taskId);
    this.setFormsFlowTaskId(this.taskIdValue);
    const pos = tasks
      .map(function (e: any) {
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

  async updated () {
    if (this.fulltasks.length && this.taskId2 !== "") {
      console.log('updated');
      this.findTaskIdDetailsFromURLrouter(this.taskId2, this.fulltasks);
      await this.getBPMTaskDetail(this.taskId2);
      await this.getTaskFormIODetails(this.taskId2);
      await this.getTaskHistoryDetails(this.taskId2);
      await this.getTaskProcessDiagramDetails(this.task);
      this.taskId2 = "";
    }
  }

  beforeDestroy () {
    SocketIOService.disconnect();
    this.$root.$off("call-fetchData");
    this.$root.$off("call-fetchPaginatedTaskList");
    this.$root.$off("call-fetchTaskList");
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
