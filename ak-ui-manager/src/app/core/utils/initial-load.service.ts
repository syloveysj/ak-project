import {HttpClient} from '@angular/common/http';
import {Config} from '@config/config';
import {Injectable} from '@angular/core';

export interface Dns {
    id: string;
    name: number;
    fullname: string;
    serverAddress: number;
    serverPort: string;
    createTime: string;
    updateTime: string;
}

/**
 * @description 系统初始化服务。最优调用。即使是异步，也确保其它接口请求数据能拿到当前初始化参数。
 */
@Injectable()
export class InitialLoadService {

    public constructor(private httpClient: HttpClient,
                       private config: Config) {
    }

    public load(): void {
        const hostname = window.location.hostname;


    }
}
