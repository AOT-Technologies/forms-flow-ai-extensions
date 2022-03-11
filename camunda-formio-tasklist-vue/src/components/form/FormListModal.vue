<template>
  <div class="cftf-form-conatiner cft-text-font">
    <button
      class="btn btn-primary"
      variant="primary"
      v-b-modal.modal-multi-1
      @click="formListItems"
    >

      <i class="fa fa-wpforms mx-1"></i>
      Forms
    </button>
    <b-modal
      ref="modal-1"
      id="modal-multi-1"
      size="xl"
      no-close-on-backdrop
      no-close-on-esc
      :hide-footer="true"
    >
      <template #modal-header="{ close }">
        <h5>FORM LIST</h5>
        <b-button
          size="sm"
          variant="outline-danger"
          @click="close()"
        >
          <h5>
            Close
            <i class="fa fa-times"></i>
          </h5>
        </b-button>
      </template>
      <div>
        <b-table
          v-if="formitems.length > 0"
          sort-icon-left
          bordered
          hover
          responsive
          caption-top
          :fields="formfields"
          :items="formitems"
          :per-page="formperPage"
          :current-page="formcurrentPage"
          id="formListTable"
        >
          <template #cell(formName)="data">
            <span class="font-weight-bold text-justify">{{data.item.formName}}</span>
          </template>
          <template #cell(operations)="data">
            <b-button
              squared
              variant="primary"
              size="small"
              v-b-modal.modal-multi-2
              @click="storeFormValue(data.item.formId, data.item.formName)"
            >Submit New</b-button>
          </template>
        </b-table>

        <b-pagination
          v-if="formitems.length > 0"
          v-model="formcurrentPage"
          :per-page="formperPage"
          :total-rows="totalrows"
          aria-controls="formListTable"
          class="cft-form-list-paginate"
        ></b-pagination>
      </div>
    </b-modal>
    <b-modal
      ref="modal-2"
      id="modal-multi-2"
      size="xl"
      no-close-on-backdrop
      no-close-on-esc
      scrollable
      :hide-footer="true"
    >
      <template #modal-header="{ close }">
        <b-button
          variant="link"
          @click="backClickToFormList"
        >
          <h5>
            <i class="fa fa-chevron-left"></i> Back
          </h5>
        </b-button>
        <h5>SUBMIT FORM</h5>
        <b-button
          size="sm"
          variant="outline-danger"
          @click="close()"
        >
          <h5>
            Close
            <i class="fa fa-times"></i>
          </h5>
        </b-button>
      </template>
      <h4>{{ formTitle }}</h4>
      <Form
        :src="formValueId"
        form=""
        submission=""
        options=""
        v-on:submit="onSubmit"
        v-on:customEvent="oncustomEventCallback"
      ></Form>
    </b-modal>
  </div>
</template>

<script lang="ts">
import "semantic-ui-css/semantic.min.css";
import "../../styles/camundaFormIOFormList.scss";
import {
  CamundaRest, FORMLIST_FIELDS, formApplicationSubmit
} from "../../services";
import {
  Component, Mixins
} from "vue-property-decorator";
import {
  CustomEventPayload,
  FormListFieldsPayload,
  FormListItemsPayload,
  FormioSubmissionPayload
} from "../../models";
import BaseMixin from "../../mixins/BaseMixin.vue";
import {
  Form
} from "vue-formio";

@Component({
  components: {
    Form
  }
})
export default class FormListModal extends Mixins(BaseMixin) {
  private formfields: FormListFieldsPayload[] = FORMLIST_FIELDS;
  private formitems: FormListItemsPayload[] = [];
  private formperPage: number = 10;
  private formcurrentPage: number = 1;
  private formValueId?: string = "";
  private formId?: string = undefined;
  private formioUrl?: string = undefined;
  private formTitle: string = "";
  private submissionId?: string = "";

  get totalrows() {
    return this.formitems.length;
  }

  formListItems() {
    this.formitems = [];
    CamundaRest.listForms(this.token, this.bpmApiUrl).then(response => {
      response.data.forEach((form: FormListItemsPayload) => {
        this.formitems.push(form);
      });
    });
  }

  storeFormValue(val: string, title: string) {
    this.$bvModal.hide("modal-multi-1");
    const forms = localStorage.getItem("formioApiUrl") + "/form/";
    this.formId = val;
    this.formValueId = forms.concat(val);
    this.formTitle = title;
  }

  backClickToFormList() {
    this.$bvModal.hide("modal-multi-2");
    this.$bvModal.show("modal-multi-1");
  }

  onSubmit(submission: FormioSubmissionPayload) {
    this.formId = submission.form;
    this.submissionId = submission._id;

    const formsflowAIApiUrl = localStorage.getItem("formsflow.ai.api.url");
    if (
      typeof formsflowAIApiUrl !== "undefined"
      && formsflowAIApiUrl !== null
    ) {
      this.formioUrl
        = localStorage.getItem("formsflow.ai.url")
        + "/form/"
        + this.formId
        + "/submission/"
        + this.submissionId;
      formApplicationSubmit(
        formsflowAIApiUrl,
        {
          formId: this.formId,
          formSubmissionId: this.submissionId,
          formUrl: this.formioUrl
        },
        this.token
      );
    }
    this.$bvModal.hide("modal-multi-2");
    // this.$bvModal.show('modal-multi-1');
  }

  oncustomEventCallback = (customEvent: CustomEventPayload) => {
    switch (customEvent.type) {
    case "customSubmitDone":
      this.$bvModal.hide("modal-multi-2");
      break;
    }
  };

}
</script>
