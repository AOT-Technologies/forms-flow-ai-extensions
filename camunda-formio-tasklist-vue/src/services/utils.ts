import { FilterPayload } from "../models/FilterPayload";
import { TaskPayload } from "../models/TaskPayload";

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

export const TASK_FILTER_LIST_DEFAULT_PARAM = [{
  sortBy: "created",
  sortOrder: "desc",
  label: "Created"
}];

export const sortingList = [
  { "sortOrder": "desc", "label": "Created", "sortBy": "created" },
  { "sortOrder": "desc", "label": "Due Date", "sortBy": "dueDate" },
  { "sortOrder": "desc", "label": "Follow-up Date", "sortBy": "followUpDate" },
  { "sortOrder": "desc", "label": "Task Name", "sortBy": "name" },
  { "sortOrder": "desc", "label": "Assignee", "sortBy": "assignee" },
  { "sortOrder": "desc", "label": "Priority", "sortBy": "priority" }
];

export const getUserName = () => {
  const userDetails: any = localStorage.getItem('UserDetails');
  const userDetailsObj: any = JSON.parse(userDetails);
  /* eslint-disable camelcase */
  return userDetailsObj?.preferred_username;
};
