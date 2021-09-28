import {
  UserDetailFromToken 
} from "../models";
/**
* checks the logged in user belongs to the reviewers group
* @param userDetails logged in userDetails from token.
* @param allowedGroups allowed groups.
* @returns if the logged in user is authorized or not
*/
export function isAllowedUser(userDetails: UserDetailFromToken, allowedGroup: string): boolean {
  return userDetails?.resource_access?.["forms-flow-web"]?.roles.includes(allowedGroup);
}
