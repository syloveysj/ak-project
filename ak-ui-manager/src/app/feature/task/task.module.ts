import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {TaskRoutingModule} from "@feature/task/task-routing.module";
import {TaskComponent} from "@feature/task/task.component";
import {ScheduleJobComponent} from "@feature/task/schedule-job/schedule-job.component";
import {TenantJobComponent} from "@feature/task/tenant-job/tenant-job.component";

@NgModule({
    entryComponents: [
    ],
    imports: [
        SharedModule,
        TaskRoutingModule
    ],
    declarations: [
        TaskComponent,
        ScheduleJobComponent,
        TenantJobComponent
    ],
    providers: [],
    exports: []
})

export class TaskModule {
}
