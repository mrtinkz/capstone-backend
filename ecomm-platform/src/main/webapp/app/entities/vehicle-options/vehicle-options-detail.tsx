import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-options.reducer';

export const VehicleOptionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleOptionsEntity = useAppSelector(state => state.vehicleOptions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleOptionsDetailsHeading">Vehicle Options</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{vehicleOptionsEntity.id}</dd>
          <dt>
            <span id="estimatedMileage">Estimated Mileage</span>
          </dt>
          <dd>{vehicleOptionsEntity.estimatedMileage}</dd>
          <dt>
            <span id="engine">Engine</span>
          </dt>
          <dd>{vehicleOptionsEntity.engine}</dd>
          <dt>
            <span id="drivetrain">Drivetrain</span>
          </dt>
          <dd>{vehicleOptionsEntity.drivetrain}</dd>
          <dt>
            <span id="transmission">Transmission</span>
          </dt>
          <dd>{vehicleOptionsEntity.transmission}</dd>
          <dt>
            <span id="trim">Trim</span>
          </dt>
          <dd>{vehicleOptionsEntity.trim}</dd>
          <dt>
            <span id="color">Color</span>
          </dt>
          <dd>{vehicleOptionsEntity.color}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-options" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-options/${vehicleOptionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleOptionsDetail;
