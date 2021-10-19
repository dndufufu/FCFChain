import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TransactionInput from './transaction-input';
import TransactionInputDetail from './transaction-input-detail';
import TransactionInputUpdate from './transaction-input-update';
import TransactionInputDeleteDialog from './transaction-input-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TransactionInputUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TransactionInputUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TransactionInputDetail} />
      <ErrorBoundaryRoute path={match.url} component={TransactionInput} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TransactionInputDeleteDialog} />
  </>
);

export default Routes;
