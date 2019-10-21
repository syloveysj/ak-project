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
}
