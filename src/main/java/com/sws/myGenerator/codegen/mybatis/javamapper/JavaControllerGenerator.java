package com.sws.myGenerator.codegen.mybatis.javamapper;

import com.sws.myGenerator.api.MyIntrospectedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class JavaControllerGenerator extends AbstractJavaGenerator {

    public JavaControllerGenerator(){
        super();
    }
    public boolean isSimple = true;

    public String serviceTypeName = "";
    public String entityName = "";

    @Override
    public List<CompilationUnit> getCompilationUnits() {

        FullyQualifiedJavaType entity = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        //String entityType = entity.getShortName();
        entityName = entity.getShortName().substring(0, 1).toLowerCase()+entity.getShortName().substring(1);

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(((MyIntrospectedTable)introspectedTable).getControllerType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addAnnotation("@RestController");
        topLevelClass.addAnnotation("@RequestMapping(\"/"+entityName+"\")");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RestController");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");

        Method method = new Method();

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        FullyQualifiedJavaType serviceType = new FullyQualifiedJavaType(((MyIntrospectedTable)introspectedTable).getServiceType());
        serviceTypeName = serviceType.getShortName().substring(0, 1).toLowerCase()+serviceType.getShortName().substring(1);
        field.setType(serviceType);
        field.setName(serviceTypeName);
        field.addAnnotation("@Autowired");
        topLevelClass.addField(field);
        topLevelClass.addImportedType(serviceType);



        //增加
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getInsertStatementId());
        FullyQualifiedJavaType parameterType1;
        if (isSimple) {
            parameterType1 = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        } else {
            parameterType1 = introspectedTable.getRules().calculateAllFieldsClass();
        }
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(parameterType1);
        method.addParameter(new Parameter(parameterType1, "entity"));
        method.addBodyLine("return "+serviceTypeName+"."+introspectedTable.getInsertStatementId()+"(entity);");
        method.addAnnotation("@RequestMapping(\"/"+introspectedTable.getInsertStatementId()+"\")");
        topLevelClass.addMethod(method);


        //删除
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getDeleteByPrimaryKeyStatementId());
        StringBuilder sb = new StringBuilder();
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type2 = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
            importedTypes.add(type2);
            method.addParameter(new Parameter(type2, "key"));
            sb.append("key");
        } else {
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type3 = introspectedColumn.getFullyQualifiedJavaType();
                importedTypes.add(type3);
                Parameter parameter = new Parameter(type3, introspectedColumn.getJavaProperty());
                if(sb.toString().length()>0){
                    sb.append(",");
                }
                sb.append(introspectedColumn.getJavaProperty());
                method.addParameter(parameter);
            }
        }
        method.addBodyLine("return "+serviceTypeName+"."+introspectedTable.getDeleteByPrimaryKeyStatementId()+"("+sb.toString()+");");
        method.addAnnotation("@RequestMapping(\"/"+introspectedTable.getDeleteByPrimaryKeyStatementId()+"\")");
        topLevelClass.addMethod(method);

        //修改

        FullyQualifiedJavaType parameterType2;
        if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType2 = new FullyQualifiedJavaType(this.introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType2 = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        }
        importedTypes.add(parameterType2);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getUpdateByPrimaryKeyStatementId());
        method.addParameter(new Parameter(parameterType2, "record"));
        method.addBodyLine("return "+serviceTypeName+"."+introspectedTable.getUpdateByPrimaryKeyStatementId()+"(record);");
        method.addAnnotation("@RequestMapping(\"/"+introspectedTable.getUpdateByPrimaryKeyStatementId()+"\")");
        topLevelClass.addMethod(method);

        //查询
        method = new Method();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(introspectedTable.getSelectAllStatementId());
        method.addBodyLine("return "+serviceTypeName+"."+introspectedTable.getSelectAllStatementId()+"();");
        method.addAnnotation("@RequestMapping(\"/"+introspectedTable.getSelectAllStatementId()+"\")");
        topLevelClass.addMethod(method);
        topLevelClass.addImportedTypes(importedTypes);


        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelExampleClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

}
