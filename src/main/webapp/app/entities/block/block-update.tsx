import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './block.reducer';
import { IBlock } from 'app/shared/model/block.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BlockUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const blockEntity = useAppSelector(state => state.block.entity);
  const loading = useAppSelector(state => state.block.loading);
  const updating = useAppSelector(state => state.block.updating);
  const updateSuccess = useAppSelector(state => state.block.updateSuccess);

  const handleClose = () => {
    props.history.push('/block');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...blockEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timestamp: displayDefaultDateTime(),
        }
      : {
          ...blockEntity,
          timestamp: convertDateTimeFromServer(blockEntity.timestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chainApp.block.home.createOrEditLabel" data-cy="BlockCreateUpdateHeading">
            <Translate contentKey="chainApp.block.home.createOrEditLabel">Create or edit a Block</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="block-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('chainApp.block.hash')} id="block-hash" name="hash" data-cy="hash" type="text" />
              <ValidatedField
                label={translate('chainApp.block.previousHash')}
                id="block-previousHash"
                name="previousHash"
                data-cy="previousHash"
                type="text"
              />
              <ValidatedField
                label={translate('chainApp.block.merkleRoot')}
                id="block-merkleRoot"
                name="merkleRoot"
                data-cy="merkleRoot"
                type="text"
              />
              <ValidatedField
                label={translate('chainApp.block.timestamp')}
                id="block-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('chainApp.block.nonce')} id="block-nonce" name="nonce" data-cy="nonce" type="text" />
              <ValidatedField
                label={translate('chainApp.block.tradingVolume')}
                id="block-tradingVolume"
                name="tradingVolume"
                data-cy="tradingVolume"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/block" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BlockUpdate;
