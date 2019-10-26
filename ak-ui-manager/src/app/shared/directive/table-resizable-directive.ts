import {AfterViewInit, Directive, ElementRef, Input} from '@angular/core';

@Directive({
    selector: '[appTableResizable]',
})
/*自定义宽度*/
export class TableResizableDirective implements AfterViewInit {
    @Input('tableResizable') columnWidths: string[];
    @Input() usePixel = false;

    constructor(private $element: ElementRef<HTMLTableElement>) {
    }

    ngAfterViewInit() {
        const el = (() => {
            let result = this.$element.nativeElement;
            if (result.tagName === 'NZ-TABLE') {
                result = result.querySelector('table');
            }
            if (result.tagName !== 'TABLE') {
                throw new TypeError('Must be a TABLE element');
            }
            if (!result.tHead) {
                throw new TypeError('Must have a THEAD element');
            }
            return result;
        })();
        el.classList.add('table-resizable');
        const tr = el.tHead.rows[0];
        if (!tr) {
            throw new TypeError('Must have at least one TR element inside THEAD element');
        }

        setTimeout(() => {
            const ths = Array.from(tr.cells) as HTMLTableHeaderCellElement[];
            if (ths.length <= 1) {
                return;
            }
            --ths.length; // 最后一个方格总是自动列宽的（用于占满整行）

            if (!Array.isArray(this.columnWidths)) {
                this.columnWidths = new Array(ths.length).fill('');
            } else {
                this.columnWidths.length = ths.length;
                this.columnWidths.forEach((x, i, arr) => {
                    if (!x) {
                        arr[i] = '';
                    }
                });
            }

            ths.forEach(th => {
                if (th.classList.contains('no-resize')) {
                    return;
                }
                if (this.columnWidths[th.cellIndex]) {
                    th.width = this.columnWidths[th.cellIndex];
                }

                const i = document.createElement('i');
                i.classList.add('resize-indicator');
                th.appendChild(i);

                i.addEventListener('mousedown', e => {
                    if (e.button === 1) { // 鼠标中键
                        th.width = '';
                        return;
                    }
                    if (e.button !== 0) {
                        return;
                    }
                    const startX = e.pageX;
                    const startThWidth = th.clientWidth;
                    document.body.classList.add('table-resizing');

                    let mousemoveHandler;

                    document.body.addEventListener('mousemove', mousemoveHandler = e => {
                        if (e.button !== 0) {
                            return;
                        }
                        const pixel = e.pageX - startX + startThWidth;
                        if (this.usePixel) {
                            th.width = pixel + '';
                        } else {
                            th.width = pixel / tr.offsetWidth * 100 + '%';
                        }
                        this.columnWidths[th.cellIndex] = th.width;
                    });

                    document.body.addEventListener('mouseup', e => {
                        if (e.button !== 0) {
                            return;
                        }
                        document.body.removeEventListener('mousemove', mousemoveHandler);
                        document.body.classList.remove('table-resizing');
                    }, {once: true});
                });
            });
        });
    }
}
