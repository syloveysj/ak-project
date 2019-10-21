import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, Renderer2} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Menu} from '@model/common';
import {Subject} from 'rxjs';
import {BaseComponent} from "@shared/base-class/base.component";
import {BaseService} from "@service/http/base.service";
import {isEmpty} from "@core/utils/string.util";
import {ApisPortfolioWinComponent} from "@feature/interfaces/apis/components/apis-portfolio-win.component";
import {ApisServerWinComponent} from "@feature/interfaces/apis/components/apis-server-win.component";

@Component({
    selector: 'app-apis-manager-sider',
    templateUrl: './apis-manager-sider.component.html',
    styleUrls: ['./apis-manager-sider.component.css'],
})
export class ApisManagerSiderComponent extends BaseComponent implements OnInit, OnDestroy {

    @Input() initServerId = null;

    @Output() changeFold = new EventEmitter<boolean>();
    fold: boolean = false;
    serverId = null;

    serversLoading: boolean = false;
    portfolioLoading: boolean = false;
    serverNodes = [
        {
            title: '平台应用',
            key: '99',
            children: [
                {
                    title: '网关集群',
                    key: '1001',
                    isLeaf: true
                },
                {
                    title: '工作流集群',
                    key: '1002',
                    isLeaf: true
                }
            ]
        },
        {
            title: '业务应用',
            key: '100',
            children: [
                {
                    title: '供应链集群',
                    key: '1011',
                    isLeaf: true
                },
                {
                    title: '第三方对接集群',
                    key: '1012',
                    isLeaf: true
                }
            ]
        }
    ];
    menus: Menu[] = [
        {
            title: '网关配置',
            id: '99',
            selected: false,
            level: 1,
            children: [
                {
                    title: '集群管理',
                    id: '1001',
                    selected: true,
                    level: 2
                },
                {
                    title: '路由管理',
                    id: '1002',
                    selected: false,
                    level: 2
                }
            ]
        },
        {
            title: 'APIs管理',
            id: '100',
            selected: false,
            level: 1,
            children: [
                {
                    title: 'APIs服务',
                    id: '1011',
                    selected: false,
                    level: 2
                },
                {
                    title: 'APIs客户',
                    id: '1012',
                    selected: false,
                    level: 2
                }
            ]
        }
    ];
    apisNum = 1199; // 接口数量

    // 辅助函数
    isBlank = isEmpty;

    private _destroy = new Subject<void>();
    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public cdr: ChangeDetectorRef,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit(): void {
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
                        return false;
                    },
                    // loading: (componentInstance) => {
                    //     return componentInstance.loading;
                    // },
                    onClick: (componentInstance) => {
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
            }
        });
    }

    addPortfolio() {
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
                        return false;
                    },
                    // loading: (componentInstance) => {
                    //     return componentInstance.loading;
                    // },
                    onClick: (componentInstance) => {
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
            }
        });

        modal.afterClose.subscribe((result) => {
            if (result) {
            }
        });
    }

    typeChange() {

    }

    serverIdChange(server: { serverId?: string }) {
        this.serverId = server.serverId;
    }

    menuHomeClicked() {
        // if (this.menuId$.value !== null) {
        //     // if (this.adsData.isApi()) {
        //         this.setSuperiorMenuInactive(this.getMenus(this.menuLevel, this.menuId$.value) as Menu);
        //     // } else {
        //     //     this.menus.forEach(menu => menu.selected = false);
        //     // }
        //     this.menuLevel$.next(this.menuLevel = AdsMenuLevel.PORTFOLIO);
        //     this.tabIndex = 0;
        // }
        // this.setAdsData(null);
        // this.menuId$.next(null);
    }

    menuClicked(menu: Menu, childrenMenu?: Menu[]) {
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

    /**********************************
     *      菜单增、删、改、查
     **********************************/

    getMenus(level: number, id: number | string = null, parentMenu: Menu = null): Menu[] | Menu {
        // let levelMenus: Menu[] = [];
        // if (!isBlank(parentMenu) && parentMenu.children.length > 0) {
        //     levelMenus = parentMenu.children;
        // } else {
        //     if (level === AdsMenuLevel.PORTFOLIO) {
        //         levelMenus = this.menus.filter(menu => menu.level === level);
        //     } else {
        //         levelMenus = this.menus.map(menu => menu.children).reduce((e1, e2) => {
        //             if (e1 && !e2) {
        //                 return e1;
        //             } else if (!e1 && e2) {
        //                 return e2;
        //             } else {
        //                 return e1.concat(e2);
        //             }
        //         }, []);
        //         if (level === AdsMenuLevel.GROUP) {
        //             levelMenus = levelMenus.map(menu => menu.children).reduce((e1, e2) => {
        //                 if (e1 && !e2) {
        //                     return e1;
        //                 } else if (!e1 && e2) {
        //                     return e2;
        //                 } else {
        //                     return e1.concat(e2);
        //                 }
        //             }, []);
        //         }
        //     }
        // }
        // if (id !== null) {
        //     const levelIdMenu = levelMenus.filter(menu => menu.id === id);
        //     return levelIdMenu.length > 0 ? levelIdMenu[0] : null;
        // }
        // return levelMenus;
        return null;
    }

    ngOnDestroy(): void {
        this._destroy.next();
        this._destroy.complete();
    }

}













