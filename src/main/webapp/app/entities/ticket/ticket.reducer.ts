import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ITicket } from 'app/shared/model/ticket.model';

export const ACTION_TYPES = {
  FETCH_TICKET_LIST: 'ticket/FETCH_TICKET_LIST',
  FETCH_TICKET: 'ticket/FETCH_TICKET',
  CREATE_TICKET: 'ticket/CREATE_TICKET',
  UPDATE_TICKET: 'ticket/UPDATE_TICKET',
  DELETE_TICKET: 'ticket/DELETE_TICKET',
  RESET: 'ticket/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: {},
  updating: false,
  updateSuccess: false
};

// Reducer

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TICKET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TICKET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TICKET):
    case REQUEST(ACTION_TYPES.UPDATE_TICKET):
    case REQUEST(ACTION_TYPES.DELETE_TICKET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TICKET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TICKET):
    case FAILURE(ACTION_TYPES.CREATE_TICKET):
    case FAILURE(ACTION_TYPES.UPDATE_TICKET):
    case FAILURE(ACTION_TYPES.DELETE_TICKET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TICKET_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TICKET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TICKET):
    case SUCCESS(ACTION_TYPES.UPDATE_TICKET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TICKET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVER_API_URL + '/api/tickets';

// Actions

export const getEntities: ICrudGetAllAction<ITicket> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TICKET_LIST,
  payload: axios.get(`${apiUrl}?cacheBuster=${new Date().getTime()}`) as Promise<ITicket>
});

export const getEntity: ICrudGetAction<ITicket> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TICKET,
    payload: axios.get(requestUrl) as Promise<ITicket>
  };
};

export const createEntity: ICrudPutAction<ITicket> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TICKET,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITicket> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TICKET,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITicket> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TICKET,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
