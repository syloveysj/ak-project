import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GatewayComponent} from '@feature/gateway/gateway.component';
import {ClusterComponent} from '@feature/gateway/cluster/cluster.component';
import {RouterComponent} from '@feature/gateway/router/router.component';
import {AdvancedRouterComponent} from '@feature/gateway/advanced-router/advanced-router.component';
import {AuthorizeComponent} from '@feature/gateway/authorize/authorize.component';

const routes: Routes = [
    {
        path: '',
        data: {pathname: '/gateway', title: '网关配置'},
        component: GatewayComponent,
        children: [
            {
                path: 'cluster',
                component: ClusterComponent,
                data: {title: '集群管理', componentName: 'ClusterComponent', module: '/gateway/cluster', keepAlive: true}
            },
            {
                path: 'router',
                component: RouterComponent,
                data: {title: '路由管理', componentName: 'RouterComponent', module: '/gateway/router', keepAlive: true}
            },
            {
                path: 'advRouter',
                component: AdvancedRouterComponent,
                data: {title: '高级路由管理', componentName: 'AdvancedRouterComponent', module: '/gateway/advRouter', keepAlive: true}
            },
            {
                path: 'authorize',
                component: AuthorizeComponent,
                data: {title: '服务授权管理', componentName: 'AuthorizeComponent', module: '/gateway/authorize', keepAlive: true}
            }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class GatewayRoutingModule {
}
