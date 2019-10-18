import {
    Directive,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    Output,
    Renderer2, SimpleChanges
} from '@angular/core';
import {fromEvent, Subscription} from 'rxjs';
import {debounceTime} from 'rxjs/operators';
import {defaultDebounceTime} from "@core/utils/constant.util";

@Directive({
    selector: '[appCalcTableScrolly]'
})
export class CalcTableScrollyDirective implements OnInit, OnChanges, OnDestroy {
    @Output() scrollY = new EventEmitter<string | number>();
    @Input() scrollYUnit = 'px';
    @Input() showShadow = true;
    @Input() rowLine = 1;
    @Input() offsetTop = 0;
    offsetMap = {
        0: 0,
        1: 260,
        2: 380
    };
    private subscription: Subscription;

    constructor(private el: ElementRef,
                private rd: Renderer2) {
    }

    ngOnInit() {
        if (this.showShadow) {
            this.rd.addClass(this.el.nativeElement, 'shadow');
        }
        this.calc();
        this.subscription = fromEvent(window, 'resize').pipe(
            debounceTime(defaultDebounceTime)
        ).subscribe(() => {
            this.calc();
        });
    }

    calc() {
        const tabHeight = this.el.nativeElement.scrollTop;
        const height = document.documentElement.clientHeight - tabHeight;
        let data = 0;
        if (this.offsetTop) {
            data = (height - this.offsetTop);
        } else {
            data = (height - this.offsetMap[this.rowLine]);
        }
        if (this.scrollYUnit !== '') {
            this.scrollY.emit(data - 10 + this.scrollYUnit);
        } else {
            this.scrollY.emit(data - 10);
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes && changes.offsetTop) {
            this.calc();
        }
    }

    ngOnDestroy() {
        this.subscription && this.subscription.unsubscribe();
    }
}
