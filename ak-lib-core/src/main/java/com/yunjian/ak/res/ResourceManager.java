package com.yunjian.ak.res;

import com.yunjian.ak.exception.ServerConfigException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @Description: 资源管理
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class ResourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceManager.class);

    private static ResourceManager instance;
    private String appRoot;

    /**
     * 初始化资源管理
     * @param appRoot
     */
    public static void init(String appRoot) {
        instance = new ResourceManager(appRoot);
    }

    /**
     * 获取资源管理实例
     * @return
     */
    public static ResourceManager getInstance() {
        if (instance == null) {
            throw new ServerConfigException("服务器配置错误：资源管理器没有初始化，请检查web.xml中是否添加了Listener：com.yunjian.ak.web.init.ServerInitListener");
        } else {
            return instance;
        }
    }

    /**
     * 设置应用根目录
     * @param appRoot
     */
    private ResourceManager(String appRoot) {
        this.appRoot = appRoot;
    }

    /**
     * 根据相对路径获取绝对路径
     * @param relativePath
     * @return
     */
    public String getAbsolutePath(String relativePath) {
        if (StringUtils.isEmpty(relativePath)) {
            return this.appRoot;
        } else {
            return !this.appRoot.endsWith("/") && !this.appRoot.endsWith("\\") ? this.appRoot + (!relativePath.startsWith("/") && !relativePath.startsWith("\\") ? "/" + relativePath : relativePath) : this.appRoot + (!relativePath.startsWith("/") && !relativePath.startsWith("\\") ? relativePath : relativePath.substring(1));
        }
    }

    /**
     * 获取定义内容
     * @param id
     * @return
     */
    public InputStream getDefineContent(String id) {
        String contentType = getContentType(id);
        return StringUtils.isNotEmpty(contentType) ? this.readBusinessResource(id, contentType) : this.readSystemResource(id);
    }

    /**
     * 根据资源文件名获取系统资源输入流
     * @param id
     * @return
     */
    private InputStream readSystemResource(String id) {
        File file = new File(this.appRoot + "/conf/" + id);
        if (!file.exists()) {
            file = new File(this.appRoot + "/metadata/" + id);
        }

        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                LOGGER.error("id[" + id + "]对应的资源不存在");
            }
        } else {
            InputStream stream = ResourceManager.class.getClassLoader().getResourceAsStream("/conf/" + id);
            if (stream != null) {
                return stream;
            } else {
                LOGGER.error("未找到系统资源[" + id + "]");
            }
        }

        return null;
    }

    /**
     * 获取业务资源流【xml:org.xtframe.res.config】
     * @param id
     * @param contentType
     * @return
     */
    private InputStream readBusinessResource(String id, String contentType) {
        String noPrefixId = removePrefix(contentType, id);
        String path = getRelativePath(contentType, noPrefixId);
        File file = new File(this.appRoot + getResourceFolder(contentType) + path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                LOGGER.error("id[" + id + "]对应的资源不存在");
            }
        } else {
            String pathInJar = getResourceFolderInJar(contentType) + path;
            InputStream stream = ResourceManager.class.getClassLoader().getResourceAsStream(pathInJar);
            if (stream != null) {
                return stream;
            }

            LOGGER.error("id[" + id + "]对应的资源不存在，相对路径：" + pathInJar);
        }

        return null;
    }

    /**
     * 根据资源类型获取资源所在根目录
     * @param contentType
     * @return
     */
    private static String getResourceFolder(String contentType) {
        return "/platform";
    }

    /**
     * 根据资源类型获取资源所在Jar中的根目录
     * @param contentType
     * @return
     */
    private static String getResourceFolderInJar(String contentType) {
        return "META-INF/platform";
    }

    /**
     * 获取内容类型
     * @param id
     * @return
     */
    private static String getContentType(String id) {
        return id.indexOf(":") > -1 ? id.substring(0, id.indexOf(":")) : "";
    }

    /**
     * 删除内容前缀
     * @param contentType
     * @param id
     * @return
     */
    public static String removePrefix(String contentType, String id) {
        return id.substring(contentType.length() + 1);
    }

    /**
     * 获取内容相对路径
     * @param contentType
     * @param noPrefixId
     * @return
     */
    public static String getRelativePath(String contentType, String noPrefixId) {
        String path = noPrefixId.replaceAll("\\.", "/");
        path = path + "." + contentType;
        path = "/" + path;
        return path;
    }
}
