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
import {SimpleReuseStrategy} from '@core/utils/reuse-stategy.util';
import {httpInterceptorProviders} from "@service/interceptor";

registerLocaleData(zh);

export let AppInjector: Injector;

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        CoreModule,
        SharedModule
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
