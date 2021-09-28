import {
  FilterPayload 
} from "../models/FilterPayload";
import {
  TaskPayload 
} from "../models/TaskPayload";

export const getTaskFromList = (tasks: TaskPayload[], taskId: string) => {
  const task = tasks.find(task => task.id === taskId);
  return task!;
};

export const findFilterKeyOfAllTask = (array: FilterPayload[], value: string) => {
  for (let i = 0; i < array.length; i++) {
    if (array[i]?.["name"] === value) {
      return array[i]["id"];
    }
  }
  return array[0]["id"];
};

export const getUserName = () => {
  const userDetails: any = localStorage.getItem('UserDetails');
  const userDetailsObj: any = JSON.parse(userDetails);
  /* eslint-disable camelcase */
  return userDetailsObj?.preferred_username;
};
