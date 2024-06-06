import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFinancing } from 'app/shared/model/financing.model';
import { getEntity, updateEntity, createEntity, reset } from './financing.reducer';

export const FinancingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const financingEntity = useAppSelector(state => state.financing.entity);
  const loading = useAppSelector(state => state.financing.loading);
  const updating = useAppSelector(state => state.financing.updating);
  const updateSuccess = useAppSelector(state => state.financing.updateSuccess);

  const handleClose = () => {
    navigate('/financing');
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
    if (values.interestRate !== undefined && typeof values.interestRate !== 'number') {
      values.interestRate = Number(values.interestRate);
    }
    if (values.loanTerm !== undefined && typeof values.loanTerm !== 'number') {
      values.loanTerm = Number(values.loanTerm);
    }
    if (values.downPayment !== undefined && typeof values.downPayment !== 'number') {
      values.downPayment = Number(values.downPayment);
    }
    if (values.orderId !== undefined && typeof values.orderId !== 'number') {
      values.orderId = Number(values.orderId);
    }

    const entity = {
      ...financingEntity,
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
          ...financingEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.financing.home.createOrEditLabel" data-cy="FinancingCreateUpdateHeading">
            Create or edit a Financing
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="financing-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Provider"
                id="financing-provider"
                name="provider"
                data-cy="provider"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Interest Rate"
                id="financing-interestRate"
                name="interestRate"
                data-cy="interestRate"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Loan Term"
                id="financing-loanTerm"
                name="loanTerm"
                data-cy="loanTerm"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Down Payment" id="financing-downPayment" name="downPayment" data-cy="downPayment" type="text" />
              <ValidatedField label="Order Id" id="financing-orderId" name="orderId" data-cy="orderId" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/financing" replace color="info">
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

export default FinancingUpdate;
