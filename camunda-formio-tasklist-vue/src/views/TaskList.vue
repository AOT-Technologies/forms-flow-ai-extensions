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
      :formIOJwtSecret="configs.FORMIO_JWT_SECRET"
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
    "FORM_IO_RESOURCE_ID": "61b857cef85e9220818f89fc",
    "FORM_IO_REVIEWER_ID": "61b857cdf85e9225c08f89fa",
    "FORM_IO_REVIEWER": "formsflow-reviewer",
    "FORM_FLOW_API_URL":"http://206.116.106.159:5000/",
    "FORM_FLOW_URL":"http://localhost:3000",
    "SERVICEFLOW_ENABLED": true,
    "FORMIO_ROLES" : "formsflow-reviewer",
    "SOCKET_ENCRYPT_KEY": "giert989jkwrgb@DR55",
    "FORMIO_JWT_SECRET": "--- change me now ---"
  }
  public isServiceFLowEnabled = true
  public jwttoken: any = false
  get token () {
    return "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrYTFKalB6Vy1EaHNFSE9vd2NZVHRJdW9sR3FqT0NhN1NYV0RFcDI1dGZBIn0.eyJqdGkiOiI5YWExYjdmNS1lZGJmLTRmOTgtODA4OS05NjAyYzc2MzZiY2QiLCJleHAiOjE2NDI3NDcxMzcsIm5iZiI6MCwiaWF0IjoxNjQyNzQ2NTM3LCJpc3MiOiJodHRwczovL2lhbS5hb3QtdGVjaG5vbG9naWVzLmNvbS9hdXRoL3JlYWxtcy9mb3Jtc2Zsb3ctYWktd2lsbG93IiwiYXVkIjpbImNhbXVuZGEtcmVzdC1hcGkiLCJmb3Jtcy1mbG93LXdlYiIsInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6IjE5YWNlNjExLWM5NTEtNDc4Ni1hNDFmLTNlNmU5Yzc0YzMwYiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImZvcm1zLWZsb3ctd2ViIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiMjE1NmVkN2MtMDVkZS00NThhLWEzNWItYTUxOTQ0YjY5NTI5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiXX0sImZvcm1zLWZsb3ctd2ViIjp7InJvbGVzIjpbImZvcm1zZmxvdy1yZXZpZXdlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJjYW11bmRhLXJlc3QtYXBpIGVtYWlsIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInJvbGUiOlsiZm9ybXNmbG93LXJldmlld2VyIl0sIm5hbWUiOiJOYW5jeSBTbWl0aCIsImdyb3VwcyI6WyIvY2FtdW5kYS1hZG1pbiIsIi9mb3Jtc2Zsb3cvZm9ybXNmbG93LWNtcy1yZXZpZXdlciIsIi9mb3Jtc2Zsb3cvZm9ybXNmbG93LXJldmlld2VyIiwiL2Zvcm1zZmxvdy1hbmFseXRpY3MvZ3JvdXAxIl0sInByZWZlcnJlZF91c2VybmFtZSI6Im5hbmN5LXNtaXRoIiwiZ2l2ZW5fbmFtZSI6Ik5hbmN5IiwiZmFtaWx5X25hbWUiOiJTbWl0aCIsImRhc2hib2FyZHMiOlsiW3snNic6ICdOZXcgQnVzaW5lc3MgTGljZW5zZSBBcHBsaWNhdGlvbid9LCB7JzgnOiAnU2VudGltZW50IEFuYWx5c2knfSwgeycxMyc6ICdzYW1wbGUyJ30sIHsnMTQnOiAnU2FtcGxlMyd9LCB7JzEwJzogJ1NBTVBMRTEnfSwgeycxNSc6ICdTYW1wbGUgNCd9LCB7JzUnOiAnTmV3IEJ1c2luZXNzIExpY2Vuc2UgQXBwbGljYXRpb24nfSwgeyc5JzogJ0ZyZWVkb20gb2YgSW5mb3JtYXRpb24gYW5kIFByb3RlY3Rpb24gb2YgUHJpdmFjeSd9XSJdLCJlbWFpbCI6Im5hbmN5LnNtaXRoQGFvdC5jb20ifQ.3wOqmzlPNujMqyWdD2UrgOjxhxJ7TtOUptEXlp20sPBstz4U_Z1WYnul-pGr4Eh1PAit8EO2WbMddI7ouR8GK2o-OEDjsovsoMP868FwCgYbqCFkQ1eVArRyq75R7-Rpcf5DmkHS4uU_zqCvakOmUUoIVAaWyeVD0KBM5E-6IQCqfWts0Jbl6GVt3MG9FpXZtgT6UkI0y0p7DphGeg3XR9p6YWFbCGCMAKpPYB_E_5logvI7SBNAgt2yq7WE2cW0gkhDDbZBBCMR7T5VZvmWsBkjwZss-s1f2YRPPX9jNJ6XHHoLK_tsHb9OAQ9Qxt_bK5TkM5EVJ_EFMtUC5er63g";
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
