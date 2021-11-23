import {
  SearchQueryPayload, VariablePayload, 
} from ".";

export interface FilterPayload {
    id: string;
    name: string;
    owner?: string;
    properties: {
        color: string;
        priority: number;
        refresh: boolean;
        showUndefinedVariable: boolean;
    };
    query: {
        candidateUser?: string;
        includeAssignedTasks?: boolean;
        taskVariables?: VariablePayload[];
        processVariables?: VariablePayload[];
        caseInstanceVariables?: VariablePayload[];
        orQueries?: SearchQueryPayload[];
    };
    resourceType: string;
}
