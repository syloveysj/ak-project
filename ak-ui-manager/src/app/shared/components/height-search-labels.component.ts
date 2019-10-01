import {Component, EventEmitter, Input, Output, TemplateRef} from '@angular/core';
import {InputBoolean} from '@core/utils/convert.util';

@Component({
    selector: 'app-height-search-labels',
    template: `
        <ng-container>
            <!--显示高级搜索-->
            <ng-container *ngIf="advancedSearch">
                <button nz-button (click)="advancedSearchClick.emit(isShowAdviceSearch = !isShowAdviceSearch)">
                    <ng-container *ngIf="isShowAdviceSearch then packUpTemplate; else showAllTemplate"></ng-container>
                    <ng-template #showAllTemplate><span>{{'高级'}}</span>
                        <i nz-icon nzType="down" nzTheme="outline"></i>
                    </ng-template>
                    <ng-template #packUpTemplate><span>{{'普通'}}</span>
                        <i nz-icon nzType="up" nzTheme="outline"></i>
                    </ng-template>
                </button>
            </ng-container>
            <ng-container *ngIf="resetFilters">
                <app-reset-filters (resetFilters)="resetFiltersClick.emit()"></app-reset-filters>
            </ng-container>
            <!--显示任务进度按钮-->
            <ng-container *ngIf="progress">
                <a (click)="progressClick.emit()" nz-tooltip [nzTitle]="processText">
                    <button nz-button
                            nz-popover
                            nzTrigger="click"
                            nzPlacement="bottomRight"
                            [nzTitle]="processTitle"
                            [nzContent]="processTemplate">
                        <i nz-icon nzType="bars" nzTheme="outline"></i>
                    </button>
                </a>
            </ng-container>

            <!--显示下载按钮-->
            <ng-container *ngIf="download">
                <ng-container *ngIf="downloadHref; else downloadTemplateRef">
                    <a [href]="downloadHref" target="_blank" nz-tooltip [nzTitle]="downloadText">
                        <button nz-button>
                            <i nz-icon nzType="download" nzTheme="outline"></i>
                        </button>
                    </a>
                </ng-container>
                <ng-template #downloadTemplateRef [ngTemplateOutlet]="downloadTemplate"></ng-template>
            </ng-container>

            <!--设置自定义列按钮-->
            <ng-container *ngIf="setting">
                <button nz-button (click)="settingClick.emit()" nz-tooltip [nzTitle]="'自定义列'">
                    <i nz-icon nzType="setting" nzTheme="outline"></i>
                </button>
            </ng-container>

            <!--按钮-->
            <ng-container *ngIf="question">
                <a [href]="questionHref" target="_blank" nz-tooltip [nzTitle]="questionText">
                    <button nz-button>
                        <i nz-icon nzType="question" nzTheme="outline"></i>
                    </button>
                </a>
            </ng-container>
            <ng-container>
                <ng-content></ng-content>
            </ng-container>
        </ng-container>
    `,
    styles: []
})

/**
 * @description 下载支持两种格式
 * example:
 *
 * 自定义模板引用
 * <app-height-search-labels download [downloadTemplate]="downloadTemplate"></app-height-search-labels>
 *
 * 添加链接
 * <app-height-search-labels download [downloadHref]="'http://www....'"></app-height-search-labels>
 */
export class HeightSearchLabelsComponent {
    @Input() isShowAdviceSearch = false; // 普通搜索/高级搜索状态切换
    @Input() @InputBoolean() advancedSearch = false; // 是否显示高级搜索文本
    @Input() @InputBoolean() download = false; // 是否显示下载按钮
    @Input() @InputBoolean() progress = false; // 是否显示进度按钮
    @Input() @InputBoolean() setting = false; // 是否显示设置按钮
    @Input() @InputBoolean() question = false; // 是否显示帮助按钮
    @Input() @InputBoolean() resetFilters = false; // 是否显示重置搜索按钮
    @Input() questionHref = null;   // 帮助文档链接地址
    @Input() downloadHref = null;   // 下载链接地址
    @Input() processText = '显示任务进度';   // 进度文本
    @Input() processTitle = '进度标题';   // 进度标题
    @Input() downloadText = '下载';   // 下载文本
    @Input() settingText = '设置自定义列';   // 设置自定义列
    @Input() questionText = '帮助';   // 查看文档

    @Output() advancedSearchClick = new EventEmitter<boolean>();
    @Output() settingClick = new EventEmitter<void>();
    @Output() progressClick = new EventEmitter<void>();
    @Output('resetFilters') resetFiltersClick = new EventEmitter<void>();

    @Input() processTemplate: TemplateRef<void>;
    @Input() downloadTemplate: TemplateRef<void>;
}
