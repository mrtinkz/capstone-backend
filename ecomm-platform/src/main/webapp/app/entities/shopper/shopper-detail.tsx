import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './shopper.reducer';

export const ShopperDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shopperEntity = useAppSelector(state => state.shopper.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shopperDetailsHeading">Shopper</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{shopperEntity.id}</dd>
          <dt>
            <span id="userId">User Id</span>
          </dt>
          <dd>{shopperEntity.userId}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{shopperEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{shopperEntity.lastName}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{shopperEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/shopper" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/shopper/${shopperEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShopperDetail;
