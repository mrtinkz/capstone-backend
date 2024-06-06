import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pricing-summary.reducer';

export const PricingSummaryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pricingSummaryEntity = useAppSelector(state => state.pricingSummary.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pricingSummaryDetailsHeading">Pricing Summary</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{pricingSummaryEntity.id}</dd>
          <dt>
            <span id="msrp">Msrp</span>
          </dt>
          <dd>{pricingSummaryEntity.msrp}</dd>
          <dt>
            <span id="taxesAndFees">Taxes And Fees</span>
          </dt>
          <dd>{pricingSummaryEntity.taxesAndFees}</dd>
          <dt>
            <span id="incentives">Incentives</span>
          </dt>
          <dd>{pricingSummaryEntity.incentives}</dd>
          <dt>
            <span id="tradeInEstimate">Trade In Estimate</span>
          </dt>
          <dd>{pricingSummaryEntity.tradeInEstimate}</dd>
          <dt>
            <span id="subscriptionServices">Subscription Services</span>
          </dt>
          <dd>{pricingSummaryEntity.subscriptionServices}</dd>
          <dt>
            <span id="protectionPlan">Protection Plan</span>
          </dt>
          <dd>{pricingSummaryEntity.protectionPlan}</dd>
        </dl>
        <Button tag={Link} to="/pricing-summary" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pricing-summary/${pricingSummaryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PricingSummaryDetail;
