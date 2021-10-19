import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './block.reducer';
import { IBlock } from 'app/shared/model/block.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Block = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const blockList = useAppSelector(state => state.block.entities);
  const loading = useAppSelector(state => state.block.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="block-heading" data-cy="BlockHeading">
        <Translate contentKey="chainApp.block.home.title">Blocks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="chainApp.block.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="chainApp.block.home.createLabel">Create new Block</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {blockList && blockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="chainApp.block.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.hash">Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.previousHash">Previous Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.merkleRoot">Merkle Root</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.timestamp">Timestamp</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.nonce">Nonce</Translate>
                </th>
                <th>
                  <Translate contentKey="chainApp.block.tradingVolume">Trading Volume</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {blockList.map((block, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${block.id}`} color="link" size="sm">
                      {block.id}
                    </Button>
                  </td>
                  <td>{block.hash}</td>
                  <td>{block.previousHash}</td>
                  <td>{block.merkleRoot}</td>
                  <td>{block.timestamp ? <TextFormat type="date" value={block.timestamp} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{block.nonce}</td>
                  <td>{block.tradingVolume}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${block.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${block.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${block.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="chainApp.block.home.notFound">No Blocks found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Block;
