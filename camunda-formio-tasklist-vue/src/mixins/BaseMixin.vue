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
  @Prop() protected readonly formioServerUrl!: string;
  @Prop({
    default: "formflowai"
  }) public webSocketEncryptkey!: string;
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
    if (!this.formioServerUrl || this.formioServerUrl === "") {
      console.warn("formIOApiUrl prop not passed");
    }
    if (!this.formsflowaiApiUrl || this.formsflowaiApiUrl === "") {
      console.warn("formsflow.ai API url prop not passed");
    }
    if (!this.webSocketEncryptkey || this.webSocketEncryptkey === "") {
      console.warn("WEBSOCKET_ENCRYPT_KEY prop not passed");
    }

    const decodeToken = JSON.parse(Buffer.from(this.token.split(".")[1], 'base64').toString('utf8'));
    localStorage.setItem("bpmApiUrl", `${this.bpmApiUrl}/${engine}`);
    localStorage.setItem("authToken", this.token);
    const currentUrl = `${window.location.protocol}//${window.location.host}`;
    localStorage.setItem("formsflow.ai.url", currentUrl);
    localStorage.setItem("formsflow.ai.api.url", this.formsflowaiApiUrl);
    localStorage.setItem("formioApiUrl", this.formioServerUrl);
    localStorage.setItem("UserDetails", JSON.stringify(decodeToken));
  }
}
</script>
