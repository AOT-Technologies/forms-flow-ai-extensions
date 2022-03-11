import {
  FilterPayload, KeyCloakPayload, TaskPayload,
} from "../models";

export const getTaskFromList = (tasks: TaskPayload[], taskId: string) => {
  return tasks.find(task => task.id === taskId);
};

export const findFilterIdForDefaultFilterName = (array: FilterPayload[], value: string) => {
  for (let i = 0; i < array.length; i++) {
    if (array[i]?.["name"] === value) {
      return array[i]["id"];
    }
  }
  if(array?.length) {
    return array[0]["id"];
  }
  return '';
};

export const getUserName = () => {
  const userDetails: string | null = localStorage.getItem('UserDetails');
  if (userDetails) {
    const userDetailsObj: KeyCloakPayload = JSON.parse(userDetails);
    /* eslint-disable camelcase */
    return userDetailsObj?.preferred_username;
  }
};
