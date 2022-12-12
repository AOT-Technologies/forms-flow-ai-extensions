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
    firstName: `${user.firstName}  ${user.lastName}`,
    lastName: `${user.firstName} ${user.lastName===null?"":user.lastName}`,
  };
};

export interface KeyCloakPayload {
  exp?: string;
  iat?: number;
  jti?: string;
  iss?: string;
  aud?: string[];
  sub?: string;
  typ?: string;
  azp?: string;
  "session_state": string;
  acr: string;
  "allowed-origins": string[];
  "realm_access"?: Record<string, any>;
  "resource_access"?: Record<string, any>;
  scope?: string;
  "email_verified"?: boolean;
  role?: string[];
  name?: string;
  groups?: string[];
  "preferred_username": string;
  locale?: string;
  "given_name"?: string;
  dashboards?: Record<string, any>;
  "family_name": string;
  email: string;
}
