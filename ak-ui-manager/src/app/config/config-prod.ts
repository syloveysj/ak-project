import {Config} from './config';

/**
 * @description 生产环境转发的地址
 */
export class ConfigProd extends Config {
    public apiAddr = '';
    public menus = [];
}
