# TaskList Component

The customizable component which can be used to integrate formsflow.ai
solution in your Vue.js based project. 

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  bpmApiUrl :triangular_flag_on_post: | string | undefined | To specify the Camunda Rest API URL from which the TaskList is being generated. Use Camunda Rest API URL used in formsflow.ai installation.|
|  token :triangular_flag_on_post:  | string | undefined | To pass the JWT token used for authorising TaskList component.|
|  formIOApiUrl :triangular_flag_on_post: | string | undefined | The URL of the form.io server for showing forms |
| formIOResourceId :triangular_flag_on_post: | string | undefined | User forms form-Id obtained from formio for role based access. |
| formIOReviewerId :triangular_flag_on_post: | string | undefined | forms-flow-forms Reviewer role Id from formio for role based access. |
| formIOReviewer :triangular_flag_on_post: | string | undefined | The group name passed to specify which user has access to the TaskList component. In formsflow.ai, it usually set as `formsflow-designer` |
| UserRoles :triangular_flag_on_post: | string | undefined | The valid FORMIO_ROLES as part of formio service. It should be passed as props to user |
| formsflowaiApiUrl :triangular_flag_on_post: | string | undefined | The URL of the form-flow API  endpoint.|
| formsflowaiUrl :triangular_flag_on_post: | string | undefined | The URL endpoint which you would like to host your Vue based application.|
| containerHeight | string | 100vh | Prop to adjust the height values of Vue component, in case of Vue component is too much for your integrated application. You can adjust the height from the range 100-400 |
| taskSortBy | string | "created" | Prop to decide by default what value should the tasks be sorted when displayed at first. You can use the values like - "created", "dueDate", "follweowUpDate", "priority", "name", "assignee" |
| taskSortOrder  | string | "desc" | Prop to decide by default what value should the tasks should be ordered when displayed at first. You can use the values like - "desc", "asc" |
| webSocketEncryptkey | string | "formflowai" | Specifies the secret key used to connect to webSocket endpoint for encrypting keys in the taskList |

:triangular_flag_on_post: - mandatory props to be passed

## Usage

```
import CamundaTasklist from "camunda-formio-tasklist-vue/src/components/TaskList.vue";

<CamundaTasklist
  bpmApiUrl="https://somewebsite.com/camunda/engine-rest"
  token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrYTFKalB6Vy1EaHNFSE9vd2NZVHRJdW9sR3FqT0NhN1NYV0RFc"
  formIOApiUrl="https://random.com/api"
  formIOResourceId="3242affds123adfa"
  formIOReviewerId="123adsf123dafd1a"
  formIOReviewer="reviewer"
  formsflowaiUrl="https://yourwebsite.com"
  formsflowaiApiUrl="https://yourwebsite.com/api"
  formIOUserRoles="["forms-flow"]"
  taskSortBy="priority"
  taskSortOrder="asc"
/>
```
