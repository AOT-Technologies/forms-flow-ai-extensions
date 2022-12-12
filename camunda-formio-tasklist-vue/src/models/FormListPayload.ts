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

export interface FormioSubmissionPayload {
    _id: string;
    access?: string[];
    created: Date;
    data?:  Record<string, any>;
    externalIds?: string[];
    form: string;
    metadata?: Record<string, any>;
    owner: string;
    roles?: string[];
}
