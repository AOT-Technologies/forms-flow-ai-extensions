import {
  FormsFlowStateModelIF 
} from '../../models';
import {
  QUERY_TYPES
} from '../../services';

export const FormsFlowstateModel: FormsFlowStateModelIF = {
  formsFlowTaskCurrentPage: 1,
  formsFlowTaskId: '',
  formsFlowactiveIndex: NaN,
  searchQueryType: QUERY_TYPES.ALL,
  variableNameIgnoreCase: false,
  variableValueIgnoreCase: false,
  taskLength: 0,
};
