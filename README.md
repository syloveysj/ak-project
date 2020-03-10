# AK云平台
> 框架结构图 ![Framework](http://www.1990tu.com/i/20200310134522kfu.png)
## 目录  
* [背景介绍](#背景介绍)  
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
    * *ak-service-platform* 用户管理服务。
    * *ak-web-swagger* 针对 *api网关* 中注册的服务提供的 文档 和 调试 应用。
    * *ak-ui-manager* 针对AK云平台提供的管理前端应用。
    * *ak-web-app* 统一后端应用，提供登录认证检测、授权的代理请求等。

<a name="部署说明"></a>  
## 部署说明
> 部署将分两部分说明：

#### 一、应用部署
> 将结合Docker来介绍，请根据各自的情况进行调整。<br/>
> 演示2台服务器的应用部署：

IP  | 端口  |  映射公网端口  | 描述
---- | ----- | ----- |  ------  
192.168.1.100  | 80、8443、8001、8444、5432、8080、8081、8082、8083 | 80  |  路由、认证授权应用、统一后端应用、接口文档和调试应用、云平台管理前端应用
192.168.1.101  | 8000、8443、8001、8444、5432、3306、8091、8092 |   |  网关、基础数据库、路由/网关服务、用户管理服务

1. *web路由* 的部署，在192.168.1.100上：
    ``` dockerfile
    # 路由数据库
    docker run -d --name postgres \
        -e "POSTGRES_DB=kong" \
        -e "POSTGRES_USER=kong" \
        -e "POSTGRES_PASSWORD=kong" \
        -v "/data/postgresql/data:/var/lib/postgresql/data" \
        -p 5432:5432 \
        postgres:9.6.14
    
    # 路由
    docker run -d --name kong \
        --link postgres:postgres \
        -e "KONG_DATABASE=postgres" \
        -e "KONG_PG_HOST=postgres" \
        -e "KONG_PG_DATABASE=kong" \
        -e "KONG_PG_USER=kong"  \
        -e "KONG_PG_PASSWORD=kong"  \
        -e "KONG_CASSANDRA_CONTACT_POINTS=postgres" \
        -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
        -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
        -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
        -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
        -e "KONG_ADMIN_LISTEN=0.0.0.0:8001, 0.0.0.0:8444 ssl" \
        -p 80:8000 \
        -p 8443:8443 \
        -p 8001:8001 \
        -p 8444:8444 \
        kong:1.3.0
    ``` 
2. *api网关* 的部署，在192.168.1.101上：
    ``` dockerfile
    # 网关数据库
    docker run -d --name postgres \
        -e "POSTGRES_DB=kong" \
        -e "POSTGRES_USER=kong" \
        -e "POSTGRES_PASSWORD=kong" \
        -v "/data/postgresql/data:/var/lib/postgresql/data" \
        -p 5432:5432 \
        postgres:9.6.14
    
    # 网关
    docker run -d --name kong \
        --link postgres:postgres \
        -e "KONG_DATABASE=postgres" \
        -e "KONG_PG_HOST=postgres" \
        -e "KONG_PG_DATABASE=kong" \
        -e "KONG_PG_USER=kong"  \
        -e "KONG_PG_PASSWORD=kong"  \
        -e "KONG_CASSANDRA_CONTACT_POINTS=postgres" \
        -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
        -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
        -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
        -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
        -e "KONG_ADMIN_LISTEN=0.0.0.0:8001, 0.0.0.0:8444 ssl" \
        -p 8000:8000 \
        -p 8443:8443 \
        -p 8001:8001 \
        -p 8444:8444 \
        kong:1.3.0
    ``` 
3. 基础数据库，在192.168.1.101上：
    ``` dockerfile
    # 基础数据库
    docker run -d --name mysql \
        -v "/data/mysql/conf:/etc/mysql" \
        -v "/data/mysql/logs:/var/log/mysql" \
        -v "/data/mysql/data:/var/lib/mysql" \
        -e MYSQL_ROOT_PASSWORD=123456 \
        -p 3306:3306 \
        mysql:5.7.29
    ``` 
4. 路由/网关服务，在192.168.1.101上：
    ``` dockerfile
    # 路由/网关服务
    docker run -d --name ak-service-gateway \
        -e router_db_url=jdbc:postgresql://192.168.1.100:5432/kong \
        -e router_db_username=kong \
        -e router_db_password=kong \
        -e apis_db_url=jdbc:postgresql://192.168.1.101:5432/kong \
        -e apis_db_username=kong \
        -e apis_db_password=kong \
        -e kong_router_admin_url=http://192.168.1.100:8001 \
        -e kong_apis_admin_url=http://192.168.1.101:8001 \
        -p 8091:8080 \
        ak-service-gateway:1.0.0
    ``` 
5. 用户管理服务，在192.168.1.101上：
    ``` dockerfile
    # 用户管理服务
    docker run -d --name ak-service-platform \
        -e sys_db_url=jdbc:mysql://192.168.1.101:3306/platform?useUnicode=yes&characterEncoding=UTF-8 \
        -e sys_db_username=platform \
        -e sys_db_password=123456 \
        -e ak_gateway_apis_url=http://192.168.1.101:8000 \
        -p 8092:8080 \
        ak-service-platform:1.0.0
    ``` 
6. 认证授权应用，在192.168.1.100上：
    ``` dockerfile
    # 认证授权应用
    docker run -d --name ak-web-oauth \
        -e ak_gateway_apis_url=http://192.168.1.101:8000 \
        -p 8080:8080 \
        ak-web-oauth:1.0.0
    ``` 
7. 统一后端应用，在192.168.1.100上：
    ``` dockerfile
    # 统一后端应用
    docker run -d --name ak-web-app \
        -e ak_gateway_apis_url=http://192.168.1.101:8000 \
        -p 8081:8080 \
        ak-web-oauth:1.0.0
    ``` 
8. 接口文档和调试应用，在192.168.1.100上：
    ``` dockerfile
    # 接口文档和调试应用
    docker run -d --name ak-web-swagger \
        -e ak_gateway_apis_url=http://192.168.1.101:8000 \
        -p 8082:8080 \
        ak-web-swagger:1.0.0
    ``` 
9. 云平台管理前端应用，在192.168.1.100上：
    ``` dockerfile
    # 云平台管理前端应用
    docker run -d --name ak-ui-manager \
        -p 8083:8080 \
        ak-ui-manager:1.0.0
    ``` 

#### 二、用云管理平台对路由和网关的配置
1. 登录云管理平台
    > 地址：[http://192.168.1.100:8083](http://192.168.1.100:8083)<br/>
    用户名：admin<br/>
    密码：123456
2. 在集群管理下添加云平台管理前端应用的集群
    ![setp1-1](http://www.1990tu.com/i/202003102010402el.png)
    ![setp1-2](http://www.1990tu.com/i/202003102013072re.png)  
3. 在路由管理或者高级路由管理中添加云平台UI的路由（域名可以先在hosts中设置）
    ![setp2-1](http://www.1990tu.com/i/202003102016163wq.png)
    ![setp2-2](http://www.1990tu.com/i/20200310201648q8s.png)
    ![setp2-3](http://www.1990tu.com/i/20200310201708ogk.png)
4. 在APIs服务中添加服务分类，并导入服务
    ![setp3-1](http://www.1990tu.com/i/20200310201949geq.png)
    ![setp3-2](http://www.1990tu.com/i/20200310202010ki2.png)
    ![setp3-3](http://www.1990tu.com/i/20200310202028dwu.png)
    ![setp3-4](http://www.1990tu.com/i/20200310202041cr5.png)
    ![setp3-5](http://www.1990tu.com/i/20200310202056qdx.png)
5. 检查服务接口文档，点击服务列表中服务的试运行，将出来如下界面
    ![setp4-1](http://www.1990tu.com/i/20200310202251w6k.png)
6. 如上设置，其它应用和服务都如此操作添加，这样路由、网关、服务、应用就都集成起来了。
    > [http://192.168.1.100:80](http://192.168.1.100) 即为所有应用的入口<br/>
      [http://192.168.1.101:8000](http://192.168.1.101:8000) 即为所有服务API的入口<br/>

<a name="开发样例"></a>  
## 开发样例
1. 应用服务开发
    * *pom.xml* 中加入依赖
    ```xml
    <dependency>
        <groupId>com.yunjian.ak</groupId>
        <artifactId>ak-lib-dao</artifactId>
        <version>${ak.base.version}</version>
    </dependency>
    <dependency>
        <groupId>com.yunjian.ak</groupId>
        <artifactId>ak-lib-service</artifactId>
        <version>${ak.base.version}</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
    </dependency>
    ```
    * *datasorce.xml* 数据源配置,可配置多个 *datasource* 节点：
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <DataSources xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" default="">
      <!--
           default: 是否是缺省操作的库（未切换数据源时操作的库）
           isEncrypt：是否进行数据库密码加密的配置（开启后，下面密码处填密文，代码中会解密）
           isTenant：是否开启租户模式，开启后当前数据源将根据租户自动切换PostgreSql的schema 或 MySql的db（目前租户模式只支持PostgreSql和MySql）
       -->
      <datasource xsi:type="DBCPDataSource" default="true" isEncrypt="false" isTenant="false" id="router" name="router" sys="false" type="CustomDB">
        <poolAttribute key="defaultAutoCommit" value="true" desc="自动提交"/>
        <poolAttribute key="maxActive" value="${db_maxActive}" desc="最大连接数"/>
        <poolAttribute key="initialSize" value="${db_initialSize}" desc="初始连接数"/>
        <poolAttribute key="poolPreparedStatements" value="false" desc="启用预编译"/>
        <poolAttribute key="maxOpenPreparedStatements" value="${db_maxOpenPreparedStatements}" desc="预编译缓存数"/>
        <poolAttribute key="maxIdle" value="${db_maxIdle}" desc="最大空闲数"/>
        <poolAttribute key="minIdle" value="${db_minIdle}" desc="最小空闲数"/>
        <poolAttribute key="maxWait" value="60000" desc="从池中取连接的最大等待时间,单位ms，-1无限制"/>
        <poolAttribute key="validationQueryTimeout" value="1" desc="验证查询超时时间"/>
        <poolAttribute key="testOnBorrow" value="false" desc="获取时验证"/>
        <poolAttribute key="testOnReturn" value="false" desc="返回时验证"/>
        <poolAttribute key="testWhileIdle" value="true" desc="空闲验证"/>
        <poolAttribute key="timeBetweenEvictionRunsMillis" value="${db_timeBetweenEvictionRunsMillis}" desc="空闲连接超时检测间隔"/>
        <poolAttribute key="numTestsPerEvictionRun" value="${db_numTestsPerEvictionRun}" desc="检测空闲连接超时数(每次)"/>
        <poolAttribute key="minEvictableIdleTimeMillis" value="${db_minEvictableIdleTimeMillis}" desc="空闲连接超时时间(毫秒)"/>
        <poolAttribute key="removeAbandoned" value="true" desc="移除弃用连接"/>
        <poolAttribute key="removeAbandonedTimeout" value="${db_removeAbandonedTimeout}" desc="弃用连接超时时间(毫秒)"/>
        <poolAttribute key="logAbandoned" value="false" desc="记录弃用连接日志"/>
        <driverClassName>org.postgresql.Driver</driverClassName>
        <url>jdbc:postgresql://192.168.1.100:5432/kong</url>
        <username>kong</username>
        <password>kong</password>
        <dataSourceFactory>com.yunjian.ak.dao.datasource.impl.DBCPDataSourceFactory</dataSourceFactory>
      </datasource>
      <datasource xsi:type="DBCPDataSource" default="false" isEncrypt="false" isTenant="false" id="apis" name="apis" sys="false" type="CustomDB">
        <poolAttribute key="defaultAutoCommit" value="true" desc="自动提交"/>
        <poolAttribute key="maxActive" value="${db_maxActive}" desc="最大连接数"/>
        <poolAttribute key="initialSize" value="${db_initialSize}" desc="初始连接数"/>
        <poolAttribute key="poolPreparedStatements" value="false" desc="启用预编译"/>
        <poolAttribute key="maxOpenPreparedStatements" value="${db_maxOpenPreparedStatements}" desc="预编译缓存数"/>
        <poolAttribute key="maxIdle" value="${db_maxIdle}" desc="最大空闲数"/>
        <poolAttribute key="minIdle" value="${db_minIdle}" desc="最小空闲数"/>
        <poolAttribute key="maxWait" value="60000" desc="从池中取连接的最大等待时间,单位ms，-1无限制"/>
        <poolAttribute key="validationQueryTimeout" value="1" desc="验证查询超时时间"/>
        <poolAttribute key="testOnBorrow" value="false" desc="获取时验证"/>
        <poolAttribute key="testOnReturn" value="false" desc="返回时验证"/>
        <poolAttribute key="testWhileIdle" value="true" desc="空闲验证"/>
        <poolAttribute key="timeBetweenEvictionRunsMillis" value="${db_timeBetweenEvictionRunsMillis}" desc="空闲连接超时检测间隔"/>
        <poolAttribute key="numTestsPerEvictionRun" value="${db_numTestsPerEvictionRun}" desc="检测空闲连接超时数(每次)"/>
        <poolAttribute key="minEvictableIdleTimeMillis" value="${db_minEvictableIdleTimeMillis}" desc="空闲连接超时时间(毫秒)"/>
        <poolAttribute key="removeAbandoned" value="true" desc="移除弃用连接"/>
        <poolAttribute key="removeAbandonedTimeout" value="${db_removeAbandonedTimeout}" desc="弃用连接超时时间(毫秒)"/>
        <poolAttribute key="logAbandoned" value="false" desc="记录弃用连接日志"/>
        <driverClassName>${apis_db_driver}</driverClassName>
        <url>${apis_db_url}</url>
        <username>${apis_db_username}</username>
        <password>${apis_db_password}</password>
        <dataSourceFactory>com.yunjian.ak.dao.datasource.impl.DBCPDataSourceFactory</dataSourceFactory>
      </datasource>
    </DataSources>
    ```
    * *ak-config.properties* 进行插件参数配置
    * *ak-local-config.properties* 进行本地参数配置
    ``` properties
    mybatis.mapper-locations=classpath*:mapper/**/*.xml
    
    # 数据库连接参数
    db_maxActive=5
    db_initialSize=2
    db_maxIdle=5
    db_minIdle=3
    db_maxOpenPreparedStatements=50
    db_numTestsPerEvictionRun=50
    db_minEvictableIdleTimeMillis=60000
    db_timeBetweenEvictionRunsMillis=5000
    # 关系异常后的阻塞(秒)
    db_removeAbandonedTimeout=300
    
    # 数据库配置文件扫描路径和连接信息
    apis_db_driver=org.postgresql.Driver
    apis_db_url=jdbc:postgresql://192.168.1.101:5432/kong
    apis_db_username=kong
    apis_db_password=kong
    
    # 是否启用租户(全局开关)
    ak_dao_enable_tenant_mode=false
    ```
    * 应用入库添加注解
    ```java
    @SpringBootApplication
    @ServletComponentScan
    @EnableSwagger2
    public class PlatformApp {
        public static void main(String[] args) {
            SpringApplication.run(PlatformApp.class, args);
        }
    }
    ```
    * 代码生成工具使用（Spring-Generator，里面的模版已调整）
    > 工具放在 *assets/tools* 文件夹
    ![setp5-1](http://www.1990tu.com/i/202003102244475zk.png)
    ![setp5-2](http://www.1990tu.com/i/202003102245023e3.png)
    * 数据库实体类示例
    ```java
    @Entity(
            // id 与对应的 Mapper.xml 文件中 namespace 对应
            // 用于关联对应的 Mapper.xml 文件
            id = "entity:com.yunjian.ak.gateway.entity.router.TargetEntity",
            // table 对应数据库中的表
            table = "targets",
            // ds 对应数据源的 id，即这个实体类的操作默认切换到这个数据源执行SQL
            ds = "router",
            // cache 是否开启缓存
            cache = false
    )
    @Data
    public class TargetEntity {
        // 映射的数据库字段
        @Column( id = "id", type = ColumnType.custom )
        // Swagger的描述，用于接口文档的生成
        @ApiModelProperty(value = "ID")
        private String id;
    
        @Column( id = "target" )
        @ApiModelProperty(value = "目标地址")
        @NotBlank(message = "目标地址不能为空")
        private String target;
    
        @Column( id = "weight" )
        @ApiModelProperty(value = "权重")
        private Integer weight;
    
        @ApiModelProperty(value = "标签")
        private List<String> tags;
    }
    ```
    * 数据库操作示例
    ```java
    @Service
    public class TargetService {
        // 数据库查询
        // DaoFactory 进行动态数据源的切换
        // DaoFactory.create(Class<T> entityClass) 数据源为实体类中的设置
        // DaoFactory.create(Class<T> entityClass, String datasourceid) 数据源为指定
        public List<TargetEntity> getAll() {
            return DaoFactory.create(TargetEntity.class).selectAll();
        }
    
        // 开启事务
        // datasourceID 为Sql执行的数据源
        @AkTransactional(datasourceID = "router")
        public void deletBatch(List<String> ids) {
            DaoFactory.create(TargetEntity.class).deleteBatch(ids);
        }
        
        // 分页处理
        public Page<TargetEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
            Pageable pageable = new Pageable(page, pagesize);
            Sortable sortable = SortableUtil.getSortable(sort, order);
            TargetEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, TargetEntity.class) : new TargetEntity();
            return DaoFactory.create(TargetEntity.class).selectByPage(entity, pageable, sortable, true);
        }
    }
    ```

