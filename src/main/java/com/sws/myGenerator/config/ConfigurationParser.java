package com.sws.myGenerator.config;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sws.myGenerator.codegen.XmlConstants;
import com.sws.myGenerator.config.xml.LocalMyBatisGeneratorConfigurationParser;
import com.sws.myGenerator.config.xml.ParserEntityResolver;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.MyBatisGeneratorConfigurationParser;
import org.mybatis.generator.config.xml.ParserErrorHandler;
import org.mybatis.generator.exception.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ConfigurationParser {

    private List<String> warnings;
    private List<String> parseErrors;
    private Properties extraProperties;

    public ConfigurationParser(List<String> warnings) {
        this(null, warnings);
    }

    public ConfigurationParser(Properties extraProperties, List<String> warnings) {
        super();
        this.extraProperties = extraProperties;

        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        } else {
            this.warnings = warnings;
        }

        parseErrors = new ArrayList<String>();
    }

    public MyConfiguration parseConfiguration(File inputFile) throws IOException, XMLParserException {

        FileReader fr = new FileReader(inputFile);

        return parseConfiguration(fr);
    }

    public MyConfiguration parseConfiguration(Reader reader) throws IOException,
            XMLParserException {

        InputSource is = new InputSource(reader);

        return parseConfiguration(is);
    }

    private MyConfiguration parseConfiguration(InputSource inputSource) throws IOException, XMLParserException {
        parseErrors.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //设置验证dtd资源文件
            builder.setEntityResolver(new ParserEntityResolver());
            ParserErrorHandler handler = new ParserErrorHandler(warnings, parseErrors);
            builder.setErrorHandler(handler);
            Document document = null;
            try {
                document = builder.parse(inputSource);
            } catch (SAXParseException e) {
                throw new XMLParserException(parseErrors);
            } catch (SAXException e) {
                if (e.getException() == null) {
                    parseErrors.add(e.getMessage());
                } else {
                    parseErrors.add(e.getException().getMessage());
                }
            }
            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }
            MyConfiguration config;
            Element rootNode = document.getDocumentElement();
            DocumentType docType = document.getDoctype();
            //修改验证xml的SystemId
            if (rootNode.getNodeType() == Node.ELEMENT_NODE && docType.getSystemId().equals(XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID)) {
                config = parseMyBatisGeneratorConfiguration(rootNode);
            } else {
                throw new XMLParserException(getString("RuntimeError.5")); //$NON-NLS-1$
            }

            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }
            return config;
        } catch (ParserConfigurationException e) {
            parseErrors.add(e.getMessage());
            throw new XMLParserException(parseErrors);
        }
    }

    private MyConfiguration parseMyBatisGeneratorConfiguration(Element rootNode) throws XMLParserException {
        MyBatisGeneratorConfigurationParser parser = new MyBatisGeneratorConfigurationParser(extraProperties);
        Configuration configuration =  parser.parseConfiguration(rootNode);
        LocalMyBatisGeneratorConfigurationParser localParser = new LocalMyBatisGeneratorConfigurationParser(extraProperties,configuration);
        MyConfiguration myConfiguration = localParser.parseConfiguration(rootNode);
        return myConfiguration;
    }
}
