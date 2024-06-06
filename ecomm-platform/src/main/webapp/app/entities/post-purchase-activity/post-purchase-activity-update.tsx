import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPostPurchaseActivity } from 'app/shared/model/post-purchase-activity.model';
import { PostPurchaseActivityType } from 'app/shared/model/enumerations/post-purchase-activity-type.model';
import { PostPurchaseActivityStatus } from 'app/shared/model/enumerations/post-purchase-activity-status.model';
import { getEntity, updateEntity, createEntity, reset } from './post-purchase-activity.reducer';

export const PostPurchaseActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postPurchaseActivityEntity = useAppSelector(state => state.postPurchaseActivity.entity);
  const loading = useAppSelector(state => state.postPurchaseActivity.loading);
  const updating = useAppSelector(state => state.postPurchaseActivity.updating);
  const updateSuccess = useAppSelector(state => state.postPurchaseActivity.updateSuccess);
  const postPurchaseActivityTypeValues = Object.keys(PostPurchaseActivityType);
  const postPurchaseActivityStatusValues = Object.keys(PostPurchaseActivityStatus);

  const handleClose = () => {
    navigate('/post-purchase-activity');
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
    if (values.orderId !== undefined && typeof values.orderId !== 'number') {
      values.orderId = Number(values.orderId);
    }

    const entity = {
      ...postPurchaseActivityEntity,
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
          activityType: 'PAPERWORK_COMPLETION',
          status: 'PENDING',
          ...postPurchaseActivityEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.postPurchaseActivity.home.createOrEditLabel" data-cy="PostPurchaseActivityCreateUpdateHeading">
            Create or edit a Post Purchase Activity
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
                <ValidatedField name="id" required readOnly id="post-purchase-activity-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Order Id" id="post-purchase-activity-orderId" name="orderId" data-cy="orderId" type="text" />
              <ValidatedField
                label="Activity Type"
                id="post-purchase-activity-activityType"
                name="activityType"
                data-cy="activityType"
                type="select"
              >
                {postPurchaseActivityTypeValues.map(postPurchaseActivityType => (
                  <option value={postPurchaseActivityType} key={postPurchaseActivityType}>
                    {postPurchaseActivityType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Status" id="post-purchase-activity-status" name="status" data-cy="status" type="select">
                {postPurchaseActivityStatusValues.map(postPurchaseActivityStatus => (
                  <option value={postPurchaseActivityStatus} key={postPurchaseActivityStatus}>
                    {postPurchaseActivityStatus}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/post-purchase-activity" replace color="info">
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

export default PostPurchaseActivityUpdate;
