import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transaction-input.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionInputDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transactionInputEntity = useAppSelector(state => state.transactionInput.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionInputDetailsHeading">
          <Translate contentKey="chainApp.transactionInput.detail.title">TransactionInput</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionInputEntity.id}</dd>
          <dt>
            <span id="transactionOutputId">
              <Translate contentKey="chainApp.transactionInput.transactionOutputId">Transaction Output Id</Translate>
            </span>
          </dt>
          <dd>{transactionInputEntity.transactionOutputId}</dd>
          <dt>
            <span id="uTXO">
              <Translate contentKey="chainApp.transactionInput.uTXO">U TXO</Translate>
            </span>
          </dt>
          <dd>{transactionInputEntity.uTXO}</dd>
          <dt>
            <Translate contentKey="chainApp.transactionInput.transaction">Transaction</Translate>
          </dt>
          <dd>{transactionInputEntity.transaction ? transactionInputEntity.transaction.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transaction-input" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction-input/${transactionInputEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionInputDetail;
