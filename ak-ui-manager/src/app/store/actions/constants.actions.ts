import {Action} from '@ngrx/store';
import {Err} from '@model/common';

// 7版本推崇的写法

export enum ConstantsActionTypes {

    // 应用分类
    LOAD_APPLICATION_TYPES = '[CONSTANT/API] LOAD Application Types',
    LOAD_APPLICATION_TYPES_SUCCESS = '[CONSTANT/API] LOAD Application Types Success',

    // 服务应用
    LOAD_SERVICES= '[CONSTANT/API] LOAD Services',
    LOAD_SERVICES_SUCCESS = '[CONSTANT/API] LOAD Services Success',

    Load_FAIL = '[CONSTANT/API] LOAD Fail',
}

export class LoadApplicationTypes implements Action {
    readonly type = ConstantsActionTypes.LOAD_APPLICATION_TYPES;

    constructor() {
    }
}

export class LoadApplicationTypesSuccess implements Action {
    readonly type = ConstantsActionTypes.LOAD_APPLICATION_TYPES_SUCCESS;

    constructor(public payload: any) {
    }
}

export class LoadServices implements Action {
    readonly type = ConstantsActionTypes.LOAD_SERVICES;

    constructor() {
    }
}

export class LoadServicesSuccess implements Action {
    readonly type = ConstantsActionTypes.LOAD_SERVICES_SUCCESS;

    constructor(public payload: any) {
    }
}

export class LoadFail implements Action {
    readonly type = ConstantsActionTypes.Load_FAIL;

    constructor(public payload: Err) {
    }
}

export type ConstantsActionsUnion
    = LoadApplicationTypes
    | LoadApplicationTypesSuccess
    | LoadServices
    | LoadServicesSuccess
    | LoadFail
    ;
