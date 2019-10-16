import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {NzMessageService, NzModalService} from "ng-zorro-antd";
import {BaseService} from "@service/http/base.service";

@Component({
    selector: 'app-gateway-router-plugins-edit',
    template: `
    <form class="wrapper" nz-form [formGroup]="form">
        <div class="head">
            <div nz-row>
                <div nz-col nzSpan="17">
                    <nz-form-item nzFlex>
                        <nz-form-label nzRequired nzFor="name" style="width: 125px;">插件名称</nz-form-label>
                        <nz-form-control style="width: 100%;">
                            <input nz-input placeholder="" formControlName="name" id="name"
                                   maxlength="100"/>
                        </nz-form-control>
                    </nz-form-item>
                </div>
                <div nz-col nzSpan="4" nzOffset="2">
                    <nz-form-item nzFlex>
                        <nz-form-label nzFor="enabled" style="width: 115px;">启用</nz-form-label>
                        <nz-form-control style="width: 100%;">
                            <nz-switch formControlName="enabled" id="enabled" nzCheckedChildren="开" nzUnCheckedChildren="关"></nz-switch>
                        </nz-form-control>
                    </nz-form-item>
                </div>
            </div>
            <div nz-row>
                <div nz-col nzSpan="23">
                    <nz-form-item nzFlex>
                        <nz-form-label nzFor="config" style="width: 115px;">配置</nz-form-label>
                        <nz-form-control style="width: 100%;">
                            <textarea placeholder="" formControlName="config" id="config" nz-input rows="3"></textarea>
                        </nz-form-control>
                    </nz-form-item>
                </div>
            </div>
        </div>
    </form>
    `,
    styles: [`
    `]
})
export class RouterPluginsEditComponent implements OnInit {

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService) {
    }

    @Input() bean: any;
    form: FormGroup;

    ngOnInit(): void {
        this.form = this.fb.group({
            name: [this.bean == null ? null : this.bean.name, [Validators.required]],
            enabled: [this.bean == null ? true : this.bean.enabled],
            config: [this.bean == null ? '' : this.bean.config]
        });
    }
}
