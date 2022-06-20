<template>
  <div class="cftf-form-conatiner cft-text-font">
    <button
      @click="formListItems"
      type="button"
      id="formsButton"
      class="btn btn-primary"
      data-bs-toggle="modal"
      data-bs-target="#formListModal"
    >
      Forms
    </button>

    <!-- Modal -->

    <div
      class="modal fade"
      id="formListModal"
      tabindex="-1"
      aria-labelledby="formListModalTitle"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5
              class="modal-title"
              id="formListModalTitle"
            >FORM LIST</h5>
            <button
              type="button"
              class="btn btn-outline-danger"
              data-bs-dismiss="modal"
              aria-label="Close"
            >
              Close <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <!-- normal table -->
            <table
              v-if="formitems.length > 0"
              class="table table-bordered"
            >
              <thead>
                <tr>
                  <th scope="col">Form Name</th>
                  <th scope="col">Operations</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(item,index) in formitems"
                  :key="index"
                >
                  <td>{{item.formName}}</td>
                  <td>
                    <button
                      class="btn btn-primary"
                      data-bs-toggle="modal"
                      data-bs-target="#formSubmitModal"
                      @click="storeFormValue(item.formId,item.formName)"
                    >Submit New</button>
                  </td>
                </tr>

              </tbody>
            </table>
            <Pagination
              v-if="formitems.length > 0 && totalrows > formitems.length"
              ref="taskListPaginationRef"
              :perPage="formperPage"
              :totalRecords="totalrows"
              @go-to-page="onPageChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Form submit modal -->

    <div
      class="modal fade"
      id="formSubmitModal"
      tabindex="-1"
      aria-labelledby="formSubmitModalTitle"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <button
               id="backtoFormListButton"
              class="btn btn-outline-dark"
              data-bs-dismiss="modal"
              aria-label="Close"
              data-bs-toggle="modal"
              data-bs-target="#formListModal"
            >
              <i class="fa fa-chevron-left"></i> Back
            </button>
            <h5>SUBMIT FORM</h5>
            <button
              type="button"
              id="formSubmitClose"
              class="btn btn-outline-danger"
              data-bs-dismiss="modal"
              aria-label="Close"
            >
              Close <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <h4>{{ formTitle }}</h4>

            <Form
              v-if="formValueId"
              :src="formValueId"
              form=""
              submission=""
              options=""
              v-on:submit="onSubmit"
              v-on:customEvent="oncustomEventCallback"
            />

          </div>
        </div>
      </div>
    </div>

    <!-- Form submit end -->

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
import Pagination from "../layout/Pagination.vue";


@Component({
  components: {
    Form,
    Pagination
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
  private totalrows: number= 0;

  formListItems() {
    const url: any= localStorage.getItem('formsflow.ai.api.url');
    CamundaRest.listForms(this.token, url,{
      page:this.formcurrentPage,limit:this.formperPage
    }).then(response => {
      if(response.data.forms&&response.data.totalCount){
        this.formitems=response.data.forms;
        this.totalrows=response.data.totalCount;
      }
    });
  }

  backToFormList(){
    const btn = document.querySelector('#backtoFormListButton') as HTMLElement | null;
    if(btn){
      btn.click();
    }
    this.formValueId ="";
  }

  onPageChange(number){
    this.formcurrentPage= number;
    this.formListItems();
  }

  storeFormValue(val: string, title: string) {
    const forms = localStorage.getItem("formioApiUrl") + "/form/";
    this.formId = val;
    this.formValueId = forms.concat(val);
    this.formTitle = title;
  }

 
  async onSubmit(submission: FormioSubmissionPayload) {

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
      ).then(()=>{
        this.backToFormList();
        this.$emit("update-task");
      });
    }
    // this.$bvModal.hide("modal-multi-2");
    // this.$bvModal.show('modal-multi-1');
    
  }

  oncustomEventCallback = (customEvent: CustomEventPayload) => {
    switch (customEvent.type) {
    case "customSubmitDone":
      this.$emit("update-task");
      break;
    default: 
      break;
    }
  };

}
</script>

