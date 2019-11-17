import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {TenantRoutingModule} from "@feature/tenant/tenant-routing.module";
import {TenantComponent} from "@feature/tenant/tenant.component";
import {TenantCenterComponent} from "@feature/tenant/tenant-center/tenant-center.component";

@NgModule({
    entryComponents: [
    ],
    imports: [
        SharedModule,
        TenantRoutingModule
    ],
    declarations: [
        TenantComponent,
        TenantCenterComponent,
    ],
    providers: [],
    exports: []
})

export class TenantModule {
}
