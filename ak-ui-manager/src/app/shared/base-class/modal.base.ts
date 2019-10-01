import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {CheckBase} from '@shared/base-class/check.base';
import {BaseService} from '@service/http/base.service';
import {Renderer2} from '@angular/core';

/**
 * @description 弹框类
 */
export class ModalBase extends CheckBase {
    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService) {
        super();
    }
}
