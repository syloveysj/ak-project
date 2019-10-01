import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {SimpleReuseStrategy} from '@core/utils/reuse-stategy.util';
import {Config} from '@config/config';
import {NzNotificationService} from 'ng-zorro-antd';
import {Platform} from '@angular/cdk/platform';
import {filter, map, mergeMap} from 'rxjs/operators';

export interface RouterInfo {
    title: string;
    module: string;
    power?: string;
    isSelect?: boolean;
    componentName?: string;
}

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    isShowHeadBar = true;
    isAuthPage = false;
    selectedIndex = 0; // 选中的tab页面标签
    menuList: RouterInfo[] = []; // 路由列表

    constructor(private config: Config,
                private router: Router,
                private route: ActivatedRoute,
                private platform: Platform,
                private nzNotificationService: NzNotificationService) {
    }

    ngOnInit(): void {
        this.menuList = [ {title: 'Dashboard', module: '/index', power: '', isSelect: true} ];

        this.checkBrowser();

        this.router.events.pipe(
            filter(event => event instanceof NavigationEnd),
            map(() => this.route),
            map(route => {
                while (route.firstChild) {
                    route = route.firstChild;
                }
                return route;
            }),
            filter(route => route.outlet === 'primary'),
            mergeMap(route => route.data)
        ).subscribe((event) => {
            // 识别登录页面和内页。用来区分是否显示头部
            this.isAuthPage = location.pathname === '/auth/login';

            // 订阅是否显示头部状态
            this.isShowHeadBar = true;

            // 添加退回到登录页面的判断
            if (this.isAuthPage) {
                SimpleReuseStrategy.deleteAllRouteSnapshot();
                this.menuList = [];
            } else {
                // 路由data的标题
                const title = event['title'];
                const module = event['module'];

                // 路径参数：获取queryParams变量
                const param = this.router['browserUrlTree'] &&
                this.router['browserUrlTree'].queryParams ? this.router['browserUrlTree'].queryParams : {};

                // 设置tags标题，暂时不管
                /*if (param && param['taskId']) {
                    if (url.indexOf('/task/taskDetail') >= 0) {
                        title = (param['clientName'] ? param['clientName'] : title) + '[' + param['taskId'] + ']';
                    } else if (param && param['isUpdateTime'] && url.indexOf('/task/analysisPerson') >= 0) {
                        title = '耗时修改' + '[' + param['taskId'] + ']';
                    } else {
                        title = title + '[' + param['taskId'] + ']';
                    }
                }*/

                // 关闭指定页面
                /*let colsePage = '';
                if (window['colsePage']) {
                    colsePage = window['colsePage'];
                }*/
                // 初始化所有menu的isSelected为false
                this.menuList.forEach(p => p.isSelect = false);
                let menu: RouterInfo;
                // 获取新增的路由信息
                if (!title && !module) {
                    menu = {title: '404', module: '/404', power: event['power'], isSelect: true};
                    this.activeRoute(menu);
                    return;
                } else {
                    menu = {title: title, module: event['module'], power: event['power'], isSelect: true};
                }
                // this.titleService.setTitle(title); // 设置title标题
                const exitMenu = this.menuList.find(info => info.title === title);
                if (exitMenu) {// 如果存在不添加，当前表示选中
                    this.menuList.forEach(p => p.isSelect = p.title === title);
                    this.selectedIndex = this.menuList.findIndex(item => item.title === exitMenu.title);
                    return;
                } else {
                    if (!this.isAuthPage) { // 排除登录页面
                        this.menuList = [...this.menuList, menu];
                        this.selectedIndex = this.menuList.length - 1;
                    }
                }

                // console.log(this.menuList);
            }
        });
    }

    // 激活tab所关联的路由
    activeRoute(menu: RouterInfo) {
        this.router.navigateByUrl(menu.module).finally();
        // this.titleService.setTitle(this.menuList[this.selectedIndex].title);
    }

    // 关闭tab页签
    // TODO 缓存清理 如何实现？ 是不是即使handlers清空，内存和dom节点都不会被释放？
    // TODO 需要定制一个事件来处理这些问题。
    closeTab(selectedMenu: RouterInfo) {
        // 最后一个不允许关闭
        if (this.menuList.length === 1) {
            return;
        }
        // 获取当前选中的tab项在集合里面的索引位置
        const optionIndex = this.menuList.findIndex(item => item.module === selectedMenu.module);
        this.menuList = this.menuList.filter(item => item.module !== selectedMenu.module);

        setTimeout(() => {
            this.deleteRouteSnapshot([selectedMenu.module]);
        }, 500);

        // 正在关闭激活tab
        if (this.selectedIndex === optionIndex) {
            // 激活上一个tab
            const nextIndex = this.selectedIndex - 1;
            this.selectedIndex = nextIndex > 0 ? nextIndex : 0;
            // 激活上一个路由
            this.activeRoute(this.menuList[this.selectedIndex]);
        } else if (this.selectedIndex > optionIndex) { // 正在关闭激活tab的左侧tab
            // 关闭前面的tab, 索引下标前移一位
            this.selectedIndex -= 1;
        } else { // 正在关闭激活tab的右侧tab
            // 关闭后面的tab, 不作任何处理
        }
    }

    deleteRouteSnapshot(uris: string[]) {
        uris.forEach(uri => {
            SimpleReuseStrategy.deleteRouteSnapshot(uri);
        });
    }

    // 关闭其它页面
    closeOtherPage() {
        if (this.menuList.length === 1) {
            return;
        }
        const currentTab = this.menuList.filter(p => p.isSelect === true);
        const otherTab = this.menuList.filter(p => p.isSelect === false);
        const defaultTab = this.menuList.filter(p => p.module === '/index');

        if (currentTab[0].module === '/index') {
            this.menuList = currentTab;
            this.selectedIndex = 0;
        } else {
            this.menuList = [...defaultTab, ...currentTab];
            this.selectedIndex = 1;
        }

        const uris = [];
        for (let i = 0; i < otherTab.length; i++) {
            uris.push(otherTab[i].module);
        }

        setTimeout(() => {
            this.deleteRouteSnapshot(uris);
        }, 500);

        this.router.navigate(['/' + currentTab[0].module]);
    }

    // 关闭所有页面
    closeAllPage() {
        if (this.menuList.length === 1) {
            return;
        }

        const uris = [];
        for (let i = 1; i < this.menuList.length; i++) {
            uris.push(this.menuList[i].module);
        }

        setTimeout(() => {
            this.deleteRouteSnapshot(uris);
        }, 500);

        const tempList = this.menuList[0];
        this.menuList.splice(0, this.menuList.length);
        this.menuList.push(tempList);
        this.router.navigate(['/' + this.menuList[0].module]);
    }

    // 重新加载当前页面
    reloadCurrentPage() {
        const currentTab = this.menuList.find(p => p.isSelect === true);
        this.router.navigate([currentTab.module]);
    }

    // 检测浏览器类型
    checkBrowser() {
        const isRecommendedBrowser = this.platform.WEBKIT || this.platform.BLINK;
        if (!isRecommendedBrowser) {
            this.nzNotificationService.warning(
                '提示',
                '检测到您正在使用非谷歌浏览器，我们推荐您使用谷歌浏览器以达到最佳体验效果。');
        }
    }
}
