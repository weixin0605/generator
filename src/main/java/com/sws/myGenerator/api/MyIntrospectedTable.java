
package com.sws.myGenerator.api;


import com.sws.myGenerator.codegen.mybatis.javamapper.JavaControllerGenerator;
import com.sws.myGenerator.codegen.mybatis.javamapper.JavaServiceGenerator;
import com.sws.myGenerator.config.JavaControllerGeneratorConfiguration;
import com.sws.myGenerator.config.JavaServiceGeneratorConfiguration;
import com.sws.myGenerator.config.MyConfiguration;
import com.sws.myGenerator.config.MyContext;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.rules.ConditionalModelRules;
import org.mybatis.generator.internal.rules.FlatModelRules;
import org.mybatis.generator.internal.rules.HierarchicalModelRules;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class MyIntrospectedTable extends IntrospectedTable {

    protected MyContext mycontext;
    protected Map<MyInternalAttribute, String> internalAttributes;
    protected IntrospectedTable oldIntrospectedTabke;

    public void setOldIntrospectedTabke(IntrospectedTable oldIntrospectedTabke){
        this.oldIntrospectedTabke = oldIntrospectedTabke;
    }

    public IntrospectedTable getOldIntrospectedTabke(){
        return this.oldIntrospectedTabke;
    }
    protected enum MyInternalAttribute {
        ATTR_SERVICE_TYPE,
        ATTR_CONTROLLER_TYPE
    }

    public void setMycontext(MyContext mycontext){
        this.mycontext = mycontext;
    }

    public MyContext getMycontext(){
        return mycontext;
    }

    protected List<AbstractJavaGenerator> javaServiceGenerators;

    protected List<AbstractJavaGenerator> javaControllerGenerators;

    public MyIntrospectedTable() {
        super(TargetRuntime.MYBATIS3);

        javaServiceGenerators = new ArrayList<AbstractJavaGenerator>();
        javaControllerGenerators = new ArrayList<AbstractJavaGenerator>();
        internalAttributes = new HashMap<MyInternalAttribute, String>();
    }



    public void initialize() {
        this.context = this.mycontext.getContext();
        this.fullyQualifiedTable = this.oldIntrospectedTabke.getFullyQualifiedTable();
        this.tableConfiguration = this.oldIntrospectedTabke.getTableConfiguration();
        this.rules = this.oldIntrospectedTabke.getRules();
        this.setInsertSelectiveStatementId(this.oldIntrospectedTabke.getInsertSelectiveStatementId());
        this.setBaseRecordType(this.oldIntrospectedTabke.getBaseRecordType());
        this.setDeleteByPrimaryKeyStatementId(this.oldIntrospectedTabke.getDeleteByPrimaryKeyStatementId());
        this.setPrimaryKeyType(this.oldIntrospectedTabke.getPrimaryKeyType());
        this.primaryKeyColumns= this.oldIntrospectedTabke.getPrimaryKeyColumns();

        calculateXmlAttributes();
        calculateJavaClientAttributes();
        calculateJavaServiceAttributes();//service
        calculateModelAttributes();
        calculateJavaControllerAttributes();

        if (tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
            rules = new HierarchicalModelRules(this);
        } else if (tableConfiguration.getModelType() == ModelType.FLAT) {
            rules = new FlatModelRules(this);
        } else {
            rules = new ConditionalModelRules(this);
        }

        context.getPlugins().initialized(this);
    }
    protected void calculateJavaControllerAttributes() {
        if (mycontext.getJavaControllerGeneratorConfiguration() == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(calculateJavaConPacktrollerPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Controller");
        setControllerType(sb.toString());
    }

    protected String calculateJavaConPacktrollerPackage() {
        JavaControllerGeneratorConfiguration config = mycontext.getJavaControllerGeneratorConfiguration();
        if (config == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
        return sb.toString();
    }

    protected void calculateXmlAttributes() {
        setIbatis2SqlMapPackage(calculateSqlMapPackage());
        setIbatis2SqlMapFileName(calculateIbatis2SqlMapFileName());
        setMyBatis3XmlMapperFileName(calculateMyBatis3XmlMapperFileName());
        setMyBatis3XmlMapperPackage(calculateSqlMapPackage());

        setIbatis2SqlMapNamespace(calculateIbatis2SqlMapNamespace());
        setMyBatis3FallbackSqlMapNamespace(calculateMyBatis3FallbackSqlMapNamespace());

        setSqlMapFullyQualifiedRuntimeTableName(calculateSqlMapFullyQualifiedRuntimeTableName());
        setSqlMapAliasedFullyQualifiedRuntimeTableName(calculateSqlMapAliasedFullyQualifiedRuntimeTableName());

        setCountByExampleStatementId("countByExample"); //$NON-NLS-1$
        setDeleteByExampleStatementId("deleteByExample"); //$NON-NLS-1$
        setDeleteByPrimaryKeyStatementId("deleteByPrimaryKey"); //$NON-NLS-1$
        setInsertStatementId("insert"); //$NON-NLS-1$
        setInsertSelectiveStatementId("insertSelective"); //$NON-NLS-1$
        setSelectAllStatementId("selectAll"); //$NON-NLS-1$
        setSelectByExampleStatementId("selectByExample"); //$NON-NLS-1$
        setSelectByExampleWithBLOBsStatementId("selectByExampleWithBLOBs"); //$NON-NLS-1$
        setSelectByPrimaryKeyStatementId("selectByPrimaryKey"); //$NON-NLS-1$
        setUpdateByExampleStatementId("updateByExample"); //$NON-NLS-1$
        setUpdateByExampleSelectiveStatementId("updateByExampleSelective"); //$NON-NLS-1$
        setUpdateByExampleWithBLOBsStatementId("updateByExampleWithBLOBs"); //$NON-NLS-1$
        setUpdateByPrimaryKeyStatementId("updateByPrimaryKey"); //$NON-NLS-1$
        setUpdateByPrimaryKeySelectiveStatementId("updateByPrimaryKeySelective"); //$NON-NLS-1$
        setUpdateByPrimaryKeyWithBLOBsStatementId("updateByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        setBaseResultMapId("BaseResultMap"); //$NON-NLS-1$
        setResultMapWithBLOBsId("ResultMapWithBLOBs"); //$NON-NLS-1$
        setExampleWhereClauseId("Example_Where_Clause"); //$NON-NLS-1$
        setBaseColumnListId("Base_Column_List"); //$NON-NLS-1$
        setBlobColumnListId("Blob_Column_List"); //$NON-NLS-1$
        setMyBatis3UpdateByExampleWhereClauseId("Update_By_Example_Where_Clause"); //$NON-NLS-1$
    }
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        if (stringHasValue(tableConfiguration.getMapperName())) {
            sb.append(tableConfiguration.getMapperName());
        } else {
            if (stringHasValue(fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); //$NON-NLS-1$
        }
        setMyBatis3JavaMapperType(sb.toString());

    }


    protected void calculateModelAttributes() {
        String pakkage = calculateJavaModelPackage();

        StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Key"); //$NON-NLS-1$
        setPrimaryKeyType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        setBaseRecordType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs"); //$NON-NLS-1$
        setRecordWithBLOBsType(sb.toString());

        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Example"); //$NON-NLS-1$
        setExampleType(sb.toString());
    }
    protected String calculateJavaClientInterfacePackage() {
        JavaClientGeneratorConfiguration config = this.context.getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(config.getTargetPackage());
            sb.append(this.fullyQualifiedTable.getSubPackageForClientOrSqlMap(this.isSubPackagesEnabled(config)));
            return sb.toString();
        }
    }

    protected void calculateJavaServiceAttributes() {
        if (mycontext.getJavaServiceGeneratorConfiguration() == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(calculateJavaServicePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Service");
        setServiceType(sb.toString());
    }

    protected String calculateJavaServicePackage() {
        JavaServiceGeneratorConfiguration config = mycontext.getJavaServiceGeneratorConfiguration();
        if (config == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(fullyQualifiedTable.getSubPackageForClientOrSqlMap(isSubPackagesEnabled(config)));
        return sb.toString();
    }

    public void setServiceType(String serviceType) {
        internalAttributes.put(MyInternalAttribute.ATTR_SERVICE_TYPE, serviceType);
    }

    public void setControllerType(String controllerType) {
        internalAttributes.put(MyInternalAttribute.ATTR_CONTROLLER_TYPE, controllerType);
    }

    private boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
        return isTrue(propertyHolder.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
    }





    @Override
    public void calculateGenerators(List<String> warnings,
                                    ProgressCallback progressCallback) {

        calculateJavaServiceGenerators(warnings, progressCallback);
        calculateJavaControllerGenerators(warnings,progressCallback);
    }


    protected AbstractJavaGenerator calculateJavaControllerGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaGenerator javaGenerator = new JavaControllerGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaControllerGenerators.add(javaGenerator);

        return javaGenerator;
    }

    protected AbstractJavaGenerator calculateJavaServiceGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaGenerator javaGenerator = new JavaServiceGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaServiceGenerators.add(javaGenerator);

        return javaGenerator;
    }


    protected void initializeAbstractGenerator(
            AbstractGenerator abstractGenerator, List<String> warnings,
            ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }
    public String getServiceType() {
        return internalAttributes.get(MyInternalAttribute.ATTR_SERVICE_TYPE);
    }

    public String getControllerType() {
        return internalAttributes.get(MyInternalAttribute.ATTR_CONTROLLER_TYPE);
    }
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        for (AbstractJavaGenerator javaGenerator : javaServiceGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        mycontext.getJavaServiceGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        for (AbstractJavaGenerator javaGenerator : javaControllerGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        mycontext.getJavaControllerGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {

        return null;
    }

    @Override
    public int getGenerationSteps() {
        return javaServiceGenerators.size();
    }

    @Override
    public boolean isJava5Targeted() {
        return true;
    }

    @Override
    public boolean requiresXMLGenerator() {
       return false;
    }
}
