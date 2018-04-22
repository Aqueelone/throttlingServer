import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FaBan, FaFloppyO, FaArrowLeft } from 'react-icons/lib/fa';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './sla.reducer';
import { ISLA } from 'app/shared/model/sla.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

export interface ISLAUpdateProps {
  getEntity: ICrudGetAction<ISLA>;
  updateEntity: ICrudPutAction<ISLA>;
  createEntity: ICrudPutAction<ISLA>;
  getUsers: ICrudGetAllAction<IUser>;
  users: IUser[];
  sLA: ISLA;
  reset: Function;
  loading: boolean;
  updating: boolean;
  match: any;
  history: any;
}

export interface ISLAUpdateState {
  isNew: boolean;
  userId: number;
}

export class SLAUpdate extends React.Component<ISLAUpdateProps, ISLAUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id,
      userId: 0
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { sLA } = this.props;
      const entity = {
        ...sLA,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/sla');
  };

  userUpdate = element => {
    const login = element.target.value;
    for (const i in this.props.users) {
      if (login.toString() === this.props.users[i].login.toString()) {
        this.setState({
          userId: this.props.users[i].id
        });
      }
    }
  };

  render() {
    const isInvalid = false;
    const { sLA, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhi-sla-heading">Create or edit a SLA</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : sLA} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="rpsLabel" for="rps">
                    Rps
                  </Label>
                  <AvField type="number" className="form-control" name="rps" />
                </AvGroup>
                <AvGroup>
                  <Label for="user.login">User</Label>
                  <AvInput type="select" className="form-control" name="userId" onChange={this.userUpdate}>
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/sla" replace color="info">
                  <FaArrowLeft />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={isInvalid || updating}>
                  <FaFloppyO />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = storeState => ({
  users: storeState.userManagement.users,
  sLA: storeState.sLA.entity,
  loading: storeState.sLA.loading,
  updating: storeState.sLA.updating
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(SLAUpdate);
