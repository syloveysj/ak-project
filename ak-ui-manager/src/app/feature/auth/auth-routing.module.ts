import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "@feature/auth/auth.component";
import {LoginComponent} from "@feature/auth/login.component";

const routes: Routes = [
    {
        path: '',
        component: AuthComponent,
        children: [
            {path: '', redirectTo: 'auth/login', pathMatch: 'full'},
            {
                path: 'auth/login',
                component: LoginComponent,
                data: {title: '登录', componentName: 'LoginComponent', module: '/auth/login', keepAlive: false}
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthRoutingModule {
}
