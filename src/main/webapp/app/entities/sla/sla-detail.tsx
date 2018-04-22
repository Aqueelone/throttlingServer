import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './sla.reducer';
import { ISLA } from 'app/shared/model/sla.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISLADetailProps {
  getEntity: ICrudGetAction<ISLA>;
  sLA: ISLA;
  match: any;
}

export class SLADetail extends React.Component<ISLADetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { sLA } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            SLA [<b>{sLA.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="rps">Rps</span>
            </dt>
            <dd>{sLA.rps}</dd>
            <dt>User</dt>
            <dd>{sLA.userLogin ? sLA.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/sla" replace color="info">
            <FaArrowLeft /> <span className="d-none d-md-inline">Back</span>
          </Button>
          <Button tag={Link} to={`/entity/sla/${sLA.id}/edit`} replace color="primary">
            <FaPencil /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ sLA }) => ({
  sLA: sLA.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(SLADetail);
