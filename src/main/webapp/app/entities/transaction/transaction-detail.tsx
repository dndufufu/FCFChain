import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transaction.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transactionEntity = useAppSelector(state => state.transaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionDetailsHeading">
          <Translate contentKey="chainApp.transaction.detail.title">Transaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.id}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="chainApp.transaction.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.hash}</dd>
          <dt>
            <span id="sender">
              <Translate contentKey="chainApp.transaction.sender">Sender</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.sender}</dd>
          <dt>
            <span id="recipient">
              <Translate contentKey="chainApp.transaction.recipient">Recipient</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.recipient}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="chainApp.transaction.value">Value</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.value}</dd>
          <dt>
            <span id="signature">
              <Translate contentKey="chainApp.transaction.signature">Signature</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.signature}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="chainApp.transaction.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {transactionEntity.timestamp ? <TextFormat value={transactionEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="chainApp.transaction.status">Status</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="chainApp.transaction.block">Block</Translate>
          </dt>
          <dd>{transactionEntity.block ? transactionEntity.block.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction/${transactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionDetail;
