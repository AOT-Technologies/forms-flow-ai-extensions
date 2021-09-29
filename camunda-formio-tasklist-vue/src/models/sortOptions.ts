export interface TaskListSortType {
  sortBy: string;
  sortOrder: string;
  label: string;
}
export const TASK_FILTER_LIST_DEFAULT_PARAM_CREATED: TaskListSortType = {
  sortOrder: "desc",
  label: "Created",
  sortBy: "created",
};

export const TASK_SORT_DEFAULT_DUE_DATE: TaskListSortType = {
  sortOrder: "desc",
  label: "Due Date",
  sortBy: "dueDate",
};

export const TASK_SORT_DEFAULT_FOLLOW_UP_DATE: TaskListSortType = {
  sortOrder: "desc",
  label: "Follow-up Date",
  sortBy: "followUpDate",
};

export const TASK_SORT_DEFAULT_PARAM_NAME: TaskListSortType = {
  sortOrder: "desc",
  label: "Task Name",
  sortBy: "name",
};

export const TASK_SORT_DEFAULT_ASSINGEE: TaskListSortType = {
  sortOrder: "desc",
  label: "Assignee",
  sortBy: "assignee",
};

export const TASK_SORT_DEFAULT_PRIORITY: TaskListSortType = {
  sortOrder: "desc",
  label: "Priority",
  sortBy: "priority",
};

export enum SORT_ORDER {
  ASCENDING = "asc",
  DESCENDING = "desc"
}

// export const TASK_SORTING_FULL_LIST = [
//   TASK_FILTER_LIST_DEFAULT_PARAM_CREATED,
//   TASK_SORT_DEFAULT_DUE_DATE,
//   TASK_SORT_DEFAULT_FOLLOW_UP_DATE,
//   TASK_SORT_DEFAULT_PARAM_NAME,
//   TASK_SORT_DEFAULT_ASSINGEE,
//   TASK_SORT_DEFAULT_PRIORITY
// ];

export const TASK_SORTING_FULL_LIST = [
  {
    "sortOrder": "desc", "label": "Created", "sortBy": "created" 
  },
  {
    "sortOrder": "desc", "label": "Due Date", "sortBy": "dueDate" 
  },
  {
    "sortOrder": "desc", "label": "Follow-up Date", "sortBy": "followUpDate" 
  },
  {
    "sortOrder": "desc", "label": "Task Name", "sortBy": "name" 
  },
  {
    "sortOrder": "desc", "label": "Assignee", "sortBy": "assignee" 
  },
  {
    "sortOrder": "desc", "label": "Priority", "sortBy": "priority" 
  }
];
