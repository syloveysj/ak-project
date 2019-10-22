import {Action} from '@ngrx/store';
import {Err, UserInfo} from '@model/common';

// 7版本推崇的写法

export enum AuthActionTypes {

    LOGIN = '[Auth] Login',
    LOGIN_SUCCESS = '[Auth] Login Success',
    LOGIN_FAIL = '[Auth] Login Fail',

    GET_USER_INFO = '[Auth] Get User Info',
    GET_USER_INFO_SUCCESS = '[Auth] Get User Info Success',
    GET_USER_INFO_FAIL = '[Auth] Get User Info Fail',

    LOGOUT = '[Auth] Logout',
}

export class LoginAction implements Action {
    readonly type = AuthActionTypes.LOGIN;

    constructor(public payload: { username: string; password: string }) {
    }
}

export class LoginSuccessAction implements Action {
    readonly type = AuthActionTypes.LOGIN_SUCCESS;
}

export class LoginFailAction implements Action {
    readonly type = AuthActionTypes.LOGIN_FAIL;

    constructor(public payload: Err) {
    }
}

export class GetUserInfoAction implements Action {
    readonly type = AuthActionTypes.GET_USER_INFO;

    constructor() {
    }
}

export class GetUserInfoSuccessAction implements Action {
    readonly type = AuthActionTypes.GET_USER_INFO_SUCCESS;

    constructor(public payload: UserInfo) {
    }
}

export class GetUserInfoFailAction implements Action {
    readonly type = AuthActionTypes.GET_USER_INFO_FAIL;

    constructor(public payload: Err) {
    }
}

export class LogoutAction implements Action {
    readonly type = AuthActionTypes.LOGOUT;

    constructor() {
    }
}

// 导出action实现类
export type AuthActionsUnion
    = LoginAction
    | LoginSuccessAction
    | LoginFailAction
    | GetUserInfoAction
    | GetUserInfoSuccessAction
    | GetUserInfoFailAction
    | LogoutAction;
