import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, Injector, NgModule} from '@angular/core';

import {CoreModule} from '@core/core.module';
import {SharedModule} from '@shared/shared.module';
import {AppComponent} from '@core/app.component';
import {ErrorComponent} from '@core/component/error.component';

import {NZ_I18N, NZ_MESSAGE_CONFIG, NZ_MODAL_CONFIG, zh_CN} from 'ng-zorro-antd';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {registerLocaleData} from '@angular/common';
import zh from '@angular/common/locales/zh';
import {Config} from '@config/config';
import {configFactory} from '@config/config.factory';
import {InitialLoadService} from '@core/utils/initial-load.service';
import {RouteReuseStrategy} from '@angular/router';
import {metaReducers, ROOT_REDUCERS} from '@store/reducers';
import {SimpleReuseStrategy} from '@core/utils/reuse-stategy.util';
import {httpInterceptorProviders} from "@service/interceptor";
import {StoreModule} from "@ngrx/store";
import {StoreRouterConnectingModule} from "@ngrx/router-store";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {EffectsModule} from "@ngrx/effects";
import {ConstantsEffect} from "@store/effects/constants.effect";

registerLocaleData(zh);

export let AppInjector: Injector;

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        CoreModule,
        SharedModule,
        StoreModule.forRoot(ROOT_REDUCERS, {
            metaReducers,
        }),
        /**
         * @ngrx/router-store keeps router state up-to-date in the store.
         */
        StoreRouterConnectingModule.forRoot(),
        /**
         * Store devtools instrument the store retaining past versions of state
         * and recalculating new states. This enables powerful time-travel
         * debugging.
         *
         * To use the debugger, install the Redux Devtools extension for either
         * Chrome or Firefox
         *
         * See: https://github.com/zalmoxisus/redux-devtools-extension
         */
        StoreDevtoolsModule.instrument({
            name: 'NgRx Store App',

            // In a production build you would want to disable the Store Devtools
            // logOnly: environment.production,
        }),
        /**
         * EffectsModule.forRoot() is imported once in the root module and
         * sets up the effects class to be initialized immediately when the
         * application starts.
         *
         * See: https://ngrx.io/guide/effects#registering-root-effects
         */
        EffectsModule.forRoot([ConstantsEffect]),
    ],
    declarations: [
        ErrorComponent
    ],
    exports: [
        ErrorComponent
    ],
    bootstrap: [AppComponent],
    providers: [
        {provide: Config, useFactory: configFactory}, // 配置文件注入。
        /**
         * @description http拦截器
         */
        httpInterceptorProviders,
        /**
         * @description 初始化加载服务
         */
        InitialLoadService,
        /**
         * @description 初始化加载
         */
        {
            provide: APP_INITIALIZER,
            useFactory: (s: InitialLoadService) => () => s.load(),
            deps: [InitialLoadService],
            multi: true
        },
        /**
         * @description nz-zorro模态框配置信息
         */
        {provide: NZ_MODAL_CONFIG, useValue: {autoBodyPadding: true}},
        /**
         * @description nz-zorro消息框配置信息
         */
        {provide: NZ_MESSAGE_CONFIG, useValue: {nzMaxStack: 1}},
        /**
         * @description 路由重用配置模块
         */
        {provide: RouteReuseStrategy, useClass: SimpleReuseStrategy},
        {provide: NZ_I18N, useValue: zh_CN}]
})
export class AppModule {
    constructor(private injector: Injector) {
        AppInjector = this.injector;
    }
}
