import {ChangeDetectorRef, Component, Input, OnInit, Renderer2} from '@angular/core';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder} from "@angular/forms";
import {BaseComponent} from "@shared/base-class/base.component";
import {ToolService} from "@core/utils/tool.service";

@Component({
    selector: 'app-apis-import-two',
    templateUrl: './apis-import-two.component.html',
    styles: [`
    `]
})
export class ApisImportTwoComponent extends BaseComponent implements OnInit {

    @Input() apiList: any[];

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public cdr: ChangeDetectorRef,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public toolService: ToolService,
                public fb: FormBuilder) {
        super(baseService, rd, modalService, nzMessageService);
    }

    size = 'small';
    value;

    ngOnInit(): void {
    }

    deleteRoute(bean: any) {

    }
}
