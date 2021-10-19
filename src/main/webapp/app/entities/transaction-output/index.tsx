import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TransactionOutput from './transaction-output';
import TransactionOutputDetail from './transaction-output-detail';
import TransactionOutputUpdate from './transaction-output-update';
import TransactionOutputDeleteDialog from './transaction-output-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TransactionOutputUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TransactionOutputUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TransactionOutputDetail} />
      <ErrorBoundaryRoute path={match.url} component={TransactionOutput} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TransactionOutputDeleteDialog} />
  </>
);

export default Routes;
