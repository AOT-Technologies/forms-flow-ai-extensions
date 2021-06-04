import { engine, formExt } from "../services/constants";
import { bpmAxios } from "../services/axios";

const CamundaRest = {
  getProcessDefinitions (bearerToken: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition`
    );
  },

  getProcessDefinitionById: (
    bearerToken: string,
    processDefinitionId: string,
    CamundaUrl: string
  ) => {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition/${processDefinitionId}`
    );
  },

  startProcess (
    bearerToken: string,
    processDefinitionKey: string,
    values: string,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/process-definition/key/${processDefinitionKey}/start`,
      values
    );
  },

  getTaskById (bearerToken: string, taskId: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).get(`/${engine}/task/${taskId}`);
  },

  updateTasksByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string,
    values: any
  ) {
    return bpmAxios(bearerToken, CamundaUrl).put(
      `/${engine}/task/${taskId}`,
      values
    );
  },

  getTaskGroupByID (bearerToken: string, taskId: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/task/${taskId}/identity-links?type=candidate`
    );
  },

  createTaskGroupByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string,
    values: any
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/identity-links`,
      values
    );
  },

  deleteTaskGroupByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string,
    values: any
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/identity-links/delete`,
      values
    );
  },

  complete (
    bearerToken: string,
    taskId: string,
    values: string,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/complete`,
      values
    );
  },

  claim (bearerToken: string, taskId: string, values: any, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/claim`,
      values
    );
  },

  unclaim (bearerToken: string, taskId: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/unclaim`
    );
  },

  setassignee (
    bearerToken: string,
    taskId: string,
    values: any,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/assignee`,
      values
    );
  },

  getVariablesByTaskId (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/task/${taskId}/variables`
    );
  },

  getVariablesByProcessId (
    bearerToken: string,
    processInstanceId: string,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-instance/${processInstanceId}/variables`
    );
  },

  getUsers (bearerToken: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).get(`/${engine}/user`)
  },


  getUsersByMemberGroups (
    bearerToken: string,
    CamundaUrl: string,
    group: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?memberOfGroup=${group}`
    );
  },

  getUsersByLastName (
    bearerToken: string,
    CamundaUrl: string,
    lastName: string | null,
    group: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?lastNameLike=%${lastName}%&memberOfGroup=${group}`
    );
  },

  filterList (bearerToken: string, CamundaUrl: string) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/filter?resourceType=Task&itemCount=false&sortBy=name&sortOrder=asc`
    );
  },

  filterTaskList (
    bearerToken: string,
    filterId: string,
    values: any,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/filter/${filterId}/list`,
      values
    );
  },

  filterTaskListPagination (
    bearerToken: string,
    filterId: string,
    values: any,
    firstResults: number,
    maxResults: number,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/filter/${filterId}/list?firstResult=${firstResults}&maxResults=${maxResults}`,
      values
    );
  },

  formTaskSubmit (
    bearerToken: string,
    taskId: string,
    values: object,
    CamundaUrl: string
  ) {
    return bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/submit-form`,
      values
    );
  },

  listForms (bearerToken: any, CamundaUrl: any) {
    return bpmAxios(bearerToken, CamundaUrl).get(`/${formExt}/form`);
  },

  getProcessDiagramXML (
    bearerToken: any,
    processDefinitionId: string,
    CamundaUrl: any
  ) {
    return bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition/${processDefinitionId}/xml`
    );
  },
};

export default CamundaRest;
