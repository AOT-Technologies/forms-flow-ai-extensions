<script lang="ts">
import {
  Component, Prop, Vue, Watch 
} from "vue-property-decorator";
import {
  engine 
} from "../services";

@Component({
})
export default class BaseMixin extends Vue {
  @Prop() protected readonly bpmApiUrl!: string;
  @Prop() protected readonly token!: string;
  @Prop() protected readonly formIOResourceId!: string;
  @Prop() protected readonly formIOReviewerId!: string;
  @Prop() protected readonly formIOReviewer!: string;
  @Prop() protected readonly formIOApiUrl!: string;
  @Prop() protected readonly formsflowaiApiUrl!: string;
  @Prop() protected readonly formsflowaiUrl!: string;
  @Prop() protected readonly formIOUserRoles!: string;
  @Prop() protected readonly formIOJwtSecret!: string;
  @Prop({
    default: "formflowai" 
  }) public webSocketEncryptkey!: string;

  @Watch("token")
  ontokenChange (newVal: string) {
    localStorage.setItem("authToken", newVal);
  }

  checkProps () {
    if (!this.bpmApiUrl || this.bpmApiUrl === "") {
      console.warn("bpmApiUrl prop not Passed");
    }
    if (!this.token || this.token === "") {
      console.warn("token prop not Passed");
    }
    if (!this.formIOResourceId || this.formIOResourceId === "") {
      console.warn("formIOResourceId prop not passed");
    }
    if (!this.formIOReviewerId || this.formIOReviewerId === "") {
      console.warn("formIOReviewerId prop not passed");
    }
    if (!this.formIOApiUrl || this.formIOApiUrl === "") {
      console.warn("formIOApiUrl prop not passed");
    }
    if (!this.formsflowaiApiUrl || this.formsflowaiApiUrl === "") {
      console.warn("formsflow.ai API url prop not passed");
    }
    if (!this.formsflowaiUrl || this.formsflowaiUrl === "") {
      console.warn("formsflow.ai URL prop not passed");
    }
    if (!this.webSocketEncryptkey || this.webSocketEncryptkey === "") {
      console.warn("WEBSOCKET_ENCRYPT_KEY prop not passed");
    }

    const decodeToken = JSON.parse(atob(this.token.split(".")[1]));
    localStorage.setItem("bpmApiUrl", `${this.bpmApiUrl}/${engine}`);
    localStorage.setItem("authToken", this.token);
    const currentUrl = `${window.location.protocol}//${window.location.host}`;
    localStorage.setItem("formsflow.ai.url", currentUrl);
    localStorage.setItem("formsflow.ai.api.url", this.formsflowaiApiUrl);
    localStorage.setItem("formioApiUrl", this.formIOApiUrl);
    localStorage.setItem("UserDetails", JSON.stringify(decodeToken));
  }
}
</script>
