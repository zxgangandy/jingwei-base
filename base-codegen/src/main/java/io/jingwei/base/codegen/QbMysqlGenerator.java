package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class QbMysqlGenerator extends BaseMysqlGenerator2 {

    @Override
    protected String getDbName() {
        return "qb";
    }

    @Override
    protected String getModuleName() {
        return "qb";
    }

    @Override
    protected String getPkgName() {
        return "io.qb";
    }

    @Override
    protected String getTableName() {
        return "cut_log";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        QbMysqlGenerator generator = new QbMysqlGenerator();
        generator.run();
    }
}
