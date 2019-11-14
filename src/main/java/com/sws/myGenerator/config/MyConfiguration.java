package com.sws.myGenerator.config;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import com.sws.myGenerator.codegen.XmlConstants;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;




public class MyConfiguration {


    private List<MyContext> contexts;
    private Configuration originalConfig;

    public void setOriginalConfig(Configuration originalConfig){
        this.originalConfig = originalConfig;
    }

    public Configuration getOriginalConfig(){
        return originalConfig;
    }



    public MyConfiguration(Configuration originalConfig){
        super();
        contexts = new ArrayList<MyContext>();
        this.originalConfig = originalConfig;
    }


    public void validate() throws InvalidConfigurationException {
        List<String> errors = new ArrayList<String>();
        if (contexts.size() == 0) {
            errors.add(getString("ValidationError.11")); //$NON-NLS-1$
        } else {
            for (MyContext context : contexts) {
                context.validate(errors);
            }
        }
        if (errors.size() > 0) {
            throw new InvalidConfigurationException(errors);
        }
    }


    public List<MyContext> getContexts() {
        return contexts;
    }
    public void addContext(MyContext context) {
        contexts.add(context);
    }


    public MyContext getContext(String id) {
        for (MyContext context : contexts) {
            if (id.equals(context.getId())) {
                return context;
            }
        }
        return null;
    }


    public Document toDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID);
        XmlElement rootElement = new XmlElement("generatorConfiguration");
        document.setRootElement(rootElement);


        for (MyContext context : contexts) {
            rootElement.addElement(context.toXmlElement());
        }

        return document;
    }
}
