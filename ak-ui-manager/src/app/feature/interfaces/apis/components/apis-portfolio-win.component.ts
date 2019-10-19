import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder} from "@angular/forms";
import {Config} from "@config/config";
import {BaseService} from "@service/http/base.service";
import {GatewayService} from "@service/http/gateway.service";

@Component({
    selector: 'app-apis-porfolio-win',
    template: `
    `,
    styles: [`
    `],
})
export class ApisPortfolioWinComponent implements OnInit {

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
