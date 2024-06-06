export interface IShopper {
  id?: number;
  userId?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
}

export const defaultValue: Readonly<IShopper> = {};
