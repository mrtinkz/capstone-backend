import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './vehicle.reducer';

export const Vehicle = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const vehicleList = useAppSelector(state => state.vehicle.entities);
  const loading = useAppSelector(state => state.vehicle.loading);

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
      <h2 id="vehicle-heading" data-cy="VehicleHeading">
        Vehicles
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/vehicle/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Vehicle
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vehicleList && vehicleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('vin')}>
                  Vin <FontAwesomeIcon icon={getSortIconByFieldName('vin')} />
                </th>
                <th className="hand" onClick={sort('make')}>
                  Make <FontAwesomeIcon icon={getSortIconByFieldName('make')} />
                </th>
                <th className="hand" onClick={sort('model')}>
                  Model <FontAwesomeIcon icon={getSortIconByFieldName('model')} />
                </th>
                <th className="hand" onClick={sort('modelYear')}>
                  Model Year <FontAwesomeIcon icon={getSortIconByFieldName('modelYear')} />
                </th>
                <th className="hand" onClick={sort('dealerId')}>
                  Dealer Id <FontAwesomeIcon icon={getSortIconByFieldName('dealerId')} />
                </th>
                <th className="hand" onClick={sort('pricingSummaryId')}>
                  Pricing Summary Id <FontAwesomeIcon icon={getSortIconByFieldName('pricingSummaryId')} />
                </th>
                <th className="hand" onClick={sort('vehicleOptionsId')}>
                  Vehicle Options Id <FontAwesomeIcon icon={getSortIconByFieldName('vehicleOptionsId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vehicleList.map((vehicle, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vehicle/${vehicle.id}`} color="link" size="sm">
                      {vehicle.id}
                    </Button>
                  </td>
                  <td>{vehicle.vin}</td>
                  <td>{vehicle.make}</td>
                  <td>{vehicle.model}</td>
                  <td>{vehicle.modelYear}</td>
                  <td>{vehicle.dealerId}</td>
                  <td>{vehicle.pricingSummaryId}</td>
                  <td>{vehicle.vehicleOptionsId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vehicle/${vehicle.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/vehicle/${vehicle.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/vehicle/${vehicle.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Vehicles found</div>
        )}
      </div>
    </div>
  );
};

export default Vehicle;
