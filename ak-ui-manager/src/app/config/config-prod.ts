import {Config} from './config';

/**
 * @description 生产环境转发的地址
 */
export class ConfigProd extends Config {
    public auth = {
        addr: 'http://192.168.2.154:8092/oauth/token',
        clientId: 'fooClientIdPassword',
        secret: 'secret'
    };
    public apiAddr = 'http://192.168.2.154:8093';
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
                {iconCls: 'icon-company', text: '租户信息', uri: '/tenant/center'}]
        },
        {
            iconCls: 'icon-company',
            text: '监控中心',
            children: [
                {iconCls: 'icon-company', text: '异常预警', uri: '/monitor/center'},
                {iconCls: 'icon-company', text: '操作日志', uri: '/monitor/center'},
                {iconCls: 'icon-company', text: '链路分析', uri: '/monitor/center'},
                {iconCls: 'icon-company', text: '接口调用分析', uri: '/monitor/center'}]
        },
        {
            iconCls: 'icon-company',
            text: '定时任务',
            children: [
                {iconCls: 'icon-company', text: '任务列表', uri: '/task/scheduleJob'},
                {iconCls: 'icon-company', text: '租户任务', uri: '/task/tenantJob'}]
        }
    ];
}
