package io.jingwei.base.codegen;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public abstract class BaseMysqlGenerator {

    protected abstract String getDbName();

    protected abstract String getModuleName();

    protected abstract String getPkgName();

    protected abstract String getTableName();

    protected boolean projectDir() {
        return false;
    }

    public void run() {
        //1. 全局配置
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);

        // System.out.println(paths[0]);
        String parentModule = getModuleName();
        String moduleName = parentModule + "-biz";

        String outputDir = projectDir() ? (projectPath + "/" + moduleName + "/src/main/java") :
                (projectPath + "/" + parentModule + "/" + moduleName + "/src/main/java");
        GlobalConfig globalConfig = new GlobalConfig()
                //.setOutputDir(projectPath + "/" + parentModule + "/" + moduleName + "/src/main/java")
                .setOutputDir(outputDir)
                .setAuthor("Andy")
                .setOpen(false);

        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/" + getDbName() + "?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("root")
                .setPassword("root");

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                .setModuleName("biz")
                .setParent(getPkgName());

        //4. 策略配置
        StrategyConfig stConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setInclude(getTableName())
                .setControllerMappingHyphenStyle(true);
        //.setTablePrefix(pkConfig.getModuleName() + "_");

        //5. 整合配置
        AutoGenerator mpg = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(stConfig)
                .setTemplate(new TemplateConfig().setController(null))
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setPackageInfo(pkConfig);

        mpg.execute();
    }

}
