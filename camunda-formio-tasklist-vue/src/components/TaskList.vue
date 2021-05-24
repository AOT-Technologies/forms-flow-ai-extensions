<template> 
<b-container fluid class="task-outer-container">
  <Header
  v-if="token  && bpmApiUrl && maximize"
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
          v-if="token  && bpmApiUrl"
          :token="token"
          :formsflowaiApiUrl="formsflowaiApiUrl"
          :formIOApiUrl="formIOApiUrl"
          :bpmApiUrl="bpmApiUrl"
          :tasks='tasks'
          :Lentask='tasklength'
          :perPage="perPage"
          :selectedfilterId="selectedfilterId"
          :payload="payload"
        />
      </b-col>
      <b-col v-if="(getFormsFlowTaskId && task)" :lg="maximize ? 9 : 12" md="12">
        <ExpandContract/>
        <div class="cft-service-task-details">
          <b-row class="ml-0 task-header task-header-title" data-title="Task Name">
            {{ task.name }}</b-row
          >
          <br />
          <b-row class="ml-0 task-name">
            <span class="cft-process-name" data-title="Process Name">{{ taskProcess }}</span></b-row
          >
          <br />
          <b-row class="ml-0 cft-application-id" data-title="application ID"
            >Application ID # {{ applicationId }}</b-row
          >
          <div class="cft-actionable-container">
            <b-row class="cft-actionable">
              <b-col v-if='task.followUp' cols="12" md="3">
                <span>
                  <i class="fa fa-calendar"></i>
                  {{timedifference(task.followUp)}}
                  <i class="bi bi-x-circle" @click="removeFollowupDate"></i>
                </span>
              </b-col>
              <b-col v-else cols="12" md="3">
                <DatePicker
                  type="datetime"
                  placeholder="Set Follow-up date"
                  v-model="setFollowup[
                  (getFormsFlowTaskCurrentPage-1)*perPage + getFormsFlowactiveIndex
                  ]"
                  @change="updateFollowUpDate"
                ></DatePicker>
              </b-col>
              <b-col v-if='task.due' cols="12" md="3">
                <span>
                  <i class="fa fa-calendar"></i>
                  {{timedifference(task.due)}}
                  <i class="bi bi-x-circle" @click="removeDueDate"></i>
                </span>
              </b-col>
              <b-col v-else cols="12" md="3">
                <DatePicker
                  type="datetime"
                  placeholder="Set Due Date"
                  v-model="setDue[
                  (getFormsFlowTaskCurrentPage-1)*perPage + getFormsFlowactiveIndex
                  ]"
                  @change="updateDueDate"
                ></DatePicker>
              </b-col>
              <b-col cols="12" md="3">
                <div
                    id='groups'
                    v-b-modal.AddGroupModal
                >
                  <i class="bi bi-grid-3x3-gap-fill"></i>
                    <span v-if="groupListNames">
                        {{ String(groupListNames) }}
                    </span>
                    <span v-else>Add Groups</span>
                  <b-tooltip target="groups" triggers="hover">
                    <b>Groups</b>
                  </b-tooltip>
                </div>
                <b-modal
                  id="AddGroupModal"
                  ref="modal"
                  title="Manage Groups"
                  :hide-footer=true
                >
                  <div class="modal-text">
                    <i class="bi bi-exclamation-circle"></i>
                    You can add a group by typing a group ID into the input
                    field and afterwards clicking the button with the plus sign.
                    <b-row class="mt-3 mb-3">
                      <b-col>
                        <b-button
                          variant="primary"
                          @click="addGroup"
                          :disabled='!setGroup'
                        >
                          <span>Add a group</span>
                          <span>
                            <i class="bi bi-plus"></i>
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
              <b-col v-if="task.assignee" cols="12" :md="editAssignee ? 3: 2">
                <div>
                  <div v-if="editAssignee" class="cft-user-edit">
                    <div class='cft-assignee-change-box row'>
                      <v-select @search="fetchOptions" :options="autoUserList" v-model="userSelected" class="col-9 col-md-9"/>
                      <span @click="onSetassignee" class="col-9 col-md-1">
                        <i class="bi bi-check"></i>
                      </span>
                      <span @click="toggleassignee" class="col-9 col-md-1">
                        <i class="fa fa-times ml-1"></i>
                      </span>
                    </div>
                  </div>
                  <div class="cft-user-details" v-else>
                  <b-tooltip target="setAssignee" triggers="hover">
                    Click to change <b>assignee</b>
                  </b-tooltip>
                  <span id='setAssignee'>
                    <i class="bi bi-person-fill cft-person-fill" />
                    <span class="cft-user-span" @click="toggleassignee">
                    {{task.assignee}}
                    </span>
                  </span>
                  <b-tooltip target="resetAssignee" triggers="hover">
                    Reset <b>Assignee</b>
                  </b-tooltip>
                  <span id="resetAssignee">
                    <i class="fa fa-times ml-1" @click="onUnClaim" />
                  </span>
                  </div>
                </div>
              </b-col>
              <b-col v-else cols="12" md="2">
                <div @click="onClaim">
                  <span id='claimAssignee'>
                    <i class="bi bi-person-fill" />
                    Claim
                  </span>
                </div>
                <b-tooltip target="claimAssignee" triggers="hover">
                    Claim a <b>task</b>
                </b-tooltip>
              </b-col>
            </b-row>
            <div class="height-100">
              <b-tabs pills class="height-100" content-class="mt-3">
                <b-tab title="Form" active>
                  <div v-if="showfrom" class="ml-4 mr-4">
                    <b-overlay
                      :show="task.assignee !== userName"
                      variant="light"
                      opacity="0.75"
                      spinner-type="none"
                      aria-busy="true"
                    >
                      <formio
                        :src="formioUrl"
                        :options="
                          task.assignee === userName ? options : { readOnly: true }
                        "
                        v-on:submit="onFormSubmitCallback"
                        v-on:customEvent="oncustomEventCallback"
                      ></formio>
                    </b-overlay>
                  </div>
                </b-tab>
                <b-tab title="History">
                  <TaskHistory :taskHistoryList='taskHistoryList' :applicationId="applicationId"/>
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
        <ExpandContract/>
        <b-row class="cft-not-selected mt-2 ml-1 row">
          <i
            class="bi bi-exclamation-circle-fill"
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
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'font-awesome/scss/font-awesome.scss';
import 'formiojs/dist/formio.full.min.css'
import 'vue2-datepicker/index.css';
import 'vue-select/src/scss/vue-select.scss';
import 'semantic-ui-css/semantic.min.css';
import '../styles/user-styles.css';
import '../styles/camundaFormIOTasklist.scss';
import { Component, Mixins, Prop } from 'vue-property-decorator'
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  findFilterKeyOfAllTask,
  getTaskFromList,
  getUserName
} from '../services/utils';
import BpmnViewer from 'bpmn-js';
import CamundaRest from '../services/camunda-rest';
import DatePicker from 'vue2-datepicker';
import ExpandContract from './addons/ExpandContract.vue';
import { Form } from 'vue-formio';
import Header from './layout/Header.vue';
import LeftSider from './layout/LeftSider.vue';
import {Payload} from '../services/TasklistTypes';
import SocketIOService from '../services/SocketIOServices';
import TaskHistory from '../components/TaskHistory.vue';
import TaskListMixin from './mixins/TaskListMixin.vue';
import {authenticateFormio} from '../services/formio-token';
import {getFormDetails} from '../services/get-formio';
import {getISODateTime} from '../services/format-time';
import {getformHistoryApi} from '../services/formsflowai-api';
import moment from 'moment';
import { namespace } from 'vuex-class';
import vSelect from 'vue-select';

