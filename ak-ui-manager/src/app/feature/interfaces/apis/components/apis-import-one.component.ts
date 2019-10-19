import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {GatewayService} from "@service/http/gateway.service";
import {Config} from "@config/config";
import {FormBuilder} from "@angular/forms";

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
                        <nz-option-group nzLabel="Manager">
                            <nz-option nzValue="jack" nzLabel="Jack"></nz-option>
                            <nz-option nzValue="lucy" nzLabel="Lucy"></nz-option>
                        </nz-option-group>
                        <nz-option-group nzLabel="Engineer">
                            <nz-option nzValue="Tom" nzLabel="tom"></nz-option>
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
                public gatewayService: GatewayService) {
    }

    serverId: string;
    jsonUrl: string;
    jsonText: string;
    isUrl: boolean = true;

    ngOnInit(): void {
    }

    getFromValues() {
        return {
            serverId: this.serverId,
            jsonUrl: this.isUrl ? this.jsonUrl : '',
            jsonText: this.isUrl ? '' : this.jsonText
        }
    }
}
