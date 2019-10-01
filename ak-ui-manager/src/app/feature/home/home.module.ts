import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {HomeRoutingModule} from '@feature/home/home-routing.module';
import {HomeComponent} from '@feature/home/home/home.component';

@NgModule({
    entryComponents: [
    ],
    imports: [
        SharedModule,
        HomeRoutingModule
    ],
    declarations: [
        HomeComponent,
    ],
    providers: [
    ],
    exports: [
    ]
})

export class HomeModule {
}
