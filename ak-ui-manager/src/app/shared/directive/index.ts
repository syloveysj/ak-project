import {NgModule} from '@angular/core';
import {HighlightDirective} from "@shared/directive/highlight.directive";
import {DebounceClickDirective} from "@shared/directive/debounce-click.directive";
import {NumberOnlyDirective} from "@shared/directive/number-only.directive";
import {CalcTableScrollyDirective} from "@shared/directive/calc-table-scrolly.directive";
import {CalcTableWidthDirective} from "@shared/directive/calc-table-width.directive";
import {TableResizableDirective} from "@shared/directive/table-resizable-directive";
import {CalcTableTheadWidthDirective} from "@shared/directive/calc-table-thead-width.directive";
import {CalcThWidthDirective} from "@shared/directive/calc-th-width.directive";
import {AutoHeightBlockDirective} from "@shared/directive/auto-height-block.directive";

const directives = [
    HighlightDirective,
    DebounceClickDirective,
    NumberOnlyDirective,
    CalcTableScrollyDirective,
    CalcTableWidthDirective,
    TableResizableDirective,
    CalcTableTheadWidthDirective,
    CalcThWidthDirective,
    AutoHeightBlockDirective,
];

@NgModule({
    declarations: [
        ...directives
    ],
    exports: [
        ...directives
    ]
})
export class DirectiveModule {
}
