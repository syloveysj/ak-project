import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TenantComponent} from "@feature/tenant/tenant.component";
import {TenantCenterComponent} from "@feature/tenant/tenant-center/tenant-center.component";

const routes: Routes = [
    {
        path: '',
        data: {pathname: '/tenant', title: '租户管理'},
        component: TenantComponent,
        children: [
            {
                path: 'center',
                component: TenantCenterComponent,
                data: {title: '租户信息', componentName: 'TenantCenterComponent', module: '/tenant/center', keepAlive: true}
            }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TenantRoutingModule {
}
