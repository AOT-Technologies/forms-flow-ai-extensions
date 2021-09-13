# FormViewSubmission Component

Customised component for viewing FormSubmissions in Application
History.

## Props

| Name | Type |  Default | Description |
| --- | --   | --- | ------- |
|  formId   | string | undefined | Specify the formid of formio form submission you want to view.|
|  submissionId   | string | undefined | Specify the formid of formio form submission you want to view.|

## Usage

```<template>
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
