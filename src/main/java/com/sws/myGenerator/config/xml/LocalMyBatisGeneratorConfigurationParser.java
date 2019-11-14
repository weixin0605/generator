package com.sws.myGenerator.config.xml;


import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.sws.myGenerator.config.JavaControllerGeneratorConfiguration;
import com.sws.myGenerator.config.JavaServiceGeneratorConfiguration;
import com.sws.myGenerator.config.MyConfiguration;
import com.sws.myGenerator.config.MyContext;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LocalMyBatisGeneratorConfigurationParser {
    private Properties extraProperties;
    private Properties configurationProperties;

    private Configuration orginalConfig;

    public LocalMyBatisGeneratorConfigurationParser(Properties extraProperties,Configuration orginalConfig) {
        super();
        if (extraProperties == null) {
            this.extraProperties = new Properties();
        } else {
            this.extraProperties = extraProperties;
        }
        configurationProperties = new Properties();
        this.orginalConfig = orginalConfig;
    }

    public void setOrginalConfig(Configuration orginalConfig){
        this.orginalConfig = orginalConfig;
    }

    public Configuration getOrginalConfig(){
        return orginalConfig;
    }

    //重新加载配置的properties
    protected void parseProperties(Configuration configuration, Node node) throws XMLParserException {
        Properties attributes = this.parseAttributes(node);
        String resource = attributes.getProperty("resource");
        String url = attributes.getProperty("url");
        if (!StringUtility.stringHasValue(resource) && !StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        } else if (StringUtility.stringHasValue(resource) && StringUtility.stringHasValue(url)) {
            throw new XMLParserException(Messages.getString("RuntimeError.14"));
        } else {
            try {
                URL resourceUrl;
                if (StringUtility.stringHasValue(resource)) {
                    resourceUrl = ObjectFactory.getResource(resource);
                    if (resourceUrl == null) {
                        throw new XMLParserException(Messages.getString("RuntimeError.15", resource));
                    }
                } else {
                    resourceUrl = new URL(url);
                }

                InputStream inputStream = resourceUrl.openConnection().getInputStream();
                this.configurationProperties.load(inputStream);
                inputStream.close();
            } catch (IOException var8) {
                if (StringUtility.stringHasValue(resource)) {
                    throw new XMLParserException(Messages.getString("RuntimeError.16", resource));
                } else {
                    throw new XMLParserException(Messages.getString("RuntimeError.17", url));
                }
            }
        }
    }

    public MyConfiguration  parseConfiguration(Element rootNode) throws XMLParserException {
        MyConfiguration configuration = new MyConfiguration(orginalConfig);
        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("properties".equals(childNode.getNodeName())) {
                this.parseProperties(configuration.getOriginalConfig(), childNode);
            } else if ("context".equals(childNode.getNodeName())) {
                parseContext(configuration, childNode);
            }
        }

        return configuration;
    }

    private void parseContext(MyConfiguration configuration, Node node) {
        Properties attributes = parseAttributes(node);
        String id = attributes.getProperty("id");
        MyContext context = new MyContext(configuration.getOriginalConfig().getContext(id));
        context.setId(id);

        configuration.addContext(context);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

           if ("javaServiceGenerator".equals(childNode.getNodeName())) {
                //context添加javaServiceGenerator标签解析
                parseJavaServiceGenerator(context, childNode);
            }
            if ("javaControllerGenerator".equals(childNode.getNodeName())) {
                //context添加javaControllerGenerator标签解析
                parseJavaControllerGenerator(context, childNode);
            }
        }
    }

    protected void parseJavaControllerGenerator(MyContext context, Node node) {
        //自定义解析器
        JavaControllerGeneratorConfiguration javaControllerGeneratorConfiguration = new JavaControllerGeneratorConfiguration();
        //将解析器法放入上下文
        context.setJavaControllerGeneratorConfiguration(javaControllerGeneratorConfiguration);
        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage");
        String targetProject = attributes.getProperty("targetProject");
        javaControllerGeneratorConfiguration.setTargetPackage(targetPackage);
        javaControllerGeneratorConfiguration.setTargetProject(targetProject);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) {
                parseProperty(javaControllerGeneratorConfiguration, childNode);
            }
        }
    }


    protected void parseJavaServiceGenerator(MyContext context, Node node) {
        //自定义解析器
        JavaServiceGeneratorConfiguration javaServiceGeneratorConfiguration = new JavaServiceGeneratorConfiguration();
        //将解析器法放入上下文
        context.setJavaServiceGeneratorConfiguration(javaServiceGeneratorConfiguration);
        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage");
        String targetProject = attributes.getProperty("targetProject");
        javaServiceGeneratorConfiguration.setTargetPackage(targetPackage);
        javaServiceGeneratorConfiguration.setTargetProject(targetProject);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) {
                parseProperty(javaServiceGeneratorConfiguration, childNode);
            }
        }
    }


    protected void parseProperty(PropertyHolder propertyHolder, Node node) {
        Properties attributes = parseAttributes(node);

        String name = attributes.getProperty("name"); //$NON-NLS-1$
        String value = attributes.getProperty("value"); //$NON-NLS-1$

        propertyHolder.addProperty(name, value);
    }

    protected Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    private String parsePropertyTokens(String string) {
        final String OPEN = "${"; //$NON-NLS-1$
        final String CLOSE = "}"; //$NON-NLS-1$

        String newString = string;
        if (newString != null) {
            int start = newString.indexOf(OPEN);
            int end = newString.indexOf(CLOSE);

            while (start > -1 && end > start) {
                String prepend = newString.substring(0, start);
                String append = newString.substring(end + CLOSE.length());
                String propName = newString.substring(start + OPEN.length(),
                        end);
                String propValue = resolveProperty(propName);
                if (propValue != null) {
                    newString = prepend + propValue + append;
                }

                start = newString.indexOf(OPEN, end);
                end = newString.indexOf(CLOSE, end);
            }
        }
        return newString;
    }

    private String resolveProperty(String key) {
        String property = null;

        property = System.getProperty(key);

        if (property == null) {
            property = configurationProperties.getProperty(key);
        }

        if (property == null) {
            property = extraProperties.getProperty(key);
        }

        return property;
    }
}
