import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './transaction-output.reducer';
import { ITransactionOutput } from 'app/shared/model/transaction-output.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionOutput = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const transactionOutputList = useAppSelector(state => state.transactionOutput.entities);
  const loading = useAppSelector(state => state.transactionOutput.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="transaction-output-heading" data-cy="TransactionOutputHeading">
        <Translate contentKey="chainApp.transactionOutput.home.title">Transaction Outputs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="chainApp.transactionOutput.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="chainApp.transactionOutput.home.createLabel">Create new Transaction Output</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {transactionOutputList && transactionOutputList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="chainApp.transactionOutput.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transactionOutput.recipient">Recipient</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transactionOutput.value">Value</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transactionOutput.parentTransactionId">Parent Transaction Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {transactionOutputList.map((transactionOutput, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${transactionOutput.id}`} color="link" size="sm">
                      {transactionOutput.id}
                    </Button>
                  </td>
                  <td>{transactionOutput.recipient}</td>
                  <td>{transactionOutput.value}</td>
                  <td>{transactionOutput.parentTransactionId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${transactionOutput.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${transactionOutput.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${transactionOutput.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="chainApp.transactionOutput.home.notFound">No Transaction Outputs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TransactionOutput;
