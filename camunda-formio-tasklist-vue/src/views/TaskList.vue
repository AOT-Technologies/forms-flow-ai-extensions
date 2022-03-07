<template>
  <div v-if="token" class="task-demo">
      <CamundaTasklist
      class="ctf-task-list px-3"
      :bpmApiUrl="configs.BPM_URL"
      :token="configs.token"
      :formIOApiUrl="configs.FORM_IO_API_URL"
      :formIOResourceId="configs.FORM_IO_RESOURCE_ID"
      :formIOReviewerId="configs.FORM_IO_REVIEWER_ID"
      :formIOReviewer="configs.FORM_IO_REVIEWER"
      :formsflowaiUrl="configs.FORM_FLOW_URL"
      :formsflowaiApiUrl="configs.FORM_FLOW_API_URL"
      :formIOUserRoles="configs.FORMIO_ROLES"
      :formIOJwtSecret="configs.FORMIO_JWT_SECRET"
      :getTaskId="getTaskId"
      v-if="isServiceFLowEnabled"
    />
    <div class="no-content" v-else>You shouldnot be here !!!</div>
  </div>
</template>

<script lang="ts">
import {
  Component, Prop, Vue 
} from "vue-property-decorator";
import CamundaTasklist from "@/components/TaskList.vue";

@Component({
  components: {
    CamundaTasklist,
  },
})

export default class TaskList extends Vue {
  
  @Prop() private getTaskId!: string;
  public configs = {
    token: "Bearer Token",
    BPM_URL: "Set Camunda URL",
    FORM_IO_API_URL: "Set FormIO Project URL",
    FORM_IO_RESOURCE_ID: "Set Formio Resource ID",
    FORM_IO_REVIEWER_ID: "Set Formio Reviewer ID",
    FORM_IO_REVIEWER: "Set Formio Reviewer Name",
    FORM_FLOW_API_URL: "Set formsflow.ai API URL",
    FORM_FLOW_URL: "Set formsflow.ai Project URL",
    SERVICEFLOW_ENABLED: true,
    FORMIO_ROLES: "Set FORMIO User ROLES",
    FORMIO_JWT_SECRET: "Set FORMIO JWT SECRET",
  };

  public isServiceFLowEnabled = true;
  mounted () {
    this.isServiceFLowEnabled = true;
  }
}
</script>

<style scoped>
.no-content {
  display: flex;
  justify-content: center;
  font-size: 18px;
}
.task-demo {
  background: #ddd;
  height: 100vh;
}
.ctf-task-list {
  height: calc(100vh - 48px);
  overflow: auto;
}
</style>
