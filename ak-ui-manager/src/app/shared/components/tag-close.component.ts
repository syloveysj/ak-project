import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Option} from '@model/common';
import {convertToDate} from '@core/utils/date.util';
import {transFormMap} from '@core/utils/tool.util';

@Component({
    selector: 'app-tag-close',
    template: `

        <!--单选类型-->
        <ng-container *ngIf="mode === 'default'">
            <div class="tag">
                <nz-tag *ngIf="selectedId !== null"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    <span>{{attrName}}</span>：<span
                        style="color:#0046f0">{{dataMap[selectedId]?.text | textSubstring:0:10 }}</span>
                </nz-tag>
            </div>
        </ng-container>

        <!--多选类型-->
        <ng-container *ngIf="mode === 'multiple'">
            <div class="tag">
                <nz-tag *ngIf="selectedId?.length"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    <span>{{attrName}}</span>：
                    <span style="color:#0046f0">
                        <ng-container *ngIf="selectedId?.length">
                            <ng-container *ngFor="let item of selectedId; let index = index;">
                                {{ dataMap[item]?.text | textSubstring:0:10 }}
                                <ng-container *ngIf="index !== selectedId?.length - 1">,</ng-container>
                            </ng-container>
                        </ng-container>
                    </span>
                </nz-tag>
            </div>
        </ng-container>

        <!--输入框input类型-->
        <ng-container *ngIf="mode === 'search'">
            <div class="tag">
                <nz-tag *ngIf="keyword?.length"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    <span>{{attrName}}</span>:
                    <span style="color:#0046f0">
                        <ng-container>{{keyword}}</ng-container>
                    </span>
                </nz-tag>
            </div>
        </ng-container>
        <!--组合输入框input类型-->
        <ng-container *ngIf="mode === 'groupSearch'">
            <div class="tag">
                <nz-tag *ngIf="dynamicKey !== null && keyword?.length"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    {{dataMap[dynamicKey]?.text}}
                    <span *ngIf="conditionKey">{{conditionKey}}</span>：
                    <span style="color:#0046f0">{{keyword}}</span>
                </nz-tag>
            </div>
        </ng-container>

        <!--具体日期类型-->
        <ng-container *ngIf="mode === 'date'">
            <div class="tag">
                <nz-tag *ngIf="date"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    <span>{{attrName}}</span>:
                    <span style="color:#0046f0">
                        <ng-container>
                            {{date}}
                        </ng-container>
                    </span>
                </nz-tag>
            </div>
        </ng-container>

        <!--日期范围类型-->
        <ng-container *ngIf="mode === 'dateRange'">
            <div class="tag">
                <nz-tag *ngIf="startDate && endDate"
                        [nzMode]="tagMode"
                        (nzOnClose)="tagClose.emit()">
                    <ng-container *ngIf="Object.keys(attrNameOptions).length then aTemplate; else bTemplate"></ng-container>
                    <ng-template #aTemplate>
                        <span>{{attrNameOptions[attrName]?.text}}:</span>
                    </ng-template>
                    <ng-template #bTemplate>
                        <span>{{attrName}}:</span>
                    </ng-template>
                    <span style="color:#0046f0">
                        <ng-container>
                            {{startFormatDate}} ~ {{endFormatDate}}
                        </ng-container>
                    </span>
                </nz-tag>
            </div>
        </ng-container>
    `,
    styles: [
            `
            .tag {
                display: inline-block;
                margin-top: 10px;
            }
        `
    ]
})
export class TagCloseComponent {
    @Input() mode: 'default' | 'multiple' | 'search' | 'groupSearch' | 'date' | 'dateRange' = 'default';
    @Input() selectedId: any; // 当前选中的id
    @Input() attrName = 'Label';    // label名称
    @Input() data: Option[] = [];   // 渲染的数据集合
    @Output() tagClose = new EventEmitter<void>();  // 点击某个item的事件触发器
    @Input() dynamicKey = ''; // 动态筛选值
    @Input() conditionKey = ''; // 动态条件值
    @Input() keyword = ''; // 关键字
    @Input() startDate: string | Date = null; // 日期范围-开始范围
    @Input() endDate: string | Date = null; // 日期范围-结束范围
    @Input() date = null;  // 具体日期
    @Input() tagMode: 'closeable' | 'default' | 'checkable' = 'closeable';
    private selfAttrNames: Option[] = [];

    @Input() get dataMap() {
        return transFormMap(this.data);
    }

    @Input() set attrNameOptions(value: Option[]) {
        this.selfAttrNames = value;
    }

    get startFormatDate() {
        if (this.startDate instanceof Date) {
            return convertToDate(this.startDate);
        } else {
            return this.startDate;
        }
    }

    get endFormatDate() {
        if (this.endDate instanceof Date) {
            return convertToDate(this.endDate);
        } else {
            return this.endDate;
        }
    }

    get attrNameOptions(): Option[] {
        return transFormMap(this.selfAttrNames);
    }

    get Object() {
        return Object;
    }
}
