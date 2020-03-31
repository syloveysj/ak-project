import {Injectable} from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpErrorResponse,
    HttpResponse
} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';
import {ToolService} from '@core/utils/tool.service';
import {environment} from '@environments/environment';
import {isNotEmpty} from "@core/utils/string.util";
import {Cookie} from "ng2-cookies";

/**
 * @description 全局http拦截器, 对http请求进行处理。
 */
@Injectable()
export class CredentialInterceptor implements HttpInterceptor {

    constructor(private router: Router,
                public nzMessageService: NzMessageService,
                private toolService: ToolService) {

    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        request = request.clone({
            withCredentials: false
        });

        const accessToken = Cookie.get('access_token');
        let useRequest = request;
        if (accessToken && !request.headers.has('Authorization')) {
            useRequest = request.clone({
                headers: request.headers.set("Authorization", "Bearer " + accessToken)
            });
        }

        return next.handle(useRequest).pipe(
            tap(
                event => {
                    if (event instanceof HttpResponse) {
                        if('message' in event.body && isNotEmpty(event.body.message)) {
                            this.nzMessageService.error(errHandle(event.body.message));
                        }
                    }
                    return event;
                }
            ),
            /*mergeMap((event) => {
                if (event instanceof HttpResponse && (event.status !== 200)) {
                    return new Observable((observer: Observer<any>) => observer.error(event));
                } else {
                    return new Observable((observer: Observer<any>) => observer.next(event));
                }
            }),*/
            catchError((res: HttpErrorResponse) => {
                const status = res.status;
                console.log('CredentialInterceptor => ', res);
                if (status === 401) {
                    if (res.url.indexOf('/account/current-username') > -1) {
                        // this.router.navigate(['/auth/login']);
                        this.router.navigate(['/index']);
                    } else {
                        this.nzMessageService.error(errHandle(res));
                    }
                    return throwError(res); // 将错误信息抛给下个拦截器或者请求调用方
                } else if (status === 0) {
                    // session 超时的情况
                    // this.router.navigate(['/auth/login']);
                    this.router.navigate(['/index']);
                } else {
                    this.nzMessageService.error(errHandle(res));
                }
                return throwError(res);
            })
        );
    }
}

/**
 * @description
 *  @param error: HttpErrorResponse对象
 */
export function errHandle(error: HttpErrorResponse) {
    if (!environment.production) {
        if (error.error instanceof ErrorEvent) {
            // 发生客户端或planItemVoList错误。相应的处理。
            console.error('前端错误信息:', error.error.message);
        } else {
            // 后端返回一个不成功的响应代码。如401, 500 等类型的错误.
            // 响应主体可能包含出问题的线索
            // console.error(`后端接口调用异常！当前状态码为: ${error.status}`);
        }
    }

    return (error.error && error.error.message) || '服务器超时，请稍后再试.';
}
