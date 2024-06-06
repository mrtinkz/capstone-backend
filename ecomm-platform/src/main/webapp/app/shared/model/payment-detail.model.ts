import dayjs from 'dayjs';
import { PaymentType } from 'app/shared/model/enumerations/payment-type.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';

export interface IPaymentDetail {
  id?: number;
  orderId?: number | null;
  paymentType?: keyof typeof PaymentType;
  paymentAmount?: number;
  paymentDate?: dayjs.Dayjs;
  cardType?: keyof typeof CardType | null;
  cardNumber?: string | null;
  cardHolderName?: string | null;
  cardExpirationDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPaymentDetail> = {};
