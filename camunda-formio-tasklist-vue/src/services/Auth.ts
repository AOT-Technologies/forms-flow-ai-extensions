/**
* checks the logged in user belongs to the reviewers group
* @param allowedGroups allowed groups.
* @param formIOUserRoles logged in user keyclock groups.
* @returns if the logged in user is authorized or not
*/
export function isAllowedUser(allowedGroup: string, formIOUserRoles?: string): boolean {
  const UserRoles = formIOUserRoles?.split(",");
  return UserRoles ? UserRoles.includes(allowedGroup) : false;
}
