import {AfterViewInit, ChangeDetectorRef, Component, OnInit, QueryList, Renderer2, ViewChildren} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService, NzThComponent} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {Option} from '@model/common';
import {ToolService} from '@core/utils/tool.service';
import {map} from 'rxjs/operators';
import {GatewayService} from "@service/http/gateway.service";

@Component({
    selector: 'app-interfaces-apis',
    templateUrl: './apis.component.html',
    styleUrls: ['./apis.component.css']
})
export class ApisComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChildren(NzThComponent, {read: false}) private nzThComponents: QueryList<NzThComponent>;
    scrollXWidth = 0;

    fold: boolean = false;
    dynamicKeys: Option[] = [
        {id: 'alias', text: '集群名称'},
        {id: 'name', text: '集群编号'}
    ];

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public gatewayService: GatewayService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit() {
    }

    openWin(bean: any) {
    }

    deleteCluster(bean: any) {
    }

    resetFilters() {
        this.selfPage.next(this.selfQueryParams.page);
    }

    changeFold(fold: boolean) {
        this.fold = fold;
    }

    ngAfterViewInit(): void {
        // 监听th的宽度变化
        this.nzThComponents.changes.pipe(
            map((components: QueryList<NzThComponent>) => components.map(item => item.nzWidth)),
            map(array => array.filter(item => !!item)), // 过滤出有值的数据,形如 [undefined, '100px', '200px'] => ['100px', '200px'];
            map((res) => {
                let sum = 0;
                // 去除px字符串，进行累加
                res.map(item => {
                    if (item.indexOf('px') > -1) {
                        sum = sum + Number(item.slice(0, -2));
                    } else {
                        return;
                    }
                });
                return sum;
            }),
        ).subscribe(
            res => {
                this.scrollXWidth = res;
                this.cdr.detectChanges(); // 解决变更检测报错
            }
        );
    }
}
