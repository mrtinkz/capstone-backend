import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShopper } from 'app/shared/model/shopper.model';
import { getEntity, updateEntity, createEntity, reset } from './shopper.reducer';

export const ShopperUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const shopperEntity = useAppSelector(state => state.shopper.entity);
  const loading = useAppSelector(state => state.shopper.loading);
  const updating = useAppSelector(state => state.shopper.updating);
  const updateSuccess = useAppSelector(state => state.shopper.updateSuccess);

  const handleClose = () => {
    navigate('/shopper');
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
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...shopperEntity,
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
          ...shopperEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.shopper.home.createOrEditLabel" data-cy="ShopperCreateUpdateHeading">
            Create or edit a Shopper
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="shopper-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="User Id" id="shopper-userId" name="userId" data-cy="userId" type="text" />
              <ValidatedField label="First Name" id="shopper-firstName" name="firstName" data-cy="firstName" type="text" />
              <ValidatedField label="Last Name" id="shopper-lastName" name="lastName" data-cy="lastName" type="text" />
              <ValidatedField label="Email" id="shopper-email" name="email" data-cy="email" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/shopper" replace color="info">
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

export default ShopperUpdate;
