import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Shopper from './shopper';
import ShopperDetail from './shopper-detail';
import ShopperUpdate from './shopper-update';
import ShopperDeleteDialog from './shopper-delete-dialog';

const ShopperRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Shopper />} />
    <Route path="new" element={<ShopperUpdate />} />
    <Route path=":id">
      <Route index element={<ShopperDetail />} />
      <Route path="edit" element={<ShopperUpdate />} />
      <Route path="delete" element={<ShopperDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShopperRoutes;
