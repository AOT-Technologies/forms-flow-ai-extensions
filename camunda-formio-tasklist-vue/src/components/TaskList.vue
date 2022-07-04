<template>
  <div
    class="d-flex flex-column"
    ref="taskListContainerRef"
  >
    <template v-if="isUserAllowed">
      <Header
        class="mb-2"
        ref="presetSortFiltersRef"
        v-if="token && bpmApiUrl && maximize&&(!disableComponents.sort||!disableComponents.form||!disableComponents.filterList)"
        :token="token"
        :bpmApiUrl="bpmApiUrl"
        :filterList="filterList"
        :perPage="perPage"
        :selectedfilterId="selectedfilterId"
        :payload="payload"
        :defaultTaskSortBy="taskSortBy"
        :defaultTaskSortOrder="taskSortOrder"
        :disableOption="disableComponents"
      />
      <div class="d-flex flex-md-row flex-column">
        <div
          class="col-md-3 col-12"
          v-if="maximize"
        >
          <LeftSider
            :taskLoading="taskLoading"
            v-if="token && bpmApiUrl"
            :token="token"
            :formsflowaiApiUrl="formsflowaiApiUrl"
            :bpmApiUrl="bpmApiUrl"
            :tasks="tasks"
            :perPage="perPage"
            :selectedfilterId="selectedfilterId"
            :payload="payload"
            :containerHeight="containerHeight"
            :disableOption="disableComponents"
            :listItemCardStyle="listItemCardStyle"
            :filterList="filterList"
          />
        </div>

       <!-- need to bring here right side -->
        <div
          class="ctf-task-details-container ms-md-2 rounded"
          :class="{ 'col-12 mx-0': !maximize ,'col-md-9 col-12':maximize}"
        >

       <!-- single taks loading -->

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
          </template>
          <div
            v-else
            class="d-flex align-items-center justify-content-center task-details-empty"
            :style="{
              height: taskScrollableHeight
            }"
          >
            <i class="fa fa-exclamation-circle"></i>
            <h4 class="mt-0 mx-2">Please select a task from the list</h4>
          </div>
       <!-- single task loading end -->

      <!-- right side if not loading  -->
    <RightSider
  v-else-if="getFormsFlowTaskId && task"
  :task="task"
  :bpmApiUrl="bpmApiUrl"
  :token="token"
  :getBPMTaskandReload="getBPMTaskandReload"
  :taskScrollableHeight="taskScrollableHeight"
  :reloadCurrentTask="reloadCurrentTask"
  :getDiagramDetails="getDiagramDetails"
  :formioUrl="formioUrl"
  :onFormSubmitCallback="onFormSubmitCallback"
  :oncustomEventCallback="oncustomEventCallback"
  :taskHistoryList="taskHistoryList"
  :diagramLoading="diagramLoading"
  :userName="userName"
  :hideTaskDetails="hideTaskDetails"
/>

      <!-- right side end  -->

      <!-- task not selected start -->
          <div
            v-else
            class="d-flex align-items-center justify-content-center task-details-empty"
            :style="{
              height: taskScrollableHeight
            }"
          >
            <i class="fa fa-exclamation-circle"></i>
            <h4 class="mt-0 mx-2">Please select a task from the list</h4>
          </div>
      <!-- task not selected end -->


        </div>
       <!-- end  right side -->

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
    <div class="toast-container position-absolute bottom-0 end-0 py-5 px-4">
      <div
        class="toast align-items-center text-white bg-dark border-0"
        role="alert"
        ref="toastMsgRef"
        aria-live="assertive"
        aria-atomic="true"
      >
        <div class="d-flex">
          <div class="toast-body">
            {{ toastMsgTxt }}
          </div>
          <button
            type="button"
            class="btn-close btn-close-white me-2 m-auto"
            data-bs-dismiss="toast"
            aria-label="Close"
          ></button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import "font-awesome/scss/font-awesome.scss";
import "../styles/user-styles.css";
import "../styles/camundaFormIOTasklist.scss";
import * as bootstrap from 'bootstrap';
import {
  ALL_FILTER,
  CamundaRest,
  SocketIOService,
  authenticateFormio, 
  findFilterIdForDefaultFilterName,
  getFormDetails,
  getTaskFromList,
  getUserName,
  getformHistoryApi,
  isAllowedUser,
  sortByPriorityList,
} from "../services";
import {
  Component, Mixins, Prop,
} from "vue-property-decorator";
import {
  CustomEventPayload,
  FilterPayload,
  FormRequestActionPayload,
  FormRequestPayload,
  Payload,
  TaskHistoryListPayload,
  TaskListSortType,
  TaskPayload,
} from "../models";
import  {
  Toast,
  Tooltip
} from 'bootstrap';
import BpmnViewer from "bpmn-js/dist/bpmn-navigated-viewer.production.min.js";
import ExpandContract from "./addons/ExpandContract.vue";

import {
  Formio
} from 'vue-formio';
import Header from "./layout/Header.vue";
import LeftSider from "./layout/LeftSider.vue";
import RightSider from "../components/layout/RightSider.vue";
import TaskListMixin from "../mixins/TaskListMixin.vue";

