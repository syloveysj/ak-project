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
            .pipe(
                filter((c: CustomResponse<any>) => isSuccess(c)),
                map((c: CustomResponse<any>) => c.data));
    }

}
