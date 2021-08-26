export interface FormListFieldsPayload {
    key: string;
    sortable?: boolean;
    label?: string;
}

export interface FormListItemsPayload {
    formId?: string;
    formName?: string;
    processKey?: string;
}
