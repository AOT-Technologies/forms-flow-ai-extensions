# TaskList Component

The customizable component which can be used to integrate formsflow.ai
solution in your Vue.js based project. 

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  bpmApiUrl   | string | undefined | Specifies the Camunda Rest API URL from which the TaskList is being generated.|
|  token   | string | undefined | Specifies the JWT token used for authorising formsflow.ai based component.|
|  formIOApiUrl | string | undefined | The URL of the form.io server. |
| formIOResourceId | string | undefined | User forms form-Id obtained from formio for role based access. |
| formIOReviewerId | string | undefined | forms-flow-forms administrator role Id from formio for role based access. |
| formIOReviewer | string | undefined | The role name used for designer users. In formsflow.ai usually set as `formsflow-designer` |
| formIOUserRoles | string | undefined | The valid FORMIO_ROLES as part of formio service. |
| formsflowaiApiUrl | string | undefined | The URL of the form-flow API  endpoint.|
| formsflowaiUrl | string | undefined | The URL endpoint which you would like to host your Vue based application.|
| containerHeight | string | 100vh | Prop to adjust the height values of Vue component, in case of Vue component is too much for your integrated application. You can adjust the height from the range 100-400 |
| taskSortBy | string | "created" | Prop to decide by default what value should the tasks be sorted when displayed at first. You can use the values like - "created", "dueDate", "followUpDate", "priority", "name", "assignee" |

## Usage

```<template>
    <CamundaTasklist
      :bpmApiUrl="configs.BPM_URL"
      :token="jwttoken"
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
</template>
<script>
import CamundaTasklist from "camunda-formio-tasklist-vue/src/components/TaskList.vue";
</script>
```
