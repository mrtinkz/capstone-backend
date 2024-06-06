import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document-blob.reducer';

export const DocumentBlobDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentBlobEntity = useAppSelector(state => state.documentBlob.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentBlobDetailsHeading">Document Blob</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{documentBlobEntity.id}</dd>
          <dt>
            <span id="postPurchaseActivityId">Post Purchase Activity Id</span>
          </dt>
          <dd>{documentBlobEntity.postPurchaseActivityId}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{documentBlobEntity.name}</dd>
          <dt>
            <span id="mimeType">Mime Type</span>
          </dt>
          <dd>{documentBlobEntity.mimeType}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>
            {documentBlobEntity.data ? (
              <div>
                {documentBlobEntity.dataContentType ? (
                  <a onClick={openFile(documentBlobEntity.dataContentType, documentBlobEntity.data)}>
                    <img
                      src={`data:${documentBlobEntity.dataContentType};base64,${documentBlobEntity.data}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {documentBlobEntity.dataContentType}, {byteSize(documentBlobEntity.data)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/document-blob" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document-blob/${documentBlobEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentBlobDetail;
