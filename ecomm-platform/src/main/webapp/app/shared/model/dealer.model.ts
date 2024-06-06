export interface IDealer {
  id?: number;
  name?: string;
  address?: string | null;
  city?: string | null;
  state?: string | null;
  zipCode?: string | null;
  website?: string | null;
}

export const defaultValue: Readonly<IDealer> = {};
