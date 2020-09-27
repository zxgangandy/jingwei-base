package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class WalletMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "jingwei-wallet";
    }

    @Override
    protected String getModuleName() {
        return "wallet-service";
    }

    @Override
    protected String getPkgName() {
        return "io.jingwei.wallet";
    }

    @Override
    protected String getTableName() {
        return "eth_address";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        WalletMysqlGenerator generator = new WalletMysqlGenerator();
        generator.run();
    }
}
