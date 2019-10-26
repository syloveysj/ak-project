import {Directive, EventEmitter, HostListener, OnInit, Output, OnDestroy, Input} from '@angular/core';
import {Subject, Subscription} from 'rxjs';
import {debounceTime, throttleTime} from 'rxjs/operators';

@Directive({
    selector: '[appDebounceClick]'
})
export class DebounceClickDirective implements OnInit, OnDestroy {
    @Input() debounceTime = 0;
    @Input() throttleTime = 3000;
    @Output() debounceClick = new EventEmitter();
    private clicks = new Subject<any>();
    private subscription: Subscription;

    constructor() {
    }

    ngOnInit() {
        this.subscription = this.clicks.pipe(
            throttleTime(this.throttleTime),
            debounceTime(this.debounceTime)
        ).subscribe(e => this.debounceClick.emit(e));
    }

    ngOnDestroy() {
        this.subscription && this.subscription.unsubscribe();
    }

    @HostListener('click', ['$event'])
    clickEvent(event: MouseEvent) {
        event.preventDefault();
        event.stopPropagation();
        this.clicks.next(event);
    }
}
