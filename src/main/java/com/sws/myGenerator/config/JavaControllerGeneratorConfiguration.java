package com.sws.myGenerator.config;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyHolder;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class JavaControllerGeneratorConfiguration extends PropertyHolder {

    private String targetPackage;
    private String targetProject;
    private String implementationPackage;

    public JavaControllerGeneratorConfiguration() {
        super();
    }

    public String getTargetProject() {
        return targetProject;
    }
    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }
    public String getTargetPackage() {
        return targetPackage;
    }
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("javaControllerGenerator");
        if (targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", targetPackage));
        }
        if (targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", targetProject));
        }

        if (implementationPackage != null) {
            answer.addAttribute(new Attribute(
                    "implementationPackage", targetProject)); //$NON-NLS-1$
        }

        addPropertyXmlElements(answer);
        return answer;
    }

    public String getImplementationPackage() {
        return implementationPackage;
    }

    public void setImplementationPackage(String implementationPackage) {
        this.implementationPackage = implementationPackage;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(targetProject)) {
            errors.add(getString("ValidationError.0", contextId));
        }
        if (!stringHasValue(targetPackage)) {
            errors.add(getString("ValidationError.12", "javaServiceGenerator", contextId));
        }
//        if (!stringHasValue(getConfigurationType())) {
//            errors.add(getString("ValidationError.20", contextId));
//        }
    }
}
