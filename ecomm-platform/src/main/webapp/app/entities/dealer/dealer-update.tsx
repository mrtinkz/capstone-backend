import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDealer } from 'app/shared/model/dealer.model';
import { getEntity, updateEntity, createEntity, reset } from './dealer.reducer';

export const DealerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dealerEntity = useAppSelector(state => state.dealer.entity);
  const loading = useAppSelector(state => state.dealer.loading);
  const updating = useAppSelector(state => state.dealer.updating);
  const updateSuccess = useAppSelector(state => state.dealer.updateSuccess);

  const handleClose = () => {
    navigate('/dealer');
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

    const entity = {
      ...dealerEntity,
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
          ...dealerEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.dealer.home.createOrEditLabel" data-cy="DealerCreateUpdateHeading">
            Create or edit a Dealer
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="dealer-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="dealer-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Address" id="dealer-address" name="address" data-cy="address" type="text" />
              <ValidatedField label="City" id="dealer-city" name="city" data-cy="city" type="text" />
              <ValidatedField label="State" id="dealer-state" name="state" data-cy="state" type="text" />
              <ValidatedField label="Zip Code" id="dealer-zipCode" name="zipCode" data-cy="zipCode" type="text" />
              <ValidatedField label="Website" id="dealer-website" name="website" data-cy="website" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dealer" replace color="info">
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

export default DealerUpdate;
