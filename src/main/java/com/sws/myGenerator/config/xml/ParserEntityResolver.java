package com.sws.myGenerator.config.xml;

import java.io.IOException;
import java.io.InputStream;


import com.sws.myGenerator.codegen.XmlConstants;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ParserEntityResolver implements EntityResolver {

    public ParserEntityResolver() {
        super();
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID
                .equalsIgnoreCase(systemId)) {
            InputStream is = getClass().getClassLoader().getResourceAsStream(
                    "./src/main/resources/mybatis/mybatis-generator-config_1_0.dtd");
            InputSource ins = new InputSource(is);
            return ins;
        } else {
            return null;
        }
    }
}
