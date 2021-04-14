export const getTaskFromList = (tasks: any[], taskId: string) =>{
  const task = tasks.find(task=>task.id===taskId);
  return task;
}

export const findFilterKeyOfAllTask = (array: string|any[], key: string|number, value: any) => {
  for (let i = 0; i < array.length; i++) {
    if (array[i][key] === value) {
      return array[i]["id"];
    }
  }
  return array[0]["id"];
}

export const TASK_FILTER_LIST_DEFAULT_PARAM =  [{sortBy: "created",
  sortOrder: "desc",
  label:"Created"
}];

export const sortingList = [
  {"sortOrder": "desc" ,"label": "Created" , "sortBy": "created"},
  {"sortOrder": "desc" ,"label": "Due Date" , "sortBy": "dueDate"},
  {"sortOrder": "desc" ,"label": "Follow-up Date" , "sortBy": "followUpDate"},
  {"sortOrder": "desc" ,"label": "Task Name" , "sortBy": "name"},
  {"sortOrder": "desc" ,"label": "Assignee" , "sortBy": "assignee"},
  {"sortOrder": "desc", "label": "Priority", "sortBy": "priority"}
]

export const decodeTokenValues = (token: any, userName: any, formIOUserRoles: any) =>{
  const decodeToken = JSON.parse(atob(token.split('.')[1]))
  localStorage.setItem("UserDetails", JSON.stringify(decodeToken))
  userName = !userName ? decodeToken && decodeToken["preferred_username"] : userName
  const userEmail = decodeToken["email"] || "external"
  const resourceacess = decodeToken && decodeToken["resource_access"]
  let Resourceaud: any = null;
  if(resourceacess && resourceacess[decodeToken["aud"]]&&resourceacess[decodeToken["aud"][0]]==="forms-flow-web"){
    Resourceaud= resourceacess[decodeToken["aud"][0]];
  }
  else if(resourceacess){
    Resourceaud = resourceacess['forms-flow-web']
  }
  else{
    console.error("Unable to set formio Userroles");
  }
  
  if(Resourceaud && Array.isArray(Resourceaud["roles"]) && Resourceaud["roles"].length){
    formIOUserRoles = !formIOUserRoles ? Resourceaud["roles"] : formIOUserRoles
  }
  else{
    console.error("Unable to set formioUserRoles")
  }
  return {userName, userEmail, formIOUserRoles};
}

export const getFormattedDateAndTime = (date: Date)=>{

  return new Date(date).toLocaleDateString('en-us',  {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric',hour: '2-digit', minute: '2-digit', hour12: true});

};

export const getLocalDateTime = (date: any) => {
  return date?new Date(date.replace(' ','T')+'Z').toLocaleString(): "-";
}
