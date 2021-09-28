export interface UserPayload {
    id?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
}

export interface UserListPayload {
    code?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
}

export enum SEARCH_OPTION_TYPE {
    FIRST_NAME = 'firstName',
    LAST_NAME = 'lastName',
    EMAIL = 'email',
}

/*Function to avoid boiler plate code for duplication UserListObject */
export const UserListObject = (user: UserPayload) => {
  return {
    code: user.id,
    email: user.email!,
    firstName: `${user.firstName!} ${user.lastName!}`,
    lastName: `${user.lastName!} ${user.firstName!}`,
  };
};
