export interface TaskPayload {
  id?: string;
  name?: string;
  assignee?: string;
  applicationId?: string;
  taskProcess?: string;
  created?: string;
  due?: string|null;
  followUp?: string|null;
  delegationState?: string;
  description?: string;
  executionId?: string;
  owner?: string;
  parentTaskId?: string;
  priority?: string;
  processDefinitionId?: string;
  processInstanceId?: string;
  taskDefinitionKey?: string;
  caseExecutionId?: string;
  caseInstanceId?: string;
  caseDefinitionId?: string;
  suspended?: string;
  formKey?: string;
  tenantId?: string;
}

export interface CustomEventPayload {
  actionType: string;
  component: object;
  type: string;
}

export interface UserSearchListLabelPayload {
  searchType: string;
  label: string;
}
