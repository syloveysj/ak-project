import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {GatewayRoutingModule} from '@feature/gateway/gateway-routing.module';
import {GatewayComponent} from '@feature/gateway/gateway.component';
import {ClusterComponent} from '@feature/gateway/cluster/cluster.component';
import {RouterComponent} from '@feature/gateway/router/router.component';
import {AdvancedRouterComponent} from '@feature/gateway/advanced-router/advanced-router.component';
import {AuthorizeComponent} from '@feature/gateway/authorize/authorize.component';
import {ClusterEditComponent} from '@feature/gateway/cluster/components/cluster-edit.component';
import {RouterEditComponent} from '@feature/gateway/router/components/router-edit.component';
import {AdvancedRouterEditComponent} from '@feature/gateway/advanced-router/components/advanced-router-edit.component';
import {CertificateEditComponent} from "@feature/gateway/authorize/components/certificate-edit.component";
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";

@NgModule({
    entryComponents: [
        ClusterEditComponent,
        RouterEditComponent,
        AdvancedRouterEditComponent,
        CertificateEditComponent,
        RouterPluginsEditComponent,
    ],
    imports: [
        SharedModule,
        GatewayRoutingModule
    ],
    declarations: [
        GatewayComponent,

        ClusterComponent,
        ClusterEditComponent,

        RouterComponent,
        RouterEditComponent,

        AdvancedRouterComponent,
        AdvancedRouterEditComponent,

        AuthorizeComponent,
        CertificateEditComponent
    ],
    providers: [],
    exports: []
})

export class GatewayModule {
}
