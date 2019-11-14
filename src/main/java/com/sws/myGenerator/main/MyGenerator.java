package com.sws.myGenerator.main;

import com.sws.myGenerator.api.LocalMyBatisGenerator;
import com.sws.myGenerator.config.MyConfiguration;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyGenerator {
    public static void main(String[] args) throws Exception{
        String path = "src/main/resources/mybatis/generatorConfig.xml";
        File configFile = new File(path);
        MyConfiguration configuration = OriginalGenerator.originalConfig(configFile);
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        //创建一个MyBatisGenerator对象。MyBatisGenerator类是真正用来执行生成动作的类
        LocalMyBatisGenerator myBatisGenerator = new LocalMyBatisGenerator(configuration, callback, warnings);
        myBatisGenerator.generate(null);
    }
}
