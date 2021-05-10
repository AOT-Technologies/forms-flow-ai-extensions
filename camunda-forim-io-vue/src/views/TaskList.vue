<template>
  <div>
    <CamundaTasklist
      :bpmApiUrl="configs.BPM_URL"
      :token="token"
      :userName="user && user.username"
      :formIOUserRoles="configs.FORM_IO_USER_ROLES"
      :formIOApiUrl= "configs.FORM_IO_API_URL"
      :formIOResourceId = "configs.FORM_IO_RESOURCE_ID"
      :formIOReviewerId = "configs.FORM_IO_REVIEWER_ID"
      :formIOReviewer = "configs.FORM_IO_REVIEWER"
      :formsflowaiUrl="configs.FORM_FLOW_URL"
      :formsflowaiApiUrl="configs.FORM_FLOW_API_URL"
      :getTaskId="getTaskId"
      v-if="isServiceFLowEnabled"
    />
    <div class="no-content" v-else>You shouldnot be here !!!</div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import CamundaTasklist from '@/components/TaskList.vue'
import { State } from 'vuex-class'
// import configMap from '../utils/config-helper'
@Component({
  components: {
    CamundaTasklist
  }
})
export default class TaskList extends Vue {
  @Prop() private getTaskId!: string;
  @State('user') private user!: any
  public token: any = ''
  public  configs = {
    "BPM_URL": "https://dev-sbc-ffa-bpm.apps.silver.devops.gov.bc.ca/camunda",
    "FORM_IO_USER_ROLES": "formsflow-reviewer",
    "FORM_IO_API_URL": "https://dev-sbc-ffa-forms.apps.silver.devops.gov.bc.ca",
    "FORM_IO_RESOURCE_ID": "601f0527c716d313bc266919",
    "FORM_IO_REVIEWER_ID": "604be2f34c71022e29c03603",
    "FORM_IO_REVIEWER": "formsflow-reviewer",
    "FORM_FLOW_API_URL":"https://dev-sbc-serviceflow.apps.silver.devops.gov.bc.ca/api",
    "FORM_FLOW_URL":"https://dev-sbc-serviceflow.apps.silver.devops.gov.bc.ca",
    "SERVICEFLOW_ENABLED": true
  }
  public isServiceFLowEnabled = true
  mounted () {
    // this.token = sessionStorage.getItem('token')
    this.isServiceFLowEnabled = true
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
