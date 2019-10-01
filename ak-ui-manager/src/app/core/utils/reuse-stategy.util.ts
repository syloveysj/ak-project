import {
    RouteReuseStrategy, ActivatedRouteSnapshot, DetachedRouteHandle,
    Route
} from '@angular/router';

/**
 * 重写angular路由重用策略，路由重用（多标签页实现）和路由懒加载最终解决方案
 */
export class SimpleReuseStrategy implements RouteReuseStrategy {

    public static handlers: { [key: string]: DetachedRouteHandle } = {};

    /* 根据路由缓存key，删除快照 */
    public static deleteRouteSnapshot(name: string): void {
        let myNmae: string = name.replace(/\//g, '_');
        let flag = true;
        Object.keys(SimpleReuseStrategy.handlers).forEach(key => {
            if (flag) {
                const index: number = key.lastIndexOf('_');
                if (index > 0) {
                    if (key.substring(0, index) === myNmae) {
                        myNmae = key;
                        flag = false;
                    }
                }
            }
        });

        if (SimpleReuseStrategy.handlers.hasOwnProperty(myNmae)) {
            delete SimpleReuseStrategy.handlers[myNmae];
        }
    }

    /* 删除全部快照 */
    public static deleteAllRouteSnapshot(): void {
        SimpleReuseStrategy.handlers = {};
    }


    /* 第一步： 进入路由触发，判断是否同一路由 */
    public shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
        const featureRouteConfig = future.routeConfig;
        const currentRouteConfig = curr.routeConfig;
        const futureParams = future.params;
        const currParams = curr.params;
        const isReUsed = featureRouteConfig === currentRouteConfig &&
            JSON.stringify(futureParams) === JSON.stringify(currParams);
        return isReUsed;
        // 如果路由未来路由url（即要跳转的路由url）和当前路由url（即要离开的路由url）一致，返回false
        /*if (isReUsed && future && future.routeConfig
            && curr && curr.routeConfig
            && future.routeConfig.data['module']
            && future.routeConfig.data['module'] === curr.routeConfig.data['module']) {
            isReUsed = false;
        }
        return isReUsed;*/
    }

    /* 第二步：从缓存中获取快照，若无则返回null */
    public retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
        const config: Route = route.routeConfig;
        // We don't store lazy loaded routes, so don't even bother trying to retrieve them（我们不会存储懒加载路线，所以甚至不试图检索他们）
        if (!config || config.loadChildren) {
            return false;
        }
        return SimpleReuseStrategy.handlers[this.getRouteUrl(route)];
    }

    /* 第三步： 表示对所有路由允许复用，返回true，说明允许复用， 如果你有路由不想利用可以在这加一些业务逻辑判断 */
    public shouldDetach(route: ActivatedRouteSnapshot): boolean {
        // Avoid second call to getter(避免第二次调用getter)
        return route.routeConfig && route.data && route.data.keepAlive;
    }

    /* 第四步： 当路由离开时会触发。按path作为key存储路由快照&组件当前实例对象 */
    public store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
        const routerUrl = this.getRouteUrl(route);
        SimpleReuseStrategy.handlers[routerUrl] = handle;

        /*
          This is where we circumvent the error.
          Detached route includes nested routes, which causes error when parent route does not include the same nested routes
          To prevent this, whenever a parent route is stored, we change/add a redirect route to the current child route
          (这是我们规避错误的地方。
          分离的路由包括嵌套路由，当父路由不包含相同的嵌套路由时会导致错误
          为了防止这种情况，无论何时存储父路由，我们都会将重定向路由更改/添加到当前子路由)
        */
        const config: Route = route.routeConfig;
        if (config) {
            const childRoute: ActivatedRouteSnapshot = route.firstChild;
            const futureRedirectTo = childRoute ? childRoute.url.map(function(urlSegment) {
                return urlSegment.path;
            }).join('/') : '';
            const childRouteConfigs: Route[] = config.children;
            if (childRouteConfigs) {
                let redirectConfigIndex: number;
                const redirectConfig: Route = childRouteConfigs.find(function(childRouteConfig, index) {
                    if (childRouteConfig.path === '' && !!childRouteConfig.redirectTo) {
                        redirectConfigIndex = index;
                        return true;
                    }
                    return false;
                });
                // Redirect route exists(重定向路由存在)
                if (redirectConfig) {
                    if (futureRedirectTo !== '') {
                        // Current activated route has child routes, update redirectTo(当前激活的路由有子路由，更新redirectTo )
                        redirectConfig.redirectTo = futureRedirectTo;
                    } else {
                        // Current activated route has no child routes,
                        // remove the redirect (otherwise retrieval will always fail for this route)（当前激活的路由没有子路由，删除重定向（否则此路由的检索将始终失败））
                        childRouteConfigs.splice(redirectConfigIndex, 1);
                    }
                } else if (futureRedirectTo !== '') {
                    childRouteConfigs.push({
                        path: '',
                        redirectTo: futureRedirectTo,
                        pathMatch: 'full'
                    });
                }
            }
        }
    }

    /* 若 path 在缓存中有的都认为允许还原路由：第五步 */
    public shouldAttach(route: ActivatedRouteSnapshot): boolean {
        return !!SimpleReuseStrategy.handlers[this.getRouteUrl(route)];
    }

    /* 根据路由url并加以处理 */
    private getRouteUrl(route: ActivatedRouteSnapshot) {
        //  因为环境中使用了路由懒加载，返回路径最好带上组件名，防止路由报错->（Cannot reattach ActivatedRouteSnapshot created from a different route）
        // 这句代码可以获取当前路由的组件名componentName，但生成环境（打包）将组建名缩写成随机单个字母，所以需要手动通过route.routeConfig.data['componentName']去获取在路由上自定义的组件名
        let componentShortName = (
            route.routeConfig.loadChildren ||
            route.routeConfig.children ||
            route.routeConfig.component.toString().split('(')[0].split(' ')[1]);
        if (route.routeConfig.data && route.routeConfig.data.componentName) {
            componentShortName = route.routeConfig.data.componentName;
        }
        // "/product/brands"
        const routerUrl = route['_routerState'].url.replace(/\//g, '_') + '_' + componentShortName;
        return routerUrl;
    }

}

