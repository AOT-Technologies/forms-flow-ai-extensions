<template>
  <div
    class="d-flex flex-column"
    ref="taskListContainerRef"
  >
    <template v-if="isUserAllowed">
      <Header
        class="mb-2"
        ref="presetSortFiltersRef"
        v-if="token && bpmApiUrl && maximize && showPresetSortFilters"
        :token="token"
        :bpmApiUrl="bpmApiUrl"
        :filterList="filterList"
        :perPage="perPage"
        :selectedfilterId="selectedfilterId"
        :payload="payload"
        :defaultTaskSortBy="taskSortBy"
        :defaultTaskSortOrder="taskSortOrder"
      />
      <div class="d-flex">
        <div
          class="col-3"
          v-if="maximize"
        >
          <LeftSider
            :taskLoading="taskLoading"
            v-if="token && bpmApiUrl"
            :token="token"
            :formsflowaiApiUrl="formsflowaiApiUrl"
            :formIOApiUrl="formIOApiUrl"
            :bpmApiUrl="bpmApiUrl"
            :tasks="tasks"
            :perPage="perPage"
            :selectedfilterId="selectedfilterId"
            :payload="payload"
            :containerHeight=containerHeight
          />
        </div>
        <div
          class="col-9 task-details-container"
          :class="{ 'col-12 mx-0': !maximize }"
        >
          <div
            v-if="singleTaskLoading"
            class="d-flex justify-content-center align-items-center"
          >
            <div
              class="spinner-grow text-primary"
              role="status"
            >
              <span class="sr-only">Loading...</span>
            </div>
          </div>
          <template v-else-if="getFormsFlowTaskId && task">
            <ExpandContract />
            <div
              class="bg-primary task-title"
              ref="taskTitleRef"
            >
              <h3
                class="m-0"
                data-bs-toggle="tooltip"
                title="Task Name"
              >{{ task.name }}</h3>
            </div>
            <div
              class="d-flex flex-column w-100 px-4 py-2 task-details"
              :style="{
                height: taskScrollableHeight
              }"
            >
              <div class="d-flex mb-1">
                <h4
                  class="mt-2 mb-3"
                  data-bs-toggle="tooltip"
                  title="Process Name"
                >{{ task.taskProcess }}
                </h4>
                <p
                  class="mt-2 mb-3"
                  v-if="task.applicationId"
                  data-bs-toggle="tooltip"
                  title="Application Id"
                > <span class="mx-4">|</span>Application ID <strong>#{{ task.applicationId }}</strong>
                </p>
              </div>
              <div class="d-flex justify-content-between mb-4">
                <section class="task-assignee">
                  <label class="fw-bold">Task assignee</label>
                  <button
                    v-if="task.assignee"
                    class="btn task-icon-btn"
                    @click="toggleassignee"
                    data-bs-toggle="tooltip"
                    title="Click to change assignee"
                  >
                    <i
                      class="fa fa-pencil"
                      :class="{
                      'fa-times-circle-o':editAssignee,
                      'fa-pencil': !editAssignee
                    }"
                    />
                  </button>
                  <div class="d-flex align-items-baseline">
                    <template v-if="task.assignee">
                      <div
                        v-if="editAssignee"
                        class="d-flex w-100 mt-1"
                      >
                        <v-select
                          :label="selectSearchType"
                          :options="reviewerUsersList"
                          :filterable="false"
                          v-model="userSelected"
                          :placeholder="`Search by ${selectSearchType}`"
                          @search="onUserSearch"
                          class="select-assignee"
                        />
                        <div class="dropdown assignee-search-filter mx-2">
                          <button
                            class="btn btn-secondary dropdown-toggle"
                            type="button"
                            data-bs-toggle="dropdown"
                            aria-expanded="false"
                          >
                            <i class="fa fa-filter" />
                          </button>
                          <ul
                            class="dropdown-menu"
                            aria-labelledby="dropdownMenuButton1"
                          >
                            <li
                              v-for="(row, index) in UserSearchListLabel"
                              :key="row.id"
                              @click="setSelectedUserSearchBy(row.searchType, index)"
                              class="dropdown-item"
                              :class="{'active': index === activeUserSearchindex}"
                            >
                              {{ row.label }}
                            </li>
                          </ul>
                        </div>
                        <button
                          class="btn task-icon-btn btn-light"
                          @click="onSetassignee"
                          data-bs-toggle="tooltip"
                          title="Set assignee"
                        >
                          <i class="fa fa-check-circle-o fa-lg" />
                        </button>
                      </div>
                      <template v-else>
                        <div class="assignee-name">{{ task.assignee }}</div>
                        <button
                          class="btn task-icon-btn"
                          @click="onUnClaim"
                          data-bs-toggle="tooltip"
                          title="Reset assignee"
                        >
                          <i class="fa fa-times-circle-o" />
                        </button>
                      </template>
                    </template>
                    <button
                      v-else
                      class="btn btn-light"
                      @click="onClaim"
                      data-bs-toggle="tooltip"
                      title="Claim task"
                    >
                      <i class="fa fa-plus" />
                      <span class="mx-1">Claim</span>
                    </button>
                  </div>
                </section>
                <section
                  class="task-groups mx-4"
                  data-bs-toggle="tooltip"
                  title="Click to modify groups"
                >
                  <label class="fw-bold">Groups</label>
                  <button
                    v-if="groupListNames && groupListNames.length"
                    class="btn task-icon-btn"
                    data-bs-toggle="modal"
                    data-bs-target="#groupsModal"
                  >
                    <i class="fa fa-pencil" />
                  </button>
                  <div class="d-flex align-items-baseline group-name">
                    <template v-if="groupListNames && groupListNames.length">
                      {{ String(groupListNames) }}
                    </template>
                    <button
                      v-else
                      class="btn btn-light"
                      data-bs-toggle="modal"
                      data-bs-target="#groupsModal"
                    >
                      <i class="fa fa-plus" />
                      <span class="mx-1">Add Groups</span>
                    </button>
                  </div>
                  <div
                    class="modal fade task-groups-modal"
                    id="groupsModal"
                    tabindex="-1"
                    aria-labelledby="groupsModal"
                    aria-hidden="true"
                  >
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <h5 class="modal-title">MANAGE GROUPS</h5>
                          <button
                            type="button"
                            class="btn-close mx-2"
                            data-bs-dismiss="modal"
                            aria-label="Close"
                          ></button>
                        </div>
                        <div class="modal-body px-4 pb-5">
                          <i class="fa fa-exclamation-circle"></i>
                          You can add a group by typing a group ID into the input
                          field and afterwards clicking the button with the plus sign.
                          <div class="d-flex my-3">
                            <input
                              class="form-control group-name-input"
                              type="text"
                              v-model="setGroup"
                              @keyup.enter="addGroup"
                              placeholder="Group ID"
                            />
                            <button
                              class="btn btn-primary add-group-btn"
                              @click="addGroup"
                              :disabled="!setGroup"
                            >
                              <i class="fa fa-plus" />
                              <span class="mx-1">Add group</span>
                            </button>
                          </div>
                          <div
                            v-if="groupList.length"
                            class="d-flex flex-wrap"
                          >
                            <div
                              class="d-flex align-items-baseline added-group-chip"
                              v-for="g in groupList"
                              :key="g.groupId"
                              @click="deleteGroup(g.groupId)"
                              data-bs-toggle="tooltip"
                              title="Click to remove this group"
                            >
                              <div class="mx-1">{{ g.groupId }}</div>
                              <i class="fa fa-times-circle-o" />
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </section>
                <section></section>
              </div>
              <div class="d-flex justify-content-between mb-2">
                <section class="task-date-picker">
                  <label class="fw-bold mb-1">Follow up</label>
                  <div
                    class="d-flex align-items-baseline"
                    v-if="task.followUp"
                  >
                    <p
                      data-bs-toggle="tooltip"
                      :title="getExactDate(task.followUp)"
                    >{{ timedifference(task.followUp) }}</p>
                    <button
                      class="btn task-icon-btn"
                      @click="removeFollowupDate"
                      data-bs-toggle="tooltip"
                      title="Click to remove FollowUp Date"
                    >
                      <i class="fa fa-times-circle-o" />
                    </button>
                  </div>
                  <v-date-picker
                    v-else
                    v-model="task.followUp"
                    :popover="{ visibility: 'click' }"
                  >
                    <template v-slot="{ inputValue, inputEvents }">
                      <div class="input-group">
                        <input
                          class="form-control"
                          :value="inputValue"
                          v-on="inputEvents"
                          @input="updateFollowUpDate"
                          placeholder="dd/mm/yyyy"
                        />
                        <i class="fa fa-calendar-alt"></i>
                      </div>
                    </template>
                  </v-date-picker>
                </section>
                <section class="task-date-picker">
                  <label class="fw-bold mb-1">Due date</label>
                  <div
                    class="d-flex align-items-baseline"
                    v-if="task.due"
                  >
                    <p
                      data-bs-toggle="tooltip"
                      :title="getExactDate(task.due)"
                    >{{ timedifference(task.due) }}</p>
                    <button
                      class="btn task-icon-btn"
                      @click="removeDueDate"
                      data-bs-toggle="tooltip"
                      title="Click to remove Due date"
                    >
                      <i class="fa fa-times-circle-o" />
                    </button>
                  </div>
                  <v-date-picker
                    v-else
                    v-model="task.due"
                    :popover="{ visibility: 'click' }"
                  >
                    <template v-slot="{ inputValue, inputEvents }">
                      <div class="input-group">
                        <input
                          class="form-control"
                          :value="inputValue"
                          v-on="inputEvents"
                          @input="updateDueDate"
                          placeholder="dd/mm/yyyy"
                        />
                        <i class="fa fa-calendar-alt"></i>
                      </div>
                    </template>
                  </v-date-picker>
                </section>
                <section>
                  <label class="fw-bold mb-1">Created</label>
                  <p
                    data-bs-toggle="tooltip"
                    :title="getExactDate(task.created)"
                  >{{ timedifference(task.created) }}</p>
                </section>
                <section></section>
              </div>
              <ul
                class="nav nav-tabs mt-3 task-tabs"
                role="tablist"
              >
                <li
                  class="nav-item"
                  role="presentation"
                >
                  <button
                    class="nav-link active"
                    id="task-form-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#taskForm"
                    type="button"
                    role="tab"
                    aria-controls="form"
                    aria-selected="true"
                  >Form</button>
                </li>
                <li
                  class="nav-item"
                  role="presentation"
                >
                  <button
                    class="nav-link"
                    id="task-history-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#taskHistory"
                    type="button"
                    role="tab"
                    aria-controls="history"
                    aria-selected="false"
                  >History</button>
                </li>
                <li
                  class="nav-item"
                  role="presentation"
                >
                  <button
                    class="nav-link"
                    id="task-diagram-tab"
                    data-bs-toggle="tab"
                    data-bs-target="#diagramContainer"
                    type="button"
                    role="tab"
                    aria-controls="diagram"
                    aria-selected="false"
                    @click="getDiagramDetails"
                  >Diagram</button>
                </li>
              </ul>
              <div class="tab-content py-3 task-tab-content">
                <!-- Form tab content -->
                <div
                  class="tab-pane fade show active form-tab-content"
                  id="taskForm"
                  role="tabpanel"
                  aria-labelledby="form-tab"
                  :class="{'disabled': task.assignee !== userName}"
                >
                  <div v-if="task.assignee === userName">
                    <transition name="fade">
                      <FormEdit
                        :formioUrl="formioUrl"
                        @onformsubmit="onFormSubmitCallback"
                        @oncustomevent="oncustomEventCallback"
                      />
                    </transition>
                  </div>
                  <div v-else>
                    <FormView :formioUrl="formioUrl" />
                  </div>
                </div>
                <!-- Form tab content end -->
                <div
                  class="tab-pane fade"
                  id="taskHistory"
                  role="tabpanel"
                  aria-labelledby="history-tab"
                >
                  <TaskHistory
                    :taskHistoryList="taskHistoryList"
                    :applicationId="task.applicationId"
                  />
                </div>
                <div
                  class="tab-pane fade"
                  id="diagramContainer"
                  role="tabpanel"
                  aria-labelledby="diagram-tab"
                >
                  <div
                    v-if="diagramLoading"
                    class="d-flex justify-content-center"
                  >
                    <div
                      class="spinner-border"
                      role="status"
                    >
                      <span class="sr-only">Loading...</span>
                    </div>
                  </div>
                  <div
                    class="diagram-full-screen"
                    id="canvas"
                  ></div>
                </div>
              </div>
            </div>
          </template>
          <div
            v-else
            class="d-flex align-items-center justify-content-center task-details-empty"
            :style="{
              height: taskScrollableHeight
            }"
          >
            <i class="far fa-exclamation-circle"></i>
            <h4 class="mt-0 mx-2">Please select a task from the list</h4>
          </div>
        </div>
      </div>
    </template>
    <div v-else>
      <div
        class="alert alert-danger mt-4"
        role="alert"
      >
        You don't have access. Contact your administrator.
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import "font-awesome/scss/font-awesome.scss";
import "../styles/user-styles.css";
import "../styles/camundaFormIOTasklist.scss";
import {
  ALL_FILTER,
  CamundaRest,
  SEARCH_USERS_BY,
  SocketIOService,
  authenticateFormio,
  findFilterKeyOfAllTask,
  getFormDetails,
  getFormattedDateAndTime,
  getISODateTime,
  getTaskFromList,
  getUserName,
  getformHistoryApi,
  isAllowedUser,
  reviewerGroup,
  sortByPriorityList,
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
  SEARCH_OPTION_TYPE,
  TaskHistoryListPayload,
  TaskListSortType,
  TaskPayload,
  UserListObject,
  UserListPayload,
  UserPayload,
  UserSearchListLabelPayload,
} from "../models";
import BpmnViewer from "bpmn-js";
import DatePicker from "v-calendar/lib/components/date-picker.umd";
import ExpandContract from "./addons/ExpandContract.vue";
import FormEdit from "./form/Edit.vue";
import FormView from "./form/View.vue";
import {
  Formio
} from 'vue-formio';
import Header from "./layout/Header.vue";
import LeftSider from "./layout/LeftSider.vue";
import TaskHistory from "../components/addons/TaskHistory.vue";
import TaskListMixin from "../mixins/TaskListMixin.vue";
import {
  Tooltip
} from 'bootstrap';
import moment from "moment";
import {
  namespace
} from "vuex-class";
import serviceFlowModule from "../store/modules/serviceFlow-module";
import vSelect from "vue-select";

const StoreServiceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    TaskHistory,
    Header,
    LeftSider,
    vSelect,
    ExpandContract,
    BpmnViewer,
    FormEdit,
    FormView,
    VDatePicker: DatePicker,
  },
})
export default class Tasklist extends Mixins(TaskListMixin) {
  @Prop() private getTaskId!: string;
  @Prop({
    default: "created",
  })
  public taskSortBy!: string;
  @Prop({
    default: "desc",
  })
  public taskSortOrder!: string;
  @Prop({
    default: true
  })
  private showPresetSortFilters!: boolean;

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
  @StoreServiceFlowModule.Mutation("setFormsFlowTaskLength")
  public setFormsFlowTaskLength: any;

  private tasks: TaskPayload[] = [];
  private fulltasks: TaskPayload[] = [];
  private formId: string = "";
  private taskLoading!: boolean;
  private singleTaskLoading: boolean = false;
  private diagramLoading: boolean = false;
  private submissionId: string = "";
  private formioUrl: string = "";
  private task: TaskPayload = {
  };
  private setGroup = null;
  private userSelected: UserListPayload = {
  };
  public perPage: number = 10;
  private filterList: FilterPayload[] = [];
  private editAssignee: boolean = false;
  private groupList: GroupListPayload[] = [];
  private groupListNames?: string[] = [];
  private groupListItems: string[] = [];
  private userEmail: string = "external";
  private selectedfilterId: string = "";
  private xmlData!: string;
  private payload: Payload = {
    sorting: [] as TaskListSortType[],
    firstResult: 0,
    maxResults: this.perPage,
  };
  private taskHistoryList: TaskHistoryListPayload[] = [];
  private reviewerUsersList: UserListPayload[] = [];
  private selectSearchType: string = "lastName";
  private taskIdValue: string = "";
  private taskId2: string = "";
  // private taskIdWebsocket: string = "";
  private activeUserSearchindex = 1;
  private UserSearchListLabel: UserSearchListLabelPayload[] = SEARCH_USERS_BY;
  private isUserAllowed: boolean = false
  private containerHeight: number = 0;
  private taskScrollableHeight: string = '100px';
  private isHeightViewUpdated: boolean = false;

