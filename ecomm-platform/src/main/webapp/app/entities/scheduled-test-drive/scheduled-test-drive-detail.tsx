import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './scheduled-test-drive.reducer';

export const ScheduledTestDriveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const scheduledTestDriveEntity = useAppSelector(state => state.scheduledTestDrive.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="scheduledTestDriveDetailsHeading">Scheduled Test Drive</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{scheduledTestDriveEntity.id}</dd>
          <dt>
            <span id="shopperId">Shopper Id</span>
          </dt>
          <dd>{scheduledTestDriveEntity.shopperId}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>
            {scheduledTestDriveEntity.date ? (
              <TextFormat value={scheduledTestDriveEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/scheduled-test-drive" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduled-test-drive/${scheduledTestDriveEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ScheduledTestDriveDetail;
