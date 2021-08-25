import axios from "axios";

export function bpmAxios (bearerToken: string, CamundaUrl: any) {
  return axios.create({
    baseURL: CamundaUrl,
    withCredentials: false,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
      Authorization: `Bearer ${bearerToken}`,
    },
  });
}

export const httpPOSTRequest = (
  url: string,
  data: any,
  bearerToken: string,
  isBearer = true
) => {
  return axios.post(url, data, {
    headers: {
      Authorization: isBearer ? `Bearer ${bearerToken}` : bearerToken,
    },
  });
};

export const httpGETRequest = (
  url: string,
  data: any,
  bearerToken: string,
  isBearer = true
) => {
  return axios.get(url, {
    params: data,
    headers: {
      Authorization: isBearer ? `Bearer ${bearerToken}` : bearerToken,
    },
  });
};
