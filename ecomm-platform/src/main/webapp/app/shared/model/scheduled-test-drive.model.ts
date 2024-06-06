import dayjs from 'dayjs';

export interface IScheduledTestDrive {
  id?: number;
  shopperId?: number | null;
  date?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IScheduledTestDrive> = {};
