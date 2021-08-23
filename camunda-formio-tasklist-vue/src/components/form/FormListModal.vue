<template>
  <div class="cftf-form-conatiner cft-text-font">
    <b-button variant="primary" v-b-modal.modal-multi-1>
      <span ref="btn-show"><i class="fa fa-wpforms"> Forms</i></span>
    </b-button>
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
        <b-button size="sm" variant="outline-danger" @click="close()">
          <h5>Close <i class="fa fa-times"></i></h5>
        </b-button>
      </template>
      <div v-if="formList.length" class="overflow-auto">
        <b-table-simple
          hover
          small
          caption-top
          responsive
          :bordered="true"
          :outlined="true"
          :per-page="formperPage"
        >
          <b-thead>
            <b-tr>
              <b-th>
              <span @click="sortFormList" class="cft-cursor-pointer">
                  Form Name
                  <i
                    :class="
                      sortAscending ? 'fa fa-caret-up' : 'fa fa-caret-down'
                    "
                  />
              </span>
              </b-th>
              <b-th>Operations</b-th>
            </b-tr>
          </b-thead>
          <b-tbody>
            <b-tr v-for="form in formList" :key="form.formId">
              <b-th> {{ form.formName }}</b-th>
              <b-th>
                <b-button
                  variant="primary"
                  v-b-modal.modal-multi-2
                  @click="storeFormValue(form.formId, form.formName)"
                  >Submit New
                </b-button>
              </b-th>
            </b-tr>
          </b-tbody>
        </b-table-simple>

        <b-pagination-nav
          :link-gen="linkFormGen"
          :number-of-pages="formNumPages"
          v-model="formcurrentPage"
          class="cft-form-list-paginate"
        />
      </div>
      <div v-else>
        <b-list-group-item>
          <b-row class="cft-not-selected mt-2 ml-1 row">
            <i class="fa fa-exclamation-circle"></i>
            <p>No Form found in the list.</p>
          </b-row>
        </b-list-group-item>
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
        <b-button variant="link" @click="backClick">
          <h5><i class="fa fa-chevron-left"></i> Back</h5>
        </b-button>
        <h5>SUBMIT FORM</h5>
        <b-button size="sm" variant="outline-danger" @click="close()">
          <h5>Close <i class="fa fa-times"></i></h5>
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
      >
      </Form>
    </b-modal>
        <!-- <b-modal
          ref="modal-3"
          id="modal-multi-3"
          size="xl"
          title="View form"
          ok-only
        >
        <FormViewSubmission :formid="formId" :submissionid="submissionId">
        </FormViewSubmission>
        </b-modal> -->
  </div>
</template>

<script lang="ts">
import "../../styles/camundaFormIOFormList.scss";
import { Component, Mixins } from "vue-property-decorator";
import BaseMixin from "../mixins/BaseMixin.vue";
import CamundaRest from "../../services/camunda-rest";
import { Form } from "vue-formio";
import { formApplicationSubmit } from "../../services/formsflowai-api";

@Component({
  components: {
    Form,
  },
})
export default class FormListModal extends Mixins(BaseMixin) {
  private formList: Array<object> = [];
  private formperPage: number = 10;
  private formNumPages: number = 1;
  private formcurrentPage: number = 1;
  private formValueId?: string  = "";
  private formId?: string = undefined;
  private formioUrl?: string = undefined;
  private formTitle: string = "";
  private sortAscending: boolean = false;
  private submissionId?: string = "";

  linkFormGen () {
    this.formListItems();
  }

  formListItems () {
    CamundaRest.listForms(this.token, this.bpmApiUrl).then((response) => {
      this.formNumPages = Math.ceil(response.data.length / this.formperPage);
      this.formList = response.data.splice(
        (this.formcurrentPage - 1) * this.formperPage,
        this.formperPage
      );
    });
  }

  storeFormValue (val: string, title: string) {
    this.$bvModal.hide("modal-multi-1");
    const forms = localStorage.getItem("formioApiUrl") + "/form/";
    this.formId = val;
    this.formValueId = forms.concat(val);
    this.formTitle = title;
  }

  backClick () {
    this.$bvModal.hide("modal-multi-2");
    this.$bvModal.show("modal-multi-1");
  }

  sortFormList () {
    this.sortAscending = !this.sortAscending;
    this.formList = this.formList.reverse();
  }

  onSubmit (submission: any) {
    this.formId = submission.form;
    this.submissionId = submission._id;

    const formsflowAIApiUrl = localStorage.getItem("formsflow.ai.api.url");
    if (
      typeof formsflowAIApiUrl !== "undefined" &&
      formsflowAIApiUrl !== null
    ) {
      this.formioUrl =
        localStorage.getItem("formsflow.ai.url") +
        "/form/" +
        this.formId +
        "/submission/" +
        this.submissionId;
      formApplicationSubmit(
        formsflowAIApiUrl,
        {
          formId: this.formId,
          formSubmissionId: this.submissionId,
          formUrl: this.formioUrl,
        },
        this.token
      );
    }
    this.$bvModal.hide("modal-multi-2");
    // this.$bvModal.show('modal-multi-1');
  }

  oncustomEventCallback = (customEvent: any) => {
    switch (customEvent.type) {
    case "customSubmitDone":
      this.$bvModal.hide("modal-multi-2");
      // this.$bvModal.show('modal-multi-1');
      break;
    }
  };

  mounted () {
    this.formListItems();
  }
}
</script>
