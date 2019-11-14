
package com.sws.myGenerator.internal;

import com.sws.myGenerator.api.MyIntrospectedTable;
import com.sws.myGenerator.config.MyContext;
import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.config.ConnectionFactoryConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.ObjectFactory.createInternalObject;


public class ObjectFactory {
    private static List<ClassLoader> externalClassLoaders = new ArrayList();

    private ObjectFactory() {
    }

    public static MyIntrospectedTable createIntrospectedTableForValidation(MyContext context) {

        String type = MyIntrospectedTable.class.getName();

        MyIntrospectedTable answer = (MyIntrospectedTable)createInternalObject(type);
        answer.setContext(context.getContext());
        answer.setMycontext(context);
        return answer;
    }
    public static JavaTypeResolver createJavaTypeResolver(Context context, List<String> warnings) {
        JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
        String type;
        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
            if ("DEFAULT".equalsIgnoreCase(type)) {
                type = JavaTypeResolverDefaultImpl.class.getName();
            }
        } else {
            type = JavaTypeResolverDefaultImpl.class.getName();
        }

        JavaTypeResolver answer = (JavaTypeResolver)createInternalObject(type);
        answer.setWarnings(warnings);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }

        answer.setContext(context);
        return answer;
    }
    public static ConnectionFactory createConnectionFactory(Context context) {
        ConnectionFactoryConfiguration config = context.getConnectionFactoryConfiguration();
        String type;
        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
        } else {
            type = JDBCConnectionFactory.class.getName();
        }

        ConnectionFactory answer = (ConnectionFactory)createInternalObject(type);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }

        return answer;
    }



}
