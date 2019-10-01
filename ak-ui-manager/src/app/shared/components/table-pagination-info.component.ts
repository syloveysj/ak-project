import {Component, Input, TemplateRef, ViewChild} from '@angular/core';

@Component({
    selector: 'app-table-pagination-info',
    template: `
        <ng-container>
            <ng-container *ngIf="total > 0">{{range[0]}}-{{range[1]}},</ng-container>
            {{total}}{{'Page Items'}}
        </ng-container>
    `,
    styles: [
            `

        `
    ]
})
export class TablePaginationInfoComponent {
    @ViewChild('contentTemplate') templateRef: TemplateRef<void>;
    @Input() total;
    @Input() range;
}
