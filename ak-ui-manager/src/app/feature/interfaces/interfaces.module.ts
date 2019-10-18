import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {InterfacesRoutingModule} from "@feature/interfaces/interfaces-routing.module";
import {InterfacesComponent} from "@feature/interfaces/interfaces.component";
import {ApisComponent} from "@feature/interfaces/apis/apis.component";
import {ApisManagerSiderComponent} from "@feature/interfaces/apis/components/apis-manager-sider.component";

@NgModule({
    entryComponents: [
        ApisManagerSiderComponent,
    ],
    imports: [
        SharedModule,
        InterfacesRoutingModule
    ],
    declarations: [
        InterfacesComponent,

        ApisComponent,
        ApisManagerSiderComponent,
    ],
    providers: [],
    exports: []
})

export class InterfacesModule {
}
