<template>
   <div class="cft-wrapper-div">
      <ExpandContract />
        <div class="bg-primary task-title"  ref="taskTitleRef"  >
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
              <!-- assignee and group on one div -->
              <div class="d-flex justify-content-start mb-4">
                <section v-if="!hideTaskDetails.assignee" class="task-assignee">
                  <label class="fw-bold">Task assignee</label>
                  <button
                    v-if="task.assignee"
                    class="btn task-icon-btn"
                    @click="toggleassignee"
                    data-bs-toggle="tooltip"
                    title="Click to change assignee"
                  >
                   <span v-if="loadingEditAssignee" class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>
                    <i v-if="!loadingEditAssignee"
                      class="fa fa-pencil"
                      :class="{
                      'fa-times-circle-o':editAssignee,
                      'fa-pencil': !editAssignee
                    }"
                    />
                  </button>
                  <!-- assignee section -->
                  <div class="d-flex align-items-baseline">
                    <template v-if="task.assignee">
                      <div
                        v-if="editAssignee"
                        class="d-flex w-100 mt-1"
                      >
                        <v-select
                          :label="selectSearchType!=='email'?selectSearchType:'code'"
                          :options="reviewerUsersList"
                          :filterable="false"
                          v-model="userSelected"
                          :placeholder="`Search by ${selectSearchType}`"
                          @search="onUserSearch"
                          class="select-assignee"
                        >
                         <template v-if="selectSearchType==='email' " v-slot:option="option">
                          <div>{{option.code}}</div>
                          <div>({{option.email}})</div>
                         </template>
                          </v-select>
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
                         <span v-if="loadingClaimAndUnclaim" class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>

                          <i v-if="!loadingClaimAndUnclaim" class="fa fa-times-circle-o" />
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
                     <span v-if="loadingClaimAndUnclaim" class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>
                     <i v-if="!loadingClaimAndUnclaim" class="fa fa-plus" />
                      <span class="mx-1">Claim</span>
                    </button>
                  </div>
                </section>
                <!-- end assignee section -->
                <!-- group section -->
                <section
                v-if="!hideTaskDetails.group" 
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
                      <div  v-if="groupLoading" class="d-flex justify-content-center">
                               <div class="spinner-border" role="status"  >
                                  <span class="sr-only">Loading...</span>
                              </div>
                           </div>
                  <div  v-else class="d-flex align-items-baseline group-name">
                    <template v-if="groupListNames && groupListNames.length&&!groupLoading">
                     <p class="text-truncate"> {{ String(groupListNames) }}</p>
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
                          <i
                            type="button"
                            class="fa fa-times mx-2"
                            data-bs-dismiss="modal"
                            aria-label="Close"
                            title="cancel"
                          ></i>
                        </div>
                        <div class="modal-body px-4 pb-5">
                          <i class="fa fa-exclamation-circle"></i>
                          You can add a group by typing a group ID into the input
                          field and afterwards clicking the button with the plus sign.
                          <div class="d-flex my-3">
                            <input
                              class="form-control group-name-input"
                              type="text"
                              v-model="groupText"
                              @keyup.enter="addGroup"
                              placeholder="Group ID"
                            />
                            <button
                              class="btn btn-primary add-group-btn"
                              @click="addGroup"
                              :disabled="!groupText"
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
                              class="d-flex align-items-baseline added-group-chip  text-truncate  "
                              v-for="g in groupList"
                              :key="g.groupId"
                              @click="deleteGroup(g.groupId)"
                              data-bs-toggle="tooltip"
                              title="Click to remove this group"
                            >
                              <div class="mx-1 text-truncate ">{{ g.groupId }}</div>
                              <i class="fa fa-times-circle-o" />
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </section>
                <!-- end group section -->
              </div>
              <!-- end assignee and group div -->
              <!-- followUp date starts here -->
              <div class="d-flex justify-content-between mb-2">
                <section class="task-date-picker" v-if="!hideTaskDetails.followUpDate" >
                  <label class="fw-bold mb-1">Follow up</label>
                  <div
                    class="d-flex align-items-baseline"
                    v-if="task.followUp"
                  >
                    <p
                      data-bs-toggle="tooltip"
                      v-if="!showCalenderFollowup"
                      :title="getExactDate(task.followUp)"
                      @click="showCalenderFollowup=true"
                      
                    >{{ timedifference(task.followUp) }}</p>
                  <template v-if="showCalenderFollowup">
                    <div class="input-group">
                      <input 
                       class="form-control"
                       type="datetime-local"
                       v-model="task.followUp"
                       @change="updateFollowUpDate" />
                    </div>
                  </template>
                  <button
                      class="btn task-icon-btn"
                      data-bs-toggle="tooltip"
                      v-if="showCalenderFollowup"
                      @click="showCalenderFollowup=false"
                      title="click to update FollowUp date"
                    >
                    <i class="fa fa-check-circle"></i>
                    </button>
                    <button
                      v-if="!showCalenderFollowup"
                      class="btn task-icon-btn"
                      @click="removeFollowupDate"
                      data-bs-toggle="tooltip"
                      title="Click to remove FollowUp Date"
                    >
                      <i class="fa fa-times-circle-o" />
                    </button>
                  </div>
                  <template v-else>
                    <div class="input-group">
                      <input 
                       class="form-control"
                       type="datetime-local"
                       v-model="task.followUp"
                       @change="updateFollowUpDate" />
                    </div>
                  </template>

                </section>
                <!------- FollowUp date ends here ----->
                <!------- Due  date starts here ----->
                <section class="task-date-picker" v-if="!hideTaskDetails.dueDate" >
                  <label class="fw-bold mb-1">Due date</label>
                  <div
                    class="d-flex align-items-baseline"
                    v-if="task.due"
                  >
                    <p
                     v-if="!showCalenderDue"
                      data-bs-toggle="tooltip"
                      :title="getExactDate(task.due)"
                      @click="showCalenderDue=true"
                    >{{ timedifference(task.due) }}</p>
                  <template v-if="showCalenderDue">
                    <div class="input-group">
                      <input 
                       class="form-control"
                       type="datetime-local"
                       v-model="task.due"
                       @input="updateDueDate" />
                      
                    </div>
                  </template>
                  <button
                      class="btn task-icon-btn"
                      data-bs-toggle="tooltip"
                      v-if="showCalenderDue"
                      @click="showCalenderDue=false"
                      title="click to update Due date"
                    >
                    <i class="fa fa-check-circle"></i>
                    </button>
                    <button
                      v-if="!showCalenderDue"
                      class="btn task-icon-btn"
                      @click="removeDueDate"
                      data-bs-toggle="tooltip"
                      title="Click to remove Due date"
                    >
                      <i class="fa fa-times-circle-o" />
                    </button>
                  </div>
                  <template v-else>
                    <div class="input-group">
                      <input 
                       class="form-control"
                       type="datetime-local"
                       v-model="task.due"
                       @input="updateDueDate" />
                      
                    </div>
                  </template>
                </section>
                <!------- Due date ends here ----->
                <section v-if="!hideTaskDetails.createdDate" >
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
                >
                  <div :class="{'disabled': task.assignee !== userName}" >
                    <div v-if="task.assignee === userName">
                    <transition name="fade"
                      >
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
                </div>
                <!-- Form tab content end -->
                <div
                  class="tab-pane fade"
                  id="taskHistory"
                  role="tabpanel"
                  aria-labelledby="history-tab"
                >
                  <TaskHistory
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
                    v-show="!diagramLoading"
                    class="cft-bpmn-viewer-container"
                  >
                    <div
                      class="cft-bpm-container cft-grab-cursor"
                      id="process-diagram-container"
                    ></div>
                  </div>
                </div>
              </div>
            </div>
   </div>
        
       
