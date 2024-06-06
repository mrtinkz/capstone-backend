import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle.reducer';

export const VehicleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDetailsHeading">Vehicle</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vehicleEntity.id}</dd>
          <dt>
            <span id="vin">Vin</span>
          </dt>
          <dd>{vehicleEntity.vin}</dd>
          <dt>
            <span id="make">Make</span>
          </dt>
          <dd>{vehicleEntity.make}</dd>
          <dt>
            <span id="model">Model</span>
          </dt>
          <dd>{vehicleEntity.model}</dd>
          <dt>
            <span id="modelYear">Model Year</span>
          </dt>
          <dd>{vehicleEntity.modelYear}</dd>
          <dt>
            <span id="dealerId">Dealer Id</span>
          </dt>
          <dd>{vehicleEntity.dealerId}</dd>
          <dt>
            <span id="pricingSummaryId">Pricing Summary Id</span>
          </dt>
          <dd>{vehicleEntity.pricingSummaryId}</dd>
          <dt>
            <span id="vehicleOptionsId">Vehicle Options Id</span>
          </dt>
          <dd>{vehicleEntity.vehicleOptionsId}</dd>
        </dl>
        <Button tag={Link} to="/vehicle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle/${vehicleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDetail;
