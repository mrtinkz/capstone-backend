export interface IPricingSummary {
  id?: number;
  msrp?: number;
  taxesAndFees?: number;
  incentives?: number | null;
  tradeInEstimate?: number | null;
  subscriptionServices?: number | null;
  protectionPlan?: number | null;
}

export const defaultValue: Readonly<IPricingSummary> = {};
