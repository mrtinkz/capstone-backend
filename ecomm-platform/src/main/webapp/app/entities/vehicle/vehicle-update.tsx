import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicle } from 'app/shared/model/vehicle.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle.reducer';

export const VehicleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  const loading = useAppSelector(state => state.vehicle.loading);
  const updating = useAppSelector(state => state.vehicle.updating);
  const updateSuccess = useAppSelector(state => state.vehicle.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle');
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
    if (values.modelYear !== undefined && typeof values.modelYear !== 'number') {
      values.modelYear = Number(values.modelYear);
    }
    if (values.dealerId !== undefined && typeof values.dealerId !== 'number') {
      values.dealerId = Number(values.dealerId);
    }
    if (values.pricingSummaryId !== undefined && typeof values.pricingSummaryId !== 'number') {
      values.pricingSummaryId = Number(values.pricingSummaryId);
    }
    if (values.vehicleOptionsId !== undefined && typeof values.vehicleOptionsId !== 'number') {
      values.vehicleOptionsId = Number(values.vehicleOptionsId);
    }

    const entity = {
      ...vehicleEntity,
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
          ...vehicleEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.vehicle.home.createOrEditLabel" data-cy="VehicleCreateUpdateHeading">
            Create or edit a Vehicle
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="vehicle-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Vin"
                id="vehicle-vin"
                name="vin"
                data-cy="vin"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Make"
                id="vehicle-make"
                name="make"
                data-cy="make"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Model"
                id="vehicle-model"
                name="model"
                data-cy="model"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Model Year"
                id="vehicle-modelYear"
                name="modelYear"
                data-cy="modelYear"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Dealer Id"
                id="vehicle-dealerId"
                name="dealerId"
                data-cy="dealerId"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Pricing Summary Id"
                id="vehicle-pricingSummaryId"
                name="pricingSummaryId"
                data-cy="pricingSummaryId"
                type="text"
              />
              <ValidatedField
                label="Vehicle Options Id"
                id="vehicle-vehicleOptionsId"
                name="vehicleOptionsId"
                data-cy="vehicleOptionsId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle" replace color="info">
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

export default VehicleUpdate;
