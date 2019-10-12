import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {UUID} from 'angular2-uuid';
import {isEmpty, isNotEmpty} from "@core/utils/string.util";
import {GatewayService} from "@service/http/gateway.service";

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
                public baseService: BaseService,
                public gatewayService: GatewayService) {
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

        this.initTaggets();
    }

    initTaggets() {
        // 编辑时初始化
        if (this.bean != null) {
            this.gatewayService.getUpstreamTargetList(this.bean.id).subscribe(
                (res) => {
                    this.targets = res || [];
                    this.updateEditCache();
                }
            );
        } else {
            this.updateEditCache();
        }
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
        if(this.bean != null) {
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
        if('flag' in this.targets[index] && this.bean != null) {
            //编辑状态下 -> 新增目标状态，取消移除
            this.targets.splice(index, 1);
            this.targets = [...this.targets];
            this.updateEditCache();
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
            weight: '100',
            //标记新增
            flag: true
        });
        this.editCache[newId].edit = true;
        this.editing = true;
        this.targets = [...this.targets];
        this.updateEditCache();
    }

    saveTarget(id: string) {
        const index = this.targets.findIndex(item => item.id === id);
        Object.assign(this.targets[index], this.editCache[id].data);

        const item = this.targets[index];
        if (this.bean != null) {
            this.gatewayService.addTarget(this.bean.id, {target:item.ip + ':' + (isNotEmpty(item.port) ? item.port : '80'), weight: isNotEmpty(item.weight) ? item.weight : '100'}).subscribe(
                (res) => {
                    this.initTaggets();
                }
            );
        } else {
            this.targets = [...this.targets];
            this.updateEditCache();
        }
    }

    deleteTargets(obj:any) {
        if(this.bean != null) {
            if(obj === null) {
                let ids = "";
                this.targets.forEach(item => {
                    if(item.checked === true) {
                        ids += isEmpty(ids)? item.id : ',' + item.id;
                    }
                });
                if(isNotEmpty(ids)) {
                    this.gatewayService.removeTargetList(this.bean.id, ids).subscribe(
                        (res) => {
                            this.initTaggets();
                        }
                    );
                }
            } else {
                this.gatewayService.removeTarget(this.bean.id, obj.id).subscribe(
                    (res) => {
                        this.initTaggets();
                    }
                );
            }
        } else {
            if(obj === null) {
                this.targets = this.targets.filter(item => !item.checked);
            } else {
                const index = this.targets.findIndex(item => item.id === obj.id);
                this.targets.splice(index, 1);
                this.targets = [...this.targets];
            }
            this.updateEditCache();
        }
    }

}
