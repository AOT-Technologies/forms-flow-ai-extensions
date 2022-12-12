import {
  TaskListSortType 
} from "../models";
export interface Payload {
  active?: boolean;
  sorting: TaskListSortType[];
  orQueries?: Array<any>;
  maxResults?: number;
  firstResult?: number;
}