  created() {
    Array.from(document.querySelectorAll('button[data-bs-toggle="tooltip"]'))
      .forEach(tooltipNode => new Tooltip(tooltipNode));

  }

  checkforTaskID() {
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

  timedifference(date: Date) {
    return moment(date).fromNow();
  }

  async toggleassignee() {
    this.editAssignee = !this.editAssignee;
    const reviewerList = await CamundaRest.getUsersByMemberGroups(
      this.token,
      this.bpmApiUrl,
      reviewerGroup
    );
    if (reviewerList) {
      this.reviewerUsersList = [];
      reviewerList.data.forEach((user: UserPayload) => {
        this.reviewerUsersList.push(UserListObject(user));
      });
      const userList = JSON.parse(JSON.stringify(this.reviewerUsersList));
      this.userSelected = userList.find(((user: any) => user.code?.includes(this.task.assignee)));
    }
  }

  setSelectedUserSearchBy(searchby: string, index: number) {
    this.selectSearchType = searchby;
    this.activeUserSearchindex = index;
  }

  async onFormSubmitCallback(actionType = "") {
    if (this.task.id !== null) {
      await this.onBPMTaskFormSubmit(this.task.id!, actionType);
      await this.reloadTasks();
    }
  }

  addGroup() {
    CamundaRest.createTaskGroupByID(
      this.token,
      this.task.id!,
      this.bpmApiUrl,
      {
        userId: null,
        groupId: this.setGroup,
        type: "candidate",
      }
    ).then(() => {
      this.getGroupDetails();
      this.reloadCurrentTask();
    });
    this.setGroup = null;
  }

  async getGroupDetails() {
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

  async deleteGroup(groupid: string) {
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

  async onBPMTaskFormSubmit(taskId: string, actionType: string) {
    let formRequestFormat: FormRequestPayload = {
      variables: {
        formUrl: {
          value: this.formioUrl,
        },
        applicationId: {
          value: this.task.applicationId!,
        },
      },
    };
    if (actionType !== "") {
      const newformRequestFormat: FormRequestActionPayload = Object.assign(
        formRequestFormat.variables,
        {
          action: {
            value: actionType,
          },
        }
      );
      formRequestFormat = {
        variables: newformRequestFormat,
      };
    }

    await CamundaRest.formTaskSubmit(
      this.token,
      taskId,
      formRequestFormat,
      this.bpmApiUrl
    );
  }

  async getBPMTaskDetail(taskId: string) {

    const [taskResult, applicationIdResult] = await Promise.all([
      CamundaRest.getTaskById(this.token, taskId, this.bpmApiUrl),
      CamundaRest.getVariablesByTaskId(this.token, taskId, this.bpmApiUrl),
    ]);

    const processResult = await CamundaRest.getProcessDefinitionById(
      this.token,
      taskResult.data.processDefinitionId,
      this.bpmApiUrl
    );
    this.task = taskResult.data;
    this.task.taskProcess = processResult.data.name;
    this.task.applicationId = applicationIdResult.data.applicationId.value;
    await this.getGroupDetails();
  }

  async getTaskFormIODetails(taskId: string) {
    const formResult = await CamundaRest.getVariablesByTaskId(
      this.token,
      taskId,
      this.bpmApiUrl
    );


    if (formResult.data.formUrl?.value) {
      const formUrlPattern = formResult.data.formUrl?.value;
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
  }

  async getTaskHistoryDetails() {
    this.taskHistoryList = [];

    if (this.task?.applicationId) {
      const applicationHistoryList = await getformHistoryApi(
        this.formsflowaiApiUrl,
        this.task.applicationId,
        this.token
      );
      this.taskHistoryList = applicationHistoryList.applications;
    }
  }

  async getTaskProcessDiagramDetails(processDefinitionId: string) {
    this.diagramLoading = true;
    const getProcessResult = await CamundaRest.getProcessDiagramXML(
      this.token,
      processDefinitionId,
      this.bpmApiUrl
    );
    const processInstanceId = this.task.processInstanceId || '';
    const getActivity = await CamundaRest.getProcessActivity(
      this.token,
      processInstanceId,
      this.bpmApiUrl
    );
    this.xmlData = getProcessResult.data.bpmn20Xml;
    const activityList = getActivity.data.childActivityInstances;
    const div = document.getElementById("canvas");
    if (div) {
      div.innerHTML = "";
    }
    const viewer = new BpmnViewer({
      container: "#canvas",
    });

    this.diagramLoading = false;
    await viewer.importXML(this.xmlData);
    viewer.get("canvas").zoom("fit-viewport");
    for (let i: number = 0; i < activityList.length; i++) {
      viewer.get("canvas").addMarker({
        'id': activityList[i].activityId
      }, 'highlight');
    }


  }

  async getDiagramDetails() {

    await this.getTaskProcessDiagramDetails(this.task.processDefinitionId!);

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
      default:
        // this call is for formio
        this.$root.$emit(customEvent.type, {
          customEvent
        });
    }
  };

  async reloadTasks() {
    this.setFormsFlowTaskId("");
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage
    );
  }

  async reloadCurrentTask() {
    await this.getBPMTaskDetail(this.task.id!);
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage
    );
  }

  async reloadLHSTaskList() {
    await this.fetchPaginatedTaskList(
      this.selectedfilterId,
      this.payload,
      (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
      this.perPage,
    );
  }

  async onClaim() {
    await CamundaRest.claim(
      this.token,
      this.task.id!,
      {
        userId: this.userName,
      },
      this.bpmApiUrl
    );
    if (!SocketIOService.isConnected()) {
      await this.getBPMTaskDetail(this.getFormsFlowTaskId);
      await this.reloadLHSTaskList();
    }
  }

  async onUnClaim() {
    await CamundaRest.unclaim(this.token, this.task.id!, this.bpmApiUrl);

    if (!SocketIOService.isConnected()) {
      await this.getBPMTaskDetail(this.getFormsFlowTaskId);
      await this.reloadLHSTaskList();
    }
  }

  async onSetassignee() {
    await CamundaRest.setassignee(
      this.token,
      this.task.id!,
      {
        userId: this.userSelected?.code,
      },
      this.bpmApiUrl
    );
    await this.toggleassignee();
    if (!SocketIOService.isConnected()) {
      await this.getBPMTaskDetail(this.getFormsFlowTaskId);
      await this.reloadLHSTaskList();
    }
  }

  async fetchFullTaskList(filterId: string, requestData: Payload) {
    const taskList = await CamundaRest.filterTaskList(
      this.token,
      filterId,
      requestData,
      this.bpmApiUrl
    );
    this.fulltasks = taskList.data;
  }

  async fetchPaginatedTaskList(
    filterId: string,
    requestData: any,
    first: number,
    max: number,
    taskIdToRemove?: string
  ) {
    this.selectedfilterId = filterId;
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
    this.tasks = _embedded.task;
    this.taskLoading = false;
    this.setFormsFlowTaskLength(responseData.count);

    if (taskIdToRemove) {
      //if the list has the task with taskIdToRemove remove that task and decrement
      if (this.tasks.find((task: TaskPayload) => task.id === taskIdToRemove)) {
        this.tasks = _embedded.task.filter((task: TaskPayload) => task.id !== taskIdToRemove);
        this.setFormsFlowTaskLength(responseData.count--); // Count has to be decreased since one task id is removed.
      }
    }
  }

  async onUserSearch(search: string, loading: any) {
    if (search.length) {
      loading(true);
      this.reviewerUsersList = [];
    }

    if (this.selectSearchType === SEARCH_OPTION_TYPE.FIRST_NAME) {
      const firstNameUserList = await CamundaRest.getUsersByFirstNameGroups(
        this.token,
        this.bpmApiUrl,
        search,
        reviewerGroup
      );
      this.reviewerUsersList = [];
      firstNameUserList.data.map((user: UserPayload) => {
        this.reviewerUsersList.push(UserListObject(user));
      });
      loading(false);
    }

    if (this.selectSearchType === SEARCH_OPTION_TYPE.LAST_NAME) {
      const lastNameUserList = await CamundaRest.getUsersByLastNameGroups(
        this.token,
        this.bpmApiUrl,
        search,
        reviewerGroup
      );
      this.reviewerUsersList = [];
      lastNameUserList.data.map((user: UserPayload) => {
        this.reviewerUsersList.push(UserListObject(user));
      });
      loading(false);
    }

    if (this.selectSearchType === SEARCH_OPTION_TYPE.EMAIL) {
      const emailUserList = await CamundaRest.getUsersByEmailGroups(
        this.token,
        this.bpmApiUrl,
        search,
        reviewerGroup
      );
      this.reviewerUsersList = [];
      emailUserList.data.map((user: UserPayload) => {
        this.reviewerUsersList.push(UserListObject(user));
      });
      loading(false);
    }
    loading(false);
  }

  async updateTaskDatedetails(taskId: string, task: TaskPayload) {
    await CamundaRest.updateTasksByID(this.token, taskId, this.bpmApiUrl, task);
    if (!SocketIOService.isConnected()) {
      await this.reloadCurrentTask();
    }
  }

  async updateFollowUpDate() {
    const referenceobject = this.task;
    try {
      if (this.task?.followUp !== null) {
        referenceobject.followUp = getISODateTime(this.task?.followUp);
        await this.updateTaskDatedetails(this.task.id!, referenceobject);
      }
    } catch {
      console.warn("Follow date error"); // eslint-disable-line no-console
    }
  }

  async updateDueDate() {
    const referenceobject = this.task;
    try {
      if (this.task?.due !== null) {
        referenceobject["due"] = getISODateTime(this.task?.due);
        await this.updateTaskDatedetails(this.task.id!, referenceobject);
      }
    } catch {
      console.warn("Due date error"); // eslint-disable-line no-console
    }
  }

  async removeDueDate() {
    const referenceobject = this.task;
    try {
      referenceobject["due"] = null;
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Due date error"); // eslint-disable-line no-console
    }
  }

  async removeFollowupDate() {
    const referenceobject = this.task;
    try {
      referenceobject["followUp"] = null;
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    } catch {
      console.warn("Follow up date error"); // eslint-disable-line no-console
    }
  }

  async fetchTaskDetails(taskId: string) {
    await Promise.all([
      this.getBPMTaskDetail(taskId),
      this.getTaskFormIODetails(taskId),
    ]);
    /*await is not used for this promise, as if a user doesn't want to wait for
     the history and process diagram to load */
    Promise.all([
      this.getTaskHistoryDetails(),
      this.getTaskProcessDiagramDetails(this.task.processDefinitionId!),
    ]);
  }

  async findTaskIdDetailsFromURLrouter(
    taskId: string,
    fulltasks: TaskPayload[]
  ) {
    this.task = getTaskFromList(fulltasks, taskId)!;
    this.setFormsFlowTaskId(this.taskIdValue);
    const pos = fulltasks
      .map(function (e: TaskPayload) {
        return e.id;
      })
      .indexOf(this.taskIdValue);
    this.setFormsFlowactiveIndex(pos % this.perPage);
    this.setFormsFlowTaskCurrentPage(Math.floor(pos / this.perPage) + 1);
    this.$root.$emit("update-pagination-currentpage", {
      page: this.getFormsFlowTaskCurrentPage,
    });
  }

  updated() {
    this.calculateViewHeights();
  }

  /*** to calculate the height and handling scroll views accordingly */
  calculateViewHeights() {
    // add 8px to the headerHeight since the margin mb-2 applied to it
    const headerHeight = ((this.$refs.presetSortFiltersRef as any)?.$el?.offsetHeight || 0) + 8;
    const titleHeight = (this.$refs.taskTitleRef as any)?.offsetHeight || 0;
    if (!this.isHeightViewUpdated && headerHeight) {
      this.containerHeight = this.containerHeight - headerHeight;
      this.isHeightViewUpdated = true;
    }
    this.taskScrollableHeight = `${this.containerHeight - (titleHeight + 1)}px`;
  }

  async mounted() {
    this.containerHeight = (this.$refs.taskListContainerRef as any).clientHeight;
    Formio.setBaseUrl(this.formIOApiUrl);
    Formio.setProjectUrl(this.formIOApiUrl);
    this.isUserAllowed = isAllowedUser(this.formIOReviewer, this.formIOUserRoles);
    this.setFormsFlowTaskCurrentPage(1);
    this.setFormsFlowTaskId("");
    this.setFormsFlowactiveIndex(NaN);
    this.taskLoading = true;
    this.$root.$on("call-fetchTaskDetails", async (para: any) => {
      this.editAssignee = false;
      this.singleTaskLoading = true;
      this.setFormsFlowTaskId(para.selectedTaskId);
      await this.fetchTaskDetails(this.getFormsFlowTaskId);
      this.singleTaskLoading = false;
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
      this.formIOUserRoles,
      this.formIOJwtSecret
    );
    const filterListResult = await CamundaRest.filterList(
      this.token,
      this.bpmApiUrl
    );
    console.log(filterListResult.data);
    this.filterList = sortByPriorityList(filterListResult.data);
    this.selectedfilterId = findFilterKeyOfAllTask(this.filterList, ALL_FILTER);
    await this.reloadLHSTaskList();
    if (SocketIOService.isConnected()) {
      SocketIOService.disconnect();
    }

    SocketIOService.connect(
      this.webSocketEncryptkey,
      (refreshedTaskId: string, eventName: string, error: string) => {
        // this.taskIdWebsocket = refreshedTaskId;
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

        if (eventName === "complete") {
          this.fetchPaginatedTaskList(
            this.selectedfilterId,
            this.payload,
            (this.getFormsFlowTaskCurrentPage - 1) * this.perPage,
            this.perPage,
            refreshedTaskId
          );

          if (this.getFormsFlowTaskId && refreshedTaskId === this.getFormsFlowTaskId) {
            this.setFormsFlowTaskId("");
          }
        }
        else {
          if (this.selectedfilterId) {
            /* in case of update event update TaskList if the updated taskId is in
            the current paginated taskList for the user only refresh */
            if (eventName === "update") {
              if (getTaskFromList(this.tasks, refreshedTaskId)) {
                this.reloadLHSTaskList();
              }
            }

            // In case of a new task is being created
            else if (eventName === "create") {
              this.reloadLHSTaskList();
              this.$root.$emit("call-pagination");
            }
            else {
              this.reloadLHSTaskList();
            }
          }

          if (this.getFormsFlowTaskId && refreshedTaskId === this.getFormsFlowTaskId) {
            //condition to remove the form input when a user is typing in the form by task refresh
            if (this.task.assignee === this.userName) {
              this.getBPMTaskDetail(this.task.id!);
            }
            // If task is not being claimed, then reload the full task details
            else {
              this.fetchTaskDetails(this.getFormsFlowTaskId);
            }
          }
        }
      }
    );

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
      await this.fetchTaskDetails(this.taskId2);
      this.taskId2 = "";
    }

  }

  getExactDate(date: Date) {
    return getFormattedDateAndTime(date);
  }

  beforeDestroy() {
    SocketIOService.disconnect();
    this.$root.$off("call-fetchTaskDetails");
    this.$root.$off("call-fetchPaginatedTaskList");
    this.$root.$off("call-managerScreen");
    if (this.$store.hasModule("serviceFlowModule")) {
      this.$store.unregisterModule("serviceFlowModule");
    }
    localStorage.removeItem("authToken");
    localStorage.removeItem("UserDetails");
  }
  beforeCreate() {
    localStorage.removeItem("authToken");
    localStorage.removeItem("UserDetails");
    if (!this.$store?.hasModule("serviceFlowModule")) {
      this.$store.registerModule("serviceFlowModule", serviceFlowModule);
    }
  }
}
</script>

<style lang="scss" scoped>
.task-details-empty {
  background: #fff;
  margin-left: 4px;
}
.task-details-container {
  background: #fff;
  margin-left: 4px;
  border-radius: 0.5rem;
  .task-title {
    padding: 1rem 1.5rem;
    border-top-left-radius: 0.5rem;
    border-top-right-radius: 0.5rem;
    h3 {
      color: #fff;
    }
  }
  .task-details {
    height: 100px;
    overflow-y: auto;
  }
  .task-date-picker {
    .input-group {
      width: calc(100% - 24px);
      .form-control {
        border-radius: 0.5rem;
      }
      i {
        margin-left: -24px !important;
        margin-top: 12px;
        z-index: 3;
      }
    }
  }
  .task-icon-btn {
    min-height: unset !important;
    padding-top: 0;
    padding-bottom: 0px;
  }
  .task-assignee {
    flex: 0.5;
    .select-assignee {
      width: 100%;
      min-width: 180px;
      height: 40px;
    }
    .assignee-name {
      white-space: pre;
    }
    .btn {
      min-height: 40px;
    }
  }
  .task-groups {
    .group-name {
      white-space: normal;
    }
  }
  .task-groups-modal {
    .group-name-input {
      margin-right: 1rem;
    }
    .add-group-btn {
      min-width: 120px;
    }
    .added-group-chip {
      background: #e7e7e7;
      border-radius: 50rem;
      padding: 0.25rem 0.75rem;
      margin: 0.5rem 0.25rem;
      cursor: pointer;
      &:hover {
        background: #ceeaf1;
      }
    }
  }
  .task-tabs {
    .nav-link {
      height: 40px;
    }
    .active {
      background: var(--bs-primary);
      color: #fff;
      font-weight: 600;
    }
  }
  .task-tab-content {
    background: #fafafa;
    padding: 1rem;
    .form-tab-content {
      &.disabled {
        pointer-events: none;
        opacity: 0.33;
      }
    }
  }
}
</style>
