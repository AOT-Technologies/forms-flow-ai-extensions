export const engine = "engine-rest";
export const socketUrl = "forms-flow-bpm-socket";
export const formExt = "engine-rest-ext";
export const reviewerGroup = "formsflow/formsflow-reviewer";
export const ALL_FILTER = "All tasks";
export const SEARCH_USERS_BY = [
  {
    'searchType': 'firstName', 'label': 'Search by First Name'
  },
  {
    'searchType': 'lastName', 'label': 'Search by Last Name'
  },
  {
    'searchType': 'email', 'label': 'Search by Email'
  },
];
export const FORMLIST_FIELDS = [
  {
    key: "formName", sortable: true 
  },
  {
    key: "operations" 
  }
];

 const porjectUrl = "https://app2.aot-technologies.com/formio"||process.env.VUE_APP_FORM_IO_PROJECT_URL|| 'http://127.0.0.1:3000'
 const apiUrl ="https://app2.aot-technologies.com/formio"|| process.env.VUE_APP_FORM_IO_API_URL|| 'http://127.0.0.1:3000'

export const AppConfig ={
  porjectUrl,apiUrl
}
