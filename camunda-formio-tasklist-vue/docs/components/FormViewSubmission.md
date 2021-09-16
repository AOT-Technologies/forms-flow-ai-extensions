# FormViewSubmission Component

Customised component for viewing FormSubmissions in Application
History.

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  formId  :triangular_flag_on_post: | string | undefined | Specify the formid of formio form submission you want to view.|
|  submissionId  :triangular_flag_on_post: | string | undefined | Specify the formid of formio form submission you want to view.|

## Usage

```
<FormViewSubmission
  :formid="abc-f563-gt66-g6754"
  :submissionid="abc-hy64f-g78fcf-d35fd"
/>
import FormViewSubmission from "camunda-formio-tasklist-vue/src/components/FormViewSubmission.vue";
```
