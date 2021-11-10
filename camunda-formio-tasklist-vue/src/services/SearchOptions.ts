import {
  SearchOptionPayload, SearchQueryPayload 
} from "../models";

export const FilterSearchTypes = {
  VARIABLES: "variables",
  STRING: "string",
  DATE: "date",
  NORMAL: "normal",
};

export const QUERY_TYPES = {
  ALL: "ALL",
  ANY: "ANY",
};

export const taskSearchFilters: SearchOptionPayload[] = [
  {
    label: "Task Variables",
    key: "taskVariables",
    compares: ["=", ">", ">=", "!=", "<", "<="],
    values: ["eq", "gt", "gte", "neq", "lt", "lte"],
    type: FilterSearchTypes.VARIABLES,
    variable: "",
    value: "",
    operator: ">",
  },
  {
    label: "Process Variables",
    key: "processVariables",
    compares: ["=", ">", ">=", "!=", "<", "<="],
    values: ["eq", "gt", "gte", "neq", "lt", "lte"],
    type: FilterSearchTypes.VARIABLES,
    variable: "",
    value: "",
    operator: ">",
  },
  {
    label: "Process Definition Name",
    key: "processDefinitionName",
    compares: ["like", "="],
    values: ["processDefinitionNameLike", "processDefinitionName"],
    type: FilterSearchTypes.STRING,
    operator: "like",
    value: "",
  },
  {
    label: "Assignee",
    compares: ["like", "="],
    key: "assignee",
    values: ["assigneeLike", "assignee"],
    type: FilterSearchTypes.STRING,
    value: "",
    operator: "like",
  },
  {
    label: "Candidate Group",
    compares: ["="],
    key: "candidateGroup",
    values: ["candidateGroup"],
    type: FilterSearchTypes.NORMAL,
    value: "",
    operator: "=",
  },
  {
    label: "Candidate User",
    compares: ["="],
    key: "candidateUser",
    values: ["candidateUser"],
    type: FilterSearchTypes.NORMAL,
    value: "",
    operator: "=",
  },
  {
    label: "Name",
    compares: ["like", "="],
    key: "name",
    values: ["nameLike", "name"],
    type: FilterSearchTypes.STRING,
    value: "",
    operator: "like",
  },
  {
    label: "Description",
    compares: ["like", "="],
    key: "description",
    values: ["descriptionLike", "description"],
    type: FilterSearchTypes.STRING,
    value: "",
    operator: "like",
  },
  {
    label: "Priority",
    compares: ["="],
    key: "priority",
    values: ["priority"],
    type: FilterSearchTypes.NORMAL,
    value: "",
    operator: "=",
  },
  {
    label: "Due Date",
    compares: ["before", "after"],
    key: "due",
    values: ["dueBefore", "dueAfter"],
    type: FilterSearchTypes.DATE,
    value: "",
    operator: "before",
  },
  {
    label: "Follow up Date",
    compares: ["before", "after"],
    key: "followUp",
    values: ["followUpBefore", "followUpAfter"],
    type: FilterSearchTypes.DATE,
    value: "",
    operator: "before",
  },
  {
    label: "Created",
    compares: ["before", "after"],
    key: "created",
    values: ["createdBefore", "createdAfter"],
    type: FilterSearchTypes.DATE,
    value: "",
    operator: "before",
  },
];

export const getVariableOperator = (operator: string) => {
  switch (operator) {
  case "=":
    return "eq";
  case ">":
    return "gt";
  case ">=":
    return "gteq";
  case "!=":
    return "neq";
  case "<":
    return "lt";
  case "<=":
    return "lteq";
  case "like":
    return "like";
  default:
  }
};

export const FILTER_OPERATOR_TYPES = {
  EQUAL: "=",
  LIKE: "like",
  BEFORE: "before",
  AFTER: "after",
};

