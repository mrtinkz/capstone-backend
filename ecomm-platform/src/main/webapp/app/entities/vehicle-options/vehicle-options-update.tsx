import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleOptions } from 'app/shared/model/vehicle-options.model';
import { EngineType } from 'app/shared/model/enumerations/engine-type.model';
import { DrivetrainType } from 'app/shared/model/enumerations/drivetrain-type.model';
import { TransmissionType } from 'app/shared/model/enumerations/transmission-type.model';
import { Trim } from 'app/shared/model/enumerations/trim.model';
import { Color } from 'app/shared/model/enumerations/color.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-options.reducer';

export const VehicleOptionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleOptionsEntity = useAppSelector(state => state.vehicleOptions.entity);
  const loading = useAppSelector(state => state.vehicleOptions.loading);
  const updating = useAppSelector(state => state.vehicleOptions.updating);
  const updateSuccess = useAppSelector(state => state.vehicleOptions.updateSuccess);
  const engineTypeValues = Object.keys(EngineType);
  const drivetrainTypeValues = Object.keys(DrivetrainType);
  const transmissionTypeValues = Object.keys(TransmissionType);
  const trimValues = Object.keys(Trim);
  const colorValues = Object.keys(Color);

  const handleClose = () => {
    navigate('/vehicle-options');
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
    if (values.estimatedMileage !== undefined && typeof values.estimatedMileage !== 'number') {
      values.estimatedMileage = Number(values.estimatedMileage);
    }

    const entity = {
      ...vehicleOptionsEntity,
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
          engine: 'GASOLINE',
          drivetrain: 'FRONT_WHEEL_DRIVE',
          transmission: 'MANUAL',
          trim: 'TRIM_ONE',
          color: 'BLACK',
          ...vehicleOptionsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.vehicleOptions.home.createOrEditLabel" data-cy="VehicleOptionsCreateUpdateHeading">
            Create or edit a Vehicle Options
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
                <ValidatedField name="id" required readOnly id="vehicle-options-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Estimated Mileage"
                id="vehicle-options-estimatedMileage"
                name="estimatedMileage"
                data-cy="estimatedMileage"
                type="text"
              />
              <ValidatedField label="Engine" id="vehicle-options-engine" name="engine" data-cy="engine" type="select">
                {engineTypeValues.map(engineType => (
                  <option value={engineType} key={engineType}>
                    {engineType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Drivetrain" id="vehicle-options-drivetrain" name="drivetrain" data-cy="drivetrain" type="select">
                {drivetrainTypeValues.map(drivetrainType => (
                  <option value={drivetrainType} key={drivetrainType}>
                    {drivetrainType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Transmission"
                id="vehicle-options-transmission"
                name="transmission"
                data-cy="transmission"
                type="select"
              >
                {transmissionTypeValues.map(transmissionType => (
                  <option value={transmissionType} key={transmissionType}>
                    {transmissionType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Trim" id="vehicle-options-trim" name="trim" data-cy="trim" type="select">
                {trimValues.map(trim => (
                  <option value={trim} key={trim}>
                    {trim}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Color" id="vehicle-options-color" name="color" data-cy="color" type="select">
                {colorValues.map(color => (
                  <option value={color} key={color}>
                    {color}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-options" replace color="info">
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

export default VehicleOptionsUpdate;
