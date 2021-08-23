export interface TaskPayload {
  id?: string;
  name?: string;
  assignee?: string;
  created?: string;
  due?: string;
  followUp?: string;
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
