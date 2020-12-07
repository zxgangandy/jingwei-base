package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class CallbackMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "callback-center";
    }

    @Override
    protected String getModuleName() {
        return "callback-service";
    }

    @Override
    protected String getPkgName() {
        return "io.github.zxgangandy.callback";
    }

    @Override
    protected String getTableName() {
        return "callback_reg";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        CallbackMysqlGenerator generator = new CallbackMysqlGenerator();
        generator.run();
    }
}
