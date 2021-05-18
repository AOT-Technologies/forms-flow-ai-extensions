<script lang='ts'>
import {Component, Prop, Vue, Watch} from 'vue-property-decorator'

@Component
export default class BaseMixin extends Vue {
    @Prop() private bpmApiUrl!: string;
    @Prop() private token!: string;
    @Prop() private formIOResourceId!: string;
    @Prop() private formIOReviewerId!: string;
    @Prop() private formIOReviewer!: string;
    @Prop() private formIOApiUrl!: string;
    @Prop() private formsflowaiApiUrl!: string;
    @Prop() private formsflowaiUrl!: string;
    @Prop() private formIOUserRoles!: string;
    @Prop() private userName!: string;
    @Prop({default:'formflowai'}) private webSocketEncryptkey !: string;

    @Watch('token')
    ontokenChange (newVal: string) {
      localStorage.setItem("authToken", newVal);
    }

    public test = 'hello';
}
</script>