# AK云平台
> 框架结构图![框架结构图](https://github.com/syloveysj/ak-project/blob/master/assets/images/ak-frame.png?raw=true)
## 目录  
* [背景介绍](#背景介绍)  
* [项目结构](#项目结构)  
* [部署说明](#部署说明)  
* [开发样例](#开发样例)  

<a name="背景介绍"></a>  
## 背景介绍
*ak-project* 的设计初衷是解决公司产品单应用架构造成的：
* 功能模块之间界限不明确，随着不断的迭代，维护难度逐渐增加
* 开发时，IDE需要加载全部代码，启动和调试耗费相当长的时间
* 持续部署困难，需要重启整个web容器
* 技术栈替换的难度大

而设计的一套微服务架构、基础应用、基础类库：
* *web路由* 和 *api网关* 用的 [Kong](https://docs.konghq.com/)，一个成熟的开源产品，提供了很多丰富的插件，基本开箱即用，节省了很多的开发时间，稳定性也很可靠。
* *前端* 是用 [Ant Desing of Angular](https://ng.ant.design/version/7.5.x) 开发，为单页面应用提供了开箱即用的高质量Anglar组件。
* *后端* 技术栈：SpringBoot、Mybatis、Mockito，数据库：Mysql、PostgreSql，缓存：Redis，消息队列：Rabbitmq，开发工具：IDEA、PowerDesigner，其它：Apollo、Cat、Minio、Jenkins、Docker等。
* *基础类库* 是工程中 **ak-lib** 开头的项目：
    * *ak-lib-core* 提供统一配置管理、接口、接口的默认实现、插件接口、常用的工具类等。
    * *ak-lib-dao* 提供多数据源支持、动态数据源支持、多租户配置、SQL权限过滤、分页支持等。
    * *ak-lib-runtime* 提供应用启动初始化装载、过滤器、接口数据的统一化处理、启动的授权码校验等。
    * *ak-lib-api-proxy* 提供应用代理访问授权的 *api网关* 接口服务功能。
    * *ak-lib-kong* 提供 [Kong](https://docs.konghq.com/) 操作封装的封装。
    * *ak-lib-service* 提供服务应用基础库。
    * *ak-lib-resource-server* 提供Web应用基础库以及统一认证的集成。
* *基础应用* 是工程中 **ak-service** 开头的服务项目、 **ak-web** 开头的web项目 和 **ak-ui** 开头的前端项目：
    * *ak-web-oauth-server* 基于Spring Security Aauth2开发的认证授权应用，主要用于整合应用。
    * *ak-service-gateway* 针对 *web路由* 和 *api网关* 操作提供的服务。
    * *ak-web-swagger* 针对 *api网关* 中注册的服务提供的 文档 和 调试 应用。
    * *ak-ui-manager* 针对AK云平台提供的管理前端项目。

<a name="项目结构"></a>  
## 项目结构


<a name="部署说明"></a>  
## 部署说明


<a name="开发样例"></a>  
## 开发样例

