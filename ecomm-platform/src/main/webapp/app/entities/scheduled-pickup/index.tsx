import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ScheduledPickup from './scheduled-pickup';
import ScheduledPickupDetail from './scheduled-pickup-detail';
import ScheduledPickupUpdate from './scheduled-pickup-update';
import ScheduledPickupDeleteDialog from './scheduled-pickup-delete-dialog';

const ScheduledPickupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ScheduledPickup />} />
    <Route path="new" element={<ScheduledPickupUpdate />} />
    <Route path=":id">
      <Route index element={<ScheduledPickupDetail />} />
      <Route path="edit" element={<ScheduledPickupUpdate />} />
      <Route path="delete" element={<ScheduledPickupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ScheduledPickupRoutes;
