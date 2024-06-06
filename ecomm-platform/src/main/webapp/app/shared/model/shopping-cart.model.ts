export interface IShoppingCart {
  id?: number;
  shopperId?: number | null;
  vehicleId?: number | null;
}

export const defaultValue: Readonly<IShoppingCart> = {};
