import {NgModule, Optional, SkipSelf} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {AppRoutingModule} from '@core/app-routing.module';
import {HeadBarComponent} from '@core/component/head-bar.component';
import {AppComponent} from '@core/app.component';

@NgModule({
    entryComponents: [
    ],
    imports: [
      SharedModule,
      AppRoutingModule
    ],
    declarations: [
      HeadBarComponent,
      AppComponent
    ],
    exports: [
        HeadBarComponent,
    ]
})
export class CoreModule {
    constructor(
        @Optional() @SkipSelf() parentModule: CoreModule) {
        if (parentModule) {
            throw new Error('CoreModule 已经装载，请仅在 AppModule 中引入该模块。');
        }
    }
}
