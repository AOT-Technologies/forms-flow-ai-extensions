import { httpGETRequest, httpPOSTRequest } from "./axios";

export const formApplicationSubmit = (
  Apiurl: string,
  data: any,
  token: string
) => {
  return httpPOSTRequest(Apiurl + "/application/create", data, token);
};

export const getformHistoryApi = (
  ApiUrl: string,
  applicationId: string,
  token: string
) => {
  return httpGETRequest(
    ApiUrl + "/application/" + applicationId + "/history",
    {},
    token
  );
};
