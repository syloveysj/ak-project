import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {GatewayService} from "@service/http/gateway.service";

@Component({
    selector: 'app-apis-porfolio-win',
    template: `
        <form nz-form [formGroup]="form">
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzRequired nzFor="pid">上级节点</nz-form-label>
                <nz-form-control [nzSm]="14" [nzXs]="24">
                    <nz-select formControlName="pid" id="pid" nzPlaceHolder="请选择">
                        <nz-option nzValue="" nzLabel="根节点"></nz-option>
                        <nz-option nzValue="jack" nzLabel="Jack"></nz-option>
                        <nz-option nzValue="lucy" nzLabel="Lucy"></nz-option>
                    </nz-select>
                </nz-form-control>
            </nz-form-item>
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzFor="alias" nzRequired>分类名称</nz-form-label>
                <nz-form-control [nzSm]="14" [nzXs]="24">
                    <input nz-input formControlName="alias" id="alias" />
                </nz-form-control>
            </nz-form-item>
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzFor="memo">备&emsp;&emsp;注</nz-form-label>
                <nz-form-control [nzSm]="14" [nzXs]="24">
                    <input nz-input formControlName="memo" id="memo" />
                </nz-form-control>
            </nz-form-item>
        </form>
    `,
    styles: [`
    `],
})
export class ApisPortfolioWinComponent implements OnInit {

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                public gatewayService: GatewayService) {
    }

    form: FormGroup;
    loading = false;

    ngOnInit(): void {
        this.form = this.fb.group({
            pid: [null, [Validators.required]],
            serviceId: [null],
            alias: [null],
            memo: [null]
        });
    }

}
