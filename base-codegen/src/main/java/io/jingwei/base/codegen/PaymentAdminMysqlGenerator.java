package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class PaymentAdminMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "payment-admin";
    }

    @Override
    protected String getModuleName() {
        return "payment-admin";
    }

    @Override
    protected String getPkgName() {
        return "io.jingwei.payment_admin";
    }

    @Override
    protected String getTableName() {
        return "mch_info";
    }

    public static void main(String[] args) {
        PaymentAdminMysqlGenerator generator = new PaymentAdminMysqlGenerator();
        generator.run();
    }
}
