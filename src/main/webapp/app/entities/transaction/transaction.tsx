import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './transaction.reducer';
import { ITransaction } from 'app/shared/model/transaction.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Transaction = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const transactionList = useAppSelector(state => state.transaction.entities);
  const loading = useAppSelector(state => state.transaction.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="transaction-heading" data-cy="TransactionHeading">
        <Translate contentKey="chainApp.transaction.home.title">Transactions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="chainApp.transaction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="chainApp.transaction.home.createLabel">Create new Transaction</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {transactionList && transactionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="chainApp.transaction.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.hash">Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.sender">Sender</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.recipient">Recipient</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.value">Value</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.signature">Signature</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.timestamp">Timestamp</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transaction.block">Block</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {transactionList.map((transaction, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${transaction.id}`} color="link" size="sm">
                      {transaction.id}
                    </Button>
                  </td>
                  <td>{transaction.hash}</td>
                  <td>{transaction.sender}</td>
                  <td>{transaction.recipient}</td>
                  <td>{transaction.value}</td>
                  <td>{transaction.signature}</td>
                  <td>
                    {transaction.timestamp ? <TextFormat type="date" value={transaction.timestamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{transaction.status ? 'true' : 'false'}</td>
                  <td>{transaction.block ? <Link to={`block/${transaction.block.id}`}>{transaction.block.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${transaction.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${transaction.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${transaction.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="chainApp.transaction.home.notFound">No Transactions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Transaction;
