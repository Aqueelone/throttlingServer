import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
import {
  ICrudGetAllAction,
  ICrudPutAction,
  TextFormat,
  JhiPagination,
  getPaginationItemsNumber,
  getSortState,
  IPaginationBaseState
} from 'react-jhipster';
import { FaPlus, FaEye, FaPencil, FaSort, FaTrash } from 'react-icons/lib/fa';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { IUser } from 'app/shared/model/user.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getUsers, updateUser } from './user-management.reducer';

export interface IUserManagementProps {
  getUsers: ICrudGetAllAction<IUser>;
  updateUser: ICrudPutAction<IUser>;
  users: IUser[];
  account: any;
  match: any;
  totalItems: 0;
  history: any;
  location: any;
}

export class UserManagement extends React.Component<IUserManagementProps, IPaginationBaseState> {
  state: IPaginationBaseState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getUsers();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortUsers()
    );
  };

  sortUsers() {
    this.getUsers();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortUsers());

  getUsers = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getUsers(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  toggleActive = user => () => {
    this.props.updateUser({
      ...user,
      activated: !user.activated
    });
  };

  render() {
    const { users, account, match, totalItems } = this.props;
    return (
      <div>
        <h2>
          Users
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity">
            <FaPlus /> Create a new user
          </Link>
        </h2>
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  ID<FaSort />
                </th>
                <th className="hand" onClick={this.sort('login')}>
                  Login<FaSort />
                </th>
                <th className="hand" onClick={this.sort('email')}>
                  Email<FaSort />
                </th>
                <th />
                <th>Profiles</th>
                <th className="hand" onClick={this.sort('createdDate')}>
                  Created Date<FaSort />
                </th>
                <th className="hand" onClick={this.sort('lastModifiedBy')}>
                  Last Modified By<FaSort />
                </th>
                <th className="hand" onClick={this.sort('lastModifiedDate')}>
                  Last Modified Date<FaSort />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {users.map((user, i) => (
                <tr key={`user-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${user.login}`} color="link" size="sm">
                      {user.id}
                    </Button>
                  </td>
                  <td>{user.login}</td>
                  <td>{user.email}</td>
                  <td>
                    {user.activated ? (
                      <Button color="success" onClick={this.toggleActive(user)}>
                        Activated
                      </Button>
                    ) : (
                      <Button color="danger" onClick={this.toggleActive(user)}>
                        Deactivated
                      </Button>
                    )}
                  </td>
                  <td>
                    {user.authorities
                      ? user.authorities.map((authority, j) => (
                          <div key={`user-auth-${i}-${j}`}>
                            <span className="badge badge-info">{authority}</span>
                          </div>
                        ))
                      : null}
                  </td>
                  <td>
                    <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                  </td>
                  <td>{user.lastModifiedBy}</td>
                  <td>
                    <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${user.login}`} color="info" size="sm">
                        <FaEye /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${user.login}/edit`} color="primary" size="sm">
                        <FaPencil /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${user.login}/delete`}
                        color="danger"
                        size="sm"
                        disabled={account.login === user.login}
                      >
                        <FaTrash /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="row justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </div>
      </div>
    );
  }
}

const mapStateToProps = storeState => ({
  users: storeState.userManagement.users,
  totalItems: storeState.userManagement.totalItems,
  account: storeState.authentication.account
});

const mapDispatchToProps = { getUsers, updateUser };

export default connect(mapStateToProps, mapDispatchToProps)(UserManagement);
