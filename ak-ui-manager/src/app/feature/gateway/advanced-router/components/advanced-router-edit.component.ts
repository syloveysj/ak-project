import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";
import {isEmpty, isNotEmpty} from "@core/utils/string.util";
import {finalize} from "rxjs/operators";
import {GatewayService} from "@service/http/gateway.service";
import {UUID} from "angular2-uuid";

@Component({
    selector: 'app-gateway-advanced-router-edit',
    templateUrl: `./advanced-router-edit.component.html`,
    styles: [
            `
        `
    ]
})
export class AdvancedRouterEditComponent implements OnInit {

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

    hostsArray = [''];
    hostsValues = [''];
    pathsArray = [''];
    pathsValues = [''];

    pluginsList: any[] = [{name:'key-auth', enabled: true, config: ''}];

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
            });

            this.hostsValues = this.clearString(this.bean.hostsMemo).split(',');
            this.pathsValues = this.clearString(this.bean.pathsMemo).split(',');
            if(this.hostsValues.length > 0) {
                for(let i=0; i<this.hostsValues.length-1; i ++) {
                    this.hostsArray.push('');
                }
            } else {
                this.hostsValues.push('');
            }

            if(this.pathsValues.length > 1) {
                for(let i=0; i<this.pathsValues.length-1; i ++) {
                    this.pathsArray.push('');
                }
            } else {
                this.pathsValues.push('');
            }
        }

        this.form = this.fb.group({
            routeName: [this.bean == null ? null : this.bean.alias, [Validators.required]],
            methods: [this.typesOptions],
            host: [null],
            connectTimeout: [this.bean == null ? 60000 : null],
            writeTimeout: [this.bean == null ? 60000 : null],
            readTimeout: [this.bean == null ? 60000 : null],
            onlyHttps: [this.bean == null ? false : this.clearString(this.bean.protocolsMemo).split(',').length==1],
            stripPath: [this.bean == null ? false : this.bean.stripPath]
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
        const name = UUID.UUID();
        const result = {
            name: this.bean != null ? this.bean.service.name : UUID.UUID(),
            alias: values.routeName,
            methods: this.getSelectMethods(),
            hosts: this.getValuesArray(this.hostsValues),
            paths: this.getValuesArray(this.pathsValues),
            protocols: values.onlyHttps ? ['https'] : ['http', 'https'],
            stripPath: values.stripPath,
            preserveHost: this.bean != null ? this.bean.preserveHost : false,
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

    openPluginsWin(bean: any) {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal large',
            nzTitle: '添加路由插件',
            nzMaskClosable: false,
            nzFooter: [
                {
                    label: '保存',
                    shape: 'primary',
                    disabled: (componentInstance) => {
                        return !componentInstance.form.valid;
                    },
                    onClick: (componentInstance) => {
                        console.log(componentInstance.form.value);
                        modal.destroy();
                    }
                },
                {
                    label: '取消',
                    shape: 'default',
                    onClick: () => {
                        modal.destroy();
                    }
                }
            ],
            nzContent: RouterPluginsEditComponent,
            nzComponentParams: {
                bean
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
            }
        });
    }

    deletePlug(bean: any) {
    }

    getValuesArray(list: string[]) {
        const result = [];
        list.forEach(item => {
            if(isNotEmpty(item)) {
                result.push(item);
            }
        })
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

    addControlArray(index: number, controlArray: any[], controlValues: any[]) {
        controlArray.splice(index + 1, 0, '');
        controlValues.splice(index + 1, 0, '');
    }

    deleteControlArray(index: number, controlArray: any[], controlValues: any[]) {
        controlArray.splice(index, 1);
        controlValues.splice(index, 1);
    }
}
