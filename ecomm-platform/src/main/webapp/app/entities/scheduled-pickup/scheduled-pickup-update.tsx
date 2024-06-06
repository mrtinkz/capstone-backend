import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IScheduledPickup } from 'app/shared/model/scheduled-pickup.model';
import { getEntity, updateEntity, createEntity, reset } from './scheduled-pickup.reducer';

export const ScheduledPickupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const scheduledPickupEntity = useAppSelector(state => state.scheduledPickup.entity);
  const loading = useAppSelector(state => state.scheduledPickup.loading);
  const updating = useAppSelector(state => state.scheduledPickup.updating);
  const updateSuccess = useAppSelector(state => state.scheduledPickup.updateSuccess);

  const handleClose = () => {
    navigate('/scheduled-pickup');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.shopperId !== undefined && typeof values.shopperId !== 'number') {
      values.shopperId = Number(values.shopperId);
    }
    if (values.orderId !== undefined && typeof values.orderId !== 'number') {
      values.orderId = Number(values.orderId);
    }

    const entity = {
      ...scheduledPickupEntity,
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
          ...scheduledPickupEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.scheduledPickup.home.createOrEditLabel" data-cy="ScheduledPickupCreateUpdateHeading">
            Create or edit a Scheduled Pickup
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
                <ValidatedField name="id" required readOnly id="scheduled-pickup-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Shopper Id" id="scheduled-pickup-shopperId" name="shopperId" data-cy="shopperId" type="text" />
              <ValidatedField label="Order Id" id="scheduled-pickup-orderId" name="orderId" data-cy="orderId" type="text" />
              <ValidatedField
                label="Date"
                id="scheduled-pickup-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/scheduled-pickup" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ScheduledPickupUpdate;
