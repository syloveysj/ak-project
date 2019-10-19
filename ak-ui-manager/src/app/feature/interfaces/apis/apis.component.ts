import {ChangeDetectorRef, Component, OnInit, Renderer2, TemplateRef, ViewChild} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {ToolService} from '@core/utils/tool.service';
import {ApisImportComponent} from "@feature/interfaces/apis/components/apis-import.component";
import {InterfacesService} from "@service/http/interfaces.service";
import {finalize} from "rxjs/operators";

@Component({
    selector: 'app-interfaces-apis',
    templateUrl: './apis.component.html',
    styleUrls: ['./apis.component.css']
})
export class ApisComponent extends BaseComponent implements OnInit {
    fold: boolean = false;

    current = 0;
    @ViewChild('importSteps')
    importSteps: TemplateRef<any>;

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public interfacesService: InterfacesService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit() {
    }

    openWin() {
        this.current = 0;
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal full',
            nzTitle: this.importSteps,
            nzMaskClosable: false,
            nzClosable: false,
            nzFooter: [
                {
                    label: '上一步',
                    shape: 'primary',
                    show: () => {
                        return this.current === 1;
                    },
                    onClick: (componentInstance) => {
                        componentInstance.current = this.current -= 1;
                    }
                },
                {
                    label: '下一步',
                    shape: 'primary',
                    show: () => {
                        return this.current < 2;
                    },
                    onClick: (componentInstance) => {
                        componentInstance.current = this.current += 1;
                        componentInstance.loading = true;
                        if(this.current === 1) {
                            this.interfacesService.serverAnalysis(componentInstance.getFromValues()).pipe(
                                finalize(() => componentInstance.loading = false)
                            ).subscribe(
                                (res) => {
                                    console.log(res);
                                }
                            );
                        } else {
                            componentInstance.loading = false;
                        }
                    }
                },
                {
                    label: '完成',
                    shape: 'primary',
                    loading: (componentInstance) => {
                        return componentInstance.loading;
                    },
                    show: () => {
                        return this.current === 2;
                    },
                    onClick: (componentInstance) => {
                        modal.destroy();
                    }
                },
                {
                    label: '取消',
                    shape: 'default',
                    show: () => {
                        return this.current !== 2;
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
