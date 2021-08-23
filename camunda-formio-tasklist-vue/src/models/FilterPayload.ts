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
        taskVariables?: Array<object>;
        processVariables?: Array<object>;
        caseInstanceVariables?: Array<object>;
        orQueries?: Array<object>
    };
    resourceType: string;
}
