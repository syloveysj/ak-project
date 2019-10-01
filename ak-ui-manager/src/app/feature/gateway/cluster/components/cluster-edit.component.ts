import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';

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

    serverList: any[] = [{a: 'aa'}, {a: 'bb'}];

    ngOnInit(): void {
        this.form = this.fb.group({
            clusterName: [this.bean == null ? null : this.bean.clusterName, [Validators.required]],
            clusterCode: [this.bean == null ? null : this.bean.clusterCode]
        });

        // 编辑时初始化
        if (this.bean != null) {
        }
    }

    deleteServer(bean: any) {
    }

}
