import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {InterfacesRoutingModule} from "@feature/interfaces/interfaces-routing.module";
import {InterfacesComponent} from "@feature/interfaces/interfaces.component";
import {ApisComponent} from "@feature/interfaces/apis/apis.component";
import {ApisManagerSiderComponent} from "@feature/interfaces/apis/components/apis-manager-sider.component";
import {ApisListComponent} from "@feature/interfaces/apis/components/apis-list.component";
import {ApisPortfolioWinComponent} from "@feature/interfaces/apis/components/apis-portfolio-win.component";
import {ApisServerWinComponent} from "@feature/interfaces/apis/components/apis-server-win.component";
import {ApisPortfolioNodeComponent} from "@feature/interfaces/apis/components/apis-portfolio-node.component";
import {ApisServerSettingComponent} from "@feature/interfaces/apis/components/apis-server-setting.component";
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";
import {ApisImportComponent} from "@feature/interfaces/apis/components/apis-import.component";
import {ApisImportOneComponent} from "@feature/interfaces/apis/components/apis-import-one.component";
import {ApisImportTwoComponent} from "@feature/interfaces/apis/components/apis-import-two.component";
import {ApisImportThreeComponent} from "@feature/interfaces/apis/components/apis-import-three.component";

@NgModule({
    entryComponents: [
        ApisManagerSiderComponent,
        ApisListComponent,
        ApisPortfolioWinComponent,
        ApisServerWinComponent,
        ApisPortfolioNodeComponent,
        ApisServerSettingComponent,
        RouterPluginsEditComponent,
        ApisImportComponent,
        ApisImportOneComponent,
        ApisImportTwoComponent,
        ApisImportThreeComponent,
    ],
    imports: [
        SharedModule,
        InterfacesRoutingModule
    ],
    declarations: [
        InterfacesComponent,

        ApisComponent,
        ApisManagerSiderComponent,
        ApisListComponent,
        ApisPortfolioWinComponent,
        ApisServerWinComponent,
        ApisPortfolioNodeComponent,
        ApisServerSettingComponent,
        ApisImportComponent,
        ApisImportOneComponent,
        ApisImportTwoComponent,
        ApisImportThreeComponent,
    ],
    providers: [],
    exports: []
})

export class InterfacesModule {
}
