<script lang='ts'>

import 'bootstrap-icons/font/bootstrap-icons.css';
import 'font-awesome/scss/font-awesome.scss';
import 'formiojs/dist/formio.full.min.css'
import 'vue2-datepicker/index.css';
import 'vue-select/src/scss/vue-select.scss';
import 'semantic-ui-css/semantic.min.css';
import '../../styles/user-styles.css'
import '../../styles/camundaFormIOTasklist.scss'

import { Component, Mixins, Prop } from 'vue-property-decorator'
import {
  TASK_FILTER_LIST_DEFAULT_PARAM,
  findFilterKeyOfAllTask,
  getTaskFromList,
  sortingList,
} from '../../services/utils';
import BaseMixin from './BaseMixin.vue';
import CamundaRest from '../../services/camunda-rest';
import DatePicker from 'vue2-datepicker'
import { Form } from 'vue-formio';
import {Payload} from '../../services/TasklistTypes';
import SocketIOService from '../../services/SocketIOServices';
import {getFormDetails} from '../../services/get-formio';
import {getISODateTime} from '../../services/format-time';
import {getformHistoryApi} from '../../services/formsflowai-api';
import moment from 'moment';
import { namespace } from 'vuex-class'
import vSelect from 'vue-select'

const serviceFlowModule = namespace('serviceFlowModule')

@Component({
  Form,
  DatePicker,
  vSelect,
})
export default class TaskListMixin extends Mixins(BaseMixin) {

    private test = 'hello';
    
}
</script>
