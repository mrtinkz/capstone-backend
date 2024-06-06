import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Financing from './financing';
import FinancingDetail from './financing-detail';
import FinancingUpdate from './financing-update';
import FinancingDeleteDialog from './financing-delete-dialog';

const FinancingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Financing />} />
    <Route path="new" element={<FinancingUpdate />} />
    <Route path=":id">
      <Route index element={<FinancingDetail />} />
      <Route path="edit" element={<FinancingUpdate />} />
      <Route path="delete" element={<FinancingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FinancingRoutes;
