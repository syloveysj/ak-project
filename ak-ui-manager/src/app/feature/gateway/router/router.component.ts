import {AfterViewInit, ChangeDetectorRef, Component, OnInit, QueryList, Renderer2, ViewChildren} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService, NzThComponent} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {Option} from '@model/common';
import {ToolService} from '@core/utils/tool.service';
import {debounceTime, distinctUntilChanged, map, startWith, switchMap, tap} from 'rxjs/operators';
import {combineLatest, defer, Subject} from 'rxjs';
import {defaultDebounceTime} from '@core/utils/constant.util';
import {RouterEditComponent} from '@feature/gateway/router/components/router-edit.component';

@Component({
    selector: 'app-gateway-router',
    templateUrl: './router.component.html',
    styleUrls: ['./router.component.css']
})
export class RouterComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChildren(NzThComponent, {read: false}) private nzThComponents: QueryList<NzThComponent>;
    scrollXWidth = 0;

    dynamicKeys: Option[] = [
        {id: 'name', text: '主机名'},
        {id: 'uri', text: '服务路径'},
        {id: 'path', text: '请求地址'}
    ];
    types: Option[] = [
        {id: 'get', text: 'GET'},
        {id: 'post', text: 'POST'},
        {id: 'delete', text: 'DELETE'},
        {id: 'put', text: 'PUT'},
        {id: 'head', text: 'HEAD'},
        {id: 'patch', text: 'PATCH'},
        {id: 'options', text: 'OPTIONS'},
        {id: 'trace', text: 'TRACE'}
    ];

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit() {
        // 初始化页面参数
        this.selfQueryParams = {
            ...this.selfQueryParams,
            order: null,
            sort: null,
            pagesize: 10,

            dynamicKey: 'name'
        };

        const {
            dynamicKey$, keyword$, typeId$, page$, pageSize$, sort$, order$

        } = this.getGeneralObservables();

        // 绑定查询
        combineLatest([dynamicKey$, keyword$, typeId$, page$, pageSize$, sort$, order$]).pipe(
            map(([selfDynamicKey, selfKeyword, selfTypeId, selfPage, selfPageSize, selfSort, selfOrder]) => {
                const params: any = {};
                (selfKeyword) && (params[selfDynamicKey] = selfKeyword);
                (selfTypeId !== null) && (params.typeId = selfTypeId);
                (selfPage !== null) && (params.page = selfPage);
                (selfPageSize !== null) && (params.pagesize = selfPageSize);
                (selfSort) && (params.sort = selfSort);
                (selfOrder) && (params.order = selfOrder);
                return params;
            }),
            debounceTime(defaultDebounceTime),
            distinctUntilChanged(),
            tap(() => {
                this.loading = true;
            }),
            switchMap(val => defer(() => {
                // return this.operationAnalysisService.getBrandOpAnalysis(val).pipe(
                //     finalize(() => this.loading = false));
                console.log(val);
                const demo = new Subject<string | string[]>();
                return demo.asObservable().pipe(startWith({
                    total: 100,
                    footer: null,
                    from: 0,
                    size: 10,
                    page: 1,
                    pagesize: 10,
                    rows: [{a: 'aa'}, {a: 'bb'}, {a: 'cc'}, {a: 'dd'}, {a: 'aa'}, {a: 'bb'}, {a: 'cc'}, {a: 'dd'}]
                }));
            }))
        ).subscribe((data: any) => {
            this.loading = false;
            this.data = data;
        });
    }

    openWin(bean: any) {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal large',
            nzTitle: '添加动态新路由',
            nzMaskClosable: false,
            nzFooter: [
                {
                    label: '保存',
                    shape: 'primary',
                    disabled: (componentInstance) => {
                        return !componentInstance.form.valid;
                    },
                    onClick: (componentInstance) => {
                        console.log(componentInstance.form.value);
                        modal.destroy();
                    }
                },
                {
                    label: '取消',
                    shape: 'default',
                    onClick: () => {
                        modal.destroy();
                    }
                }
            ],
            nzContent: RouterEditComponent,
            nzComponentParams: {
                bean
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
            }
        });
    }

    deleteCluster(bean: any) {
    }

    resetFilters() {
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
