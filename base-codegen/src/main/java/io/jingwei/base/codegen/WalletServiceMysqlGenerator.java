package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class WalletServiceMysqlGenerator extends BaseMysqlGenerator2 {

    @Override
    protected String getDbName() {
        return "chia-block";
    }

    @Override
    protected String getModuleName() {
        return "wallet-service";
    }

    @Override
    protected String getPkgName() {
        return "io.zxgangandy.wallet";
    }

    @Override
    protected String getTableName() {
        return "xch_block_height";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        WalletServiceMysqlGenerator generator = new WalletServiceMysqlGenerator();
        generator.run();
    }
}
