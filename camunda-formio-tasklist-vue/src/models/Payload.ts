import {
  TaskListSortType 
} from "../services/";
export interface Payload {
  active?: boolean;
  sorting: TaskListSortType[];
  orQueries?: Array<object>;
  maxResults?: number;
  firstResult?: number;
}
