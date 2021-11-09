import {
  FormsFlowStateModelIF 
} from '../../models';
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
  },
  actions: {
  },
  mutations: {
    setFormsFlowTaskCurrentPage: (state: FormsFlowStateModelIF, payload: number) => (state.formsFlowTaskCurrentPage = payload),
    setFormsFlowTaskId: (state: FormsFlowStateModelIF, payload: string) => (state.formsFlowTaskId = payload),
    setFormsFlowactiveIndex: (state: FormsFlowStateModelIF, payload: number) => (state.formsFlowactiveIndex = payload),
    setFormsFlowTaskSearchType: (state: FormsFlowStateModelIF) => ( state.searchQueryType === "ALL" ? (state.searchQueryType = "ANY") : state.searchQueryType = "ALL"),
    setFormsFlowTaskLength: (state: FormsFlowStateModelIF, payload: number) => (state.taskLength = payload),
  }
};
