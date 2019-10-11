import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Config} from '@config/config';
import {map} from 'rxjs/operators';
import {CustomResponse, Paging} from '@model/common';
import {HttpQueryEncoderUtil} from "@core/utils/http-query-encoder.util";

@Injectable({
    providedIn: 'root',
})
export class GatewayService {

    public constructor(private config: Config,
                       private httpClient: HttpClient) {
    }

    // 获取上游列表
    public getUpstreamList(params: any): Observable<Paging<any>> {
        const values: HttpParams = new HttpParams({fromObject: params, encoder: new HttpQueryEncoderUtil()});
        return this.httpClient.get<CustomResponse<Paging<any>>>
        (`${this.config.apiAddr}/v1/mgr/gateway/upstreams`, {params : values})
            .pipe(map((c: CustomResponse<Paging<any>>) => c.data));
    }

    // 添加上游
    public addUpstream(params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/upstreams`, params)
            .pipe(map((c: CustomResponse<any>) => c.data));
    }

    // 更新上游
    public updateUpstream(id: string, params: any): Observable<any> {
        return this.httpClient.put<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/upstreams/${id}`, params)
            .pipe(map((c: CustomResponse<any>) => c.data));
    }

    // 删除上游
    public removeUpstream(id: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/upstreams/${id}`)
            .pipe(map((c: CustomResponse<any>) => c.data));
    }

    // 获取上游目标
    public getUpstreamTargetList(id: string): Observable<any[]> {
        return this.httpClient.get<CustomResponse<any[]>>
        (`${this.config.apiAddr}/v1/mgr/gateway/upstreams/${id}/targets`)
            .pipe(map((c: CustomResponse<any[]>) => c.data));
    }

    // 添加目标
    public addTarget(id: string, params: any): Observable<any> {
        return this.httpClient.post<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/upstreams/${id}/targets`, params)
            .pipe(map((c: CustomResponse<any>) => c.data));
    }

    // 删除目标
    public removeTarget(id: string, targetId: string): Observable<any> {
        return this.httpClient.delete<CustomResponse<any>>(`${this.config.apiAddr}/v1/mgr/gateway/upstreams/${id}/targets/${targetId}`)
            .pipe(map((c: CustomResponse<any>) => c.data));
    }
}
