package com.sws.myGenerator.main;

import com.sws.myGenerator.config.ConfigurationParser;
import com.sws.myGenerator.config.MyConfiguration;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OriginalGenerator {
    public static MyConfiguration originalConfig(File file) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //File configFile = new File(path);
        //初始化配置解析器
        ConfigurationParser cp = new ConfigurationParser(warnings);
        //用配置解析器创建对象
        MyConfiguration myconfig = cp.parseConfiguration(file);
        Configuration config = myconfig.getOriginalConfig();
        //shellcallback接口主要用来处理文件的创建和合并，传入overwrite参数，默认的shellcallback是不支持文件合并的
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        //创建一个MyBatisGenerator对象。MyBatisGenerator类是真正用来执行生成动作的类
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        return myconfig;
    }
}
