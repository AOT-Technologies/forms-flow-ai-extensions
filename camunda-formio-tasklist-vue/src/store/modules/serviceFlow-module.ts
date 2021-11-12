import {
  FormsFlowStateModelIF 
} from '../../models';
import {
  QUERY_TYPES
} from '../../services';
import {
  FormsFlowstateModel 
} from '../state';

export default {
  namespaced: true,
  state: {
    ...FormsFlowstateModel
  },
  getters: {
    getFormsFlowTaskCurrentPage (state: any) {
      return state.formsFlowTaskCurrentPage;
    },
    getFormsFlowTaskId (state: any) {
      return state.formsFlowTaskId;
    },
    getFormsFlowactiveIndex (state: any) {
      return state.formsFlowactiveIndex;
    },
    getFormsFlowTaskSearchType (state: any) {
      return state.searchQueryType;
    },
    getFormsFlowTaskLength (state: any) {
      return state.taskLength;
    },
    getVariableNameIgnoreCase (state: any) {
      return state.variableNameIgnoreCase;
    },
    getVariableValueIgnoreCase (state: any) {
      return state.variableValueIgnoreCase;
    }
  },
  actions: {
  },
  mutations: {
    setFormsFlowTaskCurrentPage: (state: FormsFlowStateModelIF, payload: number) => (state.formsFlowTaskCurrentPage = payload),
    setFormsFlowTaskId: (state: FormsFlowStateModelIF, payload: string) => (state.formsFlowTaskId = payload),
    setFormsFlowactiveIndex: (state: FormsFlowStateModelIF, payload: number) => (state.formsFlowactiveIndex = payload),
    setFormsFlowTaskSearchType: (state: FormsFlowStateModelIF) => ( 
    state.searchQueryType === QUERY_TYPES.ALL ? (state.searchQueryType = QUERY_TYPES.ANY) : state.searchQueryType = QUERY_TYPES.ALL
    ),
    setFormsFlowTaskLength: (state: FormsFlowStateModelIF, payload: number) => (state.taskLength = payload),
    setVariableNameIgnoreCase: (state: FormsFlowStateModelIF) => (
      state.variableNameIgnoreCase === false ? (state.variableNameIgnoreCase = true) : state.variableNameIgnoreCase = false
    ),
    setVariableValueIgnoreCase: (state: FormsFlowStateModelIF) => (
      state.variableValueIgnoreCase === false ? (state.variableValueIgnoreCase = true) : state.variableValueIgnoreCase = false
    ),
  }
};
