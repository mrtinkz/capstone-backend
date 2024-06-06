export interface IVehicle {
  id?: number;
  vin?: string;
  make?: string;
  model?: string;
  modelYear?: number;
  dealerId?: number;
  pricingSummaryId?: number | null;
  vehicleOptionsId?: number | null;
}

export const defaultValue: Readonly<IVehicle> = {};
