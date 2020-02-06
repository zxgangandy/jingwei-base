package io.jingwei.base.utils.tx;

/**
 *
 *
 * 事务业务逻辑有返回值
 */
public interface TxReturnCallback<T> {

    T executeReturn();
}
