// Interface to define the state model example

export interface FormsFlowStateModelIF {
  formsFlowTaskCurrentPage: number;
  formsFlowTaskId: string;
  formsFlowactiveIndex: number;
  searchQueryType: string;
  taskLength: number;
  variableNameIgnoreCase: boolean;
  variableValueIgnoreCase: boolean;
}
