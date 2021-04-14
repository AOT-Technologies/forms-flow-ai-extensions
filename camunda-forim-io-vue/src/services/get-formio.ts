export const getFormDetails = (formioUrl: string, formIOProjectUrl: string) => {
  const domain = (formioUrl.split("://")[1]).split('/')[0]
  const replacedomain = formIOProjectUrl.split("//")[1]
  formioUrl = formioUrl.replace(domain, replacedomain)
  const formArr = formioUrl.split("/");
  const formId: string = formArr[4];
  const submissionId: string = formArr[6];

  return {formioUrl, formId, submissionId};
}

export const getFormIdandSubmissionId = (formioUrl: string) => {
  const formArr = formioUrl.split("/");
  const formId: string = formArr[4];
  const submissionId: string = formArr[6];

  return {formId, submissionId};
}
