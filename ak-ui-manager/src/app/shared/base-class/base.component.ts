import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Renderer2} from '@angular/core';
import {ElementListenBase} from '@shared/base-class/element-listen.base';

/**
 * @description 基类组件，抽取公共的属性和方法
 */
export class BaseComponent extends ElementListenBase {
    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService) {
        super(baseService, rd, modalService, nzMessageService);
    }
}
