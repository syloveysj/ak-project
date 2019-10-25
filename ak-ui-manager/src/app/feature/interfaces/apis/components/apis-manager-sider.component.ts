import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, Renderer2} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Menu} from '@model/common';
import {combineLatest, Subject} from 'rxjs';
import {BaseComponent} from "@shared/base-class/base.component";
import {BaseService} from "@service/http/base.service";
import {isEmpty} from "@core/utils/string.util";
import {ApisPortfolioWinComponent} from "@feature/interfaces/apis/components/apis-portfolio-win.component";
import {ApisServerWinComponent} from "@feature/interfaces/apis/components/apis-server-win.component";
import {Store} from "@ngrx/store";
import * as fromRoot from "@store/reducers";
import * as ConstantsActions from "@store/actions/constants.actions";
import {finalize, map} from "rxjs/operators";
import {InterfacesService} from "@service/http/interfaces.service";

@Component({
    selector: 'app-apis-manager-sider',
    templateUrl: './apis-manager-sider.component.html',
    styleUrls: ['./apis-manager-sider.component.css'],
})
export class ApisManagerSiderComponent extends BaseComponent implements OnInit, OnDestroy {

    @Input() initServerId = null;

    @Output() changeFold = new EventEmitter<boolean>();
    @Output() dataChange = new EventEmitter<{ serverId: string, menuId:string }>();
    @Output() menuChange = new EventEmitter<Menu[]>();
    fold: boolean = false;
    serverId: string;

    serversLoading: boolean = true;
    portfolioLoading: boolean = false;
    serverNodes: any[] = [];
    menus: Menu[] = [];
    // apisNum: number = 1111; // 接口数量

    // 辅助函数
    // isBlank = isEmpty;

    private _destroy = new Subject<void>();
    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public cdr: ChangeDetectorRef,
                public modalService: NzModalService,
                private store$: Store<fromRoot.State>,
                public interfacesService: InterfacesService,
                public nzMessageService: NzMessageService) {
        super(baseService, rd, modalService, nzMessageService);

        this.applicationTypes$ = this.store$.select(fromRoot.getApplicationTypes);
        this.services$ = this.store$.select(fromRoot.getServices);
        this.store$.dispatch(new ConstantsActions.LoadApplicationTypes());
        this.store$.dispatch(new ConstantsActions.LoadServices());
    }

    ngOnInit(): void {
        combineLatest(this.applicationTypes$, this.services$).pipe(
            map(([applicationTypes, services]) => {
                console.log(applicationTypes, services);
                this.serversLoading = false;
                const data = [];
                applicationTypes.forEach(type => {
                    const bean = {
                        key: type.id,
                        title: type.typeName,
                        children: []
                    };
                    services.forEach(service => {
                        if(service.typeId === type.id) {
                            bean.children.push({
                                key: service.id,
                                title: service.alias,
                                isLeaf: true
                            });
                        }
                    });
                    data.push(bean);
                });
                return data;
            })
        ).subscribe(data =>{
            console.log(data);
            this.serverNodes = data;
        });
    }

    initPortfolio() {
        if(isEmpty(this.serverId)) {
            this.menus = [];
            return;
        }

        this.portfolioLoading = true;
        this.interfacesService.getApisClassifyListByServiceId(this.serverId).pipe(
            finalize(() => this.portfolioLoading = false)
        ).subscribe(
            (res) => {
                const temp = [{
                    title: '待分类',
                    id: 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
                    selected: true,
                    level: 2,
                    isLeaf: true
                }];
                res.forEach(item => {
                    if(item.pid === 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa') {
                        temp.push({
                            title: item.alias,
                            id: item.id,
                            selected: false,
                            level: 2,
                            isLeaf: true
                        });
                    }
                });
                res.forEach(item => {
                    if(item.pid !== 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa') {
                        temp.forEach(bean => {
                            if(item.pid === bean.id) {
                                if(bean.level === 2) {
                                    bean.level = 1;
                                    delete bean.isLeaf;
                                    bean['children'] = [];
                                }
                                bean['children'].push({
                                    title: item.alias,
                                    id: item.id,
                                    selected: false,
                                    level: 2,
                                    isLeaf: true
                                });
                            }
                        })
                    }
                });
                this.menus = temp;
                this.menuChange.emit(temp);
            }
        );
    }

    triggerFold() {
        this.changeFold.emit(this.fold = !this.fold);
    }

    addApplication() {
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal normal',
            nzTitle: "添加服务应用",
            nzMaskClosable: false,
            nzClosable: true,
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
                        componentInstance.loading = true;
                        this.interfacesService.addService(componentInstance.form.value).pipe(
                            finalize(() => componentInstance.loading = false)
                        ).subscribe(
                            (res) => {
                                modal.destroy(true);
                            }
                        );
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
            nzContent: ApisServerWinComponent,
            nzComponentParams: {
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
                this.store$.dispatch(new ConstantsActions.LoadServices());
            }
        });
    }

    addPortfolio() {
        if(isEmpty(this.serverId)) {
            this.nzMessageService.create('warning', `当前没有服务应用.`);
            return null;
        }
        const modal = this.modalService.create({
            nzWrapClassName: 'vertical-center-modal normal',
            nzTitle: "添加服务分类",
            nzMaskClosable: false,
            nzClosable: true,
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
                        componentInstance.loading = true;
                        this.interfacesService.addApisClassify(componentInstance.form.value).pipe(
                            finalize(() => componentInstance.loading = false)
                        ).subscribe(
                            (res) => {
                                modal.destroy(true);
                            }
                        );
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
            nzContent: ApisPortfolioWinComponent,
            nzComponentParams: {
                serverId: this.serverId
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
                this.initPortfolio();
            }
        });
    }

    typeChange() {

    }

    serverIdChange(server: { serverId: string }) {
        this.serverId = server.serverId;
        this.dataChange.emit({serverId: this.serverId, menuId: 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'});
        this.initPortfolio();
    }

    menuHomeClicked() {
        this.dataChange.emit({serverId: this.serverId, menuId: 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'});
        this.initPortfolio();
    }

    menuClicked(menu: Menu, childrenMenu?: Menu[]) {
        console.log(menu);
        this.dataChange.emit({serverId: this.serverId, menuId: menu.id});

        // if (this.menuId$.value === menu.id && this.menuLevel === menu.level) {
        //     if (childrenMenu && childrenMenu.length > 0) {
        //         childrenMenu.forEach(childMenu => this.setJuniorMenuInactive(childMenu));
        //     }
        // } else {
        //     this.setAdsData(menu);
        //     this.menuLevel$.next(this.menuLevel = menu.level);
        //     this.tabIndex = 0;
        //
        //     this.menuId$.next(menu.id as number);
        //     const levelMenus = this.getMenus(menu.level) as Menu[];
        //     levelMenus.forEach(menu => {
        //         if (menu.id !== this.menuId$.value) {
        //             this.setJuniorMenuInactive(menu);
        //         } else {
        //             // this.setSuperiorMenuActive(menu);
        //             menu.selected = true;
        //         }
        //     });
        // }
    }

    ngOnDestroy(): void {
        this._destroy.next();
        this._destroy.complete();
    }

}













