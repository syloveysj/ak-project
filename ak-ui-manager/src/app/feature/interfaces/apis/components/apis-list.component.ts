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
import {Menu, Option} from '@model/common';
import {combineLatest, defer, Observable, Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged, finalize, map, startWith, switchMap, tap} from 'rxjs/operators';
import {defaultDebounceTime} from "@core/utils/constant.util";
import {ToolService} from "@core/utils/tool.service";
import {Config} from "@config/config";
import {InterfacesService} from "@service/http/interfaces.service";
import {isEmpty} from "@core/utils/string.util";

@Component({
    selector: 'app-apis-list',
    templateUrl: './apis-list.component.html',
    styleUrls: ['./apis-list.component.css'],
})
export class ApisListComponent extends BaseComponent implements OnInit, AfterViewInit {
    @ViewChildren(NzThComponent, {read: false}) private nzThComponents: QueryList<NzThComponent>;
    scrollXWidth = 0;

    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                public cdr: ChangeDetectorRef,
                public toolService: ToolService,
                public interfacesService: InterfacesService,
                public config: Config) {
        super(baseService, rd, modalService, nzMessageService);
    }

    isEmpty = isEmpty;
    serviceId: string = null;
    classifyId: string = null;
    menuName: string = '';
    menus: Menu[] = [];
    dynamicKeys: Option[] = [
        {id: 'alias', text: '名称'},
        {id: 'pathsMemo', text: '路径'}
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
                let cond: any = {serviceId: isEmpty(this.serviceId) ? '-1' : this.serviceId, classifyId: isEmpty(this.classifyId) ? null : this.classifyId};
                (selfKeyword) && (cond[selfDynamicKey] = selfKeyword);
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
                return this.interfacesService.getRouteList(val).pipe(
                    finalize(() => this.loading = false));
            }))
        ).subscribe((data: any) => {
            this.loading = false;
            console.log(data);
            if(data.rows && data.rows.length > 0) {
                data.rows.forEach(item => {
                    item.methodsMemo = this.clearString(item.methodsMemo);
                })
            }
            this.data = data;
        });
    }

    setData(data: { serverId: string, menuId:string }) {
        if(this.serviceId === data.serverId && this.classifyId === data.menuId) return;
        this.serviceId = data.serverId;
        this.classifyId = data.menuId;
        this.updateMenuName();
        this.resetPage();
    }

    setMenus(menus: Menu[]) {
        this.menus = menus;
        this.updateMenuName();
    }

    updateMenuName() {
        this.menus.forEach(menu => {
            if(this.classifyId === menu.id) {
                this.menuName = menu.title;
            }
            if(menu['children'] && menu['children'].length>0) {
                menu['children'].forEach(item => {
                    if(this.classifyId === item.id) {
                        this.menuName = item.title;
                    }
                })
            }
        })
    }

    clearString(str: string) {
        if(isEmpty(str)) return '';
        return str.replace('{', '').replace('}', '');
    }

    openWin(bean: any) {
        window.open("http://192.168.2.154:8094/v1/swagger/index.html?id=" + bean.id, "_blank");
    }

    changeClassify(classifyId: string) {
        const ids = [];
        this.data.rows.forEach(item => {
            if(item.checked) {
                ids.push(item.id);
            }
        })
        this.interfacesService.updateRoutesClassify(classifyId, ids).subscribe(
            (res) => {
                this.selfPage.next(this.selfQueryParams.page);
            }
        )
    }

    deleteRoute(bean: any = null) {
        if(bean == null) {
            let ids = "";
            this.data.rows.forEach(item => {
                if(item.checked === true) {
                    ids += isEmpty(ids)? item.id : ',' + item.id;
                }
            });
            this.interfacesService.removeRoutes(ids).subscribe(
                (res) => {
                    this.selfPage.next(this.selfQueryParams.page);
                }
            )
        } else {
            this.interfacesService.removeRoute(bean.id).subscribe(
                (res) => {
                    this.selfPage.next(this.selfQueryParams.page);
                }
            );
        }
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
