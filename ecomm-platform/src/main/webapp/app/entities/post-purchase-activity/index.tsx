import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostPurchaseActivity from './post-purchase-activity';
import PostPurchaseActivityDetail from './post-purchase-activity-detail';
import PostPurchaseActivityUpdate from './post-purchase-activity-update';
import PostPurchaseActivityDeleteDialog from './post-purchase-activity-delete-dialog';

const PostPurchaseActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostPurchaseActivity />} />
    <Route path="new" element={<PostPurchaseActivityUpdate />} />
    <Route path=":id">
      <Route index element={<PostPurchaseActivityDetail />} />
      <Route path="edit" element={<PostPurchaseActivityUpdate />} />
      <Route path="delete" element={<PostPurchaseActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostPurchaseActivityRoutes;
