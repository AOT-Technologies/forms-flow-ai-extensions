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
        variables?: any;
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

export interface DisableComponentPropPayload {
    sort?: boolean ;
    form?: boolean ;
    filterList?: boolean ;
    filterTask?: boolean ;
}
