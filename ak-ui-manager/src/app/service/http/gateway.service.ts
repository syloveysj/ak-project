import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Config} from '@config/config';
import {map} from 'rxjs/operators';
import {CustomResponse, Paging} from '@model/common';
import {HttpQueryEncoderUtil} from "@service/utils/http-query-encoder.util";

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
        (`${this.config.apiAddr}/mgr/v1/gateway/upstream`, {params : values})
            .pipe(map((c: CustomResponse<Paging<any>>) => c.data));
    }

}
