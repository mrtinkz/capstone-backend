import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-detail.reducer';

export const PaymentDetailDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentDetailEntity = useAppSelector(state => state.paymentDetail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailDetailsHeading">Payment Detail</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{paymentDetailEntity.id}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{paymentDetailEntity.orderId}</dd>
          <dt>
            <span id="paymentType">Payment Type</span>
          </dt>
          <dd>{paymentDetailEntity.paymentType}</dd>
          <dt>
            <span id="paymentAmount">Payment Amount</span>
          </dt>
          <dd>{paymentDetailEntity.paymentAmount}</dd>
          <dt>
            <span id="paymentDate">Payment Date</span>
          </dt>
          <dd>
            {paymentDetailEntity.paymentDate ? (
              <TextFormat value={paymentDetailEntity.paymentDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cardType">Card Type</span>
          </dt>
          <dd>{paymentDetailEntity.cardType}</dd>
          <dt>
            <span id="cardNumber">Card Number</span>
          </dt>
          <dd>{paymentDetailEntity.cardNumber}</dd>
          <dt>
            <span id="cardHolderName">Card Holder Name</span>
          </dt>
          <dd>{paymentDetailEntity.cardHolderName}</dd>
          <dt>
            <span id="cardExpirationDate">Card Expiration Date</span>
          </dt>
          <dd>
            {paymentDetailEntity.cardExpirationDate ? (
              <TextFormat value={paymentDetailEntity.cardExpirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/payment-detail" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-detail/${paymentDetailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetailDetail;
