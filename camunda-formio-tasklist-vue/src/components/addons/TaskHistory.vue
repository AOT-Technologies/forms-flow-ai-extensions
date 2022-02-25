<template>
  <div>
    <h3 class="mb-4"><i class="fa fa-list-alt"></i> Application History</h3>
    <table
      class="table task-history-table"
      v-if="applicationId && taskHistoryList.length"
    >
      <thead class="table-dark">
        <tr>
          <th scope="col">Status</th>
          <th scope="col">Created</th>
          <th scope="col">Submissions</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="h in taskHistoryList"
          :key="h.created"
        >
          <td class="fw-bold">{{ h.applicationStatus }}</td>
          <td>{{ getExactDate(h.created) }}</td>
          <td>
            <button
              class="btn btn-primary"
              :href="formatURL(h.formUrl)"
              target="_blank"
            >
              <i class="fa fa-eye"></i>
              View Submission
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else>
      No application history found
    </div>

    <!-- <b-modal
      id="view-modal"
      size="xl"
      title="VIEW SUBMISSION"
      ok-only
    >
      <FormViewSubmission
        :formid="fId"
        :submissionid="sId"
      />
    </b-modal> -->
  </div>
</template>

<script lang="ts">
import {
  Component, Prop, Vue
} from "vue-property-decorator";
import FormViewSubmission from "../FormViewSubmission.vue";
import {
  TaskHistoryListPayload
} from "../../models";
import {
  getLocalDateTime
} from "../../services";

@Component({
  components: {
    FormViewSubmission,
  },
})
export default class TaskHistory extends Vue {
  @Prop({
    default: []
  }) private taskHistoryList!: TaskHistoryListPayload[];
  @Prop() private applicationId!: string;

  private fId: string = "";
  private sId: string = "";

  getExactDate(date: Date) {
    return getLocalDateTime(date);
  }

  // viewSubmission(url: string) {
  //   const {formId, submissionId} = getFormIdandSubmissionId(url);
  //   this.fId = formId;
  //   this.sId = submissionId;
  // }

  formatURL(url: string) {
    const currentUrl = window.location.protocol + "//" + window.location.host;
    const a = document.createElement("a");
    a.href = url;
    const processedUrl = url.replace(a.protocol + "//" + a.host, currentUrl);
    return processedUrl;
  }
}
</script>

<style lang="scss" scoped>
.task-history-table {
  thead {
    th {
      height: 48px;
      vertical-align: middle;
      padding: 0.5rem 0.75rem;
    }
  }
  tbody {
    td {
      vertical-align: middle;
      padding: 0.5rem 0.75rem;
    }
  }
}
</style>
