<template>
  <div v-if="token">
    <CamundaTasklist
      :bpmApiUrl="configs.BPM_URL"
      :token="token"
      :formIOApiUrl="configs.FORM_IO_API_URL"
      :formIOResourceId="configs.FORM_IO_RESOURCE_ID"
      :formIOReviewerId="configs.FORM_IO_REVIEWER_ID"
      :formIOReviewer="configs.FORM_IO_REVIEWER"
      :formsflowaiUrl="configs.FORM_FLOW_URL"
      :formsflowaiApiUrl="configs.FORM_FLOW_API_URL"
      :formIOUserRoles="configs.FORMIO_ROLES"
      :getTaskId="getTaskId"
      v-if="isServiceFLowEnabled"
    />
    <div class="no-content" v-else>You shouldnot be here !!!</div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import axios from "axios";
import CamundaTasklist from "@/components/TaskList.vue";
// import configMap from '../utils/config-helper'
@Component({
  components: {
    CamundaTasklist,
  },
})
export default class TaskList extends Vue {
  @Prop() private getTaskId!: string;
  public configs = {
    BPM_URL: "https://bpm3.aot-technologies.com/camunda",
    FORM_IO_API_URL: "https://forms3.aot-technologies.com",
    FORM_IO_RESOURCE_ID: "60c1c73734a299906d4b65b7",
    FORM_IO_REVIEWER_ID: "60c1c73734a2993eda4b65b5",
    FORM_IO_REVIEWER: "formsflow-reviewer",
    FORM_FLOW_API_URL: "http://52.60.234.176:5000/",
    FORM_FLOW_URL: "http://localhost:3000",
    SERVICEFLOW_ENABLED: true,
    FORMIO_ROLES: ["formsflow-reviewer"],
  };

  public isServiceFLowEnabled = true;
  public jwttoken: any = false;

  get token() {
    return this.jwttoken;
  }

  async getToken() {
    const config = {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    };
    const params = new URLSearchParams();
    params.append("grant_type", "password");
    params.append("username", "sumathi");
    params.append("password", "aot123");
    params.append("client_id", "forms-flow-web");

    const url =
      "https://iam3.aot-technologies.com/auth/realms/forms-flow-ai-app3/protocol/openid-connect/token";
    await axios.post(url, params, config).then((result: any) => {
      // Do somthing
      // console.log(result,'<<<<<<================res=====', result.data.access_token)
      this.jwttoken = result.data.access_token;
    });
  }

  created() {
    this.getToken();
  }

  mounted() {
    // this.token = sessionStorage.getItem('token')
    console.log("taskId---->", this.getTaskId);
    this.isServiceFLowEnabled = true;
    this.getToken();
    //this.token()
  }
}
</script>

<style scoped>
.no-content {
  display: flex;
  justify-content: center;
  font-size: 18px;
}
</style>
