import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-reset-filters',
    template: `
        <ng-container>
            <button nz-button nz-tooltip [nzTitle]="'Refresh'" (click)="resetFilters.emit()">
                <i nz-icon nzType="sync" nzTheme="outline"></i>
            </button>
        </ng-container>
    `,
    styles: []
})
export class ResetFiltersComponent {
    @Input() title = '刷新';
    @Output() resetFilters = new EventEmitter<void>();
}
