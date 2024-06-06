import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dealer.reducer';

export const DealerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dealerEntity = useAppSelector(state => state.dealer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dealerDetailsHeading">Dealer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{dealerEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{dealerEntity.name}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{dealerEntity.address}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{dealerEntity.city}</dd>
          <dt>
            <span id="state">State</span>
          </dt>
          <dd>{dealerEntity.state}</dd>
          <dt>
            <span id="zipCode">Zip Code</span>
          </dt>
          <dd>{dealerEntity.zipCode}</dd>
          <dt>
            <span id="website">Website</span>
          </dt>
          <dd>{dealerEntity.website}</dd>
        </dl>
        <Button tag={Link} to="/dealer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dealer/${dealerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DealerDetail;
