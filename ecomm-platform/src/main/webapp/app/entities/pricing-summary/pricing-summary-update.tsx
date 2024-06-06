import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPricingSummary } from 'app/shared/model/pricing-summary.model';
import { getEntity, updateEntity, createEntity, reset } from './pricing-summary.reducer';

export const PricingSummaryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pricingSummaryEntity = useAppSelector(state => state.pricingSummary.entity);
  const loading = useAppSelector(state => state.pricingSummary.loading);
  const updating = useAppSelector(state => state.pricingSummary.updating);
  const updateSuccess = useAppSelector(state => state.pricingSummary.updateSuccess);

  const handleClose = () => {
    navigate('/pricing-summary');
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
    if (values.msrp !== undefined && typeof values.msrp !== 'number') {
      values.msrp = Number(values.msrp);
    }
    if (values.taxesAndFees !== undefined && typeof values.taxesAndFees !== 'number') {
      values.taxesAndFees = Number(values.taxesAndFees);
    }
    if (values.incentives !== undefined && typeof values.incentives !== 'number') {
      values.incentives = Number(values.incentives);
    }
    if (values.tradeInEstimate !== undefined && typeof values.tradeInEstimate !== 'number') {
      values.tradeInEstimate = Number(values.tradeInEstimate);
    }
    if (values.subscriptionServices !== undefined && typeof values.subscriptionServices !== 'number') {
      values.subscriptionServices = Number(values.subscriptionServices);
    }
    if (values.protectionPlan !== undefined && typeof values.protectionPlan !== 'number') {
      values.protectionPlan = Number(values.protectionPlan);
    }

    const entity = {
      ...pricingSummaryEntity,
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
          ...pricingSummaryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.pricingSummary.home.createOrEditLabel" data-cy="PricingSummaryCreateUpdateHeading">
            Create or edit a Pricing Summary
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
                <ValidatedField name="id" required readOnly id="pricing-summary-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Msrp"
                id="pricing-summary-msrp"
                name="msrp"
                data-cy="msrp"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Taxes And Fees"
                id="pricing-summary-taxesAndFees"
                name="taxesAndFees"
                data-cy="taxesAndFees"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Incentives" id="pricing-summary-incentives" name="incentives" data-cy="incentives" type="text" />
              <ValidatedField
                label="Trade In Estimate"
                id="pricing-summary-tradeInEstimate"
                name="tradeInEstimate"
                data-cy="tradeInEstimate"
                type="text"
              />
              <ValidatedField
                label="Subscription Services"
                id="pricing-summary-subscriptionServices"
                name="subscriptionServices"
                data-cy="subscriptionServices"
                type="text"
              />
              <ValidatedField
                label="Protection Plan"
                id="pricing-summary-protectionPlan"
                name="protectionPlan"
                data-cy="protectionPlan"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pricing-summary" replace color="info">
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

export default PricingSummaryUpdate;
