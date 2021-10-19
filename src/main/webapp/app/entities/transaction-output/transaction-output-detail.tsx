import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transaction-output.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionOutputDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transactionOutputEntity = useAppSelector(state => state.transactionOutput.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionOutputDetailsHeading">
          <Translate contentKey="chainApp.transactionOutput.detail.title">TransactionOutput</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionOutputEntity.id}</dd>
          <dt>
            <span id="recipient">
              <Translate contentKey="chainApp.transactionOutput.recipient">Recipient</Translate>
            </span>
          </dt>
          <dd>{transactionOutputEntity.recipient}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="chainApp.transactionOutput.value">Value</Translate>
            </span>
          </dt>
          <dd>{transactionOutputEntity.value}</dd>
          <dt>
            <span id="parentTransactionId">
              <Translate contentKey="chainApp.transactionOutput.parentTransactionId">Parent Transaction Id</Translate>
            </span>
          </dt>
          <dd>{transactionOutputEntity.parentTransactionId}</dd>
        </dl>
        <Button tag={Link} to="/transaction-output" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction-output/${transactionOutputEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionOutputDetail;
