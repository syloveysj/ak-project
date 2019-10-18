import {
    AfterViewInit,
    ChangeDetectorRef,
    Component,
    Input,
    OnInit,
    QueryList,
    Renderer2,
    ViewChildren
} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService, NzThComponent} from 'ng-zorro-antd';
import {Option} from '@model/common';
import {combineLatest, defer, Observable, Subject} from 'rxjs';
import {
    debounceTime,
    distinctUntilChanged,
    map,
    startWith,
    switchMap,
    tap
} from 'rxjs/operators';
import {defaultDebounceTime} from "@core/utils/constant.util";
import {ToolService} from "@core/utils/tool.service";
import {GatewayService} from "@service/http/gateway.service";
import {Config} from "@config/config";

@Component({
    selector: 'app-apis-list',
    templateUrl: './apis-list.component.html',
    styleUrls: ['./apis-list.component.css'],
})
export class ApisListComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChildren(NzThComponent, {read: false}) private nzThComponents: QueryList<NzThComponent>;
    scrollXWidth = 0;

    @Input() portfolioId$: Observable<number>;
    @Input() chooseMenu: (id: number, level: number) => void;

    dynamicKeys: Option[] = [
        {id: 'alias', text: '路由名称'},
        {id: 'hostsMemo', text: '匹配主机名'},
        {id: 'pathsMemo', text: '匹配路径'}
    ];
    types: Option[] = [
        {id: 'GET', text: 'GET'},
        {id: 'POST', text: 'POST'},
        {id: 'DELETE', text: 'DELETE'},
        {id: 'PUT', text: 'PUT'},
        {id: 'HEAD', text: 'HEAD'},
        {id: 'PATCH', text: 'PATCH'},
        {id: 'OPTIONS', text: 'OPTIONS'},
        {id: 'TRACE', text: 'TRACE'}
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

    ngOnInit(): void {
        // 初始化页面参数
        this.selfQueryParams = {
            ...this.selfQueryParams,
            order: 'desc',
            sort: 'createdAt',
            pagesize: 10,

            dynamicKey: 'alias'
        };

        const {
            dynamicKey$, keyword$, typeId$, page$, pageSize$, sort$, order$
        } = this.getGeneralObservables();

        // 绑定查询
        combineLatest([dynamicKey$, keyword$, typeId$, page$, pageSize$, sort$, order$]).pipe(
            map(([selfDynamicKey, selfKeyword, selfTypeId, selfPage, selfPageSize, selfSort, selfOrder]) => {
                const params: any = {};
                let cond: any = null;
                (selfKeyword) && ((!cond) && (cond={}) && (cond[selfDynamicKey] = selfKeyword));
                (selfTypeId !== null) && (((!cond) && (cond={}) || cond) && (cond['methodsMemo'] = selfTypeId));
                (selfPage !== null) && (params.page = selfPage);
                (selfPageSize !== null) && (params.pagesize = selfPageSize);
                (selfSort) && (params.sort = selfSort);
                (selfOrder) && (params.order = selfOrder);
                (cond) && (params.cond = JSON.stringify(cond));
                return params;
            }),
            debounceTime(defaultDebounceTime),
            distinctUntilChanged(),
            tap(() => {
                this.loading = true;
            }),
            switchMap(val => defer(() => {
                // return this.gatewayService.getRouteList(val).pipe(
                //     finalize(() => this.loading = false));
                console.log(val);
                let demo = new Subject<string | string[]>();
                return demo.asObservable().pipe(startWith({
                    total: 100,
                    footer: null,
                    from: 0,
                    size: 10,
                    page: 1,
                    pagesize: 10,
                    rows: [{'a': 'aa'}, {'a': 'bb'}, {'a': 'cc'}, {'a': 'dd'}]
                }));
            }))
        ).subscribe((data: any) => {
            this.loading = false;
            console.log(data);
            this.data = data;
        });
    }

    openWin(bean: any = null) {

    }

    deleteRoute(bean: any = null) {

    }

    resetFilters() {
        this.selfPage.next(this.selfQueryParams.page);
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
