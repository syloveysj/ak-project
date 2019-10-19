import {ChangeDetectorRef, Component, OnInit, Renderer2} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {ToolService} from '@core/utils/tool.service';
import {GatewayService} from "@service/http/gateway.service";
import {ApisImportComponent} from "@feature/interfaces/apis/components/apis-import.component";

@Component({
    selector: 'app-interfaces-apis',
    templateUrl: './apis.component.html',
    styleUrls: ['./apis.component.css']
})
export class ApisComponent extends BaseComponent implements OnInit {
    fold: boolean = false;

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public gatewayService: GatewayService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit() {
    }

    openWin() {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal full',
            nzTitle: '服务导入',
            nzMaskClosable: false,
            nzFooter: [
                {
                    label: '上一步',
                    shape: 'default',
                    show: (componentInstance) => {
                        return componentInstance.current > 0 && componentInstance.current < 2;
                    },
                    onClick: (componentInstance) => {
                        componentInstance.pre();
                    }
                },
                {
                    label: '下一步',
                    shape: 'default',
                    show: (componentInstance) => {
                        return componentInstance.current < 2;
                    },
                    onClick: (componentInstance) => {
                        componentInstance.next();
                    }
                },
                {
                    label: '完成',
                    shape: 'primary',
                    disabled: (componentInstance) => {
                        return !componentInstance.form.valid;
                    },
                    loading: (componentInstance) => {
                        return componentInstance.loading;
                    },
                    show: (componentInstance) => {
                        return componentInstance.current === 2;
                    },
                    onClick: (componentInstance) => {
                        componentInstance.done();
                        modal.destroy();
                    }
                },
                {
                    label: '取消',
                    shape: 'default',
                    show: (componentInstance) => {
                        return componentInstance.current !== 2;
                    },
                    onClick: () => {
                        modal.destroy();
                    }
                }
            ],
            nzContent: ApisImportComponent,
            nzComponentParams: {
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
            }
        });
    }

    changeFold(fold: boolean) {
        this.fold = fold;
    }
}
