<template>
  <div v-if="token">
    <CamundaTasklist
      :bpmApiUrl="configs.BPM_URL"
      :token="token"
      :formIOApiUrl= "configs.FORM_IO_API_URL"
      :formIOResourceId = "configs.FORM_IO_RESOURCE_ID"
      :formIOReviewerId = "configs.FORM_IO_REVIEWER_ID"
      :formIOReviewer = "configs.FORM_IO_REVIEWER"
      :formsflowaiUrl="configs.FORM_FLOW_URL"
      :formsflowaiApiUrl="configs.FORM_FLOW_API_URL"
      :formIOUserRoles="configs.FORMIO_ROLES"
      formIOJwtSecret="--- change me now ---"
      :getTaskId="getTaskId"    
      v-if="isServiceFLowEnabled"
      :disableComponents="{filterList:true , filterTask:true , form:true , sort:true}"
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
import { 
  SortPayload
}from '../models/FilterPayload';
// import configMap from '../utils/config-helper'
@Component({
  components: {
    CamundaTasklist
  }
})
export default class TaskList extends Vue {
  @Prop() private getTaskId!: string;
  public  configs = {
    "BPM_URL": "https://app2.aot-technologies.com/camunda",
    "FORM_IO_API_URL": "https://app2.aot-technologies.com/formio",
    "FORM_IO_RESOURCE_ID": "61b857cef85e9220818f89fc",
    "FORM_IO_REVIEWER_ID": "61b857cdf85e9225c08f89fa",
    "FORM_IO_REVIEWER": "formsflow-reviewer",
    "FORM_FLOW_API_URL":"https://app2.aot-technologies.com/api",
    "FORM_FLOW_URL":"http://localhost:3000",
    "SERVICEFLOW_ENABLED": true,
    "FORMIO_ROLES" : "formsflow-reviewer",
    "container-height": "200"
  }
 @Prop () disableComponents !: SortPayload ;

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
    params.append('username', 'john.honai');
    params.append('password', 'aot123');
    params.append('client_id', 'forms-flow-web');
    const url = "https://iam3.aot-technologies.com/auth/realms/forms-flow-mahagony/protocol/openid-connect/token";
    await axios.post(url, params, config)
      .then((result: any) => {
        this.jwttoken =  result.data.access_token;
        console.log("token", this.jwttoken);
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
