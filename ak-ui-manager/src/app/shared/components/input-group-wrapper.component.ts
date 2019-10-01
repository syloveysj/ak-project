import {Component, Input} from '@angular/core';

/**
 * @description 用法。这里如果想要使用后缀，那么子元素必须添加class="end"这个属性
 * example:
 *             <app-input-group-wrapper location="end">
 *                 <input type="text" class="end" nz-input [(ngModel)]="_queryParams.typeDescription" (keyup)="typeDescriptionChange()"
 *                 placeholder="费用描述">
 *              </app-input-group-wrapper>
 */
@Component({
    selector: 'app-input-group-wrapper',
    template: `
        <ng-template *ngIf="location === 'start'; then startTemplate; else endTemplate;"></ng-template>
        <ng-template #startTemplate>
            <nz-input-group class="pre-search-icon" [nzPrefix]="icon">
                <ng-content></ng-content>
            </nz-input-group>
        </ng-template>
        <ng-template #endTemplate>
            <nz-input-group class="pre-search-icon" [nzSuffix]="icon">
                <ng-content select=".end"></ng-content>
            </nz-input-group>
        </ng-template>
        <ng-template #icon>
            <i nz-icon [type]="type"></i>
        </ng-template>
    `,
    styles: [``]
})

export class InputGroupWrapperComponent {
    @Input() type = 'search'; // 图标类型
    @Input() location: 'start'|'end' = 'start'; // 图标位置 - 前缀: start, 后缀: end
}
