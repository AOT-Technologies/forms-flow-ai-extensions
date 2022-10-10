
import {
  ApplicationCreatePayload
} from "../models/ApplicationCreatePayload";
import axios from "axios";
import {
  httpPOSTRequest 
} from "./Axios";
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
  return axios.get(
    ApiUrl + "/application/" + applicationId + "/history",{
      headers:{
        Authorization:`Bearer ${token}`
      }
    });
};
