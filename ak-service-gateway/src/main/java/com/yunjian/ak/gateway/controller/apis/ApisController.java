package com.yunjian.ak.gateway.controller.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.exception.ValidationException;
import com.yunjian.ak.gateway.controller.router.UpstreamController;
import com.yunjian.ak.gateway.entity.apis.*;
import com.yunjian.ak.gateway.service.apis.ApisRouteService;
import com.yunjian.ak.gateway.service.apis.ApisServiceService;
import com.yunjian.ak.gateway.service.apis.ApisUpstreamService;
import com.yunjian.ak.gateway.service.apis.SwaggerService;
import com.yunjian.ak.gateway.vo.apis.ApiAnalysisVo;
import com.yunjian.ak.gateway.vo.apis.ApiPackageVo;
import com.yunjian.ak.gateway.vo.apis.ApiVo;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.route.RouteList;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import com.yunjian.ak.kong.client.model.admin.target.Target;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;
import com.yunjian.ak.kong.client.model.admin.upstream.Upstream;
import com.yunjian.ak.utils.UUIDUtil;
import com.yunjian.ak.web.utils.http.RestUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 21:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/apis")
public class ApisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private ApisServiceService apisServiceService;

    @Autowired
    private ApisUpstreamService apisUpstreamService;

    @Autowired
    private ApisRouteService apisRouteService;

    @Autowired
    private SwaggerService swaggerService;

    @Autowired
    private KongClient kongApisClient;

    @PostMapping("/services")
    @ApiOperation("添加Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity insertService(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 ApisController 的 Services insert!");

        // 调用接口添加上游
        Upstream upstream = new Upstream();
        upstream.setName(UUIDUtil.createUUID());
        upstream.setSlots(10000);
        upstream = kongApisClient.getUpstreamService().createUpstream(upstream);

        // 调用接口添加服务
        Service service = new Service();
        service.setName(UUIDUtil.createUUID());
        service.setProtocol("http");
        service.setHost(upstream.getName());
        service.setPort(80);
        service.setConnectTimeout(60000L);
        service.setWriteTimeout(60000L);
        service.setReadTimeout(60000L);
        service = kongApisClient.getServiceService().createService(service);

        // 通过数据库更新相关信息
        UpstreamEntity upstreamEntity = new UpstreamEntity();
        upstreamEntity.setId(upstream.getId());
        upstreamEntity.setAlias(entity.getAlias());
        apisUpstreamService.update(upstreamEntity);

        entity.setId(service.getId());
        apisServiceService.update(entity);
        return entity;
    }

    @PutMapping("/services/{id}")
    @ApiOperation("更新Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity updateService(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 ApisController 的 Services update!");

        // 调用接口添加服务
        Service service = new Service();
        BeanUtils.copyProperties(entity, service);
        service = kongApisClient.getServiceService().updateService(entity.getId(), service);

        // 通过数据库更新相关信息
        UpstreamEntity upstreamEntity = new UpstreamEntity();
        upstreamEntity.setName(entity.getHost());
        upstreamEntity.setAlias(entity.getAlias());
        apisUpstreamService.update(upstreamEntity);

        apisServiceService.update(entity);
        return entity;
    }

    @DeleteMapping("/services/{id}")
    @ApiOperation("删除指定id的Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Services成功"
    )})
    public void deleteService(@PathVariable("id") String id) {
        LOGGER.debug("请求 ApisController 删除指定id的Services:{}!", id);

        ServiceEntity entity = apisServiceService.get(id);

        TargetList targetList = kongApisClient.getTargetService().listTargets(entity.getHost(), null, null, null, null, null);
        if(targetList.getData() != null && targetList.getData().size() > 0) {
            throw new ValidationException("请先删除目标Targets");
        }

        RouteList routeList = kongApisClient.getRouteService().listRoutesByService(entity.getId(), null, null, null, 2L, null);
        if(routeList.getData() != null && routeList.getData().size() > 0) {
            throw new ValidationException("请先删除路由Routes");
        }

        kongApisClient.getServiceService().deleteService(entity.getId());
        kongApisClient.getUpstreamService().deleteUpstream(entity.getHost());
    }

    @GetMapping("/services/{id}")
    @ApiOperation("获取指定id的Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity getService(@PathVariable("id") String id) {
        LOGGER.debug("请求 ApisController 获取指定id的Services:{}!", id);

        return apisServiceService.get(id);
    }

    @GetMapping("/services")
    @ApiOperation("获取所有Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有Services成功",
            response = ServiceEntity.class,
            responseContainer = "List"
    )})
    public List<ServiceEntity> getAllServices() {
        LOGGER.debug("请求 ApisController 获取所有 Services!");

        return this.apisServiceService.getAll();
    }

    @PostMapping("/services/{upstreamName}/targets")
    @ApiOperation("添加Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Target成功",
            response = Target.class
    )})
    public Target insertTarget(@PathVariable("upstreamName") String upstreamName, @Valid @RequestBody TargetEntity entity) {
        LOGGER.debug("请求 ApisController 的 Target insert!");

        // 调用接口添加目标
        Target target = new Target();
        BeanUtils.copyProperties(entity, target);
        target = kongApisClient.getTargetService().createTarget(upstreamName, target);

        return target;
    }

    @DeleteMapping("/services/{upstreamName}/targets/{tid}")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTarget(@PathVariable("upstreamName") String upstreamName, @PathVariable("tid") String tid) {
        LOGGER.debug("请求 ApisController 删除指定id的Target:{}!", tid);

        // 调用接口删除目标
        kongApisClient.getTargetService().deleteTarget(upstreamName, tid);
    }

    @DeleteMapping("/services/{upstreamName}/targets")
    @ApiOperation("删除指定ids的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTargets(@PathVariable("upstreamName") String upstreamName, @RequestParam String ids) {
        LOGGER.debug("请求 ApisController 删除指定ids的Target!");

        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除目标
        for(String tid : idList) {
            kongApisClient.getTargetService().deleteTarget(upstreamName, tid);
        }
    }

    @GetMapping("/services/{upstreamName}/targets")
    @ApiOperation("获取Upstream的Target列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Upstream的Target列表成功",
            response = Target.class,
            responseContainer = "List"
    )})
    public List<Target> getTargets(@PathVariable("upstreamName") String upstreamName) {
        LOGGER.debug("请求 ApisController 获取Upstream的Target列表!");

        // 调用接口获取所有目标
        try {
            TargetList targetList = kongApisClient.getTargetService().listTargets(upstreamName, null, null, null, 100L, null);
            return targetList.getData();
        } catch (KongClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/routes")
    @ApiOperation("添加Routes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Routes成功"
    )})
    public void insertRoutes(@Valid @RequestBody ApiPackageVo entity) {
        LOGGER.debug("请求 ApisController 的 Routes insert!");

        // 调用接口添加路由
        Service service = new Service();
        service.setId(entity.getServerId());

        Map swagger = JSON.parseObject(entity.getJsonText().replaceAll("\"\\$ref\"", "\"!!/ref\""), Map.class);

        List<RouteEntity> routeEntities = new ArrayList<>();
        List<SwaggerEntity> swaggerEntities = new ArrayList<>();
        for(ApiVo row : entity.getRows()) {
            Route route = new Route();
            route.setService(service);
            route.setName(row.getName());
            route.setMethods(Arrays.asList(row.getMethod()));
            route.setHosts(Arrays.asList());
            route.setPaths(Arrays.asList(row.getUri()));
            route.setProtocols(Arrays.asList("http", "https"));
            route.setStripPath(true);
            route.setPreserveHost(false);
            if(row.getState() == 0) { // 添加
                route = kongApisClient.getRouteService().createRoute(route);
            } else { // 更新
                route = kongApisClient.getRouteService().updateRoute(row.getId(), route);
            }

            RouteEntity routeEntity = new RouteEntity();
            routeEntity.setId(route.getId());
            routeEntity.setAlias(row.getAlias());
            routeEntity.setMemo(row.getMemo());
            routeEntity.setClassifyId("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
            routeEntities.add(routeEntity);

            SwaggerEntity swaggerEntity = new SwaggerEntity();
            swaggerEntity.setId(route.getId());
            swaggerEntity.setContent(JSON.toJSONString(getSwaggerDoc(row, swagger)).replaceAll("\"!!/ref\"", "\"\\$ref\""));
            swaggerEntities.add(swaggerEntity);
        }

        // 通过数据库更新相关信息
        apisRouteService.updateBatch(routeEntities);

        // 通过数据库更新接口swagger文档
        swaggerService.insertBatch(swaggerEntities);
    }

    @PutMapping("/routes/classify/{classifyId}")
    @ApiOperation("更新Routes分类")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Routes分类成功",
            response = Integer.class
    )})
    public int updateRoutesClassify(@PathVariable("classifyId") String classifyId, @Valid @RequestBody List<String> ids) {
        LOGGER.debug("请求 ApisController 的 Routes updateRoutesClassify!");

        // 通过数据库更新相关信息
        return apisRouteService.updateClassifyBatch(classifyId, ids);
    }

    @DeleteMapping("/routes/{id}")
    @ApiOperation("删除指定id的Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Route成功"
    )})
    public void deleteRoute(@PathVariable("id") String id) {
        LOGGER.debug("请求RouteController删除指定id的Route:{}!", id);

        // 调用接口删除路由
        kongApisClient.getRouteService().deleteRoute(id);
    }

    @DeleteMapping("/routes")
    @ApiOperation("删除指定ids的Routes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Routes成功"
    )})
    public void deleteRoutes(@RequestParam String ids) {
        LOGGER.debug("请求 ApisController 删除指定ids的Routes!");

        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除目标
        for(String id : idList) {
            kongApisClient.getRouteService().deleteRoute(id);
        }
    }

    @GetMapping("/routes")
    @ApiOperation("获取匹配Route列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Route列表成功",
            response = Page.class
    )})
    public Page<RouteEntity> getRoutesByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                            @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                            @RequestParam(value = "cond", required = false) String cond) {
        LOGGER.debug("请求 ApisController 获取匹配Route列表!");

        return apisRouteService.getListByPage(page, pagesize, sort, order, cond);
    }

    @PostMapping("/analysis")
    @ApiOperation("解析服务")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "解析服务成功",
            response = ApiPackageVo.class
    )})
    public ApiPackageVo analysis(@Valid @RequestBody ApiAnalysisVo apiAnalysisVo) {
        System.out.println(apiAnalysisVo);

        ApiPackageVo apiPackageVo = new ApiPackageVo();
        apiPackageVo.setServerId(apiAnalysisVo.getServerId());
        Map swagger = null;
        if(StringUtils.isNotEmpty(apiAnalysisVo.getJsonUrl())) {
            RestTemplate template = RestUtils.createRestTemplate();
            ResponseEntity<String> responseEntity = template.getForEntity(apiAnalysisVo.getJsonUrl(), String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String text = responseEntity.getBody();
                swagger = JSON.parseObject(text, Map.class);
                apiPackageVo.setJsonText(text);
            }
        } else if(StringUtils.isNotEmpty(apiAnalysisVo.getJsonText())) {
            swagger = JSON.parseObject(apiAnalysisVo.getJsonText(), Map.class);
            apiPackageVo.setJsonText(apiAnalysisVo.getJsonText());
        }

        if(swagger == null) {
            throw new ValidationException("json信息无法解析");
        }

        List<RouteEntity> routeEntityList = apisRouteService.getAll();
        List<ApiVo> apiVos = new ArrayList<>();
        Map paths = MapUtils.getMap(swagger, "paths", null);
        if(paths != null) {
            Iterator<Map.Entry<String, Map>> pathsEntries = paths.entrySet().iterator();
            while(pathsEntries.hasNext()){
                Map.Entry<String, Map> pathsEntry = pathsEntries.next();

                Map methods = pathsEntry.getValue();
                Iterator<Map.Entry<String, Map>> methodsEntries = methods.entrySet().iterator();
                while(methodsEntries.hasNext()){
                    Map.Entry<String, Map> methodsEntry = methodsEntries.next();
                    String uri = pathsEntry.getKey().replaceAll("\\{[^}]*\\}","[^/]*");
                    if(!StringUtils.endsWithIgnoreCase(uri, "/")) uri += "/?$";

                    ApiVo apiVo = new ApiVo();
                    apiVo.setName(Base64Utils.encodeToUrlSafeString((methodsEntry.getKey().toUpperCase() + ":" + uri).getBytes(Charset.forName("UTF-8"))).replaceAll("=", "."));
                    apiVo.setUri(uri);
                    apiVo.setMemo(pathsEntry.getKey());
                    apiVo.setMethod(methodsEntry.getKey().toUpperCase());
                    apiVo.setState(0);

                    RouteEntity routeEntity = routeEntityList.stream().filter(item -> StringUtils.equals(item.getName(), apiVo.getName())).findFirst().orElse(null);
                    if(routeEntity != null) {
                        apiVo.setId(routeEntity.getId());
                        if(StringUtils.equals(apiPackageVo.getServerId(), routeEntity.getServiceId())) {
                            apiVo.setState(1);
                        } else {
                            apiVo.setState(2);
                        }
                    }

                    Map methodInfo = methodsEntry.getValue();
                    apiVo.setAlias(MapUtils.getString(methodInfo, "summary"));
                    apiVos.add(apiVo);
                }
            }
        }
        apiPackageVo.setRows(apiVos);

        return apiPackageVo;
    }

    private Map getSwaggerDoc(ApiVo apiVo, Map swagger) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("swagger", "2.0");

        Map<String, Object> info = new HashMap<>();
        info.put("description", "Api Documentation");
        info.put("version", "1.0.0");
        info.put("title", "Api Documentation");
        info.put("termsOfService", "urn:tos");
        info.put("contact", new HashMap<>());

        Map<String, Object> license = new HashMap<>();
        license.put("name", "Apache 2.0");
        license.put("url", "http://www.apache.org/licenses/LICENSE-2.0.html");
        info.put("license", license);
        doc.put("info", info);

