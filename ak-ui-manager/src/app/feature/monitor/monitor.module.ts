import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {MonitorRoutingModule} from "@feature/monitor/monitor-routing.module";
import {MonitorComponent} from "@feature/monitor/monitor.component";
import {MonitorCenterComponent} from "@feature/monitor/monitor-center/monitor-center.component";

@NgModule({
    entryComponents: [
    ],
    imports: [
        SharedModule,
        MonitorRoutingModule
    ],
    declarations: [
        MonitorComponent,
        MonitorCenterComponent,
    ],
    providers: [],
    exports: []
})

export class MonitorModule {
}
