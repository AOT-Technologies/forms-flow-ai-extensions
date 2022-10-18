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
              @click="onClose"
            >
              Close <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <!-- normal table -->
            <table
              class="table table-bordered"
            >
              <thead>
                <tr>
                  <th scope="col"> 
                    <div class="d-flex justify-content-between align-items-center">
                      <div>
                        <span>
                        Form Name
                      </span>
                      <button
                       class="btn  btn-sm"
                       v-if="sortValue==='desc'"
                       @click="updateSort()"
                       title="Ascending"
                     >
                       <i
                         class="fa fa-arrow-up"
                         aria-hidden="true"
                       ></i>
                     </button>
                     <button
                       class="btn btn-sm"
                       v-else
                       @click="updateSort()"
                       title="Descending"
                     >
                       <i
                         class="fa fa-arrow-down"
                         aria-hidden="true"
                       ></i>
                     </button>
                      </div>
                     <div class="d-flex align-items-center">
                      <input 
                        class=" form-control"  
                        v-model="searchValue" 
                        type="text" 
                        @keypress.enter="handleSearch"
                        placeholder="Search by form name"
                        id="example-search-input">
                        <button v-if="searchValue" @click="clearSearch" title="Cancel" class="btn btn-sm btn-outline-primary mx-1">
                          <i class="fa fa-times"></i>
                       </button>
                        <button @click="handleSearch" title="Click to search" class="btn btn-sm btn-outline-primary">
                          <i class="fa fa-search" aria-hidden="true"></i>
                       </button>
                     </div>
                    </div>
                  </th>
                  <th scope="col">Operations</th>
                </tr>
              </thead>
              <tbody v-if="formitems.length>0">
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
              <tbody v-else>             
                <tr>
                  <td>
                    <h5>
                      No results have been found for &nbsp;"{{searchValue}}"
                    </h5>
                  </td>
                </tr>
              </tbody>
            </table>
            <Pagination

              v-if="formitems.length > 0 && totalrows > formitems.length"
              ref="taskListPaginationRef"
              :perPage="formperPage"
              :currentPageNumber="formcurrentPage"
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
      <div class="modal-dialog modal-xl modal-dialog-scrollable">
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
              @click="onClose"
            >
              Close <i class="fa fa-times"></i>
            </button>
          </div>
          <div class="modal-body h-100">
            <h4>{{ formTitle }}</h4>
            <div class="alert alert-danger" role="alert" v-if="formError">
              {{formError}}
            </div>
            <div v-else>
            <Form
              v-if="Object.keys(formData).length > 0"
              ref="formio-form"
              :form="formData"
              submission=""
              options=""
              v-on:submit="onSubmit"
              v-on:customEvent="oncustomEventCallback"
            />
            </div>
            <div class="alert alert-danger" role="alert" v-if="submissionError">
              {{submissionError}}
            </div>

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
  Component,Mixins
} from "vue-property-decorator";
import {
  CustomEventPayload,
  FormListFieldsPayload,
  FormListItemsPayload,
  FormioSubmissionPayload
} from "../../models";
import {
  Form,Formio
} from "vue-formio";

import BaseMixin from "../../mixins/BaseMixin.vue";
import Pagination from "../layout/Pagination.vue";
import axios from "axios";


@Component({
  components: {
    Form,
    Pagination
  }
})
export default class FormListModal extends Mixins(BaseMixin) {
  private formfields: FormListFieldsPayload[] = FORMLIST_FIELDS;
  private formitems: FormListItemsPayload[] = [];
  public formioUrl = localStorage.getItem("formioApiUrl") + "/form/"
  private formperPage: number = 10;
  public formcurrentPage: number = 1;
  private formId?: string = undefined;
  private formTitle: string = "";
  private totalrows: number= 0;
  private searchValue: any='';
  private searchTimer;
  private sortValue: string='asc';
  private sortBy: string="formName" ;
  private formError: string="";
  public submissionError: string= "";
  public isSearch: boolean= false;
  public formData = {
  };

