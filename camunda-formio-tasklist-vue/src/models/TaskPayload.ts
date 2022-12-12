export interface TaskPayload {
  id?: string;
  name?: string;
  assignee?: string;
  applicationId?: string;
  taskProcess?: string;
  created?: string;
  due?: Date | null | string;
  followUp?: Date | null| string;
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
  component: any;
  type: string;
}

export interface UserSearchListLabelPayload {
  searchType: string;
  label: string;
}
