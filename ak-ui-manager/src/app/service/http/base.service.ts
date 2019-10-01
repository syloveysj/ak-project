import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Config} from '@config/config';
import {map} from 'rxjs/operators';
import {CustomResponse, MenuBars, Paging} from '@model/common';

@Injectable({
    providedIn: 'root',
})
export class BaseService {

    public constructor(private config: Config,
                       private httpClient: HttpClient) {
    }

    // 切换语言
    public changeLanguage(params: { message: string }) {
        return this.httpClient.get<any>(`${this.config.apiAddr}/v2/system/account/languageLogin`, {params}).pipe(
            map((c: any) => c),
        );
    }

    // 获取动态菜单
    public getWebTrees() {
        return this.httpClient.get<CustomResponse<MenuBars[]>>(`${this.config.apiAddr}/v2/sm/resource/webTree`).pipe(
            map((c: CustomResponse<MenuBars[]>) => c.data),
        );
    }

    // 获取动态控制的数据
    public loadConfigWebData() {
        return this.httpClient.get<CustomResponse<Paging<any>>>(`${this.config.apiAddr}/v2/sm/resource/webData`).pipe(
            map((c: CustomResponse<Paging<any>>) => c.data),
        );
    }

    // 删除附件
    deleteAttempt(id: number): Observable<null> {
        return this.httpClient.delete(`${this.config.apiAddr}/v2/common/attachment/${id}`).pipe(
            map((c: CustomResponse<null>) => c.data),
        );
    }

}
