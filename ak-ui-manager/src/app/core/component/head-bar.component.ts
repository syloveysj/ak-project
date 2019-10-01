import {AfterViewInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Config} from '@config/config';

@Component({
    selector: 'app-head-bar',
    templateUrl: './head-bar.component.html',
    styleUrls: [`./head-bar.component.css`],
})
export class HeadBarComponent implements OnInit, AfterViewInit {

    companyName = '云管理平台';
    browserLang = 'zh';
    menus = [];

    public constructor(private config: Config,
                       private cdr: ChangeDetectorRef) {
    }

    ngOnInit(): void {
        this.menus = this.config.menus;
    }

    changeLang() {
    }

    ngAfterViewInit() {
        this.cdr.detectChanges();
    }
}
