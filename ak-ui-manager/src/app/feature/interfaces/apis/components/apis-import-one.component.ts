import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from "@config/config";
import {FormBuilder} from "@angular/forms";
import {isEmpty} from "@core/utils/string.util";
import {Store} from "@ngrx/store";
import * as fromRoot from "@store/reducers";
import {Observable} from "rxjs";

@Component({
    selector: 'app-apis-import-one',
    template: `
        <div class="steps-content">
            <div nz-row>
                <div nz-col nzSpan="4" class="label">
                    选择应用：
                </div>
                <div nz-col nzSpan="8" class="value">
                    <nz-select [(ngModel)]="serverId" nzAllowClear nzPlaceHolder="请选择" style="width: 100%">
                        <nz-option-group  *ngFor="let type of applicationTypes$ | async" [nzLabel]="type.typeName">
                            <ng-container *ngFor="let service of services$ | async">
                                <nz-option *ngIf="service.typeId === type.id" [nzValue]="service.id" [nzLabel]="service.alias"></nz-option>
                            </ng-container>
                        </nz-option-group>
                    </nz-select>
                </div>
                <div nz-col nzSpan="11" class="value" style="padding-top: 2px; margin-left: 30px;">
                    <nz-switch [(ngModel)]="isUrl" nzCheckedChildren="路径导入" nzUnCheckedChildren="文本导入"></nz-switch>
                </div>
            </div>
            <div nz-row *ngIf="isUrl">
                <div nz-col nzSpan="4" class="label">
                    Swagger Json地址：
                </div>
                <div nz-col nzSpan="19" class="value">
                    <input [(ngModel)]="jsonUrl" nz-input placeholder="http://127.0.0.1:8080/v2/api-docs" />
                </div>
            </div>
            <div nz-row *ngIf="!isUrl">
                <div nz-col nzSpan="4" class="label">
                    文本：
                </div>
                <div nz-col nzSpan="19" class="value">
                    <textarea [(ngModel)]="jsonText" nz-input rows="20" placeholder="Swagger Json"></textarea>
                </div>
            </div>
        </div>
    `,
    styles: [`
        .label {
            text-align: right;
            line-height: 40px;
        }
        .value {
            text-align: left;
            margin-left: 10px;
        }
        .steps-content {
            margin-top: 2px;
            border: 1px dashed #e9e9e9;
            border-radius: 6px;
            background-color: #fafafa;
            min-height: 200px;
            text-align: center;
            padding: 10px 10px;
        }
    `]
})
export class ApisImportOneComponent implements OnInit {

    @Input() bean: any;

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                private store$: Store<fromRoot.State>) {
    }

    serverId: string;
    jsonUrl: string = 'http://127.0.0.1:8080/v2/api-docs';
    jsonText: string;
    isUrl: boolean = true;

    applicationTypes$: Observable<any[]>;
    services$: Observable<any[]>;

    ngOnInit(): void {
        this.applicationTypes$ = this.store$.select(fromRoot.getApplicationTypes);
        this.services$ = this.store$.select(fromRoot.getServices);
    }

    getFromValues(): any {
        if(isEmpty(this.serverId)) {
            this.nzMessageService.create('error', `请选择需要导入的应用`);
            return null;
        }
        if(this.isUrl && isEmpty(this.jsonUrl)) {
            this.nzMessageService.create('error', `请填写正确的url地址`);
            return null;
        }
        if(!this.isUrl && isEmpty(this.jsonText)) {
            this.nzMessageService.create('error', `请填写正确json文本`);
            return null;
        }
        return {
            serverId: this.serverId,
            jsonUrl: this.isUrl ? this.jsonUrl : '',
            jsonText: this.isUrl ? '' : this.jsonText
        }
    }
}
