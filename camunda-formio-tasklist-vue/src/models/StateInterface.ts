// Interface to define the state model example
import { 
  SearchOptionPayload 
} from  "../models";

export interface FormsFlowStateModelIF {
  formsFlowTaskCurrentPage: number;
  formsFlowTaskId: string;
  formsFlowactiveIndex: number;
  formsFlowFilterSearchSelections: SearchOptionPayload[];
  searchQueryType: string;
  taskLength: number;
  variableNameIgnoreCase: boolean;
  variableValueIgnoreCase: boolean;
}
