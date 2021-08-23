const compareFn = (a: any,b: any) => {
  const priority1=a?.properties?.priority||0;
  const priority2=b?.properties?.priority||0;
  return priority1-priority2;
}

export const sortByPriorityList = (filterList: any[])=>{
  return filterList.sort(compareFn);
}
