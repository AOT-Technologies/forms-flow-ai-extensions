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
        taskVariables?: Array<VariablePayload>;
        processVariables?: Array<VariablePayload>;
        caseInstanceVariables?: Array<VariablePayload>;
        orQueries?: Array<SearchQueryPayload>;
    };
    resourceType: string;
}
