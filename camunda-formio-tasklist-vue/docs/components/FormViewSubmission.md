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
<template>
    <FormViewSubmission
      v-if="formId && submissionId"
      :formid="formId"
      :submissionid="submissionId"
    />
    </template>
    <script>
    import FormViewSubmission from "camunda-formio-tasklist-vue/src/components/FormViewSubmission.vue";
    </script>
```
