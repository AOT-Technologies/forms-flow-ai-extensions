<script lang='ts'>
import {Component, Prop, Vue, Watch} from 'vue-property-decorator'

@Component
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
    // @Prop() protected readonly userName!: string;
    @Prop({default:'formflowai'}) public webSocketEncryptkey !: string;

    @Watch('token')
    ontokenChange (newVal: string) {
      localStorage.setItem("authToken", newVal);
    }

    public test = 'hello';
}
</script>