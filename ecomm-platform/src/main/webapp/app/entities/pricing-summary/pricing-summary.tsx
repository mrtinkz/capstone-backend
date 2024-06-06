import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pricing-summary.reducer';

export const PricingSummary = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const pricingSummaryList = useAppSelector(state => state.pricingSummary.entities);
  const loading = useAppSelector(state => state.pricingSummary.loading);

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
      <h2 id="pricing-summary-heading" data-cy="PricingSummaryHeading">
        Pricing Summaries
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/pricing-summary/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Pricing Summary
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pricingSummaryList && pricingSummaryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('msrp')}>
                  Msrp <FontAwesomeIcon icon={getSortIconByFieldName('msrp')} />
                </th>
                <th className="hand" onClick={sort('taxesAndFees')}>
                  Taxes And Fees <FontAwesomeIcon icon={getSortIconByFieldName('taxesAndFees')} />
                </th>
                <th className="hand" onClick={sort('incentives')}>
                  Incentives <FontAwesomeIcon icon={getSortIconByFieldName('incentives')} />
                </th>
                <th className="hand" onClick={sort('tradeInEstimate')}>
                  Trade In Estimate <FontAwesomeIcon icon={getSortIconByFieldName('tradeInEstimate')} />
                </th>
                <th className="hand" onClick={sort('subscriptionServices')}>
                  Subscription Services <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionServices')} />
                </th>
                <th className="hand" onClick={sort('protectionPlan')}>
                  Protection Plan <FontAwesomeIcon icon={getSortIconByFieldName('protectionPlan')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pricingSummaryList.map((pricingSummary, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pricing-summary/${pricingSummary.id}`} color="link" size="sm">
                      {pricingSummary.id}
                    </Button>
                  </td>
                  <td>{pricingSummary.msrp}</td>
                  <td>{pricingSummary.taxesAndFees}</td>
                  <td>{pricingSummary.incentives}</td>
                  <td>{pricingSummary.tradeInEstimate}</td>
                  <td>{pricingSummary.subscriptionServices}</td>
                  <td>{pricingSummary.protectionPlan}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pricing-summary/${pricingSummary.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pricing-summary/${pricingSummary.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/pricing-summary/${pricingSummary.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Pricing Summaries found</div>
        )}
      </div>
    </div>
  );
};

export default PricingSummary;
