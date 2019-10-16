import {AfterViewInit, ChangeDetectorRef, Component, OnInit, QueryList, Renderer2, ViewChildren} from '@angular/core';
import {BaseComponent} from '@shared/base-class/base.component';
import {BaseService} from '@service/http/base.service';
import {NzMessageService, NzModalService, NzThComponent} from 'ng-zorro-antd';
import {Config} from '@config/config';
import {Option} from '@model/common';
import {ToolService} from '@core/utils/tool.service';
import {debounceTime, distinctUntilChanged, finalize, map, switchMap, tap} from 'rxjs/operators';
import {combineLatest, defer} from 'rxjs';
import {defaultDebounceTime} from '@core/utils/constant.util';
import {RouterEditComponent} from '@feature/gateway/router/components/router-edit.component';
import {GatewayService} from "@service/http/gateway.service";
import {isEmpty} from "@core/utils/string.util";

@Component({
    selector: 'app-gateway-router',
    templateUrl: './router.component.html',
    styleUrls: ['./router.component.css']
})
export class RouterComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChildren(NzThComponent, {read: false}) private nzThComponents: QueryList<NzThComponent>;
    scrollXWidth = 0;

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

    ngOnInit() {
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
                return this.gatewayService.getRouteList(val).pipe(
                    finalize(() => this.loading = false));
                console.log(val);
            }))
        ).subscribe((data: any) => {
            this.loading = false;
            console.log(data);
            this.data = data;
        });
    }

    openWin(bean: any) {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal large',
            nzTitle: (bean===null ? '新建' : '编辑') + '动态路由',
            nzMaskClosable: false,
            nzFooter: [
                {
                    label: '保存',
                    shape: 'primary',
                    disabled: (componentInstance) => {
                        return !componentInstance.form.valid;
                    },
                    loading: (componentInstance) => {
                        return componentInstance.loading;
                    },
                    onClick: (componentInstance) => {
                        if(bean === null) {
                            this.gatewayService.addRoute(componentInstance.getFromValues()).pipe(
                                finalize(() => componentInstance.loading = false)
                            ).subscribe(
                                (res) => {
                                    modal.destroy(true);
                                }
                            );
                        } else {
                            this.gatewayService.updateRoute(bean.id, componentInstance.getFromValues()).pipe(
                                finalize(() => componentInstance.loading = false)
                            ).subscribe(
                                (res) => {
                                    modal.destroy(true);
                                }
                            );
                        }
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
                this.resetPage();
            }
        });
    }

    deleteRoute(bean: any = null) {
        if(bean == null) {
            const ids = [];
            this.data.rows.forEach(item => {
                if(item.checked) {
                    ids.push({
                        routeId: item.id,
                        serviceId: item.serviceId
                    });
                }
            })
            this.gatewayService.removeRouteList(ids).subscribe(
                (res) => {
                    this.selfPage.next(this.selfQueryParams.page);
                }
            )
        } else {
            this.gatewayService.removeRoute(bean.id, bean.serviceId).subscribe(
                (res) => {
                    this.selfPage.next(this.selfQueryParams.page);
                }
            );
        }
    }

    resetFilters() {
        this.selfPage.next(this.selfQueryParams.page);
    }

    clearString(str: string) {
        if(isEmpty(str)) return '';
        return str.replace('{', '').replace('}', '');
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
