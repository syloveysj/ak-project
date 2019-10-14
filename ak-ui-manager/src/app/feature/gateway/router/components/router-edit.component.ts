import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {GatewayService} from "@service/http/gateway.service";
import {isNotEmpty} from "@core/utils/string.util";
import {UUID} from "angular2-uuid";
import {finalize} from "rxjs/operators";

@Component({
    selector: 'app-gateway-router-edit',
    templateUrl: `./router-edit.component.html`,
    styles: [
            `
        `
    ]
})
export class RouterEditComponent implements OnInit {

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                public gatewayService: GatewayService) {
    }

    @Input() bean: any;
    form: FormGroup;

    typesOptions = [
        {label: 'GET', value: 'GET', checked: false},
        {label: 'POST', value: 'POST', checked: false},
        {label: 'DELETE', value: 'DELETE', checked: false},
        {label: 'PUT', value: 'PUT', checked: false},
        {label: 'HEAD', value: 'HEAD', checked: false},
        {label: 'PATCH', value: 'PATCH', checked: false},
        {label: 'OPTIONS', value: 'OPTIONS', checked: false},
        {label: 'TRACE', value: 'TRACE', checked: false}
    ];
    clusterList = [];
    loading = false;
    allChecked = false;
    indeterminate = false;

    ngOnInit(): void {
        this.form = this.fb.group({
            routeName: [this.bean == null ? null : this.bean.name, [Validators.required]],
            methods: [this.typesOptions],
            hosts: [this.bean == null ? null : this.bean.hosts],
            paths: [this.bean == null ? null : this.bean.paths],
            host: [this.bean == null ? null : this.bean.host],
            connectTimeout: [this.bean == null ? 60000 : this.bean.connectTimeout],
            writeTimeout: [this.bean == null ? 60000 : this.bean.writeTimeout],
            readTimeout: [this.bean == null ? 60000 : this.bean.readTimeout],
            onlyHttps: [this.bean == null ? null : this.bean.onlyHttps]
        });

        this.initTaggets();
    }

    initTaggets() {
        this.loading = true;
        this.gatewayService.getUpstreamAll().pipe(
            finalize(() => this.loading = false)
        ).subscribe(
            (res: any[]) => {
                if(res != null) {
                    res.forEach(item => {
                        item.label = item.alias;
                        item.value = item.name;
                    })
                    this.clusterList = res;
                }
            }
        );

        // 编辑时初始化
        if (this.bean != null) {
        }

        this.updateSingleChecked();
    }

    getFromValues(): any {
        const values = this.form.value;
        const name = UUID.UUID();
        const result = {
            name: this.bean != null ? this.bean.name : name,
            alias: this.bean != null ? this.bean.alias : name,
            host: values.host,
            connectTimeout: values.connectTimeout,
            writeTimeout: values.writeTimeout,
            readTimeout: values.readTimeout,
            routes: [{
                name: UUID.UUID(),
                alias: values.routeName,
                methods: this.getSelectMethods(),
                hosts: [values.hosts],
                paths: [values.paths],
                protocols: values.onlyHttps ? ['https'] : ['http', 'https']
            }]
        };
        if(this.bean != null) {
            result['id'] = this.bean.id;
            result.routes['id'] = this.bean.routeId;
        }
        return result;
    }

    getSelectMethods() {
        const list: any[] = this.form.get('methods').value;
        const result = [];
        list.forEach(item => {
            if(item.checked) {
                result.push(item.value);
            }
        });
        return result;
    }

    updateAllChecked(): void {
        this.indeterminate = false;
        if (this.allChecked) {
            this.form.get('methods').setValue(this.form.get('methods').value.map(item => {
                return {
                    ...item,
                    checked: true
                };
            }));
        } else {
            this.form.get('methods').setValue(this.form.get('methods').value.map(item => {
                return {
                    ...item,
                    checked: false
                };
            }));
        }
    }

    updateSingleChecked(): void {
        if (this.form.get('methods').value.every(item => item.checked === false)) {
            this.allChecked = false;
            this.indeterminate = false;
        } else if (this.form.get('methods').value.every(item => item.checked === true)) {
            this.allChecked = true;
            this.indeterminate = false;
        } else {
            this.indeterminate = true;
        }
    }
}
