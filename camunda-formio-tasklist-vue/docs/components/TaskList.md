# TaskList Component

The customizable component which can be used to integrate formsflow.ai
solution in your Vue.js based project. 

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  bpmApiUrl :triangular_flag_on_post: | string | undefined | To specify the Camunda Rest API URL from which the TaskList is being generated. Use Camunda Rest API URL used in formsflow.ai installation.|
|  token :triangular_flag_on_post:  | string | undefined | To pass the JWT token used for authorising TaskList component.|
| formsflowaiApiUrl :triangular_flag_on_post: | string | undefined | The URL of the form-flow API  endpoint.|
| reviewer :triangular_flag_on_post: | string | undefined | To specify the reviewer|
| userRoles :triangular_flag_on_post: | Array | undefined | To set the user roles|
| formioServerUrl :triangular_flag_on_post: | string | undefined | To set the formio url|
| containerHeight | string | 100vh | Prop to adjust the height values of Vue component, in case of Vue component is too much for your integrated application. You can adjust the height from the range 100-400 |
| taskSortBy | string | "created" | Prop to decide by default what value should the tasks be sorted when displayed at first. You can use the values like - "created", "dueDate", "follweowUpDate", "priority", "name", "assignee" |
| taskSortOrder  | string | "desc" | Prop to decide by default what value should the tasks should be ordered when displayed at first. You can use the values like - "desc", "asc" |
| taskDefaultFilterListNames | Array[string] | optional | Prop to set default filterNames to be available for the component and corresponding to it get the filtered Tasklist. `eg: ['Clerk Tasks', 'XYZ |
| webSocketEncryptkey | string | "formflowai" | Specifies the secret key used to connect to webSocket endpoint for encrypting keys in the taskList |
| disableComponents  | Object | `{filterList:false,  filterTask:false, sort:false, form:false}` | Prop to decide to disable the option from components. "filterList", "sort", "form": these are Header component. if you need to disable these option the value should be =true."filterTask" is on Left side. |
| hideTaskDetails  | Object | ` {assignee:false, group:false, followUpDate:false, dueDate:false, createdDate:false}` | Prop to decide to hide the option from Task Details|
| listItemCardStyle  | boolean |false |Prop to decide to make the list item in a card style |

:triangular_flag_on_post: - mandatory props to be passed

## Usage

```
import CamundaTasklist from "camunda-formio-tasklist-vue/src/components/TaskList.vue";

<CamundaTasklist
  bpmApiUrl="https://somewebsite.com/camunda/engine-rest"
  token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrYTFKalB6Vy1EaHNFSE9vd2NZVHRJdW9sR3FqT0NhN1NYV0RFc"
  formsflowaiUrl="https://yourwebsite.com"
  formsflowaiApiUrl="https://yourwebsite.com/api"
  reviewer="reviewer"
  userRoles="['forms-flow']"
  formioServerUrl="https://example-formio.com"
  taskSortBy="priority"
  taskSortOrder="asc",
  :disableComponents="{filterList:false,  filterTask:false, sort:false, form:false}"
  :hideTaskDetails="{ assignee:true,group:true}"
  :listItemCardStyle="true" 
  taskSortOrder="asc"
  taskDefaultFilterListNames=['Clerk Tasks', 'XYZ']
/>

