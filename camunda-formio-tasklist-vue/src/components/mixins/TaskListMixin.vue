<script lang="ts">
import { Component, Mixins } from "vue-property-decorator";
import BaseMixin from "./BaseMixin.vue";

@Component({
  name: "TaskListMixin",
})
export default class TaskListMixin extends Mixins(BaseMixin) {

  public maximize: boolean = true;
  public userName: string| null = null;
}
</script>
