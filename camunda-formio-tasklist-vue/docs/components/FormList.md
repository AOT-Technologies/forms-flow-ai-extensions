# FormList Component

Customised component for listing the forms. When use this component you can see a button with labeled "Form". By clicking the button you can view all the forms here in a modal .

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  bpmApiUrl:triangular_flag_on_post: | string | undefined | To specify the Camunda Rest API URL from which the TaskList is being generated. Use Camunda Rest API URL used in formsflow.ai installation.|
|  token  :triangular_flag_on_post: | string | undefined | To pass the JWT token used for authorising TaskList component.|
|  update-task:triangular_flag_on_post: | string | undefined | Function that emits when a Form submitted.|

:triangular_flag_on_post:  - mandatory props to be passed

## Usage

```
import FormList from "camunda-formio-tasklist-vue/src/components/form/FormList.vue";

<FormList
      :token="jwttoken"
      :bpmApiUrl="configs.BPM_URL"
      @update-task="updateCheck"
    
    />
```
