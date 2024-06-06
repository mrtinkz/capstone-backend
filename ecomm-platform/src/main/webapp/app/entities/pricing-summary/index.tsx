import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PricingSummary from './pricing-summary';
import PricingSummaryDetail from './pricing-summary-detail';
import PricingSummaryUpdate from './pricing-summary-update';
import PricingSummaryDeleteDialog from './pricing-summary-delete-dialog';

const PricingSummaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PricingSummary />} />
    <Route path="new" element={<PricingSummaryUpdate />} />
    <Route path=":id">
      <Route index element={<PricingSummaryDetail />} />
      <Route path="edit" element={<PricingSummaryUpdate />} />
      <Route path="delete" element={<PricingSummaryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PricingSummaryRoutes;
