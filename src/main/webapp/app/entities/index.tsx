import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TransactionOutput from './transaction-output';
import TransactionInput from './transaction-input';
import Block from './block';
import Transaction from './transaction';
import Wallet from './wallet';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}transaction-output`} component={TransactionOutput} />
      <ErrorBoundaryRoute path={`${match.url}transaction-input`} component={TransactionInput} />
      <ErrorBoundaryRoute path={`${match.url}block`} component={Block} />
      <ErrorBoundaryRoute path={`${match.url}transaction`} component={Transaction} />
      <ErrorBoundaryRoute path={`${match.url}wallet`} component={Wallet} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
