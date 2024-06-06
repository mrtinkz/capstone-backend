import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './vehicle-options.reducer';

export const VehicleOptions = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const vehicleOptionsList = useAppSelector(state => state.vehicleOptions.entities);
  const loading = useAppSelector(state => state.vehicleOptions.loading);

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
      <h2 id="vehicle-options-heading" data-cy="VehicleOptionsHeading">
        Vehicle Options
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/vehicle-options/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Vehicle Options
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vehicleOptionsList && vehicleOptionsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('estimatedMileage')}>
                  Estimated Mileage <FontAwesomeIcon icon={getSortIconByFieldName('estimatedMileage')} />
                </th>
                <th className="hand" onClick={sort('engine')}>
                  Engine <FontAwesomeIcon icon={getSortIconByFieldName('engine')} />
                </th>
                <th className="hand" onClick={sort('drivetrain')}>
                  Drivetrain <FontAwesomeIcon icon={getSortIconByFieldName('drivetrain')} />
                </th>
                <th className="hand" onClick={sort('transmission')}>
                  Transmission <FontAwesomeIcon icon={getSortIconByFieldName('transmission')} />
                </th>
                <th className="hand" onClick={sort('trim')}>
                  Trim <FontAwesomeIcon icon={getSortIconByFieldName('trim')} />
                </th>
                <th className="hand" onClick={sort('color')}>
                  Color <FontAwesomeIcon icon={getSortIconByFieldName('color')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vehicleOptionsList.map((vehicleOptions, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vehicle-options/${vehicleOptions.id}`} color="link" size="sm">
                      {vehicleOptions.id}
                    </Button>
                  </td>
                  <td>{vehicleOptions.estimatedMileage}</td>
                  <td>{vehicleOptions.engine}</td>
                  <td>{vehicleOptions.drivetrain}</td>
                  <td>{vehicleOptions.transmission}</td>
                  <td>{vehicleOptions.trim}</td>
                  <td>{vehicleOptions.color}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vehicle-options/${vehicleOptions.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/vehicle-options/${vehicleOptions.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/vehicle-options/${vehicleOptions.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Vehicle Options found</div>
        )}
      </div>
    </div>
  );
};

export default VehicleOptions;
