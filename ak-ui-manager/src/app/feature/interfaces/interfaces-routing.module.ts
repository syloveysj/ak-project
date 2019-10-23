import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {InterfacesComponent} from "@feature/interfaces/interfaces.component";
import {ApisComponent} from "@feature/interfaces/apis/apis.component";

const routes: Routes = [
    {
        path: '',
        data: {pathname: '/interfaces', title: 'APIs管理'},
        component: InterfacesComponent,
        children: [
            {
                path: 'apis',
                component: ApisComponent,
                data: {title: 'APIs服务', componentName: 'ApisComponent', module: '/interfaces/apis', keepAlive: true}
            }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class InterfacesRoutingModule {
}
