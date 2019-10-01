import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {RouterPluginsEditComponent} from "@feature/gateway/advanced-router/components/router-plugins-edit.component";

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
                public baseService: BaseService) {
    }

    @Input() bean: any;
    form: FormGroup;

    typesOptions = [
        {label: 'GET', value: 'get', checked: false},
        {label: 'POST', value: 'post', checked: false},
        {label: 'DELETE', value: 'delete', checked: false},
        {label: 'PUT', value: 'put', checked: false},
        {label: 'HEAD', value: 'head', checked: false},
        {label: 'PATCH', value: 'patch', checked: false},
        {label: 'OPTIONS', value: 'options', checked: false},
        {label: 'TRACE', value: 'trace', checked: false}
    ];
    loading = false;
    allChecked = false;
    indeterminate = false;

    controlArray1 = [''];
    controlValues1 = [''];
    controlArray2 = [''];
    controlValues2 = [''];

    serverList: any[] = [{a: 'aa'}, {a: 'bb'}];

    ngOnInit(): void {
        this.form = this.fb.group({
            clusterName: [this.bean == null ? null : this.bean.clusterName, [Validators.required]],
            clusterCode: [this.typesOptions],
            field1: [this.bean == null ? null : this.bean.field1],
            field2: [this.bean == null ? null : this.bean.field2],
            // field2: [{value: this.bean == null ? null : this.bean.field2, disabled: true}],
            field3: [this.bean == null ? null : this.bean.field3],
            field4: [this.bean == null ? null : this.bean.field4],
            field5: [this.bean == null ? null : this.bean.field5],
            field6: [this.bean == null ? null : this.bean.field6],
            field7: [this.bean == null ? null : this.bean.field7]
        });

        // 编辑时初始化
        if (this.bean != null) {
        }

        this.updateSingleChecked();
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

    updateAllChecked(): void {
        this.indeterminate = false;
        if (this.allChecked) {
            this.form.get('clusterCode').setValue(this.form.get('clusterCode').value.map(item => {
                return {
                    ...item,
                    checked: true
                };
            }));
        } else {
            this.form.get('clusterCode').setValue(this.form.get('clusterCode').value.map(item => {
                return {
                    ...item,
                    checked: false
                };
            }));
        }
    }

    updateSingleChecked(): void {
        if (this.form.get('clusterCode').value.every(item => item.checked === false)) {
            this.allChecked = false;
            this.indeterminate = false;
        } else if (this.form.get('clusterCode').value.every(item => item.checked === true)) {
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
