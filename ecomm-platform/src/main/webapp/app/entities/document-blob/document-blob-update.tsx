import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDocumentBlob } from 'app/shared/model/document-blob.model';
import { getEntity, updateEntity, createEntity, reset } from './document-blob.reducer';

export const DocumentBlobUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const documentBlobEntity = useAppSelector(state => state.documentBlob.entity);
  const loading = useAppSelector(state => state.documentBlob.loading);
  const updating = useAppSelector(state => state.documentBlob.updating);
  const updateSuccess = useAppSelector(state => state.documentBlob.updateSuccess);

  const handleClose = () => {
    navigate('/document-blob');
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
    if (values.postPurchaseActivityId !== undefined && typeof values.postPurchaseActivityId !== 'number') {
      values.postPurchaseActivityId = Number(values.postPurchaseActivityId);
    }

    const entity = {
      ...documentBlobEntity,
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
          ...documentBlobEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommPlatformApp.documentBlob.home.createOrEditLabel" data-cy="DocumentBlobCreateUpdateHeading">
            Create or edit a Document Blob
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
                <ValidatedField name="id" required readOnly id="document-blob-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Post Purchase Activity Id"
                id="document-blob-postPurchaseActivityId"
                name="postPurchaseActivityId"
                data-cy="postPurchaseActivityId"
                type="text"
              />
              <ValidatedField label="Name" id="document-blob-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Mime Type" id="document-blob-mimeType" name="mimeType" data-cy="mimeType" type="text" />
              <ValidatedBlobField label="Data" id="document-blob-data" name="data" data-cy="data" isImage accept="image/*" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/document-blob" replace color="info">
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

export default DocumentBlobUpdate;
