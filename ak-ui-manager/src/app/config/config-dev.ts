import {Config} from './config';

/**
 * @description 开发环境转发的地址
 */
export class ConfigDev extends Config {
    public auth = {
        addr: 'http://127.0.0.1:8081/oauth/token',
        clientId: 'fooClientIdPassword',
        secret: 'secret'
    };
    public apiAddr = 'http://127.0.0.1:8080';
    public menus = [
        {
            iconCls: 'icon-company',
            text: '网关配置',
            children: [
                {iconCls: 'icon-company', text: '集群管理', uri: '/gateway/cluster'},
                {iconCls: 'icon-company', text: '路由管理', uri: '/gateway/router'},
                {iconCls: 'icon-company', text: '高级路由管理', uri: '/gateway/advRouter'},
                {iconCls: 'icon-company', text: '服务授权管理', uri: '/gateway/authorize'}]
        },
        {
            iconCls: 'icon-company',
            text: 'APIs管理',
            children: [
                {iconCls: 'icon-company', text: 'APIs服务', uri: '/interfaces/apis'},
                {iconCls: 'icon-company', text: 'APIs客户', uri: '/gateway/authorize'}]
        },
        {
            iconCls: 'icon-company',
            text: '租户管理',
            children: [
                {iconCls: 'icon-company', text: '租户信息', uri: '/gateway/authorize'}]
        },
        {
            iconCls: 'icon-company',
            text: '监控中心',
            children: [
                {iconCls: 'icon-company', text: '异常监控', uri: '/gateway/authorize'}]
        }
    ];
}
