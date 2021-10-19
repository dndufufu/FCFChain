import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './block.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BlockDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const blockEntity = useAppSelector(state => state.block.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blockDetailsHeading">
          <Translate contentKey="chainApp.block.detail.title">Block</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blockEntity.id}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="chainApp.block.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{blockEntity.hash}</dd>
          <dt>
            <span id="previousHash">
              <Translate contentKey="chainApp.block.previousHash">Previous Hash</Translate>
            </span>
          </dt>
          <dd>{blockEntity.previousHash}</dd>
          <dt>
            <span id="merkleRoot">
              <Translate contentKey="chainApp.block.merkleRoot">Merkle Root</Translate>
            </span>
          </dt>
          <dd>{blockEntity.merkleRoot}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="chainApp.block.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{blockEntity.timestamp ? <TextFormat value={blockEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="nonce">
              <Translate contentKey="chainApp.block.nonce">Nonce</Translate>
            </span>
          </dt>
          <dd>{blockEntity.nonce}</dd>
          <dt>
            <span id="tradingVolume">
              <Translate contentKey="chainApp.block.tradingVolume">Trading Volume</Translate>
            </span>
          </dt>
          <dd>{blockEntity.tradingVolume}</dd>
        </dl>
        <Button tag={Link} to="/block" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/block/${blockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BlockDetail;
