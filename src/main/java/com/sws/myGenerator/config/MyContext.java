package com.sws.myGenerator.config;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sws.myGenerator.api.MyIntrospectedTable;
import com.sws.myGenerator.internal.ObjectFactory;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.PluginAggregator;
import org.mybatis.generator.internal.db.DatabaseIntrospector;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;


public class MyContext extends PropertyHolder {

    private String id;
    private JavaServiceGeneratorConfiguration javaServiceGeneratorConfiguration;
    private JavaControllerGeneratorConfiguration javaControllerGeneratorConfiguration;
    private Context context;
    private PluginAggregator pluginAggregator;
    private List<MyIntrospectedTable> introspectedTables;
    private JDBCConnectionConfiguration jdbcConnectionConfiguration;
    public MyContext(Context context) {
        super();
        this.context = context;
        jdbcConnectionConfiguration = this.context.getJdbcConnectionConfiguration();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public void setJavaServiceGeneratorConfiguration(JavaServiceGeneratorConfiguration javaServiceGeneratorConfiguration) {
        this.javaServiceGeneratorConfiguration = javaServiceGeneratorConfiguration;
    }

    public JavaServiceGeneratorConfiguration getJavaServiceGeneratorConfiguration() {
        return javaServiceGeneratorConfiguration;
    }

    public JavaControllerGeneratorConfiguration getJavaControllerGeneratorConfiguration() {
        return javaControllerGeneratorConfiguration;
    }

    public void setJavaControllerGeneratorConfiguration(JavaControllerGeneratorConfiguration javaControllerGeneratorConfiguration) {
        this.javaControllerGeneratorConfiguration = javaControllerGeneratorConfiguration;
    }

    public void validate(List<String> errors) {
        if (!stringHasValue(id)) {
            errors.add(getString("ValidationError.16")); //$NON-NLS-1$
        }

       if (jdbcConnectionConfiguration != null) {
            jdbcConnectionConfiguration.validate(errors);
        }
        IntrospectedTable it = null;
        try {
            it = ObjectFactory.createIntrospectedTableForValidation(this);
        } catch (Exception e) {
            errors.add(getString("ValidationError.25", id)); //$NON-NLS-1$
        }

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("context"); //$NON-NLS-1$
        xmlElement.addAttribute(new Attribute("id", id)); //$NON-NLS-1$
        addPropertyXmlElements(xmlElement);
        if (javaServiceGeneratorConfiguration != null) {
            xmlElement.addElement(javaServiceGeneratorConfiguration.toXmlElement());
        }
        return xmlElement;
    }


    public void introspectTables(ProgressCallback callback, List<String> warnings, Set<String> fullyQualifiedTableNames) throws SQLException, InterruptedException {
        this.introspectedTables = new ArrayList();
        JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this.context, warnings);
        Connection connection = null;

        try {
            callback.startTask(Messages.getString("Progress.0"));
            connection = this.getConnection();
            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(this.context, connection.getMetaData(), javaTypeResolver, warnings);
            Iterator var7 = this.context.getTableConfigurations().iterator();

            while(true) {
                TableConfiguration tc;
                String tableName;
                do {
                    if (!var7.hasNext()) {
                        return;
                    }
                    tc = (TableConfiguration)var7.next();
                    tableName = StringUtility.composeFullyQualifiedTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName(), '.');
                } while(fullyQualifiedTableNames != null && fullyQualifiedTableNames.size() > 0 && !fullyQualifiedTableNames.contains(tableName));

                if (!tc.areAnyStatementsEnabled()) {
                    warnings.add(Messages.getString("Warning.0", tableName));
                } else {
                    callback.startTask(Messages.getString("Progress.1", tableName));
                    List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
                    if (tables != null) {
                        for(IntrospectedTable i : tables) {
                            MyIntrospectedTable myIntrospectedTable = new MyIntrospectedTable();
                            myIntrospectedTable.setOldIntrospectedTabke(i);
                            this.introspectedTables.add(myIntrospectedTable);
                        }
                    }

                    callback.checkCancel();
                }
            }
        } finally {
            this.closeConnection(connection);
        }
    }

    private Connection getConnection() throws SQLException {
        Object connectionFactory;
        //jdbcConnectionConfiguration = this.context.getJdbcConnectionConfiguration();
        if (this.jdbcConnectionConfiguration != null) {
            connectionFactory = new JDBCConnectionFactory(this.jdbcConnectionConfiguration);
        } else {
            connectionFactory = ObjectFactory.createConnectionFactory(this.context);
        }

        return ((ConnectionFactory)connectionFactory).getConnection();
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException var3) {
            }
        }

    }

    public void generateFiles(ProgressCallback callback, List<GeneratedJavaFile> generatedJavaFiles,
                              List<GeneratedXmlFile> generatedXmlFiles, List<String> warnings) throws InterruptedException {


        //实例化插件
        //context已实现
        pluginAggregator =(PluginAggregator) context.getPlugins();
        //introspectedTables = context.getIntrospectionSteps()
        if (introspectedTables != null) {
            for (MyIntrospectedTable introspectedTable : introspectedTables) {

                introspectedTable.setMycontext(this);
                callback.checkCancel();
                //初始化dao,service,entity,xml名
                introspectedTable.initialize();
                introspectedTable.calculateGenerators(warnings, callback);
                generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
               // generatedXmlFiles.addAll(introspectedTable.getGeneratedXmlFiles());

                generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles(introspectedTable));
                //generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles(introspectedTable));
            }
        }

        generatedJavaFiles.addAll(pluginAggregator.contextGenerateAdditionalJavaFiles());
        //generatedXmlFiles.addAll(pluginAggregator.contextGenerateAdditionalXmlFiles());
    }

}
