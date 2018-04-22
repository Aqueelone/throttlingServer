import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ISLA } from 'app/shared/model/sla.model';

export const ACTION_TYPES = {
  FETCH_SLA_LIST: 'sLA/FETCH_SLA_LIST',
  FETCH_SLA: 'sLA/FETCH_SLA',
  CREATE_SLA: 'sLA/CREATE_SLA',
  UPDATE_SLA: 'sLA/UPDATE_SLA',
  DELETE_SLA: 'sLA/DELETE_SLA',
  RESET: 'sLA/RESET'
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
    case REQUEST(ACTION_TYPES.FETCH_SLA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SLA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SLA):
    case REQUEST(ACTION_TYPES.UPDATE_SLA):
    case REQUEST(ACTION_TYPES.DELETE_SLA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SLA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SLA):
    case FAILURE(ACTION_TYPES.CREATE_SLA):
    case FAILURE(ACTION_TYPES.UPDATE_SLA):
    case FAILURE(ACTION_TYPES.DELETE_SLA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SLA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SLA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SLA):
    case SUCCESS(ACTION_TYPES.UPDATE_SLA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SLA):
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

const apiUrl = SERVER_API_URL + '/api/slas';

// Actions

export const getEntities: ICrudGetAllAction<ISLA> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SLA_LIST,
  payload: axios.get(`${apiUrl}?cacheBuster=${new Date().getTime()}`) as Promise<ISLA>
});

export const getEntity: ICrudGetAction<ISLA> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SLA,
    payload: axios.get(requestUrl) as Promise<ISLA>
  };
};

export const createEntity: ICrudPutAction<ISLA> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SLA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISLA> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SLA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISLA> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SLA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