  // update sort
  updateSort(){
    if(this.sortValue==="asc"){
      this.sortValue="desc";
    }
    else{
      this.sortValue="asc";
    }
    this.formListItems();
  }


  // handle search
  handleSearch(){
    if(this.searchValue){
      this.isSearch= true;
      this.$emit("resetValue",1);
      this.formcurrentPage=1;
      this.formListItems();
    }
  }

  clearSearch(){
    this.searchValue="";
    if(this.isSearch){
      this.$emit("resetValue",1);
      this.formcurrentPage=1;
      this.formListItems();
    }

    this.isSearch=false;
    
  }
  // formslist
  formListItems() {
    const url: any= localStorage.getItem('formsflow.ai.api.url');
    CamundaRest.listForms(this.token, url,{
      page:this.formcurrentPage,limit:this.formperPage,formName:this.searchValue,sortBy:this.sortBy,sortOrder:this.sortValue
    }).then(response => { 
      this.formitems=response.data.forms;
      this.totalrows=response.data.totalCount;
    });
  }
  // click on back button
  backToFormList(){
    Formio.clearCache();
    this.formData = {
    };
    this.formError="";
    const btn = document.querySelector('#backtoFormListButton') as HTMLElement | null;
    if(btn){
      btn.click();
    }

  }

  onPageChange(number){
    this.formcurrentPage= number;
    this.formListItems();
  }

  storeFormValue(val: string, title: string) {
    Formio.clearCache();
    this.formTitle = title;
    this.formError="";
    this.formData={
    };
    this.formId = val;
    const url = this.formioUrl.concat(val);
    axios.get(url, {
      headers:{
        "x-jwt-token": localStorage.getItem("formioToken")
      }
    }).then((res)=>{
      if(res.data){
        this.formData = res.data;
      }else{
        this.formData = {
        };
        this.formError="No data found";
      }
    }).catch((err)=>{
      this.formData = {
      };
      if(err.response?.data){
        this.formError= err.response.data;
      }else{
        this.formError= err;
      }
     
    });
    
  }

 
  async onSubmit(submission: FormioSubmissionPayload) {
    const submissionUrl = `${this.formioUrl}${this.formId}/submission`;
    axios.post(submissionUrl, submission,{
      headers:{
        "x-jwt-token":localStorage.getItem("formioToken")||""
      }
    }).then((res)=>{
      if(res.data){
        const submissionDetails = res.data;
        const formsflowAIApiUrl = localStorage.getItem("formsflow.ai.api.url");
        if (
          typeof formsflowAIApiUrl !== "undefined"
      && formsflowAIApiUrl !== null
        ) {
          const webFormUrl= `${window.location.origin}/form/${this.formId}/submission/${submissionDetails._id}`;
          formApplicationSubmit(
            formsflowAIApiUrl,
            {
              formId: this.formId,
              submissionId: submissionDetails._id,
              webFormUrl,
              formUrl: `${submissionUrl}/${submissionDetails._id}`
            },
            this.token
          ).then(()=>{
            this.backToFormList();
            this.$emit("update-task");
          }).catch((err)=>{
            if(err.response?.data){
              this.submissionError= err.response.data?.message;
            }else{
              this.submissionError= err;
            }
          });
        }
      
      }
    }).catch(err=>{
      if(err.response?.data){
        this.submissionError= err.response.data;
      }
      console.error(err);
    });
    
    
    // this.$bvModal.hide("modal-multi-2");
    // this.$bvModal.show('modal-multi-1');
    
  }

  onClose(){
    this.formitems=[];
    this.sortValue="asc";
    this.searchValue='';
    this.formcurrentPage = 1;
    this.$emit("resetValue",1);
    
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
<style>
 input{
  margin-right: 10px;
  float: right;
}
</style>
