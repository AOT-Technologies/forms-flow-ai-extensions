<script lang="ts">
import {
  Component, Prop, Vue, Watch
} from "vue-property-decorator";
import {
  DisableComponentPropPayload
} from "../models";
import {
  engine
} from "../services";

  
@Component({
})
export default class BaseMixin extends Vue {
  @Prop() protected readonly bpmApiUrl!: string;
  @Prop() protected readonly token!: string;
  @Prop() protected readonly formsflowaiApiUrl!: string;
  @Prop() protected readonly formIOJwtSecret!: string;
  @Prop({
    default: "formflowai"
  }) public webSocketEncryptkey!: string;
   @Prop({
     type:Object
   }) public formIO!: any;
  @Prop ({
    default:()=>{
      return {
        filterList:false,  filterTask:false, sort:false, form:false
      };
    }
  }) 
  
  public disableComponents !: DisableComponentPropPayload ;

   @Prop ({
     default:()=>{
       return {
         assignee:false, group:false, followUpDate:false, dueDate:false, createdDate:false
       };
     }
   }) public hideTaskDetails: any;

   @Prop({
     default:false
   }) private listItemCardStyle!: boolean;

  @Watch("token")
   ontokenChange(newVal: string) {
     localStorage.setItem("authToken", newVal);
   }

  checkProps() {
    if (!this.bpmApiUrl || this.bpmApiUrl === "") {
      console.warn("bpmApiUrl prop not Passed");
    }
    if (!this.token || this.token === "") {
      console.warn("token prop not Passed");
    }
    if (!this.formIO.resourceId || this.formIO.resourceId === "") {
      console.warn("formIOResourceId prop not passed");
    }
    if (!this.formIO.reviewerId || this.formIO.reviewerId === "") {
      console.warn("formIOReviewerId prop not passed");
    }
    if (!this.formIO.apiUrl || this.formIO.apiUrl === "") {
      console.warn("formIOApiUrl prop not passed");
    }
    if (!this.formsflowaiApiUrl || this.formsflowaiApiUrl === "") {
      console.warn("formsflow.ai API url prop not passed");
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
    localStorage.setItem("formioApiUrl", this.formIO.apiUrl);
    localStorage.setItem("UserDetails", JSON.stringify(decodeToken));
  }
}
</script>
