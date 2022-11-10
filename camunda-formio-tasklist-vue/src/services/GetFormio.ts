import axios from "axios";
export const getFormDetails = (formioUrl: string, formIOProjectUrl: string) => {

  let formId: any;
  let submissionId: any;
  if (formioUrl) {
    let formString = "/form/";
    let submissionString = "/submission/";
    let firstPositionOfString = formioUrl.indexOf("/form/");
    let lastPositionOfString = formioUrl.indexOf("/submission");
    formId = formioUrl.substring(
      firstPositionOfString + formString.length,
      lastPositionOfString
    );
    let firstPositionOfSubmissionString =
    formioUrl.indexOf(submissionString) + submissionString.length;
    submissionId = formioUrl.substring(firstPositionOfSubmissionString);
  }
  const a = document.createElement("a");
  const b = document.createElement("a");
  a.href = formioUrl;
  b.href = formIOProjectUrl;
  formioUrl = formioUrl.replace(
    a.protocol + "//" + a.host,
    b.protocol + "//" + b.host + b.pathname
  );

   
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
      const formioToken = res.headers['x-jwt-token'];
      done(null,formioToken);
    }else{
      done('no token found',null);
    }
  }).catch(err=>{
    done(err,null);
  });
};




