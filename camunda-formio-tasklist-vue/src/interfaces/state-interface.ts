// Interface to define the state model example
import { SearchOptionPayload } from  "../models"
export interface FormsFlowStateModelIF {
  formsFlowTaskCurrentPage: number;
  formsFlowTaskId: string;
  formsFlowactiveIndex: number;
  formsFlowFilterSearchSelections: SearchOptionPayload[];
  searchQueryType: string;
  variableNameIgnoreCase: boolean;
  variableValueIgnoreCase: boolean;
}
