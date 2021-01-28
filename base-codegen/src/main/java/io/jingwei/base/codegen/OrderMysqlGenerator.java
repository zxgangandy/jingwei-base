package io.jingwei.base.codegen;


/**
 * <p>
 * mysql 代码生成器
 * </p>
 */
public class OrderMysqlGenerator extends BaseMysqlGenerator {

    @Override
    protected String getDbName() {
        return "ex_order";
    }

    @Override
    protected String getModuleName() {
        return "exchange-order";
    }

    @Override
    protected String getPkgName() {
        return "com.zxgangandy.order";
    }

    @Override
    protected String getTableName() {
        return "order_info";
    }

    @Override
    protected boolean projectDir() {
        return true;
    }

    public static void main(String[] args) {
        OrderMysqlGenerator generator = new OrderMysqlGenerator();
        generator.run();
    }
}