</template>


<script lang="ts">
import {
  CamundaRest,
  SEARCH_USERS_BY,
  SocketIOService,
  getISODateTime,
  reviewerGroup,
} from "../../services";

import {
  Component, Prop, Vue,
} from "vue-property-decorator";


import {
  GroupListPayload,
  SEARCH_OPTION_TYPE,
  TaskPayload,
  UserListObject,
  UserListPayload,
  UserPayload,
  UserSearchListLabelPayload,
} from "../../models";
import DatePicker from "v-calendar/lib/components/date-picker.umd";
import ExpandContract from "../addons/ExpandContract.vue";
import FormEdit from "../form/Edit.vue";
import FormView from "../form/View.vue";
import TaskHistory from "../../components/addons/TaskHistory.vue";
import {
  getFormattedDateAndTime,
} from "../../services";
import moment from "moment";
import vSelect from "vue-select";

@Component({
  components:{
    ExpandContract,
    TaskHistory,
    FormEdit,
    FormView,
    vSelect,
    VDatePicker: DatePicker,
  }
})
export default class RightSider extends Vue {
private groupText: any = null;
private showCalenderFollowup: boolean = false;
private showCalenderDue: boolean= false;
private loadingEditAssignee: boolean = false;
private reviewerUsersList: UserListPayload[] = [];
private editAssignee: boolean = false;
private groupLoading: boolean = false;
private selectSearchType: string = "lastName";
private UserSearchListLabel: UserSearchListLabelPayload[] = SEARCH_USERS_BY;
  private groupList: GroupListPayload[] = [];
  private groupListNames?: string[] = [];
  private groupListItems: string[] = [];
  private loadingClaimAndUnclaim: boolean = false;
 private userSelected: UserListPayload = {
 };
@Prop() reloadCurrentTask;
@Prop() token;
@Prop() task;
@Prop() getBPMTaskandReload;
@Prop() bpmApiUrl;
@Prop() taskScrollableHeight;
@Prop() activeUserSearchindex;
@Prop() getDiagramDetails;
@Prop() formioUrl;
@Prop() onFormSubmitCallback;
@Prop() oncustomEventCallback;
@Prop() diagramLoading;
@Prop() private userName;
@Prop() private hideTaskDetails;
 



// mount
mounted(){
  this.getGroupDetails();
}

getExactDate(date: any) {
  return getFormattedDateAndTime(date);
}

timedifference(date: Date) {
  return moment(date).fromNow();
}

 
// set assignee 
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
    this.getBPMTaskandReload();
  }
}

