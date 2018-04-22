import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './ticket.reducer';
import { ITicket } from 'app/shared/model/ticket.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITicketDetailProps {
  getEntity: ICrudGetAction<ITicket>;
  ticket: ITicket;
  match: any;
}

export class TicketDetail extends React.Component<ITicketDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { ticket } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            Ticket [<b>{ticket.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{ticket.description}</dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{ticket.status}</dd>
            <dt>User</dt>
            <dd>{ticket.userLogin ? ticket.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/ticket" replace color="info">
            <FaArrowLeft /> <span className="d-none d-md-inline">Back</span>
          </Button>
          <Button tag={Link} to={`/entity/ticket/${ticket.id}/edit`} replace color="primary">
            <FaPencil /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ ticket }) => ({
  ticket: ticket.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(TicketDetail);
