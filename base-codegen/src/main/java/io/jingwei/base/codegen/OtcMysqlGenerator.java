package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class OtcMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "jingwei-otc";
    }

    @Override
    protected String getModuleName() {
        return "otc-service";
    }

    @Override
    protected String getPkgName() {
        return "io.jingwei.otc";
    }

    @Override
    protected String getTableName() {
        return "qrcode_limit";
    }

    public static void main(String[] args) {
        OtcMysqlGenerator generator = new OtcMysqlGenerator();
        generator.run();
    }
}
