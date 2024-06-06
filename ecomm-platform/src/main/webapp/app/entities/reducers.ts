import shopper from 'app/entities/shopper/shopper.reducer';
import vehicle from 'app/entities/vehicle/vehicle.reducer';
import dealer from 'app/entities/dealer/dealer.reducer';
import scheduledTestDrive from 'app/entities/scheduled-test-drive/scheduled-test-drive.reducer';
import scheduledPickup from 'app/entities/scheduled-pickup/scheduled-pickup.reducer';
import postPurchaseActivity from 'app/entities/post-purchase-activity/post-purchase-activity.reducer';
import documentBlob from 'app/entities/document-blob/document-blob.reducer';
import order from 'app/entities/order/order.reducer';
import financing from 'app/entities/financing/financing.reducer';
import pricingSummary from 'app/entities/pricing-summary/pricing-summary.reducer';
import vehicleOptions from 'app/entities/vehicle-options/vehicle-options.reducer';
import shoppingCart from 'app/entities/shopping-cart/shopping-cart.reducer';
import paymentDetail from 'app/entities/payment-detail/payment-detail.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  shopper,
  vehicle,
  dealer,
  scheduledTestDrive,
  scheduledPickup,
  postPurchaseActivity,
  documentBlob,
  order,
  financing,
  pricingSummary,
  vehicleOptions,
  shoppingCart,
  paymentDetail,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
