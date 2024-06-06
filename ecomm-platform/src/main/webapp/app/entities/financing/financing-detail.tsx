import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './financing.reducer';

export const FinancingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const financingEntity = useAppSelector(state => state.financing.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="financingDetailsHeading">Financing</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{financingEntity.id}</dd>
          <dt>
            <span id="provider">Provider</span>
          </dt>
          <dd>{financingEntity.provider}</dd>
          <dt>
            <span id="interestRate">Interest Rate</span>
          </dt>
          <dd>{financingEntity.interestRate}</dd>
          <dt>
            <span id="loanTerm">Loan Term</span>
          </dt>
          <dd>{financingEntity.loanTerm}</dd>
          <dt>
            <span id="downPayment">Down Payment</span>
          </dt>
          <dd>{financingEntity.downPayment}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{financingEntity.orderId}</dd>
        </dl>
        <Button tag={Link} to="/financing" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/financing/${financingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FinancingDetail;
