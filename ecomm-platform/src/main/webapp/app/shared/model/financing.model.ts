export interface IFinancing {
  id?: number;
  provider?: string;
  interestRate?: number;
  loanTerm?: number;
  downPayment?: number | null;
  orderId?: number | null;
}

export const defaultValue: Readonly<IFinancing> = {};
