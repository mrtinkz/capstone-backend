import { PostPurchaseActivityType } from 'app/shared/model/enumerations/post-purchase-activity-type.model';
import { PostPurchaseActivityStatus } from 'app/shared/model/enumerations/post-purchase-activity-status.model';

export interface IPostPurchaseActivity {
  id?: number;
  orderId?: number | null;
  activityType?: keyof typeof PostPurchaseActivityType;
  status?: keyof typeof PostPurchaseActivityStatus;
}

export const defaultValue: Readonly<IPostPurchaseActivity> = {};
