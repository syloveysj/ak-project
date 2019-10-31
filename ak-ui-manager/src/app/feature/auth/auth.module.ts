import { NgModule } from '@angular/core';
import { AuthComponent } from './auth.component';
import { SharedModule } from '@shared/shared.module';
import { AuthRoutingModule } from './auth-routing.module';
import {LoginComponent} from './login.component';

@NgModule({
    imports: [
        SharedModule,
        AuthRoutingModule,
    ],
    declarations: [
        AuthComponent,
        LoginComponent,
    ]
})

export class AuthModule {

}
