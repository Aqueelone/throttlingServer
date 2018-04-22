import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import Ticket from './ticket';
import TicketDetail from './ticket-detail';
import TicketUpdate from './ticket-update';
import TicketDeleteDialog from './ticket-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={TicketUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={TicketUpdate} />
      <Route exact path={`${match.url}/:id`} component={TicketDetail} />
      <Route path={match.url} component={Ticket} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={TicketDeleteDialog} />
  </>
);

export default Routes;
