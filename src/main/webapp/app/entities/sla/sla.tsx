import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FaPlus, FaEye, FaPencil, FaTrash } from 'react-icons/lib/fa';

import { getEntities } from './sla.reducer';
import { ISLA } from 'app/shared/model/sla.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISLAProps {
  getEntities: ICrudGetAllAction<ISLA>;
  sLAList: ISLA[];
  match: any;
}

export class SLA extends React.Component<ISLAProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { sLAList, match } = this.props;
    return (
      <div>
        <h2 id="page-heading">
          SLAS
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FaPlus />
            Create new SLA
          </Link>
        </h2>
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Rps</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sLAList.map((sLA, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${sLA.id}`} color="link" size="sm">
                      {sLA.id}
                    </Button>
                  </td>
                  <td>{sLA.rps}</td>
                  <td>{sLA.userLogin ? sLA.userLogin : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${sLA.id}`} color="info" size="sm">
                        <FaEye /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${sLA.id}/edit`} color="primary" size="sm">
                        <FaPencil /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${sLA.id}/delete`} color="danger" size="sm">
                        <FaTrash /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ sLA }) => ({
  sLAList: sLA.entities
});

const mapDispatchToProps = {
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(SLA);
