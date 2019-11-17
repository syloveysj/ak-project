import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TaskComponent} from "@feature/task/task.component";
import {ScheduleJobComponent} from "@feature/task/schedule-job/schedule-job.component";
import {TenantJobComponent} from "@feature/task/tenant-job/tenant-job.component";

const routes: Routes = [
    {
        path: '',
        data: {pathname: '/task', title: '定时任务'},
        component: TaskComponent,
        children: [
            {
                path: 'scheduleJob',
                component: ScheduleJobComponent,
                data: {title: '任务列表', componentName: 'ScheduleJobComponent', module: '/task/scheduleJob', keepAlive: true}
            },
            {
                path: 'tenantJob',
                component: TenantJobComponent,
                data: {title: '租户任务', componentName: 'TenantJobComponent', module: '/task/tenantJob', keepAlive: true}
            }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TaskRoutingModule {
}
