package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class AccountMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "ex_account";
    }

    @Override
    protected String getModuleName() {
        return "exchange-account";
    }

    @Override
    protected String getPkgName() {
        return "com.zxgangandy.account";
    }

    @Override
    protected String getTableName() {
        return "spot_account";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        AccountMysqlGenerator generator = new AccountMysqlGenerator();
        generator.run();
    }
}
