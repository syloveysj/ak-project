import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {GatewayService} from "@service/http/gateway.service";
import {Store} from "@ngrx/store";
import * as fromRoot from "@store/reducers";
import {Observable} from "rxjs";

@Component({
    selector: 'app-apis-server-win',
    template: `
        <form nz-form [formGroup]="form">
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzRequired nzFor="typeId">应用分类</nz-form-label>
                <nz-form-control [nzSm]="14" [nzXs]="24">
                    <nz-select formControlName="typeId" id="typeId" nzPlaceHolder="请选择">
                        <nz-option *ngFor="let item of applicationTypes$ | async" [nzValue]="item.id"
                                   [nzLabel]="item.typeName"></nz-option>
                    </nz-select>
                </nz-form-control>
            </nz-form-item>
            <nz-form-item>
                <nz-form-label [nzSm]="6" [nzXs]="24" nzFor="alias" nzRequired>应用名称</nz-form-label>
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
export class ApisServerWinComponent implements OnInit {

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                private store$: Store<fromRoot.State>,
                public gatewayService: GatewayService) {
    }

    form: FormGroup;
    loading = false;
    // 应用分类
    applicationTypes$: Observable<any[]>;

    ngOnInit(): void {
        this.applicationTypes$ = this.store$.select(fromRoot.getApplicationTypes);


        this.form = this.fb.group({
            typeId: [null, [Validators.required]],
            alias: [null, [Validators.required]],
            memo: [null]
        });
    }

}
