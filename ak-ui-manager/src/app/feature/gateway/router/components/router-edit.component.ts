import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {GatewayService} from "@service/http/gateway.service";
import {isEmpty} from "@core/utils/string.util";
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
        if(this.bean != null) {
            // 编辑的回选
            const methods = this.clearString(this.bean.methodsMemo);
            const list = methods.split(',');
            list.forEach(item => {
                this.typesOptions.forEach(type => {
                    if(type.value === item) {
                        type.checked = true;
                    }
                })
            })
        }

        this.form = this.fb.group({
            routeName: [this.bean == null ? null : this.bean.alias, [Validators.required]],
            methods: [this.typesOptions],
            hosts: [this.bean == null ? null : this.clearString(this.bean.hostsMemo)],
            paths: [this.bean == null ? null : this.clearString(this.bean.pathsMemo)],
            host: [null],
            connectTimeout: [this.bean == null ? 60000 : null],
            writeTimeout: [this.bean == null ? 60000 : null],
            readTimeout: [this.bean == null ? 60000 : null],
            onlyHttps: [this.bean == null ? false : this.clearString(this.bean.protocolsMemo).split(',').length==1]
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
            this.gatewayService.getService(this.bean.serviceId).subscribe(res => {
               this.bean.service = res;

               this.form.get('host').setValue(res.host);
               this.form.get('connectTimeout').setValue(res.connectTimeout);
               this.form.get('writeTimeout').setValue(res.writeTimeout);
               this.form.get('readTimeout').setValue(res.readTimeout);
            });
        }

        this.updateSingleChecked();
    }

    getFromValues(): any {
        const values = this.form.value;
        const name = UUID.UUID().replace(/-/g, '');
        const result = {
                name: this.bean != null ? this.bean.name : UUID.UUID().replace(/-/g, ''),
                alias: values.routeName,
                methods: this.getSelectMethods(),
                hosts: isEmpty(values.hosts) ? [] : [values.hosts],
                paths: isEmpty(values.paths) ? [] : [values.paths],
                protocols: values.onlyHttps ? ['https'] : ['http', 'https'],
                stripPath: this.bean != null ? this.bean.stripPath : true,
                preserveHost: this.bean != null ? this.bean.preserveHost : true,
                service: {
                    name: this.bean != null ? this.bean.service.name : name,
                    alias: this.bean != null ? this.bean.service.alias : name,
                    protocol: this.bean != null ? this.bean.service.protocol : 'http',
                    host: values.host,
                    port: 80,
                    connectTimeout: values.connectTimeout,
                    writeTimeout: values.writeTimeout,
                    readTimeout: values.readTimeout
                }
            };

        if(this.bean != null) {
            result['id'] = this.bean.id;
            result.service['id'] = this.bean.serviceId;
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

    clearString(str: string) {
        if(isEmpty(str)) return '';
        return str.replace('{', '').replace('}', '');
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
