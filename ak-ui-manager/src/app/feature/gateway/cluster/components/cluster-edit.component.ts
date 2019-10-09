import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import { UUID } from 'angular2-uuid';

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

    upstreamList: any[] = [{id: 'aa'}, {id: 'bb'}];
    editCache: { [key: string]: any } = {};

    ngOnInit(): void {
        this.form = this.fb.group({
            clusterName: [this.bean == null ? null : this.bean.clusterName, [Validators.required]],
            clusterCode: [this.bean == null ? null : this.bean.clusterCode]
        });

        // 编辑时初始化
        if (this.bean != null) {
        }

        this.updateEditCache();
    }

    updateEditCache(): void {
        this.editing = false;
        this.editCache = {};
        this.upstreamList.forEach(item => {
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
        const index = this.upstreamList.findIndex(item => item.id === id);
        if (this.bean != null) {
            this.upstreamList.splice(index, 1);
            this.updateEditCache();
            this.upstreamList = [...this.upstreamList];
        } else {
            this.editCache[id] = {
                data: { ...this.upstreamList[index] },
                edit: false
            };
        }
    }

    addTarget() {
        const newId = UUID.UUID();
        this.upstreamList.push({
            id: newId
        });
        this.updateEditCache();
        this.editCache[newId].edit = true;
        this.editing = true;
        this.upstreamList = [...this.upstreamList];
    }

    saveTarget(id: string) {
        if (this.bean != null) {

        } else {
            const index = this.upstreamList.findIndex(item => item.id === id);
            Object.assign(this.upstreamList[index], this.editCache[id].data);
            this.updateEditCache();
            this.upstreamList = [...this.upstreamList];
        }
    }

    deleteTargets() {

    }

    deleteServer(bean: any) {
    }

}
