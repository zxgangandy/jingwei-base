package io.jingwei.base.codegen;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Objects;
import java.util.Scanner;

/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class MysqlGenerator {

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        //1. 全局配置
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);
        String module = scanner("模块名");
        String[] paths = module.split("-");

        System.out.println(paths[0]);
        String parentModule = paths[0] + "-" + paths[1];

        GlobalConfig globalConfig = new GlobalConfig()
                .setOutputDir(projectPath + "/" + parentModule + "/" + module + "/src/main/java")
                .setAuthor("Andy")
                .setOpen(false);

        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/jingwei-payment?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("root")
                .setPassword("root");

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                .setModuleName("biz")
                .setParent("io.jingwei." + (Objects.equals(paths[0], "jingwei") ? paths[1] : paths[0]));

        //4. 策略配置
        StrategyConfig stConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setInclude(scanner("表名"))
                //.setSuperEntityColumns("id")
                .setControllerMappingHyphenStyle(true);
                //.setTablePrefix(pkConfig.getModuleName() + "_");

        //5. 整合配置
        AutoGenerator mpg = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(stConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setPackageInfo(pkConfig);

        mpg.execute();
    }

}
