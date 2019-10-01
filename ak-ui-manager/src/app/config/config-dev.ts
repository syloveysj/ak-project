import {Config} from './config';

/**
 * @description 开发环境转发的地址
 */
export class ConfigDev extends Config {
    public apiAddr = '';
    public menus = [
        {
            iconCls: 'icon-company',
            text: '网关配置',
            children: [
                {iconCls: 'icon-company', text: '集群管理', uri: '/gateway/cluster'},
                {iconCls: 'icon-company', text: '路由管理', uri: '/gateway/router'},
                {iconCls: 'icon-company', text: '高级路由管理', uri: '/gateway/advRouter'},
                {iconCls: 'icon-company', text: '服务授权管理', uri: '/gateway/authorize'}]
        }
    ];
}
