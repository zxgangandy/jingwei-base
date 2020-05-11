package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class PaymentMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "jingwei-payment";
    }

    @Override
    protected String getModuleName() {
        return "payment-service";
    }

    @Override
    protected String getPkgName() {
        return "io.jingwei.payment";
    }

    @Override
    protected String getTableName() {
        return "order";
    }

    public static void main(String[] args) {
        PaymentMysqlGenerator generator = new PaymentMysqlGenerator();
        generator.run();
    }
}
