import {AfterViewInit, Directive, ElementRef, EventEmitter, Input, Optional, Output} from "@angular/core";
import {NzTableComponent} from "ng-zorro-antd";
import {isEmpty} from "@core/utils/string.util";

export enum OFFSET_TO {
    PARENT,
    CLIENT
}

@Directive({
    selector: "[appAutoHeightBlock]"
})
export class AutoHeightBlockDirective implements AfterViewInit {

    @Input()
    set offsetTo(v: any){
        if (!isEmpty(v)) {
            this._offsetTo = v;
        }
    }
    @Input()
    set offsetBottom(v: any) {
        let value = parseInt(v);
        if (!isNaN(value) && value >= 0) {
            this._offsetBottom = value;
        }
    }
    @Input()
    parentElement: HTMLElement;
    @Output()
    height = new EventEmitter<any>();

    private _offsetBottom = 20;
    private _offsetTo = OFFSET_TO.PARENT;

    constructor(private el: ElementRef,
                @Optional() private table: NzTableComponent) {}

    ngOnInit() {}

    ngAfterViewInit() {
        let block: HTMLElement = this.el.nativeElement;
        if (block) {
            let blockTop = 0;
            block.style["overflow-y"] = "auto";
            if (this._offsetTo === OFFSET_TO.CLIENT && block.getBoundingClientRect && block.getBoundingClientRect().top) {
                blockTop = block.getBoundingClientRect().top;
                block.style.height = `calc(100vh - ${blockTop + this._offsetBottom}px)`;
                this.height.emit(block.style.height);
            } else if (this._offsetTo === OFFSET_TO.PARENT && this.el.nativeElement.parentElement) {
                setTimeout(() => {
                    const parentElement = this.parentElement ? this.parentElement : this.el.nativeElement.parentElement;
                    let offsetHeight = parentElement.offsetHeight -
                        (parentElement.clientTop - parentElement.firstElementChild.clientTop) - this._offsetBottom;
                    if (this.table && block.tagName.toLowerCase() === 'nz-table') {
                        this.table.nzScroll = {
                            y: offsetHeight.toString() + "px",
                            x: this.table.nzScroll.x
                        };
                    } else {
                        block.style.height = `${offsetHeight}px)`;
                    }
                    this.height.emit(offsetHeight + 'px');
                })
            }
        }
    }
}
