import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, Renderer2} from '@angular/core';
import {NzIconService, NzMessageService, NzModalService, NzTreeNodeOptions} from 'ng-zorro-antd';
import {Menu} from '@model/common';
import {BehaviorSubject, combineLatest, Subject} from 'rxjs';
import {filter, finalize, startWith, switchMap, takeUntil, tap} from 'rxjs/operators';
import {BaseComponent} from "@shared/base-class/base.component";
import {BaseService} from "@service/http/base.service";
import {isEmpty} from "@core/utils/string.util";

export enum AdsMenuLevel {
    PORTFOLIO = 1,
    CAMPAIGN = 2,
    GROUP = 3
}

@Component({
    selector: 'app-apis-manager-sider',
    templateUrl: './apis-manager-sider.component.html',
    styleUrls: ['./apis-manager-sider.component.css'],
})
export class ApisManagerSiderComponent extends BaseComponent implements OnInit, OnDestroy {

    @Input() initServerId = null;
    @Input() initMarketId = null;
    @Input() initCampaignId;
    @Input() initGroupId;

    @Output() changeFold = new EventEmitter<boolean>();
    fold: boolean = false;

    isFirstMarketChange: boolean = true;
    isFirstMenuChange: boolean = true;
    portfolioLoading: boolean = false;

    menuId_value = null;

    /**
     * 广告类型（0: 商品广告，1: 品牌广告）
     */
    readonly adsTypeId$: BehaviorSubject<number | string>;

    /**
     * 广告数量
     */
    productAdsNum = 1199; // 商品广告

    /**
     * 店铺、站点
     */
    serverAndMarkets: NzTreeNodeOptions[];
    readonly marketId$: BehaviorSubject<number>;
    serverMarketsLoading: boolean = true;

    /**
     * 菜单树（广告组合 -> 广告活动 -> 广告组）
     */
    menus: Menu[];
    readonly menuId$ = new BehaviorSubject<number>(null);
    menuLevel: number = null; // 1: 所有广告活动，2: 广告活动详情，3: 广告组详情
    menuLevel$ = new BehaviorSubject<number>(this.menuLevel);
    tabIndex: number = 0; // tab索引

    chooseMenu = (idData: any, level: number, tabIndex: number = 0) => {
        const id = 1==1 ? idData : idData.id;
        if (id !== null) {
            const menu: Menu = 1==1 ? this.getMenus(level, id) as Menu : {
                id: id,
                level: level,
                data: idData.data
            } as Menu;
            // if (this.adsData.isApi()) {
                this.setSuperiorMenuActive(menu);
            // }
            this.setAdsData(menu);
            this.menuId$.next(id);
            this.menuLevel$.next(this.menuLevel = level);
            this.tabIndex = tabIndex;
        } else {
            this.menuHomeClicked();
        }
    };

    // 辅助函数
    isBlank = isEmpty;

    private _destroy = new Subject<void>();
    adsJson = {};// 国际化
    constructor(public baseService: BaseService,
                public rd: Renderer2,
                public cdr: ChangeDetectorRef,
                public modalService: NzModalService,
                public nzMessageService: NzMessageService,
                private nzIconService: NzIconService) {
        super(baseService, rd, modalService, nzMessageService);
    }

    ngOnInit(): void {
        // this.setMenuLevel();
        // // 获取店铺站点信息
        // this.serverMarketsLoading = true;
        // this.serverAndMarkets = [];
        // // 获取广告数量信息
        // this.productAdsNum = 11;
        // // 获取菜单信息
        // combineLatest(this.adsTypeId$, this.marketId$).pipe(
        //     takeUntil(this._destroy),
        //     filter(([adsTypeId, marketId]) => marketId !== null),
        //     tap(() => { this.portfolioLoading = true }),
        //     switchMap(([adsTypeId, marketId]) => {
        //         let demo = new Subject<string | string[]>();
        //         return demo.asObservable().pipe(
        //             finalize(() => this.portfolioLoading = false),
        //             startWith([
        //                 [],
        //                 []
        //             ]));
        //     })
        // ).subscribe((portfolios: [any[], any[]]) => {
        //     let noPortfolioMenu: Menu[] = [];
        //     if (portfolios[0] && portfolios[0].length > 0) {
        //         noPortfolioMenu = [{id: -1, level: 1,
        //             title: this.adsJson['noPortfolioCampaigns'],
        //             open: false, selected: false, parenMenuId: null,
        //             children: this.campaignVosToMenus(portfolios[0], -1)
        //         } as Menu];
        //     }
        //     this.menus = noPortfolioMenu.concat(this.portfoliosToMenu(portfolios[1]));
        //     if (!isEmpty(this.initMarketId) && this.isFirstMenuChange) {
        //         const campaignMenu = this.getMenus(AdsMenuLevel.CAMPAIGN, Number(this.initCampaignId)) as Menu;
        //         this.setAdsData(campaignMenu);
        //         if (!this.isBlank(this.initGroupId)) {
        //             const groupMenu = this.getMenus(AdsMenuLevel.GROUP, Number(this.initGroupId), campaignMenu) as Menu;
        //             this.setAdsData(groupMenu);
        //             this.setSuperiorMenuActive(groupMenu);
        //             this.menuId$.next(Number(this.initGroupId));
        //             this.menuLevel$.next(this.menuLevel = AdsMenuLevel.GROUP);
        //             this.tabIndex = 0;
        //         } else {
        //             this.setSuperiorMenuActive(campaignMenu);
        //             this.menuId$.next(Number(this.initCampaignId));
        //             this.menuLevel$.next(this.menuLevel = AdsMenuLevel.CAMPAIGN);
        //             this.tabIndex = 0;
        //         }
        //     } else {
        //         this.isFirstMenuChange = false;
        //     }
        // });
        // 订阅菜单信息更新
        // this.updateMenu(info.level, info.id, info.title, info.data, info.props);
        // 订阅菜单信息新增
        // this.addMenu(info.level, info.parentId, info.data);
    }

