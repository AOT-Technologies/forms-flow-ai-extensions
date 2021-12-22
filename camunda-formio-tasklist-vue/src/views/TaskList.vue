<template>
  <div v-if="token">
    <CamundaTasklist
      :bpmApiUrl="configs.BPM_URL"
      :token="token"
      :formIOApiUrl= "configs.FORM_IO_API_URL"
      :webSocketEncryptkey= "configs.SOCKET_ENCRYPT_KEY"
      :formIOResourceId = "configs.FORM_IO_RESOURCE_ID"
      :formIOReviewerId = "configs.FORM_IO_REVIEWER_ID"
      :formIOReviewer = "configs.FORM_IO_REVIEWER"
      :formsflowaiUrl= "configs.FORM_FLOW_URL"
      :formsflowaiApiUrl= "configs.FORM_FLOW_API_URL"
      :formIOUserRoles=configs.FORMIO_ROLES
      :getTaskId="getTaskId"
        
      v-if="isServiceFLowEnabled"
    />
    <div class="no-content" v-else>You shouldnot be here !!!</div>
  </div>
</template>


<script lang="ts">
import {
  Component, Prop, Vue 
} from 'vue-property-decorator';
import CamundaTasklist from '@/components/TaskList.vue';
import axios from 'axios';
// import configMap from '../utils/config-helper'
@Component({
  components: {
    CamundaTasklist
  }
})
export default class TaskList extends Vue {
  @Prop() private getTaskId!: string;
  public  configs = {
    "BPM_URL": "https://bpm2.aot-technologies.com/camunda",
    "FORM_IO_API_URL": "https://forms2.aot-technologies.com",
    "FORM_IO_RESOURCE_ID": "6180c59690060285c3bc7e49",
    "FORM_IO_REVIEWER_ID": "6180c5969006022af2bc7e47",
    "FORM_IO_REVIEWER": "formsflow-reviewer",
    "FORM_FLOW_API_URL":"http://206.116.106.159:5000/",
    "FORM_FLOW_URL":"http://localhost:3000",
    "SERVICEFLOW_ENABLED": true,
    "FORMIO_ROLES" : "formsflow-reviewer",
    "SOCKET_ENCRYPT_KEY": "giert989jkwrgb@DR55"
  }
  public isServiceFLowEnabled = true
  public jwttoken: any = false
  get token () {
    return this.jwttoken;
  }
  
  async getToken () {    
    const config = {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    };
    const params = new URLSearchParams();
    params.append('grant_type', 'password');
    params.append('username', 'nancy-smith');
    params.append('password', 'aot123');
    params.append('client_id', 'forms-flow-web');
    const url = "https://iam.aot-technologies.com/auth/realms/formsflow-ai-willow/protocol/openid-connect/token";
    await axios.post(url, params, config)
      .then((result: any) => {
        this.jwttoken =  result.data.access_token;
      });
  }
  created () {
    this.getToken();
  }
  mounted () {
    // this.token = sessionStorage.getItem('token')
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
