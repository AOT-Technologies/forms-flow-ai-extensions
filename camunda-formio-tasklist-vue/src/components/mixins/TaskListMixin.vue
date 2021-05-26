<script lang='ts'>
import {Component, Mixins, Vue} from 'vue-property-decorator';
import BaseMixin from './BaseMixin.vue';

@Component({
  'name': 'TaskListMixin'
})
export default class TaskListMixin extends Mixins(BaseMixin) {

  public maximize = true;
}
</script>
