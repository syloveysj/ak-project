import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ErrorComponent} from './component/error.component';

const routes: Routes = [
    {
        path: '',
        loadChildren: '@feature/auth/auth.module#AuthModule',
    },
    {
        path: 'index',
        loadChildren: '@feature/home/home.module#HomeModule',
        data: {module: '/index', preload: true},
    }, {
        path: 'gateway',
        loadChildren: '@feature/gateway/gateway.module#GatewayModule',
        data: {module: '/gateway', preload: true},
    }, {
        path: 'interfaces',
        loadChildren: '@feature/interfaces/interfaces.module#InterfacesModule',
        data: {module: '/interfaces', preload: true},
    }, {
        path: 'tenant',
        loadChildren: '@feature/tenant/tenant.module#TenantModule',
        data: {module: '/tenant', preload: true},
    }, {
        path: 'monitor',
        loadChildren: '@feature/monitor/monitor.module#MonitorModule',
        data: {module: '/monitor', preload: true},
    }, {
        path: 'task',
        loadChildren: '@feature/task/task.module#TaskModule',
        data: {module: '/task', preload: true},
    }, {
        path: '**', component: ErrorComponent,
        data: {title: '404', componentName: 'ErrorComponent', module: '/404', keepAlive: true}
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
        scrollOffset: [0, 0],
        enableTracing: false, // <-- 开发调试专用
        // preloadingStrategy: PreloadAllModules, // 预加载所有模块
    })],
    exports: [RouterModule]
})
export class AppRoutingModule {

}
