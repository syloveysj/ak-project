import {AfterViewInit, ChangeDetectorRef, Directive, EventEmitter, Output} from '@angular/core';
import {NzTableComponent, NzThComponent} from 'ng-zorro-antd';

@Directive({
  // tslint:disable-next-line:directive-selector
  selector: '[appCalcThWidth]',
})
export class CalcThWidthDirective implements AfterViewInit {
    @Output() scrollXWidth = new EventEmitter<number>();
    constructor(private cdr: ChangeDetectorRef, private nzTableComponent: NzTableComponent) {
    }

    ngAfterViewInit(): void {
        let sumWidth = 0;
        this.nzTableComponent.listOfNzThComponent.forEach(item => {
            if(item instanceof NzThComponent) {
                if (item.nzWidth.indexOf('px') > -1) {
                    sumWidth = sumWidth + Number(item.nzWidth.slice(0, -2));
                }
            }
        });
        setTimeout(() => {
            this.scrollXWidth.emit(sumWidth);
        }, 500);
        this.cdr.detectChanges(); // 解决变更检测报错
    }
}
