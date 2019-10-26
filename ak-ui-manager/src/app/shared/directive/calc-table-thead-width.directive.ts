import {AfterViewInit, Directive, ElementRef, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Subject} from 'rxjs';

@Directive({
    selector: '[appCalcTableHeadWidth]'
})
export class CalcTableTheadWidthDirective implements AfterViewInit, OnDestroy {
    @Input() loading = false;
    @Output() tabHeadWidthChange = new EventEmitter<string>();
    el: HTMLElement = this.elementRef.nativeElement;
    private destroy$ = new Subject<void>();
    loading$  = new Subject<boolean>();

    constructor(private elementRef: ElementRef) {
    }

    ngAfterViewInit(): void {
        if (this.loading) {
            const childNodes = this.el.querySelectorAll('th');
            let sum = 0;
            childNodes.forEach(item => sum = sum + item.offsetWidth);
            console.log(sum);
            this.tabHeadWidthChange.emit(sum + 'px');
        }
    }

    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
