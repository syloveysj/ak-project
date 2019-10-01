import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';

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
    clusterList = [
        {label: '服务器AA服务器AA服务器AA服务器AA', value: 'get', servers: '192.168.8.175,192.168.8.174,192.168.8.176,192.168.8.177'},
        {label: '服务器bb', value: 'post', servers: '192.168.8.175'},
    ];
    loading = false;
    allChecked = false;
    indeterminate = false;

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
}
