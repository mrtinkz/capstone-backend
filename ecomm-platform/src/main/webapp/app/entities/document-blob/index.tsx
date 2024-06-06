import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DocumentBlob from './document-blob';
import DocumentBlobDetail from './document-blob-detail';
import DocumentBlobUpdate from './document-blob-update';
import DocumentBlobDeleteDialog from './document-blob-delete-dialog';

const DocumentBlobRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DocumentBlob />} />
    <Route path="new" element={<DocumentBlobUpdate />} />
    <Route path=":id">
      <Route index element={<DocumentBlobDetail />} />
      <Route path="edit" element={<DocumentBlobUpdate />} />
      <Route path="delete" element={<DocumentBlobDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DocumentBlobRoutes;
