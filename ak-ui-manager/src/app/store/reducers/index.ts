import {InjectionToken} from '@angular/core';
import {
    ActionReducerMap,
    ActionReducer,
    MetaReducer,
    Action,
    createFeatureSelector,
    createSelector
} from '@ngrx/store';
import * as fromRouter from '@ngrx/router-store';
import * as fromConstants from '@store/reducers/constants.reducer';

/**
 * storeFreeze 用于防止 state 被修改，在 Redux 中我们必须确保 state 是不可更改的，这个函数
 * 有助于帮我们检测 state 是否被有意或无意的修改了。当 state 发生修改时，会抛出一个异常，这一点
 * 在开发时非常有帮助。根据环境变量的值，发布时会不包含这个函数。
 */
import {storeFreeze} from 'ngrx-store-freeze';

import {environment} from '@environments/environment';
import {RouterStateUrl} from '@core/utils/router.util';
import {AuthActionTypes} from '@store/actions/auth.actions';

/**
 * 正如我们的 reducer 像数据库中的表一样，我们的顶层 state 也包含各个子 reducer 的 state
 * 并且使用一个 key 来标识各个子 state
 */
export interface State {
    router: fromRouter.RouterReducerState<RouterStateUrl>;  // 路由
    constants: fromConstants.State;
}

/**
 * Our state is composed of a map of action reducer functions.
 * These reducer functions are called with each dispatched action
 * and the current or initial state and return a new immutable state.
 */
export const ROOT_REDUCERS = new InjectionToken<ActionReducerMap<State, Action>>('Root reducers token', {
    factory: () => ({
        router: fromRouter.routerReducer,
        constants: fromConstants.reducer,
    }),
});

export const reducers: ActionReducerMap<State> = {
    router: fromRouter.routerReducer,
    constants: fromConstants.reducer,
};

export function logger(reducer: ActionReducer<State>): ActionReducer<State> {
    return function (state: State, action: any): State {
        /*console.log('state', state);
        console.log('action', action);*/
        return reducer(state, action);
    };
}

export function storeStateGuard(reducer: ActionReducer<State>): ActionReducer<State> {
    return function (state, action) {
        if (action.type === AuthActionTypes.LOGOUT) {
            return reducer(undefined, action);
        }

        return reducer(state, action);
    };
}

export const metaReducers: MetaReducer<State>[] = !environment.production ? [logger, storeFreeze, storeStateGuard] : [storeStateGuard];


/**
 * @description 常量相关状态
 */
export const getConstantsState = createFeatureSelector<State, fromConstants.State>(
    'constants'
);
export const getApplicationTypes = createSelector(getConstantsState, fromConstants.getApplicationTypes);   // 应用分类
export const getServices = createSelector(getConstantsState, fromConstants.getServices);   // 服务应用
