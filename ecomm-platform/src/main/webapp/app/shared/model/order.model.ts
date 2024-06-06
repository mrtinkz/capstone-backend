import dayjs from 'dayjs';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  shopperId?: number | null;
  status?: keyof typeof OrderStatus;
  scheduledDeliveryDate?: dayjs.Dayjs | null;
  vehicleId?: number;
  dealerId?: number;
}

export const defaultValue: Readonly<IOrder> = {};
