import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './transaction-output.reducer';
import { ITransactionOutput } from 'app/shared/model/transaction-output.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransactionOutputUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const transactionOutputEntity = useAppSelector(state => state.transactionOutput.entity);
  const loading = useAppSelector(state => state.transactionOutput.loading);
  const updating = useAppSelector(state => state.transactionOutput.updating);
  const updateSuccess = useAppSelector(state => state.transactionOutput.updateSuccess);

  const handleClose = () => {
    props.history.push('/transaction-output');
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
    const entity = {
      ...transactionOutputEntity,
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
      ? {}
      : {
          ...transactionOutputEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chainApp.transactionOutput.home.createOrEditLabel" data-cy="TransactionOutputCreateUpdateHeading">
            <Translate contentKey="chainApp.transactionOutput.home.createOrEditLabel">Create or edit a TransactionOutput</Translate>
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
                  id="transaction-output-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('chainApp.transactionOutput.recipient')}
                id="transaction-output-recipient"
                name="recipient"
                data-cy="recipient"
                type="text"
              />
              <ValidatedField
                label={translate('chainApp.transactionOutput.value')}
                id="transaction-output-value"
                name="value"
                data-cy="value"
                type="text"
              />
              <ValidatedField
                label={translate('chainApp.transactionOutput.parentTransactionId')}
                id="transaction-output-parentTransactionId"
                name="parentTransactionId"
                data-cy="parentTransactionId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transaction-output" replace color="info">
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

export default TransactionOutputUpdate;
