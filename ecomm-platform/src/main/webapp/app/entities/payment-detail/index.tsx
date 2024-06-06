import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentDetail from './payment-detail';
import PaymentDetailDetail from './payment-detail-detail';
import PaymentDetailUpdate from './payment-detail-update';
import PaymentDetailDeleteDialog from './payment-detail-delete-dialog';

const PaymentDetailRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentDetail />} />
    <Route path="new" element={<PaymentDetailUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentDetailDetail />} />
      <Route path="edit" element={<PaymentDetailUpdate />} />
      <Route path="delete" element={<PaymentDetailDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentDetailRoutes;
