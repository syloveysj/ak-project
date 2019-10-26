import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {QuillModule} from 'ngx-quill';
import {quillConfig} from '@core/utils/quill-config.util';
import {TagCloseComponent} from '@shared/components/tag-close.component';
import {PipeModule} from '@pipe/index';
import {DirectiveModule} from "@shared/directive";
import {ResetFiltersComponent} from '@shared/components/reset-filters.component';
import {HeightSearchLabelsComponent} from '@shared/components/height-search-labels.component';
import {TablePaginationInfoComponent} from '@shared/components/table-pagination-info.component';
import {InputGroupWrapperComponent} from '@shared/components/input-group-wrapper.component';
import {ApplicationTreeSelectComponent} from "@feature/interfaces/apis/components/application-tree-select.component";
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";

// 在这里注入模块
const MODULES = [
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    NgZorroAntdModule,
    PipeModule,
    DirectiveModule,
];

// 在这里注入组件，指令，管道
const DECLARATIONS = [
    TagCloseComponent,
    ResetFiltersComponent,
    HeightSearchLabelsComponent,
    TablePaginationInfoComponent,
    InputGroupWrapperComponent,
    ApplicationTreeSelectComponent,
    RouterPluginsEditComponent,
];

// 在这里注入组件，指令，管道
const EXPORT_COMPONENTS = [
    TagCloseComponent,
    ResetFiltersComponent,
    HeightSearchLabelsComponent,
    TablePaginationInfoComponent,
    InputGroupWrapperComponent,
    ApplicationTreeSelectComponent,
    RouterPluginsEditComponent,
];

@NgModule({
    entryComponents: [
    ],
    imports: [
        ...MODULES,
        QuillModule.forRoot({
            modules: quillConfig
        }),
    ],
    declarations: [
        ...DECLARATIONS
    ],
    exports: [
        ...MODULES,
        ...EXPORT_COMPONENTS,
    ]
})
export class SharedModule {
}