// toggle assignee
async toggleassignee() {
  this.loadingEditAssignee=true;
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
    this.loadingEditAssignee=false;
    this.editAssignee = !this.editAssignee;
  }
}


// user Search 
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


// selected user
setSelectedUserSearchBy(searchby: string, index: number) {
  this.selectSearchType = searchby;
  this.activeUserSearchindex = index;
}


// on claim
async onClaim() {
  this.loadingClaimAndUnclaim= true;
  await CamundaRest.claim(
    this.token,
      this.task.id!,
      {
        userId: this.userName,
      },
      this.bpmApiUrl
  );
   
  if (!SocketIOService.isConnected()) {
    this.getBPMTaskandReload();
  }
  this.loadingClaimAndUnclaim= false;

}
  

// on unclaim
async onUnClaim() {
  this.loadingClaimAndUnclaim= true;
  await CamundaRest.unclaim(this.token, this.task.id!, this.bpmApiUrl);

  if (!SocketIOService.isConnected()) {
    this.getBPMTaskandReload();
  }
  this.loadingClaimAndUnclaim= false;
}


// group details
async getGroupDetails() {
  this.groupLoading=true;
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
  this.groupLoading=false;

}


// add group
addGroup() {
  CamundaRest.createTaskGroupByID(
    this.token,
      this.task.id!,
      this.bpmApiUrl,
      {
        userId: null,
        groupId: this.groupText,
        type: "candidate",
      }
  ).then(() => {
    this.groupText=null;
    this.getGroupDetails();
    this.reloadCurrentTask();
  });
}

// delete group
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
    this.getGroupDetails();
    this.reloadCurrentTask();
  });
}


// update data
async updateTaskDatedetails(taskId: string, task: TaskPayload) {
  await CamundaRest.updateTasksByID(this.token, taskId, this.bpmApiUrl, task);
  if (!SocketIOService.isConnected()) {
    await this.reloadCurrentTask();
  }
}


// update follow update
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

// update due date
async updateDueDate() {
  const referenceobject = this.task;
  try {
    if (this.task?.due !== null) {
      referenceobject.due = getISODateTime(this.task.due);
      await this.updateTaskDatedetails(this.task.id!, referenceobject);
    }
  } catch {
    console.warn("Due date error"); // eslint-disable-line no-console
  }
}


// remove due date
async removeDueDate() {
  const referenceobject = this.task;
  try {
    referenceobject["due"] = null;
    await this.updateTaskDatedetails(this.task.id!, referenceobject);
  } catch {
    console.warn("Due date error"); // eslint-disable-line no-console
  }
}


// remove follow up date
async removeFollowupDate() {
  const referenceobject = this.task;
  try {
    referenceobject["followUp"] = null;
    await this.updateTaskDatedetails(this.task.id!, referenceobject);
  } catch {
    console.warn("Follow up date error"); // eslint-disable-line no-console
  }
}


}
</script>

<style lang="scss">
.cft-wrapper-div{
  position: relative;
}
.task-details-empty {
  background: #fff;
  margin-left: 4px;
}
.ctf-task-details-container {
  // margin-left: 0.5rem;
  background: #fff;
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
    p {
        cursor: pointer;
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
      max-width: 250px;
    }
  }
  .task-groups-modal {
    .group-name-input {
      margin-right: 1rem;
    }
    .add-group-btn {
      min-width: 144px;
    }
    .added-group-chip {
      background: #e7e7e7;
      max-width: 20%;
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
.cft-bpmn-viewer-container {
  min-height: 400px;
  position: relative;
}
.cft-bpm-container {
  height: 100%;
  position: absolute;
  width: 100%;
  overflow: hidden;
}

.cft-grab-cursor {
  cursor: move;
  cursor: grab;
}
.cft-btn_zoom{
  position: absolute;
  bottom: 10em;
  right:5%;
}
</style>
