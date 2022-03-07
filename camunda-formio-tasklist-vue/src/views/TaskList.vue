<template>
  <div v-if="token" class="task-demo">
      <CamundaTasklist
      class="ctf-task-list px-3"
      :bpmApiUrl="configs.BPM_URL"
      :token="configs.token"
      :formIO="configs.FORMIO_CONFIG"
      :formsflowaiUrl="configs.FORM_FLOW_URL"
      :formsflowaiApiUrl="configs.FORM_FLOW_API_URL"
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
    FORMIO_CONFIG:{ 
      apiUrl:'set formIO url', 
      resourceId:'set formIO resource id',
      reviewerId:'reviewer id', 
      reviewe:'set reviewer',
      userRoles:'set user role id' 
    },
    FORM_FLOW_API_URL: "Set formsflow.ai API URL",
    FORM_FLOW_URL: "Set formsflow.ai Project URL",
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