import {
  namespace
} from "vuex-class";
import serviceFlowModule from "../store/modules/serviceFlow-module";

const StoreServiceFlowModule = namespace("serviceFlowModule");

@Component({
  components: {
    Header,
    LeftSider,
    ExpandContract,
    BpmnViewer,
    RightSider,
 

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
    default: () => [],
  }) 
  protected taskDefaultFilterListNames !: string[];
 

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

  public perPage: number = 10;
  private filterList: FilterPayload[] = [];

  private userEmail: string = "external";
  private selectedfilterId: string = "";
  private xmlData!: string;
  private payload: Payload = {
    sorting: [] as TaskListSortType[],
    firstResult: 0,
    maxResults: this.perPage,
  };
  private taskHistoryList: TaskHistoryListPayload[] = [];

  private taskIdValue: string = "";
  private taskId2: string = "";
  private activeUserSearchindex = 1;

  private isUserAllowed: boolean = false
  private containerHeight: number = 0;
  private taskScrollableHeight: string = '100px';
  private isHeightViewUpdated: boolean = false;
  private toastMsg;
  private toastMsgTxt: string = '';

  created() {
    Array.from(document.querySelectorAll('button[data-bs-toggle="tooltip"]'))
      .forEach(tooltipNode => new Tooltip(tooltipNode));
    const toastElList = [].slice.call(document.querySelectorAll('.toast'));
    toastElList.map(function (toastEl) {
      return new bootstrap.Toast(toastEl);
    });
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



  async onFormSubmitCallback(actionType = "") {
    if (this.task.id !== null) {
      await this.onBPMTaskFormSubmit(this.task.id!, actionType);
      await this.reloadTasks();
    }
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
      }
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
    // await this.getGroupDetails();
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
        this.formIO.apiUrl
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
    const div = document.getElementById("process-diagram-container");
    if (div) {
      div.innerHTML = "";
    }
    this.diagramLoading = false;

    const viewer = new BpmnViewer({
      container: "#process-diagram-container",
    });
    await viewer.importXML(this.xmlData);
    viewer.get("canvas").addMarker(
      {
        id: activityList[0].activityId,
      },
      "highlight"
    );
  
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
      (this.getFormsFlowTaskCurrentPage - 1||0) * this.perPage,
      this.perPage,
    );
  }




  async getBPMTaskandReload(){
    await this.getBPMTaskDetail(this.getFormsFlowTaskId);
    await this.reloadLHSTaskList();

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
    this.taskLoading=true;
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
    this.taskLoading=false;

    if (taskIdToRemove) {
      //if the list has the task with taskIdToRemove remove that task and decrement
      if (this.tasks.find((task: TaskPayload) => task.id === taskIdToRemove)) {
        this.tasks = _embedded.task.filter((task: TaskPayload) => task.id !== taskIdToRemove);
        this.setFormsFlowTaskLength(responseData.count--); // Count has to be decreased since one task id is removed.
      }
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
    this.toastMsg = new Toast(this.$refs?.toastMsgRef as any);
    Formio.setBaseUrl(this.formIO.apiUrl);
    Formio.setProjectUrl(this.formIO.apiUrl);
    this.isUserAllowed = isAllowedUser(this.formIO.reviewer, this.formIO.userRoles);
    this.setFormsFlowTaskCurrentPage(1);
    this.setFormsFlowTaskId("");
    this.setFormsFlowactiveIndex(NaN);
    this.taskLoading = true;
    this.$root.$on("call-fetchTaskDetails", async (para: any) => {

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
      this.formIO.resourceId,
      this.formIO.reviewerId,
      this.formIO.reviewer,
      this.userEmail,
      this.formIO.userRoles,
      this.formIOJwtSecret
    );
    const filterListResult = await CamundaRest.filterList(
      this.token,
      this.bpmApiUrl
    );
    this.filterList = sortByPriorityList(filterListResult.data);

    if(this.taskDefaultFilterListNames.length > 0){
      this.filterList = this.filterList.filter(FilterList => { 
        return this.taskDefaultFilterListNames.some(filter=>FilterList.name.includes(filter));
      });
    }

    if(this.filterList.length>0){
      this.selectedfilterId = this.taskDefaultFilterListNames.length ? this.filterList[0].id : findFilterIdForDefaultFilterName(this.filterList, ALL_FILTER); 
    }

    else {
      this.tasks = [];
      this.taskLoading=false;
    }

    await this.reloadLHSTaskList();
    if (SocketIOService.isConnected()) {
      SocketIOService.disconnect();
    }
    SocketIOService.connect(
      this.webSocketEncryptkey,
      (refreshedTaskId: string, eventName: string, error: string) => {
        // this.taskIdWebsocket = refreshedTaskId;
        if (error) {
          this.toastMsgTxt = `WebSocket is not connected which will cause
            some inconsistency. System is trying to reconnect,
            if you see this message for more than 10sec,
            please refresh the page and try`;
          this.toastMsg.show();
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


 

<style>
.highlight:not(.djs-connection) .djs-visual > :nth-child(1) {
    fill: rgb(56,89,138) !important;
  }

</style>
