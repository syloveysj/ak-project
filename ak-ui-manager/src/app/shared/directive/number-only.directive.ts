import {Directive, ElementRef} from '@angular/core';
import {HostListener, Input} from '@angular/core';

@Directive({
    selector: '[appNumberOnly]'
})
export class NumberOnlyDirective {
    @Input() scale = 2; // 默认只能保留两位小数
    constructor(private element: ElementRef) {
    }

    @HostListener('keydown', ['$event']) onKeyDown(event) {
        const e = <KeyboardEvent> event;
        // 获取元素的值
        const curValue: string = this.element.nativeElement.value;
        // 1 精度为0时不能录入小数点(只能输入整数)
        if (this.scale === 0 && [110, 190].indexOf(e.keyCode) > 0) {
            e.preventDefault();
            return;
        }

        // 2 当录入小数点情况下，不能继续录入
        if (curValue.indexOf('.') > 0 && [110, 190].indexOf(e.keyCode) > 0) {
            e.preventDefault();
            return;
        }

        // 3 正常情况下照常录入
        // 支持delete(46)、Backspace(8)、Tab(9)、Esc(27)、Enter(13)、小数点(110-数字键盘)、.(190)、->(39) <-(37)
        if ([46, 8, 9, 27, 13, 110, 190, 37, 39].indexOf(e.keyCode) !== -1 ||
            // 允许全选: Ctrl+A
            (e.keyCode === 65 && (e.ctrlKey || e.metaKey)) ||
            // 允许复制: Ctrl+C
            (e.keyCode === 67 && (e.ctrlKey || e.metaKey)) ||
            // 允许粘贴: Ctrl+V
            (e.keyCode === 86 && (e.ctrlKey || e.metaKey)) ||
            // 允许剪切: Ctrl+X
            (e.keyCode === 88 && (e.ctrlKey || e.metaKey)) ||
            // 允许: home（头）, end（尾）, left（左移）, right（右移）
            (e.keyCode >= 35 && e.keyCode <= 39)) {
            // 保持默认的处理
            return;
        }

        // 4 小数的要符合精度要求，否则无法录入(数字以外的其他命令键可以执行)
        // 判断小数精度
        // const unit = curValue.split('.');
        // 位数满足时，不能再输入
        // if (unit[1] && unit[1].length === this.scale) {
        //     e.preventDefault();
        //     return;
        // }
        // 确保数字以外的案件被拒绝执行默认动作
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
            return;
        }

    }

    // '.' at the end or only '-' in the input box.
    @HostListener('blur', ['$event']) onBlur() {
        const curValue: string = this.element.nativeElement.value;
        if (curValue.charAt(curValue.length - 1) === '.' || curValue === '-') {
            this.element.nativeElement.value = curValue.slice(0, -1);
        } else {
            const unit = curValue.split('.');
            if (unit[1] && unit[1].length > this.scale) {
                if(this.scale > 0) {
                    this.element.nativeElement.value = unit[0] + "." + unit[1].substring(0, this.scale);
                } else {
                    this.element.nativeElement.value = unit[0];
                }
            }
        }
    }
}
