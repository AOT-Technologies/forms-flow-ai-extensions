export interface SearchOptionPayload {
    index?: number;
    label: string;
    compares: string[];
    key: string;
    values: string[];
    type: string;
    variable?: string;
    name?: string;
    operator: string;
    value?: string;
}

export interface VariablePayload {
    name: string;
    operator: string;
    value: string;
}

export interface TaskVariablesPayload {
    taskVariables?: VariablePayload[];
    processVariables?: VariablePayload[];
}

export interface SearchQueryPayload {
    taskVariables?: VariablePayload[];
    processVariables?: VariablePayload[];
    variableNamesIgnoreCase?: boolean;
    variableValuesIgnoreCase?: boolean;
    dueBefore?: string;
    dueAfter?: string;
    followUpBefore?: string;
    followUpAfter?: string;
    createdBefore?: string;
    createdAfter?: string;
    nameLike?: string;
    name?: string;
    priority?: string;
    descriptionLike?: string;
    description?: string;
    assigneeLike?: string;
    assignee?: string;
    processDefinitionNameLike?: string;
    processDefinitionName?: string;
    candidateGroup?: string;
}