export const FILTER_COMPARE_OPTIONS = {
  [FilterSearchTypes.VARIABLES]: [
    ">",
    ">=",
    FILTER_OPERATOR_TYPES.EQUAL,
    "!=",
    "<",
    "<=",
    FILTER_OPERATOR_TYPES.LIKE,
  ],
  [FilterSearchTypes.DATE]: [
    FILTER_OPERATOR_TYPES.BEFORE,
    FILTER_OPERATOR_TYPES.AFTER,
  ],
  [FilterSearchTypes.STRING]: [
    FILTER_OPERATOR_TYPES.EQUAL,
    FILTER_OPERATOR_TYPES.LIKE,
  ],
  [FilterSearchTypes.NORMAL]: [FILTER_OPERATOR_TYPES.EQUAL],
};

const getProcessedParamObject = (searchOption: SearchOptionPayload) => {
  const option: any= {
  };
  if(searchOption.operator===FILTER_OPERATOR_TYPES.EQUAL) {
    option[searchOption.key] = searchOption.value;
  }
  else if(searchOption.operator===FILTER_OPERATOR_TYPES.LIKE){
    option[`${searchOption.key}Like`] = `%${searchOption.value}%`;
  }else if(searchOption.operator===FILTER_OPERATOR_TYPES.BEFORE){
    option[`${searchOption.key}Before`] = searchOption.value;
  }else if(searchOption.operator===FILTER_OPERATOR_TYPES.AFTER){
    option[`${searchOption.key}After`] = searchOption.value;
  }

  return option;
};

export const isVariableTypeAvailable = (filterSelections: SearchOptionPayload[])=>{
  return filterSelections.some(filter=>filter.type===FilterSearchTypes.VARIABLES);
};

export const searchValueObject = (searchValue: any, operator: any) => {
  if (operator === FILTER_OPERATOR_TYPES.EQUAL) {
    return searchValue;
  } else if (operator === FILTER_OPERATOR_TYPES.LIKE) {
    return `${searchValue}Like`;
  } else if (operator === FILTER_OPERATOR_TYPES.BEFORE) {
    return `${searchValue}Before`;
  } else if (operator === FILTER_OPERATOR_TYPES.AFTER) {
    return `${searchValue}After`;
  }
};

export const getDeletedVariableIndex = (
  deletequery: any,
  selectetedSearchList: any,
  key: string,
  queryList: any
) => {
  for (let i = 0; i < queryList[key].length; i++) {
    if (
      queryList[key][i]["name"] === deletequery["name"]
      && queryList[key]["value"] === deletequery["value"]
    ) {
      queryList[key].splice(i, 1);
    }
  }
  return queryList;
};

export const getFormattedParams = (searchOptionList: SearchOptionPayload[], searchQueryType: any, variableNameIgnoreCase: string, variableValueIgnoreCase: string)=>{
  let resultList={
  };
  let paramList: any={
  };
  let isParamsHasValue=false;
  if(searchOptionList.length===0){
    return paramList;
  }
  paramList={
    processVariables: [],taskVariables:[]
  };

  searchOptionList.forEach((searchOption)=>{
    switch(searchOption.type){
    case FilterSearchTypes.VARIABLES:
      if(searchOption?.value && searchOption?.variable){
        isParamsHasValue=true;
        paramList[searchOption.key].push({
          name: searchOption.variable, operator: getVariableOperator(searchOption.operator), value: searchOption.value
        });
      }
      break;
    case FilterSearchTypes.STRING:
    case FilterSearchTypes.NORMAL:
    case FilterSearchTypes.DATE:
      if(searchOption?.value){
        isParamsHasValue=true;
        const param= getProcessedParamObject(searchOption);
        paramList ={
          ...paramList,...param
        };
      }
      break;
    default:
    }
  });


  const isVariableType = isVariableTypeAvailable(searchOptionList);
  if(isVariableType){
    paramList={
      ...paramList,...{
        variableNamesIgnoreCase: variableNameIgnoreCase,
        variableValuesIgnoreCase: variableValueIgnoreCase
      }
    };
  }
  if(searchQueryType===QUERY_TYPES.ALL){
    resultList=paramList;
  }else if(searchQueryType===QUERY_TYPES.ANY){
    resultList={
      orQueries:[paramList]
    };
  }
  return isParamsHasValue?resultList:{
  };
};

