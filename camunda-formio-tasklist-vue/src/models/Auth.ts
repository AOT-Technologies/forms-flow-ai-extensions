export interface Roles {
  "roles": string[];
}

export interface ResourceAccess {
  "account": Roles;
  "forms-flow-web": Roles;
}

export interface UserDetailFromToken {
"azp": string;
"email": string;
"email_verified": boolean;
"family_name": string;
"given_name": string;
"groups": string[];
"iss": string;
"name": string;
"preferred_username": string;
"resource_access": ResourceAccess;
"role": string[];
}
