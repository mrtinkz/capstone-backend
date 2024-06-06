import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ScheduledTestDrive from './scheduled-test-drive';
import ScheduledTestDriveDetail from './scheduled-test-drive-detail';
import ScheduledTestDriveUpdate from './scheduled-test-drive-update';
import ScheduledTestDriveDeleteDialog from './scheduled-test-drive-delete-dialog';

const ScheduledTestDriveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ScheduledTestDrive />} />
    <Route path="new" element={<ScheduledTestDriveUpdate />} />
    <Route path=":id">
      <Route index element={<ScheduledTestDriveDetail />} />
      <Route path="edit" element={<ScheduledTestDriveUpdate />} />
      <Route path="delete" element={<ScheduledTestDriveDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ScheduledTestDriveRoutes;
