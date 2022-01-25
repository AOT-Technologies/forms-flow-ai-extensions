export const getFormDetails = (formioUrl: string, formIOProjectUrl: string) => {
  const a = document.createElement("a");
  const b = document.createElement("a");
  a.href = formioUrl;
  b.href = formIOProjectUrl;
  formioUrl = formioUrl.replace(
    a.protocol + "//" + a.host,
    b.protocol + "//" + b.host
  );
    // Regex Extracts formId from */form/formId/submission/*
  const formId = formioUrl.match(/(?<=\/form\/)(.*)(?=\/submission)/)[0];
  // Regex Extracts submissionId from */submission/submissionId
  const submissionId = formioUrl.match(/(?<=\/submission\/)(.*)(?=)/)[0];
  return {
    formioUrl, formId, submissionId 
  };
};

export const getFormIdandSubmissionId = (formioUrl: string) => {
  const formArr = formioUrl.split("/");
  const formId: string = formArr[4];
  const submissionId: string = formArr[6];

  return {
    formId, submissionId 
  };
};
