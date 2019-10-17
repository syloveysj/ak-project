import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {InterfacesRoutingModule} from "@feature/interfaces/interfaces-routing.module";
import {InterfacesComponent} from "@feature/interfaces/interfaces.component";

@NgModule({
    entryComponents: [
    ],
    imports: [
        SharedModule,
        InterfacesRoutingModule
    ],
    declarations: [
        InterfacesComponent,
    ],
    providers: [],
    exports: []
})

export class InterfacesModule {
}
