import {
  bpmAxios, bpmAxiosGet, bpmAxiosWithHal, engine,
} from "../services";

export const CamundaRest = {
  async getProcessDefinitions (bearerToken: string, CamundaUrl: string) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition`
    );
  },

  async getProcessDefinitionById (
    bearerToken: string,
    processDefinitionId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition/${processDefinitionId}`
    );
  },

  async startProcess (
    bearerToken: string,
    processDefinitionKey: string,
    values: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/process-definition/key/${processDefinitionKey}/start`,
      values
    );
  },

  async getTaskById (bearerToken: string, taskId: string, CamundaUrl: string) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/task/${taskId}`
    );
  },

  async updateTasksByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string,
    values: any
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).put(
      `/${engine}/task/${taskId}`,
      values
    );
  },

  async getTaskGroupByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/task/${taskId}/identity-links?type=candidate`
    );
  },

  async createTaskGroupByID (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string,
    values: any
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/identity-links`,
      values
    );
  },

  async deleteTaskGroupByID (
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

  async complete (
    bearerToken: string,
    taskId: string,
    values: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/complete`,
      values
    );
  },

  async claim (
    bearerToken: string,
    taskId: string,
    values: any,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/claim`,
      values
    );
  },

  async unclaim (bearerToken: string, taskId: string, CamundaUrl: string) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/unclaim`
    );
  },

  async setassignee (
    bearerToken: string,
    taskId: string,
    values: any,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/assignee`,
      values
    );
  },

  async getVariablesByTaskId (
    bearerToken: string,
    taskId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/task/${taskId}/variables`
    );
  },

  async getVariablesByProcessId (
    bearerToken: string,
    processInstanceId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-instance/${processInstanceId}/variables`
    );
  },

  async getUsers (bearerToken: string, CamundaUrl: string) {
    return await bpmAxios(bearerToken, CamundaUrl).get(`/${engine}/user`);
  },

  async getUsersByMemberGroups (
    bearerToken: string,
    CamundaUrl: string,
    group: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?memberOfGroup=${group}`
    );
  },

  async getUsersByFirstName (
    bearerToken: string,
    CamundaUrl: string,
    firstName?: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?firstNameLike=%${firstName}%`
    );
  },

  async getUsersByLastName (
    bearerToken: string,
    CamundaUrl: string,
    lastName?: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?lastNameLike=%${lastName}%`
    );
  },

  async getUsersByEmail (
    bearerToken: string,
    CamundaUrl: string,
    email?: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?emailLike=%${email}%`
    );
  },

  async getUsersByFirstNameGroups (
    bearerToken: string,
    CamundaUrl: string,
    firstName: string | null,
    group: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?memberOfGroup=${group}&firstNameLike=%${firstName}%`
    );
  },

  async getUsersByLastNameGroups (
    bearerToken: string,
    CamundaUrl: string,
    lastName: string | null,
    group: string
  ) {

    const params = {
      lastNameLike:`%${lastName}%`,memberOfGroup:group
    };

    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user`,{
        params
      }
    );
  },

  async getUsersByEmailGroups (
    bearerToken: string,
    CamundaUrl: string,
    email: string | null,
    group: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/user?emailLike=%${email}%&memberOfGroup=${group}`
    );
  },

  async filterList (bearerToken: string, CamundaUrl: string) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/filter?resourceType=Task`
    );
  },

  async filterTaskList (
    bearerToken: string,
    filterId: string,
    values: any,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/filter/${filterId}/list`,
      values
    );
  },

  async filterTaskListPagination (
    bearerToken: string,
    filterId: string,
    values: any,
    firstResults: number,
    maxResults: number,
    CamundaUrl: string
  ) {
    return await bpmAxiosWithHal(bearerToken, CamundaUrl).post(
      `/${engine}/filter/${filterId}/list?firstResult=${firstResults}&maxResults=${maxResults}`,
      values
    );
  },

  async formTaskSubmit (
    bearerToken: string,
    taskId: string,
    values: any,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).post(
      `/${engine}/task/${taskId}/submit-form`,
      values
    );
  },

  async listForms (bearerToken: string, CamundaUrl: string, query: any) {
    return await bpmAxiosGet(bearerToken, CamundaUrl).get(`/form?pageNo=${query.page}&limit=${query.limit}`);
  },

  async getProcessDiagramXML (
    bearerToken: string,
    processDefinitionId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-definition/${processDefinitionId}/xml`
    );
  },

  async getProcessActivity (
    bearerToken: string,
    processInstanceId: string,
    CamundaUrl: string
  ) {
    return await bpmAxios(bearerToken, CamundaUrl).get(
      `/${engine}/process-instance/${processInstanceId}/activity-instances`
    );
  },

};
