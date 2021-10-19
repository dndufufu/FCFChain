import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './transaction-input.reducer';
import { ITransactionInput } from 'app/shared/model/transaction-input.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionInput = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const transactionInputList = useAppSelector(state => state.transactionInput.entities);
  const loading = useAppSelector(state => state.transactionInput.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="transaction-input-heading" data-cy="TransactionInputHeading">
        <Translate contentKey="chainApp.transactionInput.home.title">Transaction Inputs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="chainApp.transactionInput.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="chainApp.transactionInput.home.createLabel">Create new Transaction Input</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {transactionInputList && transactionInputList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="chainApp.transactionInput.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transactionInput.transactionOutputId">Transaction Output Id</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.transactionInput.uTXO">U TXO</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {transactionInputList.map((transactionInput, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${transactionInput.id}`} color="link" size="sm">
                      {transactionInput.id}
                    </Button>
                  </td>
                  <td>{transactionInput.transactionOutputId}</td>
                  <td>{transactionInput.uTXO}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${transactionInput.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${transactionInput.id}/edit`}
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
                        to={`${match.url}/${transactionInput.id}/delete`}
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
              <Translate contentKey="chainApp.transactionInput.home.notFound">No Transaction Inputs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TransactionInput;