//        doc.put("host", ConfigManager.getInstance().getConfig("kong_apis_url"));
        doc.put("host", "!!host!!");
        doc.put("basePath", "/");
        doc.put("schemes", Arrays.asList("http", "https"));

//        Map<String, Object> basic = new HashMap<>();
//        basic.put("type", "basic");

        Map<String, Object> api_key = new HashMap<>();
        api_key.put("type", "apiKey");
        api_key.put("name", "api_key");
        api_key.put("in", "header");

//        Map<String, Object> scopes = new HashMap<>();
//        scopes.put("write:pets", "modify pets in your account");
//        scopes.put("read:pets", "read your pets");
//        Map<String, Object> petstore_auth = new HashMap<>();
//        petstore_auth.put("type", "oauth2");
//        petstore_auth.put("authorizationUrl", "http://petstore.swagger.io/oauth/dialog");
//        petstore_auth.put("flow", "implicit");
//        petstore_auth.put("scopes", scopes);

        Map<String, Object> securityDefinitions = new HashMap<>();
//        securityDefinitions.put("basic", basic);
        securityDefinitions.put("api_key", api_key);
//        securityDefinitions.put("petstore_auth", petstore_auth);
        doc.put("securityDefinitions", securityDefinitions);

//        Map<String, Object> _basic = new HashMap<>();
//        _basic.put("basic", Arrays.asList());
        Map<String, Object> _api_key = new HashMap<>();
        _api_key.put("api_key", Arrays.asList());
