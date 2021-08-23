import { FilterPayload } from "../models/FilterPayload";

const compareFn = (a: FilterPayload,b: FilterPayload) => {
  const priority1=a?.properties?.priority||0;
  const priority2=b?.properties?.priority||0;
  return priority1-priority2;
};

export const sortByPriorityList = (filterList: FilterPayload[])=>{
  return filterList.sort(compareFn);
};
