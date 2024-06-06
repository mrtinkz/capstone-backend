export interface IDocumentBlob {
  id?: number;
  postPurchaseActivityId?: number | null;
  name?: string | null;
  mimeType?: string | null;
  dataContentType?: string | null;
  data?: string | null;
}

export const defaultValue: Readonly<IDocumentBlob> = {};