const serviceFlowModule = namespace('serviceFlowModule')


@Component({
  components: {
    formio: Form,
    DatePicker,
    TaskHistory,
    Header,
    LeftSider,
    vSelect,
    ExpandContract,
    BpmnViewer
  },
})
export default class Tasklist extends Mixins(TaskListMixin) {
  @Prop() private getTaskId!: string;

  @serviceFlowModule.Getter('getFormsFlowTaskCurrentPage') private getFormsFlowTaskCurrentPage: any;
  @serviceFlowModule.Getter('getFormsFlowTaskId') private getFormsFlowTaskId: any;
  @serviceFlowModule.Getter('getFormsFlowactiveIndex') private getFormsFlowactiveIndex: any;

  @serviceFlowModule.Mutation('setFormsFlowTaskCurrentPage') public setFormsFlowTaskCurrentPage: any
  @serviceFlowModule.Mutation('setFormsFlowTaskId') public setFormsFlowTaskId: any
  @serviceFlowModule.Mutation('setFormsFlowactiveIndex') public setFormsFlowactiveIndex: any


  private tasks: Array<object> = [];
  private fulltasks: Array<object> = [];
  private taskProcess = null;
  private processDefinitionId = '';
  private formId = '';
  private submissionId = '';
  private formioUrl = '';
  private task: any;
  private setFollowup: any = [];
  private setDue: any = [];
  private setGroup = null;
  private userSelected: any = {};
  private showfrom = false;
  public perPage = 10;
  private tasklength = 0;
  private options = {noAlerts: false,i18n: {
    en: {error: "Please fix the errors before submitting again.",},},
  };
  private filterList = [];
  private editAssignee = false;
  private applicationId = '';
  private groupList = [];
  private groupListNames: Array<string> | null = null;
  private groupListItems: string[] = [];
  private userEmail = 'external';
  private selectedfilterId = '';
  private xmlData!: string;
  private payload: Payload = {
    active: true,
    sorting: TASK_FILTER_LIST_DEFAULT_PARAM,
    firstResult: 0,
    maxResults: this.perPage
  };
  private taskHistoryList: Array<object> = [];
  private autoUserList: any = []
  private taskIdValue = '';
  private taskId2 = '';
  private userName: any = ''
  

