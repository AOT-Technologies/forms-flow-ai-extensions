export interface FilterPayload {
    id: string;
    name: string;
    owner?: string;
    properties: {
        color: string;
        priority: number;
        refresh: boolean;
        showUndefinedVariable: boolean;
    };
    query: Object;
    resourceType: string;
};
