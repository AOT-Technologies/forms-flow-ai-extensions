<template>
  <div v-if="token" class="task-demo">
      <CamundaTasklist
      class="ctf-task-list px-3"
      :bpmApiUrl="configs.BPM_URL"
      :token="configs.token"
      :formsflowaiUrl="configs.FORMS_FLOW_URL"
      :formsflowaiApiUrl="configs.FORMS_FLOW_API_URL"
      :getTaskId="getTaskId"
      :reviewer="configs.reviewer"
      :userRoles="configs.userRoles"
      :formioServerUrl="configs.formioServerUrl"
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
    reviewer : "reviewer",
    userRoles : "User Roles",
    formioServerUrl: "Set formio url",
    FORMS_FLOW_API_URL: "Set formsflow.ai API URL",
    FORMS_FLOW_URL: "Set formsflow.ai Project URL",
    SERVICEFLOW_ENABLED: true,
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
