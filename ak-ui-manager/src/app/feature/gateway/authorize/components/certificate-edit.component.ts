import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {NzMessageService, NzModalService} from "ng-zorro-antd";
import {BaseService} from "@service/http/base.service";

@Component({
    selector: 'app-gateway-certificate-edit',
    template: `
    <form class="wrapper" nz-form [formGroup]="form">
        <div class="head">
            <div nz-row>
                <div nz-col nzSpan="23">
                    <nz-form-item nzFlex>
                        <nz-form-label nzRequired nzFor="field1" style="width: 115px;">凭证名称</nz-form-label>
                        <nz-form-control style="width: 100%;">
                            <input nz-input placeholder="" formControlName="field1" id="field1"
                                   maxlength="100"/>
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
export class CertificateEditComponent implements OnInit {

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
            field1: [this.bean == null ? null : this.bean.field1, [Validators.required]]
        });
    }
}