    triggerFold() {
        this.changeFold.emit(this.fold = !this.fold);
    }

    setMenuLevel(): void {
        if (!isEmpty(this.initGroupId)) {
            this.menuLevel$.next(this.menuLevel = AdsMenuLevel.GROUP);
        } else if (!isEmpty(this.initCampaignId)) {
            this.menuLevel$.next(this.menuLevel = AdsMenuLevel.CAMPAIGN);
        } else {
            this.menuLevel$.next(this.menuLevel = AdsMenuLevel.PORTFOLIO);
        }
    }

    /********************** 广告类型相关方法 *********************************/

    /**
     * 广告类型改变
     */
    adsTypeChange(adsTypeId: number | string) {
        this.adsTypeId$.next(adsTypeId);
    }

    /********************** 站点相关方法 *********************************/

    serverMarketIdChange(serverMarketId: { serverId?: string, marketId: string }) {
    }

    /********************** 菜单相关方法 *********************************/

    /**
     * 转换Portfolio类型为Menu类型，便于装载菜单树
     */
    private portfoliosToMenu(portfolios: any[]): Menu[] {
        if (portfolios === null || portfolios.length === 0) return [];
        return portfolios.map(portfolio => {
            return {
                id: portfolio.portfolioId,
                level: 1,
                title: portfolio.portfolioName,
                open: false,
                selected: false,
                parenMenuId: null,
                children: portfolio.children ? this.campaignVosToMenus(portfolio.children, portfolio.portfolioId) : null,
                data: portfolio,
            };
        })
    }

    private campaignVosToMenus(campaignVos: any[], parentMenuId: string | number): Menu[] {
        if (campaignVos === null || campaignVos.length === 0) return [];
        return campaignVos.map(campaignVo => {
            return {
                id: campaignVo.campaignId,
                level: 2,
                title: campaignVo.campaignName,
                open: false,
                selected: false,
                parenMenuId: parentMenuId,
                children: campaignVo.children ? this.adsGroupVosToMenus(campaignVo.children, campaignVo.campaignId) : null,
                data: campaignVo,
            };
        });
    }

    private adsGroupVosToMenus(adsGroupVos: any[], parentMenuId: string | number): Menu[] {
        if (adsGroupVos === null || adsGroupVos.length === 0) return [];
        return adsGroupVos.map(adsGroupVo => this.adsGroupVoToMenu(adsGroupVo, parentMenuId));
    }

    private adsGroupVoToMenu(adsGroupVo: any, parentMenuId: string | number): Menu {
        return {
            id: adsGroupVo.groupId,
            level: AdsMenuLevel.GROUP,
            title: adsGroupVo.groupName,
            open: false,
            selected: false,
            parenMenuId: parentMenuId,
            data: adsGroupVo
        };
    }

    refresh() {
        this.setAdsData(null);
        this.marketId$.next(this.marketId$.value);
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

    setAdsData(menu: Menu) {
        // if (menu === null || menu.level === AdsMenuLevel.PORTFOLIO) {
        //     this.adsData.campaign = null;
        //     this.adsData.adsGroup = null;
        // } else if (menu.level === AdsMenuLevel.CAMPAIGN) {
        //     this.adsData.campaign = menu.data;
        //     this.adsData.adsGroup = null;
        // } else if (menu.level === AdsMenuLevel.GROUP) {
        //     this.adsData.adsGroup = menu.data;
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

    /**
     * 当前只支持新增广告组时添加
     */
    addMenu(level: number, parentId: number, data: any) {
        // const parentMenu: Menu = this.getMenus(level - 1, parentId) as Menu;
        // if (level === AdsMenuLevel.GROUP) {
        //     parentMenu.children.push(this.adsGroupVoToMenu(data, parentId));
        // }
    }

    updateMenu(level: number, id: number, title: string, data: any, props: string[]) {
        // const menu: Menu = this.getMenus(level, id) as Menu;
        // menu.title = title;
        // props.forEach(prop => {
        //     menu.data[prop] = data[prop];
        // })
    }

    /**
     * 设置上级菜单Active
     * @param menu
     */
    setSuperiorMenuActive(menu: Menu) {
        menu.selected = true;
        menu.open = true;
        if (menu.parenMenuId !== null) {
            this.setSuperiorMenuActive(this.getMenus(menu.level - 1, menu.parenMenuId) as Menu);
        }
    }

    /**
     * 设置上级菜单Inactive
     * @param menu
     */
    setSuperiorMenuInactive(menu: Menu) {
        menu.selected = false;
        menu.open = false;
        if (menu.parenMenuId !== null) {
            const parentMenu = this.getMenus(menu.level - 1, menu.parenMenuId) as Menu;
            this.setSuperiorMenuInactive(parentMenu);
        }
    }

    /**
     * 设置下级菜单Inactive
     * @param menu
     */
    setJuniorMenuInactive(menu: Menu) {
        menu.selected = false;
        menu.open = false;
        if (menu.children && menu.children.length > 0) {
            menu.children.forEach(child => this.setJuniorMenuInactive(child));
        }
    }

    /********************** 底部操作相关方法 *********************************/

    /**
     * 新增广告组合
     */
    addAdsPortfolio() {
    }

    /**
     * 新增广告活动
     */
    addAdsCampaigns() {
    }

    ngOnDestroy(): void {
        this._destroy.next();
        this._destroy.complete();
    }

}













