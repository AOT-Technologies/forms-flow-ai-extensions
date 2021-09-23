export interface TaskListSortType {
  sortBy: string;
  sortOrder: string;
  label: string;
}
export const TASK_FILTER_LIST_DEFAULT_PARAM_CREATED: TaskListSortType = {
  sortBy: "created",
  sortOrder: "desc",
  label: "Created"
};

export const TASK_SORT_DEFAULT_DUE_DATE: TaskListSortType = {
  sortBy: "dueDate",
  sortOrder: "desc",
  label: "Due Date"
};

export const TASK_SORT_DEFAULT_FOLLOW_UP_DATE: TaskListSortType = {
  sortBy: "followUpDate",
  sortOrder: "desc",
  label: "Follow-up Date"
};

export const TASK_SORT_DEFAULT_PARAM_NAME: TaskListSortType = {
  sortBy: "name",
  sortOrder: "desc",
  label: "Task Name"
};

export const TASK_SORT_DEFAULT_ASSINGEE: TaskListSortType = {
  sortBy: "assignee",
  sortOrder: "desc",
  label: "Assignee"
};

export const TASK_SORT_DEFAULT_PRIORITY: TaskListSortType = {
  sortBy: "priority",
  sortOrder: "desc",
  label: "Priority"
};

export enum SORT_ORDER {
  ASCENDING = "asc",
  DESCENDING = "desc"
}
