import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleOptions from './vehicle-options';
import VehicleOptionsDetail from './vehicle-options-detail';
import VehicleOptionsUpdate from './vehicle-options-update';
import VehicleOptionsDeleteDialog from './vehicle-options-delete-dialog';

const VehicleOptionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleOptions />} />
    <Route path="new" element={<VehicleOptionsUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleOptionsDetail />} />
      <Route path="edit" element={<VehicleOptionsUpdate />} />
      <Route path="delete" element={<VehicleOptionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleOptionsRoutes;
