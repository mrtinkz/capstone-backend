import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaymentDetail } from 'app/shared/model/payment-detail.model';
import { PaymentType } from 'app/shared/model/enumerations/payment-type.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';
import { getEntity, updateEntity, createEntity, reset } from './payment-detail.reducer';

export const PaymentDetailUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentDetailEntity = useAppSelector(state => state.paymentDetail.entity);
  const loading = useAppSelector(state => state.paymentDetail.loading);
  const updating = useAppSelector(state => state.paymentDetail.updating);
  const updateSuccess = useAppSelector(state => state.paymentDetail.updateSuccess);
  const paymentTypeValues = Object.keys(PaymentType);
  const cardTypeValues = Object.keys(CardType);

  const handleClose = () => {
    navigate('/payment-detail');
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
    if (values.orderId !== undefined && typeof values.orderId !== 'number') {
      values.orderId = Number(values.orderId);
    }
    if (values.paymentAmount !== undefined && typeof values.paymentAmount !== 'number') {
      values.paymentAmount = Number(values.paymentAmount);
    }

    const entity = {
      ...paymentDetailEntity,
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
          paymentType: 'CREDIT_CARD',
          cardType: 'VISA',
          ...paymentDetailEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.paymentDetail.home.createOrEditLabel" data-cy="PaymentDetailCreateUpdateHeading">
            Create or edit a Payment Detail
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
                <ValidatedField name="id" required readOnly id="payment-detail-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Order Id" id="payment-detail-orderId" name="orderId" data-cy="orderId" type="text" />
              <ValidatedField label="Payment Type" id="payment-detail-paymentType" name="paymentType" data-cy="paymentType" type="select">
                {paymentTypeValues.map(paymentType => (
                  <option value={paymentType} key={paymentType}>
                    {paymentType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Payment Amount"
                id="payment-detail-paymentAmount"
                name="paymentAmount"
                data-cy="paymentAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Payment Date"
                id="payment-detail-paymentDate"
                name="paymentDate"
                data-cy="paymentDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Card Type" id="payment-detail-cardType" name="cardType" data-cy="cardType" type="select">
                {cardTypeValues.map(cardType => (
                  <option value={cardType} key={cardType}>
                    {cardType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Card Number" id="payment-detail-cardNumber" name="cardNumber" data-cy="cardNumber" type="text" />
              <ValidatedField
                label="Card Holder Name"
                id="payment-detail-cardHolderName"
                name="cardHolderName"
                data-cy="cardHolderName"
                type="text"
              />
              <ValidatedField
                label="Card Expiration Date"
                id="payment-detail-cardExpirationDate"
                name="cardExpirationDate"
                data-cy="cardExpirationDate"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-detail" replace color="info">
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

export default PaymentDetailUpdate;