//        Map<String, Object> _petstore_auth = new HashMap<>();
//        _petstore_auth.put("petstore_auth", Arrays.asList("read:pets", "write:pets"));
//        doc.put("security", Arrays.asList(_basic, _api_key, _petstore_auth));
        doc.put("security", Arrays.asList(_api_key));

        Map paths = MapUtils.getMap(swagger, "paths", null);
        if(paths != null && paths.containsKey(apiVo.getMemo())) {
            Map path = MapUtils.getMap(paths, apiVo.getMemo());
            Map method = MapUtils.getMap(path, apiVo.getMethod().toLowerCase());

            // 添加tags
            Object tagsChild = MapUtils.getObject(method, "tags", null);
            if(tagsChild != null) {
                List listTagsChild = (List) tagsChild;
                if(listTagsChild != null) {
                    String tagName = listTagsChild.size() > 0 ? listTagsChild.get(0).toString() : null;
                    if (tagName != null) {
                        Object tags = MapUtils.getObject(swagger, "tags", null);
                        if (tags != null) {
                            List<Map> listTags = (List<Map>) tags;
                            if(listTags != null) {
                                Map tag = listTags.stream().filter(item -> StringUtils.equals(item.get("name").toString(), tagName)).findFirst().orElse(null);
                                if (tag != null) {
                                    doc.put("tags", Arrays.asList(tag));
                                }
                            }
                        }
                    }
                }
            }

            // 添加paths
            Object parameters = MapUtils.getObject(method, "parameters", null);
            List<Map<String, Object>> parametersList = null;
            if(parameters != null) {
                parametersList = (List<Map<String, Object>>) parameters;
            }
            if(parametersList == null) parametersList = new ArrayList<>();

            // 添加请求头参数
            Map<String, Object> header_scheme = new HashMap<>();
            header_scheme.put("in", "header");
            header_scheme.put("name", "AK-TENANT-SCHEME");
            header_scheme.put("description", "租户scheme");
            header_scheme.put("type", "string");
            header_scheme.put("required", false);
            Map<String, Object> header_userid = new HashMap<>();
            header_userid.put("in", "header");
            header_userid.put("name", "AK-USER-ID");
            header_userid.put("description", "用户ID");
            header_userid.put("type", "string");
            header_userid.put("required", false);
            Map<String, Object> header_name = new HashMap<>();
            header_name.put("in", "header");
            header_name.put("name", "AK-LOGIN-NAME");
            header_name.put("description", "登录名");
            header_name.put("type", "string");
            header_name.put("required", false);
            Map<String, Object> header_sessionid = new HashMap<>();
            header_sessionid.put("in", "header");
            header_sessionid.put("name", "AK-SESSION-ID");
            header_sessionid.put("description", "SessionId");
            header_sessionid.put("type", "string");
            header_sessionid.put("required", false);
            parametersList.add(header_scheme);
            parametersList.add(header_userid);
            parametersList.add(header_name);
            parametersList.add(header_sessionid);
            method.put("parameters", parametersList);

            Map<String, Object> methodMap = new HashMap<>();
            methodMap.put(apiVo.getMethod().toLowerCase(), method);
            Map<String, Object> pathsMap = new HashMap<>();
            pathsMap.put(apiVo.getMemo(), methodMap);
            doc.put("paths", pathsMap);

            // 添加definitions
            Set<String> keys = getKeys(method);
            if(keys.size() > 0) {
                Map definitions = MapUtils.getMap(swagger, "definitions", null);
                Map<String, Object> definitionsMap = new HashMap<>();
                addDefinitions(keys, definitions, definitionsMap);
                doc.put("definitions", definitionsMap);
            } else {
                doc.put("definitions", new HashMap<>());
            }
        }

        return doc;
    }

    private void addDefinitions(Set<String> keys, Map definitions, Map<String, Object> definitionsMap) {
        for (String key : keys) {
            if(!definitionsMap.containsKey(key)) {
                Map definition = MapUtils.getMap(definitions, key, null);
                if(definition != null) {
                    definitionsMap.put(key, definition);
                    Set<String> otherKeys = getKeys(definition);
                    addDefinitions(otherKeys, definitions, definitionsMap);
                } else {
                    definitionsMap.put(key, new HashMap<>());
                }
            }
        }
    }

    private Set<String> getKeys(Map map) {
        if(map == null) return new HashSet<>();

        Set<String> result = new HashSet<>();
        String content = JSON.toJSONString(map);
        String regEx = "\"#/definitions/([^\"]*?)\"";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(content);
        while(mat.find()) {
            result.add(mat.group(1)); //mat.group(0)包括前后两个字符
        }

        return result;
    }
}
