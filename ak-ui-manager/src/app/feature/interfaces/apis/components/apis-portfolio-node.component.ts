import {ChangeDetectorRef, Component, Input, OnInit, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {GatewayService} from "@service/http/gateway.service";
import {Config} from "@config/config";
import {FormBuilder} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";

@Component({
    selector: 'app-apis-portfolio-node',
    templateUrl: './apis-portfolio-node.component.html',
    styles: [`
    `]
})
export class ApisPortfolioNodeComponent extends BaseComponent implements OnInit {

    @Input() bean: any;

    constructor(public baseService: BaseService,
        public rd: Renderer2,
        public modalService: NzModalService,
        public nzMessageService: NzMessageService,
        public cdr: ChangeDetectorRef,
        public toolService: ToolService,
        public fb: FormBuilder,
        public gatewayService: GatewayService,
        public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit(): void {
    }
}
