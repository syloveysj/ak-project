import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {UUID} from 'angular2-uuid';
import {isNotEmpty} from "@core/utils/string.util";

@Component({
    selector: 'app-gateway-cluster-edit',
    templateUrl: `./cluster-edit.component.html`,
    styles: [
            `
        `
    ]
})
export class ClusterEditComponent implements OnInit {

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService) {
    }

    @Input() bean: any;
    form: FormGroup;
    loading = false;
    editing = false;

    targets: any[] = [];
    editCache: { [key: string]: any } = {};

    ngOnInit(): void {
        this.form = this.fb.group({
            clusterName: [this.bean == null ? null : this.bean.alias, [Validators.required]],
            clusterCode: [this.bean == null ? null : this.bean.id]
        });

        // 编辑时初始化
        if (this.bean != null) {
        }

        this.updateEditCache();
    }

    getFromValues(): any {
        const values = this.form.value;
        const list = [];
        this.targets.forEach(item => {
            list.push({
                target: item.ip + ':' + (isNotEmpty(item.port) ? item.port : '80'),
                weight: isNotEmpty(item.weight) ? item.weight : '100'
            });
        });
        const result = {
            alias: values.clusterName,
            name: isNotEmpty(values.clusterCode) ? values.clusterCode : UUID.UUID(),
            targets: list
        };
        if(this.bean !== null) {
            result['id'] = this.bean.id;
        }
        return result;
    }

    updateEditCache(): void {
        this.editing = false;
        this.editCache = {};
        this.targets.forEach(item => {
            this.editCache[item.id] = {
                edit: false,
                data: { ...item }
            };
        });
    }

    startEdit(id: string): void {
        this.editCache[id].edit = true;
    }

    cancelEdit(id: string): void {
        const index = this.targets.findIndex(item => item.id === id);
        if (this.bean != null) {
            this.targets.splice(index, 1);
            this.updateEditCache();
            this.targets = [...this.targets];
        } else {
            this.editCache[id] = {
                data: { ...this.targets[index] },
                edit: false
            };
        }
    }

    addTarget() {
        const newId = UUID.UUID();
        this.targets.push({
            id: newId,
            ip: '',
            port: '8080',
            weight: '100'
        });
        this.updateEditCache();
        this.editCache[newId].edit = true;
        this.editing = true;
        this.targets = [...this.targets];
    }

    saveTarget(id: string) {
        if (this.bean != null) {

        } else {
            const index = this.targets.findIndex(item => item.id === id);
            Object.assign(this.targets[index], this.editCache[id].data);
            this.updateEditCache();
            this.targets = [...this.targets];
        }
    }

    deleteTargets() {

    }

    deleteServer(bean: any) {
    }

}
