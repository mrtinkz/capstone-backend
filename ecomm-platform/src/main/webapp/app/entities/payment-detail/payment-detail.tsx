import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './payment-detail.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const paymentDetailList = useAppSelector(state => state.paymentDetail.entities);
  const loading = useAppSelector(state => state.paymentDetail.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="payment-detail-heading" data-cy="PaymentDetailHeading">
        Payment Details
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/payment-detail/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Payment Detail
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentDetailList && paymentDetailList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderId')}>
                  Order Id <FontAwesomeIcon icon={getSortIconByFieldName('orderId')} />
                </th>
                <th className="hand" onClick={sort('paymentType')}>
                  Payment Type <FontAwesomeIcon icon={getSortIconByFieldName('paymentType')} />
                </th>
                <th className="hand" onClick={sort('paymentAmount')}>
                  Payment Amount <FontAwesomeIcon icon={getSortIconByFieldName('paymentAmount')} />
                </th>
                <th className="hand" onClick={sort('paymentDate')}>
                  Payment Date <FontAwesomeIcon icon={getSortIconByFieldName('paymentDate')} />
                </th>
                <th className="hand" onClick={sort('cardType')}>
                  Card Type <FontAwesomeIcon icon={getSortIconByFieldName('cardType')} />
                </th>
                <th className="hand" onClick={sort('cardNumber')}>
                  Card Number <FontAwesomeIcon icon={getSortIconByFieldName('cardNumber')} />
                </th>
                <th className="hand" onClick={sort('cardHolderName')}>
                  Card Holder Name <FontAwesomeIcon icon={getSortIconByFieldName('cardHolderName')} />
                </th>
                <th className="hand" onClick={sort('cardExpirationDate')}>
                  Card Expiration Date <FontAwesomeIcon icon={getSortIconByFieldName('cardExpirationDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentDetailList.map((paymentDetail, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment-detail/${paymentDetail.id}`} color="link" size="sm">
                      {paymentDetail.id}
                    </Button>
                  </td>
                  <td>{paymentDetail.orderId}</td>
                  <td>{paymentDetail.paymentType}</td>
                  <td>{paymentDetail.paymentAmount}</td>
                  <td>
                    {paymentDetail.paymentDate ? (
                      <TextFormat type="date" value={paymentDetail.paymentDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{paymentDetail.cardType}</td>
                  <td>{paymentDetail.cardNumber}</td>
                  <td>{paymentDetail.cardHolderName}</td>
                  <td>
                    {paymentDetail.cardExpirationDate ? (
                      <TextFormat type="date" value={paymentDetail.cardExpirationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/payment-detail/${paymentDetail.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/payment-detail/${paymentDetail.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/payment-detail/${paymentDetail.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Payment Details found</div>
        )}
      </div>
    </div>
  );
};

export default PaymentDetail;
