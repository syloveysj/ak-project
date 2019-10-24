import {Injectable} from '@angular/core';
import {Actions, Effect, ofType} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Observable, of} from 'rxjs';
import {catchError, filter, map, switchMap, withLatestFrom} from 'rxjs/operators';
import {NzMessageService} from 'ng-zorro-antd';
import {ConstantsActionTypes} from '@store/actions/constants.actions';
import {fromRoot} from '@store/store';
import {ConstantsActions} from '@store/actions';
import {InterfacesService} from "@service/http/interfaces.service";

@Injectable()
export class ConstantsEffect {

    /**
     * @description 应用分类
     */
    @Effect()
    loadApplicationTypes$: Observable<Action> = this.actions$.pipe(
        ofType<ConstantsActions.LoadApplicationTypes>(ConstantsActionTypes.LOAD_APPLICATION_TYPES),
        withLatestFrom(this.store$.select(fromRoot.getApplicationTypes), (_, data) => data),
        filter(data => !data.length),
        switchMap(
            () => this.interfacesService.getApplicationTypeAll().pipe(
                map(res => {
                    // 对获取的数据进行加工
                    console.log('loadApplicationTypes$', res);
                    return res;
                }),
                map((data) => new ConstantsActions.LoadApplicationTypesSuccess(data)),
                catchError((err) => of(new ConstantsActions.LoadFail({
                    status: err.status,
                    message: err.error.messages,
                    timestamp: new Date()
                })))
            )
        )
    );

    /**
     * @description 服务应用
     */
    @Effect()
    loadServices$: Observable<Action> = this.actions$.pipe(
        ofType<ConstantsActions.LoadServices>(ConstantsActionTypes.LOAD_SERVICES),
        withLatestFrom(this.store$.select(fromRoot.getServices), (_, data) => data),
        filter(data => !data.length),
        switchMap(
            () => this.interfacesService.getServiceAll().pipe(
                map(res => {
                    // 对获取的数据进行加工
                    console.log('loadServices$', res);
                    return res;
                }),
                map((data) => new ConstantsActions.LoadServicesSuccess(data)),
                catchError((err) => of(new ConstantsActions.LoadFail({
                    status: err.status,
                    message: err.error.messages,
                    timestamp: new Date()
                })))
            )
        )
    );

    constructor(private actions$: Actions,
                private interfacesService: InterfacesService,
                private store$: Store<fromRoot.State>,
                public nzMessageService: NzMessageService) {

    }
}
