import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ElementRef, HostListener, Renderer2, ViewChild} from '@angular/core';
import {TagCloseBase} from '@shared/base-class/tag-close.base';

/**
 * @description 基类组件，抽取公共的属性和方法
 */
export class ElementListenBase extends TagCloseBase {

    @ViewChild('PW') PW: ElementRef; // tab的dom
    @ViewChild('breadcrumb') breadcrumb: ElementRef;

    @HostListener('window:scroll', ['$event'])
    scroll(event: Event) {
        if (this.breadcrumb && this.breadcrumb.nativeElement) {
            // const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            const scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop); // 滚动条滚动的高度
            const domHeight = this.breadcrumb.nativeElement.offsetHeight; // 导航条的高度
            if (scrollTop >= domHeight) {
                this.rd.addClass(this.breadcrumb.nativeElement, 'fixed');
            } else {
                this.rd.removeClass(this.breadcrumb.nativeElement, 'fixed');
            }
        }
    }

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService) {
        super(baseService, rd, modalService, nzMessageService);
    }

    /**
     * @description table的计算宽度,适配底部分页
     * @param width string
     */
    tabWidthChange(width: string) {
        this.calcWidth = width;
        this.rd.setStyle(this.PW.nativeElement, 'width', this.calcWidth);
    }

}
