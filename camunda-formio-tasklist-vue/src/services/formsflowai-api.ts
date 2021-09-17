import {
  httpGETRequest, httpPOSTRequest 
} from "./axios";
import {
  ApplicationCreatePayload
} from "../models/ApplicationCreatePayload";

export const formApplicationSubmit = (
  Apiurl: string,
  data: ApplicationCreatePayload,
  token: string
) => {
  return httpPOSTRequest(Apiurl + "/application/create", data, token);
};

export const getformHistoryApi = async (
  ApiUrl: string,
  applicationId: string,
  token: string
) => {
  return httpGETRequest(
    ApiUrl + "/application/" + applicationId + "/history",
    {
    },
    token
  );
};
