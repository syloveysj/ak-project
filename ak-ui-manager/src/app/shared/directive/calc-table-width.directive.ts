import {AfterViewInit, Directive, ElementRef, EventEmitter, Input, OnDestroy, Output, Renderer2} from '@angular/core';
import {fromEvent, merge, Subject} from 'rxjs';
import {filter, startWith, takeUntil} from 'rxjs/operators';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

@Directive({
    selector: '[appCalcTableWidth]'
})
export class CalcTableWidthDirective implements AfterViewInit, OnDestroy {
    @Output() tabWidthChange = new EventEmitter<string>();
    @Input() showShadow = true;
    el: HTMLElement = this.elementRef.nativeElement;
    private destroy$ = new Subject<void>();

    constructor(private elementRef: ElementRef,
                private route: ActivatedRoute,
                private router: Router,
                private rd: Renderer2) {
        this.rd.setStyle(this.el, 'margin-bottom', '35px');
    }

    calc() {
        const tabWidth = this.el.offsetWidth;
        this.tabWidthChange.emit(tabWidth + 'px');
    }

    ngAfterViewInit(): void {
        if (this.showShadow) {
            this.rd.addClass(this.el, 'shadow');
        }
        setTimeout(() => {
            this.calc();
        }, 1000);
        const navigationEnd$ = this.router.events.pipe(filter(e => e instanceof NavigationEnd));
        const windowResize$ = fromEvent(window, 'resize');
        merge(navigationEnd$, windowResize$).pipe(
            startWith(1),
            takeUntil(this.destroy$)
        ).subscribe(() => this.calc());
    }

    ngOnDestroy() {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
