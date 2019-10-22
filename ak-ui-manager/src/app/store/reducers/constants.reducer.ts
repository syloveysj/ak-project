import {ConstantsActionsUnion, ConstantsActionTypes} from '@store/actions/constants.actions';
import {Option} from '@model/common';

export interface State {
    applicationTypes: Option[];
    services: Option[];
}

export const initialState: State = {
    applicationTypes: [],
    services: []
};

export function reducer(state = initialState, action: ConstantsActionsUnion): State {
    switch (action.type) {
        case ConstantsActionTypes.LOAD_APPLICATION_TYPES_SUCCESS: {
            return {...state, applicationTypes: action.payload};
        }
        case ConstantsActionTypes.LOAD_SERVICES_SUCCESS: {
            return {...state, services: action.payload};
        }
        default: {
            return state;
        }
    }
}

export const getApplicationTypes = (state: State) => state && state.applicationTypes;
export const getServices = (state: State) => state && state.services;
