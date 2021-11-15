export interface SearchOptionPayload {
    label: string;
    compares: Array<string>;
    key: string;
    values: Array<string>;
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
    taskVariables?: Array<VariablePayload>;
    processVariables?: Array<VariablePayload>;
}

export interface SearchQueryPayload {
    taskVariables?: Array<VariablePayload>;
    processVariables?: Array<VariablePayload>;
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
