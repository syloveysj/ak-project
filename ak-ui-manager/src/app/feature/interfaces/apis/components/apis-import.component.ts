import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {GatewayService} from "@service/http/gateway.service";

@Component({
    selector: 'app-apis-import',
    templateUrl: './apis-import.component.html',
    styles: [`
        .steps-content {
            margin-top: 16px;
            border: 1px dashed #e9e9e9;
            border-radius: 6px;
            background-color: #fafafa;
            min-height: 200px;
            text-align: center;
            padding-top: 80px;
        }
    `],
})
export class ApisImportComponent implements OnInit {

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
            routeName: ['', [Validators.required]],
        });
    }

    current = 0;

    index = 'First-content';

    pre(): void {
        this.current -= 1;
        this.changeContent();
    }

    next(): void {
        this.current += 1;
        this.changeContent();
    }

    done(): void {
        console.log('done');
    }

    changeContent(): void {
        switch (this.current) {
            case 0: {
                this.index = 'First-content';
                break;
            }
            case 1: {
                this.index = 'Second-content';
                break;
            }
            case 2: {
                this.index = 'third-content';
                break;
            }
            default: {
                this.index = 'error';
            }
        }
    }
}
