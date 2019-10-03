package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.DBType;
import com.yunjian.ak.dao.datasource.core.DataSource;
import com.yunjian.ak.dao.datasource.core.DataSources;
import com.yunjian.ak.dao.datasource.core.PoolAttribute;
import com.yunjian.ak.dao.datasource.impl.DBCPDataSourceImpl;
import com.yunjian.ak.dao.datasource.impl.DataSourcesImpl;
import com.yunjian.ak.dao.datasource.impl.PoolAttributeImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * @Description: 转换器
 * @Author: yong.sun
 * @Date: 2019/5/22 15:37
 * @Version 1.0
 */
public class DataSourcesConverter {
    public static final String DATASOURCES = "DataSources";
    public static final String DATASOURCE = "datasource";
    public static final String POOLATTRIBUTE = "poolAttribute";
    public static final String DRIVERCLASSNAME = "driverClassName";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DATASOURCEFACTORY = "dataSourceFactory";

    public static DataSources read(InputStream inputStream) {
        DataSources dataSources = null;
        try {
            // 创建文档解析的对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 解析文档，形成文档树，也就是生成Document对象
            Document document = builder.parse(inputStream);

            // 获得根节点
            Element rootElement = document.getDocumentElement();
//            System.out.printf("Root Element: %s\n", rootElement.getNodeName());

            if(DATASOURCES.equals(rootElement.getNodeName())) {
                dataSources = new DataSourcesImpl();
                if(rootElement.hasAttribute("default")) {
                    dataSources.setDefault(rootElement.getAttribute("default"));
                }
            } else {
                throw new RuntimeException("数据源根节点异常");
            }

            // 获得根节点下的所有子节点
            NodeList students = rootElement.getChildNodes();
            for (int i = 0; i < students.getLength(); i++){
                // 获得第i个子节点
                Node childNode = students.item(i);
                // 由于节点多种类型，而一般我们需要处理的是元素节点
                // 元素节点就是非空的子节点，也就是还有孩子的子节点
                if (childNode.getNodeType() == Node.ELEMENT_NODE){
                    DataSource dataSource = null;
                    Element childElement = (Element)childNode;
//                    System.out.printf(" Element: %s\n", childElement.getNodeName());
//                    System.out.printf("  Attribute: id = %s\n", childElement.getAttribute("id"));

                    if(DATASOURCE.equals(childElement.getNodeName())) {
                        if(childElement.hasAttribute("xsi:type")) {
                            Class<DataSource> mClass = null;
                            try {
                                mClass = (Class<DataSource>) Class.forName("com.yunjian.ak.dao.datasource.impl." + childElement.getAttribute("xsi:type") + "Impl");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            dataSource = mClass.newInstance();
                        } else {
                            dataSource = new DBCPDataSourceImpl();
                        }

                        if(childElement.hasAttribute("default")) {
                            dataSource.setDefault(Boolean.valueOf(childElement.getAttribute("default")));
                        }
                        if(childElement.hasAttribute("id")) {
                            dataSource.setId(childElement.getAttribute("id"));
                        }
                        if(childElement.hasAttribute("name")) {
                            dataSource.setName(childElement.getAttribute("name"));
                        }
                        if(childElement.hasAttribute("type")) {
                            dataSource.setType(DBType.getByName(childElement.getAttribute("type")));
                        }
                        if(childElement.hasAttribute("isEncrypt")) {
                            dataSource.setIsEncrypt(Boolean.valueOf(childElement.getAttribute("isEncrypt")));
                        }
                        if(childElement.hasAttribute("isTenant")) {
                            dataSource.setIsTenant(Boolean.valueOf(childElement.getAttribute("isTenant")));
                        }
                    } else {
                        throw new RuntimeException("数据源配置节点异常");
                    }

                    // 获得第二级子元素
                    NodeList childNodes = childElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++){
                        Node child = childNodes.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE){
                            Element eChild = (Element) child;
//                            System.out.printf("  sub Element: %s value= %s\n", eChild.getNodeName(), eChild.getTextContent());

                            if(POOLATTRIBUTE.equals(eChild.getNodeName())) {
                                PoolAttribute poolAttribute = new PoolAttributeImpl();
                                poolAttribute.setKey(eChild.getAttribute("key"));
                                poolAttribute.setValue(eChild.getAttribute("value"));
                                if(eChild.hasAttribute("desc")) {
                                    poolAttribute.setDesc(eChild.getAttribute("desc"));
                                }
                                dataSource.addPoolAttribute(poolAttribute);
                            } else if(DRIVERCLASSNAME.equals(eChild.getNodeName())) {
                                dataSource.setDriverClassName(eChild.getTextContent());
                            } else if(URL.equals(eChild.getNodeName())) {
                                dataSource.setUrl(eChild.getTextContent());
                            } else if(USERNAME.equals(eChild.getNodeName())) {
                                dataSource.setUsername(eChild.getTextContent());
                            } else if(PASSWORD.equals(eChild.getNodeName())) {
                                dataSource.setPassword(eChild.getTextContent());
                            } else if(DATASOURCEFACTORY.equals(eChild.getNodeName())) {
                                dataSource.setDataSourceFactory(eChild.getTextContent());
                            }
                        }
                    }

                    dataSources.addDatasource(dataSource);
                }
            }
        } catch (Exception e) {
            dataSources = null;
            e.printStackTrace();
        }
        return dataSources;
    }
}
