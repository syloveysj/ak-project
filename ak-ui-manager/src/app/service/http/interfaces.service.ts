import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Config} from '@config/config';
import {map} from 'rxjs/operators';
import {CustomResponse} from '@model/common';

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
            .pipe(map((c: CustomResponse<any>) => c.data));
    }

}
