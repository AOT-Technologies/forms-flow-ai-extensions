import axios from "axios";
export const getFormDetails = (formioUrl: string, formIOProjectUrl: string) => {
  const a = document.createElement("a");
  const b = document.createElement("a");
  a.href = formioUrl;
  b.href = formIOProjectUrl;
  formioUrl = formioUrl.replace(
    a.protocol + "//" + a.host,
    b.protocol + "//" + b.host + b.pathname
  );

  // Regex Extracts formId from */form/formId/submission/*
  const formId = formioUrl.match(/(?<=\/form\/)(.*)(?=\/submission)/)?.[0] || "";
  // Regex Extracts submissionId from */submission/submissionId
  const submissionId = formioUrl.match(/(?<=\/submission\/)(.*)(?=)/)?.[0] || "";
  return {
    formioUrl, formId, submissionId
  };
};

export const getFormioToken = (url: string, token: string, done: Function) => {
   axios.get(`${url}/formio/roles`,{
    headers:{
      Authorization: `Bearer ${token}` 
    }
   }).then(res=>{
      if(res.headers['x-jwt-token']){
        const formioToken = res.headers['x-jwt-token']
        done(null,formioToken)
      }else{
        done('no token found',null)
      }
   }).catch(err=>{
    done(err,null)
   })
}




