export interface SearchOptionPayload {
    label: string;
    compares: Array<string>;
    key: string;
    values: Array<string>;
    type: string;
    variable?: string;
    name: string;
    operator: string;
}