  checkPropsIsPassedAndSetValue () {
    if(this.getTaskId) {
      this.taskIdValue = this.getTaskId;
    }
    if(!this.getTaskId) {
      const routeparams = this.$route?.query?.taskId;
      if(typeof(routeparams) ==='string' && this.$route.query.taskId) {
        this.taskIdValue = routeparams;
      }
    }
    this.userName = getUserName()
  }

  timedifference (date: Date) {
    return moment(date).fromNow();
  }

  toggleassignee ()  {
    this.editAssignee = ! this.editAssignee;
    this.userSelected['code'] = this.task.assignee;
  }

  onFormSubmitCallback () {
    if (this.task.id) {
      this.onBPMTaskFormSubmit(this.task.id);
      this.reloadTasks();					   
    }					  
  }

  addGroup () {
    CamundaRest.createTaskGroupByID(
      this.token,
      this.task.id,
      this.bpmApiUrl,
      { userId: null, groupId: this.setGroup, type: "candidate" }
    ).then(() => {
      this.getGroupDetails();
      this.reloadCurrentTask();
      this.setGroup = null;
    });
  }
  async getGroupDetails () {
    const grouplist = await CamundaRest.getTaskGroupByID(this.token, this.task.id, this.bpmApiUrl);
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
  deleteGroup (groupid: string) {		 
    CamundaRest.deleteTaskGroupByID(this.token, this.task.id, this.bpmApiUrl, {
      groupId: groupid,
      type: "candidate",
    }).then(() => {
      this.getGroupDetails();
      this.reloadCurrentTask();
    });
  }

  onBPMTaskFormSubmit (taskId: string) {
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
    CamundaRest.formTaskSubmit(
      this.token,
      taskId,
      formRequestFormat,
      this.bpmApiUrl
    )
      .then(() => {
        this.reloadCurrentTask();
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }


  getBPMTaskDetail (taskId: string) {
    CamundaRest.getTaskById(this.token, taskId, this.bpmApiUrl).then(
      (result) => {
        this.task = result.data;
        CamundaRest.getProcessDefinitionById(
          this.token,
          this.task.processDefinitionId,
          this.bpmApiUrl
        ).then((res) => {
          this.taskProcess = res.data.name;
        });
      }		   
    );
    this.getGroupDetails();
  }


  getTaskFormIODetails (taskId: string) {
    this.showfrom = false;
    CamundaRest.getVariablesByTaskId(
      this.token,
      taskId,
      this.bpmApiUrl
    ).then((result) => {
      if(result.data["formUrl"]?.value)
      {
        this.formioUrl = result.data["formUrl"].value;
        const { formioUrl, formId, submissionId } = getFormDetails(
          this.formioUrl,
          this.formIOApiUrl
        );

        this.formioUrl = formioUrl;
        this.submissionId = submissionId;
        this.formId = formId;
      }
      this.showfrom = true;
    });
  }


  getTaskHistoryDetails (taskId: string) {
    this.applicationId = '';
    this.taskHistoryList = [];
    CamundaRest.getVariablesByTaskId(
      this.token,
      taskId,
      this.bpmApiUrl
    ).then((result) => {
      if(result.data && result.data["applicationId"]?.value) {
        this.applicationId = result.data["applicationId"].value;
        getformHistoryApi(this.formsflowaiApiUrl, result.data["applicationId"].value, this.token)
          .then((r)=> {
            this.taskHistoryList = r.data.applications;
          })
      }
    })
  }


  getTaskProcessDiagramDetails (task: any) {
    CamundaRest.getProcessDiagramXML(
      this.token,
      task.processDefinitionId,
      this.bpmApiUrl
    ).then(async (res) => {
      this.xmlData = res.data.bpmn20Xml;
      const div = document.getElementById('canvas');
      if(div){
        div.innerHTML = ""
      }
      const viewer = new BpmnViewer({
        container: '#canvas'
      });

      try {
        const { warnings } = await viewer.importXML(this.xmlData);
        viewer.get('canvas').zoom('fit-viewport');
      } catch (err) {
        console.error('error rendering process diagram', err);
      }
    });
  }

  oncustomEventCallback = (customEvent: any) => {
    switch (customEvent.type) {
    case "reloadTasks":
      this.reloadTasks();
      break;
    case "reloadCurrentTask":
      this.reloadCurrentTask();
      break;
    }
  };

  reloadTasks () {
    this.setFormsFlowTaskId("");
    this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
  }

  reloadCurrentTask () {
    this.getBPMTaskDetail(this.task.id);
    this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
  }

  onClaim () {
    CamundaRest.claim(
      this.token,
      this.task.id,
      { userId: this.userName },
      this.bpmApiUrl
    )
      .then(() => {
        this.fetchTaskData(this.getFormsFlowTaskId);
        this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  onUnClaim () {		  
    CamundaRest.unclaim(this.token, this.task.id, this.bpmApiUrl)
      .then(() => {
        this.reloadCurrentTask();
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  onSetassignee () {
    CamundaRest.setassignee(this.token, this.task.id,
      {"userId": this.userSelected?.code },
      this.bpmApiUrl)
      .then(() => {
        this.fetchTaskData(this.getFormsFlowTaskId);
        this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
      })
      .catch((error) => {
        console.error("Error", error);
      })
    this.toggleassignee();
  }

  fetchTaskList (filterId: string, requestData: object) {
    CamundaRest.filterTaskList(
      this.token,
      filterId,
      requestData,
      this.bpmApiUrl
    ).then((result) => {
      this.fulltasks = result.data;
      this.tasklength = result.data.length;
    });
  }

  
  fetchPaginatedTaskList (filterId: string, requestData: object, first: number, max: number) {
    this.selectedfilterId = filterId;
    CamundaRest.filterTaskListPagination(
      this.token,
      filterId,
      requestData,
      first,
      max,
      this.bpmApiUrl
    ).then((result) =>{
      this.tasks = result.data;
    });
  }

  updateTaskDatedetails (taskId: string, task: any) {
    CamundaRest.updateTasksByID(
      this.token,
      taskId,
      this.bpmApiUrl,
      task
    )
      .then(() => {
        this.reloadCurrentTask();
      })
      .catch((error) => {
        console.error("Error", error);
      });
  }

  updateFollowUpDate () {
    const referenceobject = this.task;
    try{
      referenceobject['followUp'] = getISODateTime(this.setFollowup[
        (this.getFormsFlowTaskCurrentPage-1)*this.perPage + this.getFormsFlowactiveIndex
      ]);
      this.updateTaskDatedetails(this.task.id, referenceobject);
    }
    catch {
      console.warn("Follow date error");
    }
  }

  updateDueDate () {
    const referenceobject = this.task;
    try{
      referenceobject['due'] = getISODateTime(this.setDue[
        ((this.getFormsFlowTaskCurrentPage-1)*this.perPage) + this.getFormsFlowactiveIndex
      ]);
      this.updateTaskDatedetails(this.task.id, referenceobject);
    }
    catch {
      console.warn("Due date error");
    }
  }

  removeDueDate () {
    const referenceobject = this.task;
    try{
      this.setDue[
        (this.getFormsFlowTaskCurrentPage-1)*this.perPage + this.getFormsFlowactiveIndex
      ] = null
      referenceobject["due"] = null;
      this.updateTaskDatedetails(this.task.id, referenceobject);
    }
    catch {
      console.warn("Due date error");
    }
  }

  removeFollowupDate () {
    const referenceobject = this.task;
    try{
      referenceobject["followUp"] = null;
      this.setFollowup[
        (this.getFormsFlowTaskCurrentPage-1)*this.perPage + this.getFormsFlowactiveIndex
      ] = null;
      this.updateTaskDatedetails(this.task.id, referenceobject);
    }
    catch {
      console.warn("Follow up date error")
    }
  }

  fetchTaskData (taskId: string) {
    this.task = getTaskFromList(this.tasks, taskId);
    this.getBPMTaskDetail(taskId);
    CamundaRest.getVariablesByProcessId(
      this.token,
      this.task.processInstanceId,
      this.bpmApiUrl
    )
    this.getTaskFormIODetails(taskId);
    this.getTaskHistoryDetails(taskId);
    this.getTaskProcessDiagramDetails(this.task);
  }

  mounted () {
    this.setFormsFlowTaskCurrentPage(1)
    this.setFormsFlowTaskId('')
    this.setFormsFlowactiveIndex(0)
    this.$root.$on('call-fetchData', (para: any) => {
      this.editAssignee = false
      this.setFormsFlowTaskId(para.selectedTaskId);
      this.fetchTaskData(this.getFormsFlowTaskId)
    })

    this.$root.$on('call-fetchPaginatedTaskList', (para: any) => {
      this.selectedfilterId = para.filterId;
      this.payload = para.requestData;
      this.fetchPaginatedTaskList(para.filterId, para.requestData, para.firstResult, para.maxResults);
    })

    this.$root.$on('call-fetchTaskList', (para: any) => {
      this.fetchTaskList(para.filterId, para.requestData)
    })

    this.$root.$on('call-managerScreen', (para: any) => {
      this.maximize = para.maxi
    })

    this.checkPropsIsPassedAndSetValue();
    authenticateFormio(
      this.formIOResourceId,
      this.formIOReviewerId,
      this.formIOReviewer,
      this.userEmail,
      this.formIOUserRoles
    );
    CamundaRest.filterList(this.token, this.bpmApiUrl).then((response) => {
      this.filterList = response.data;
      this.selectedfilterId = findFilterKeyOfAllTask(this.filterList, "name", "All tasks");
      this.fetchTaskList(this.selectedfilterId, this.payload);
      this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
    });

    if(SocketIOService.isConnected()) {
      SocketIOService.disconnect();
    }
    SocketIOService.connect(this.webSocketEncryptkey, (refreshedTaskId: any, eventName: any)=> {
      if(this.selectedfilterId){
        this.fetchPaginatedTaskList(this.selectedfilterId, this.payload, (this.getFormsFlowTaskCurrentPage-1)*this.perPage, this.perPage);
        this.fetchTaskData(this.getFormsFlowTaskId);
        if (eventName === "create") {
          this.$root.$emit('call-pagination')
          this.fetchTaskList(this.selectedfilterId, this.payload);
        }
      }
      if((this.getFormsFlowTaskId) && (refreshedTaskId===this.getFormsFlowTaskId)){
        this.fetchTaskData(this.getFormsFlowTaskId);
        this.reloadCurrentTask();
      } 
    })

    CamundaRest.getUsers(this.token, this.bpmApiUrl).then((response) => {
      this.autoUserList = []
      response.data.forEach((element: any) => {
        this.autoUserList.push({ "code" : element.id, "label" : element.email })
         
      });
    });
    //We used two variables - taskId2 and taskIdValue because the router value from gettaskId is always constant,so after calling the required task details from router to use other tasks in list we need to set taskId2 value as ''
    if((this.taskId2 !== this.taskIdValue)) {
      this.taskId2 = this.taskIdValue;
    }
    else {
      this.taskId2 = '';
    }
  }

  fetchOptions (search: any) {
    CamundaRest.getUsersByEmail(this.token, this.bpmApiUrl, search).then((response) => {
      this.autoUserList = []
      response.data.forEach((element: any) => {
        this.autoUserList.push({ "code" : element.id, "label" : element.email })
         
      });
    });
  }

  findTaskIdDetailsFromURLrouter (taskId: string, tasks: any) {
    this.task = getTaskFromList(tasks, taskId);
    this.setFormsFlowTaskId(this.taskIdValue);
    const pos = tasks.map(function (e: any) {
      return e.id;
    }).indexOf(this.taskIdValue)
    this.setFormsFlowactiveIndex(pos%this.perPage);
    this.$root.$emit('update-activeIndex-pagination', {activeindex: this.getFormsFlowactiveIndex})
    this.setFormsFlowTaskCurrentPage(Math.floor(pos/this.perPage)+1);
    this.$root.$emit('update-pagination-currentpage', {page: this.getFormsFlowTaskCurrentPage});
    
  }
 
 
  updated () {
    if((this.fulltasks.length) && (this.taskId2 !== '')){
      this.findTaskIdDetailsFromURLrouter(this.taskId2, this.fulltasks);
      this.getBPMTaskDetail(this.taskId2);
      this.getTaskFormIODetails(this.taskId2);
      this.getTaskHistoryDetails(this.taskId2);
      this.getTaskProcessDiagramDetails(this.task);
      this.taskId2='';
    }
  }

  beforeDestroy () {
    SocketIOService.disconnect();
    this.$root.$off('call-fetchData')
    this.$root.$off('call-fetchPaginatedTaskList')
    this.$root.$off('call-fetchTaskList')
    this.$root.$off('call-managerScreen')

  }
}
</script>
