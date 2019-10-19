import {Component, Input, OnInit} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {GatewayService} from "@service/http/gateway.service";
import {Config} from "@config/config";
import {FormBuilder} from "@angular/forms";

@Component({
    selector: 'app-apis-import-three',
    template: `
        <nz-alert
            nzType="success"
            nzMessage="成功导入"
            nzDescription="已成功导入您选择的服务."
            nzShowIcon
        >
        </nz-alert>
    `,
    styles: [`
    `]
})
export class ApisImportThreeComponent implements OnInit {

    @Input() bean: any;

    constructor(public fb: FormBuilder,
                public config: Config,
                private nzMessageService: NzMessageService,
                public modalService: NzModalService,
                public baseService: BaseService,
                public gatewayService: GatewayService) {
    }

    ngOnInit(): void {
    }
}
