import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Config} from '@config/config';
import {filter, map} from 'rxjs/operators';
import {CustomResponse} from '@model/common';
import {isSuccess} from "@core/utils/http-result.util";

@Injectable({
    providedIn: 'root',
})
export class InterfacesService {

    public constructor(private config: Config,
                       private httpClient: HttpClient) {
    }

    // 解析服务
    public serverAnalysis(params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/analysis`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 获取应用分类
    public getApplicationTypeAll(): Observable<any[]> {
        return this.httpClient.get<CustomResponse<any[]>>(`${this.config.apiAddr}/v1/mgr/gateway/application-type/all`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any[]>) => c.data));
    }

    // 获取服务应用
    public getServiceAll(): Observable<any[]> {
        return this.httpClient.get<CustomResponse<any[]>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any[]>) => c.data));
    }

    // 添加服务应用
    public addService(params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 更新服务应用
    public updateService(id: string, params: any): Observable<any> {
        return this.httpClient.put<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${id}`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除服务应用
    public removeService(id: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${id}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 获取指定服务应用
    public getService(id: string): Observable<any> {
        return this.httpClient.get<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${id}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 获取服务分类
    public getApisClassifyAll(): Observable<any[]> {
        return this.httpClient.get<CustomResponse<any[]>>(`${this.config.apiAddr}/v1/mgr/gateway/apis-classify/all`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any[]>) => c.data));
    }

    // 添加服务分类
    public addApisClassify(params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis-classify`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 更新服务分类
    public updateApisClassify(id: string, params: any): Observable<any> {
        return this.httpClient.put<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis-classify/${id}`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除服务分类
    public removeApisClassify(id: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis-classify/${id}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 添加Swagger
    public addSwagger(params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/swagger`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 更新Swagger
    public updateSwagger(id: string, params: any): Observable<any> {
        return this.httpClient.put<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/swagger/${id}`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除Swagger
    public removeSwagger(id: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/swagger/${id}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 获取上游目标
    public getUpstreamTargetList(upstreamName: string): Observable<any[]> {
        return this.httpClient.get<CustomResponse<any[]>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${upstreamName}/targets`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any[]>) => c.data));
    }

    // 添加目标
    public addTarget(upstreamName: string, params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${upstreamName}/targets`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除目标
    public removeTarget(upstreamName: string, targetId: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${upstreamName}/targets/${targetId}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除目标s
    public removeTargetList(upstreamName: string, ids: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/services/${upstreamName}/targets`, {params: {ids}})
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 添加路由s
    public addRoutes(upstreamName: string, params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/routes`, params)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 更新路由s分类
    public updateRoutesClassify(classifyId: string, ids: string[]): Observable<any> {
        return this.httpClient.put<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/routes/classify/${classifyId}`, ids)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除路由
    public removeRoute(id: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/routes/${id}`)
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

    // 删除路由s
    public removeRoutes(ids: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/apis/routes`, {params: {ids}})
            .pipe(filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }
}
