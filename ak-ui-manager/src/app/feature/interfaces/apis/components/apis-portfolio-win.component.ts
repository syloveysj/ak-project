import {Component, Input, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {finalize} from "rxjs/operators";
import {InterfacesService} from "@service/http/interfaces.service";
import {UUID} from "angular2-uuid";

@Component({
    selector: 'app-apis-porfolio-win',
    template: `
        <form nz-form [formGroup]="form">
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzFor="pid">上级节点</nz-form-label>
                <nz-form-control [nzSm]="14" [nzXs]="24">
                    <nz-select formControlName="pid" id="pid" nzPlaceHolder="请选择" [nzAllowClear]="false">
                        <nz-option nzValue="aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa" nzLabel="根节点"></nz-option>
                        <nz-option *ngFor="let item of classifies" [nzValue]="item.id"
                                   [nzLabel]="item.alias"></nz-option>
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

    @Input() serverId = null;

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                public interfacesService: InterfacesService) {
    }

    form: FormGroup;
    loading = true;
    classifies = [];

    ngOnInit(): void {
        this.form = this.fb.group({
            id: [UUID.UUID(), [Validators.required]],
            pid: ['aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'],
            serviceId: [this.serverId, [Validators.required]],
            alias: [null, [Validators.required]],
            memo: [null]
        });

        this.interfacesService.getApisClassifyList({pid: 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', serviceId: this.serverId}).pipe(
            finalize(() => this.loading = false)
        ).subscribe(
            (res) => {
                this.classifies = res;
            }
        );
    }

}
