import * as React from 'react';
import { Route, Switch } from 'react-router-dom';

import SLA from './sla';
import SLADetail from './sla-detail';
import SLAUpdate from './sla-update';
import SLADeleteDialog from './sla-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <Route exact path={`${match.url}/new`} component={SLAUpdate} />
      <Route exact path={`${match.url}/:id/edit`} component={SLAUpdate} />
      <Route exact path={`${match.url}/:id`} component={SLADetail} />
      <Route path={match.url} component={SLA} />
    </Switch>
    <Route path={`${match.url}/:id/delete`} component={SLADeleteDialog} />
  </>
);

export default Routes;
