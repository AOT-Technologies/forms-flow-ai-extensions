<template>
  <div>
    <h3><i class="fa fa-list"></i> Application History</h3>
    <b-col v-if="applicationId && taskHistoryList.length">
      <b-table-simple
        caption-top
        responsive
        head-variant="light"
        :bordered="true"
        :outlined="true"
      >
        <b-thead head-variant="light">
          <b-tr>
            <b-th>Status</b-th>
            <b-th>Created</b-th>
            <b-th>Submissions</b-th>
          </b-tr>
        </b-thead>
        <b-tbody>
          <b-tr v-for="h in taskHistoryList" :key="h.created">
            <b-th>{{ h.applicationStatus }}</b-th>
            <b-th>{{ getExactDate(h.created) }}</b-th>
            <b-th>
              <b-button
                variant="primary"
                :href="formatURL(h.formUrl)"
                target="_blank"
              >
                <i class="fa fa-eye" style="color:black;"></i>
                View Submission
              </b-button>
            </b-th>
          </b-tr>
        </b-tbody>
      </b-table-simple>
    </b-col>
    <b-col v-else>
      <span> No application history found</span>
    </b-col>

    <b-modal id="view-modal" size="xl" title="VIEW SUBMISSION" ok-only>
      <FormViewSubmission :formid="fId" :submissionid="sId" />
    </b-modal>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import FormViewSubmission from "../FormViewSubmission.vue";
import { TaskHistoryListPayload } from "../../models/TaskHistoryListPayload";
import { getLocalDateTime } from "../../services/format-time";

@Component({
  components: {
    FormViewSubmission,
  },
})
export default class TaskHistory extends Vue {
  @Prop({ default: [] }) private taskHistoryList!: TaskHistoryListPayload[];
  @Prop() private applicationId!: string;

  private fId: string = "";
  private sId: string = "";

  getExactDate (date: Date) {
    return getLocalDateTime(date);
  }

  // viewSubmission(url: string) {
  //   const {formId, submissionId} = getFormIdandSubmissionId(url);
  //   this.fId = formId;
  //   this.sId = submissionId;
  // }

  formatURL (url: string) {
    const currentUrl = window.location.protocol + "//" + window.location.host;
    const a = document.createElement("a");
    a.href = url;
    const processedUrl = url.replace(a.protocol + "//" + a.host, currentUrl);
    return processedUrl;
  }
}
</script>
