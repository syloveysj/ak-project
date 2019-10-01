import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from '@feature/home/home/home.component';

const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        data: {title: 'Dashboard', componentName: 'HomeComponent', module: '/index', keepAlive: true}
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class HomeRoutingModule {
}
