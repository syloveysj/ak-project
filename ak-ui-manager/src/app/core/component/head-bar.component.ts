import {AfterViewInit, ChangeDetectorRef, Component, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {Config} from '@config/config';
import {Subject} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromRoot from "@store/reducers";
import * as ConstantsActions from "@store/actions/constants.actions";

@Component({
    selector: 'app-head-bar',
    templateUrl: './head-bar.component.html',
    styleUrls: [`./head-bar.component.css`],
})
export class HeadBarComponent implements OnInit, OnChanges, AfterViewInit, OnDestroy {

    companyName = '云管理平台';
    browserLang = 'zh';
    menus = [];

    private destroy$ = new Subject<void>();

    public constructor(private config: Config,
                       private store$: Store<fromRoot.State>,
                       private cdr: ChangeDetectorRef) {
        this.store$.dispatch(new ConstantsActions.LoadApplicationTypes());
        this.store$.dispatch(new ConstantsActions.LoadServices());
    }

    ngOnInit(): void {
        this.menus = this.config.menus;
    }

    changeLang() {
    }

    ngOnChanges(): void {
        this.cdr.detectChanges();
    }

    ngAfterViewInit() {
        this.cdr.detectChanges();
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
