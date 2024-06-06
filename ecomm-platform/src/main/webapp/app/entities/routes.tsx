import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Shopper from './shopper';
import Vehicle from './vehicle';
import Dealer from './dealer';
import ScheduledTestDrive from './scheduled-test-drive';
import ScheduledPickup from './scheduled-pickup';
import PostPurchaseActivity from './post-purchase-activity';
import DocumentBlob from './document-blob';
import Order from './order';
import Financing from './financing';
import PricingSummary from './pricing-summary';
import VehicleOptions from './vehicle-options';
import ShoppingCart from './shopping-cart';
import PaymentDetail from './payment-detail';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="shopper/*" element={<Shopper />} />
        <Route path="vehicle/*" element={<Vehicle />} />
        <Route path="dealer/*" element={<Dealer />} />
        <Route path="scheduled-test-drive/*" element={<ScheduledTestDrive />} />
        <Route path="scheduled-pickup/*" element={<ScheduledPickup />} />
        <Route path="post-purchase-activity/*" element={<PostPurchaseActivity />} />
        <Route path="document-blob/*" element={<DocumentBlob />} />
        <Route path="order/*" element={<Order />} />
        <Route path="financing/*" element={<Financing />} />
        <Route path="pricing-summary/*" element={<PricingSummary />} />
        <Route path="vehicle-options/*" element={<VehicleOptions />} />
        <Route path="shopping-cart/*" element={<ShoppingCart />} />
        <Route path="payment-detail/*" element={<PaymentDetail />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
