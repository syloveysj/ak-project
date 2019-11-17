import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MonitorComponent} from "@feature/monitor/monitor.component";
import {MonitorCenterComponent} from "@feature/monitor/monitor-center/monitor-center.component";

const routes: Routes = [
    {
        path: '',
        data: {pathname: '/monitor', title: '监控中心'},
        component: MonitorComponent,
        children: [
            {
                path: 'center',
                component: MonitorCenterComponent,
                data: {title: '异常监控', componentName: 'MonitorCenterComponent', module: '/monitor/center', keepAlive: true}
            }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MonitorRoutingModule {
}
