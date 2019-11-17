import {AfterViewInit, ChangeDetectorRef, Component, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {Config} from '@config/config';
import {Subject} from "rxjs";
import {Store} from "@ngrx/store";
import * as fromRoot from "@store/reducers";
import {Oauth2Service} from "@service/http/oauth2.service";
import {ConstantsActions} from "@store/actions";

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
                       private oauth2Service: Oauth2Service,
                       private cdr: ChangeDetectorRef) {
        // this.store$.dispatch(new ConstantsActions.LoadApplicationTypes());
        // this.store$.dispatch(new ConstantsActions.LoadServices());
    }

    ngOnInit(): void {
        this.menus = this.config.menus;
    }

    changeLang() {
    }

    testApi() {
        this.oauth2Service.getResource("http://127.0.0.1:8082/foos/123").subscribe(data => console.log(data));
    }

    logout() {
        this.oauth2Service.logout();
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
