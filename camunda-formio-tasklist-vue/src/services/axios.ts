import axios from "axios";

export function bpmAxios (bearerToken: string, CamundaUrl: string) {
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

export function bpmAxiosWithHal (bearerToken: string, CamundaUrl: string) {
  return axios.create({
    baseURL: CamundaUrl,
    withCredentials: false,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Accept": "application/hal+json",
      Authorization: `Bearer ${bearerToken}`,
    },
  });
}

export const httpPOSTRequest = async (
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

export const httpGETRequest = async (
  url: string,
  data: any,
  bearerToken: string,
  isBearer = true
) => {
  const response = await axios.get(url, {
    params: data,
    headers: {
      Authorization: isBearer ? `Bearer ${bearerToken}` : bearerToken,
    },
  });
  return response?.data;

};
