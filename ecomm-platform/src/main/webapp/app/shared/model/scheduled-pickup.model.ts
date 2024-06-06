import dayjs from 'dayjs';

export interface IScheduledPickup {
  id?: number;
  shopperId?: number | null;
  orderId?: number | null;
  date?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IScheduledPickup> = {};
