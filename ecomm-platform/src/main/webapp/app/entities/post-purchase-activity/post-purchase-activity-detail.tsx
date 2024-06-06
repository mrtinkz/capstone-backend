import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-purchase-activity.reducer';

export const PostPurchaseActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postPurchaseActivityEntity = useAppSelector(state => state.postPurchaseActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postPurchaseActivityDetailsHeading">Post Purchase Activity</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{postPurchaseActivityEntity.id}</dd>
          <dt>
            <span id="orderId">Order Id</span>
          </dt>
          <dd>{postPurchaseActivityEntity.orderId}</dd>
          <dt>
            <span id="activityType">Activity Type</span>
          </dt>
          <dd>{postPurchaseActivityEntity.activityType}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{postPurchaseActivityEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/post-purchase-activity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-purchase-activity/${postPurchaseActivityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostPurchaseActivityDetail;
