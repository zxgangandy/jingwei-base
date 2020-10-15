package io.jingwei.base.utils.tx;


import io.jingwei.base.utils.exception.BizErr;
import io.jingwei.base.utils.exception.SysErr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public class TxTemplateService {

    private TransactionTemplate defaultTx;

    public TxTemplateService(TransactionTemplate defaultTx) {
        this.defaultTx = defaultTx;
    }

    /**
     * 编程事务
     * <p>
     * 有异常则外层捕获
     */
    public void doInTransaction(final TxCallback callback) {
        defaultTx.execute((TransactionStatus status) -> {
                    try {
                        callback.execute();
                    } catch (BizErr e) {
                        log.warn("TxTemplate BizErr  : {}", e.getCode());
                        status.setRollbackOnly();
                        throw e;
                    } catch (DuplicateKeyException e) {
                        log.warn("TxTemplate DuplicateKeyException : {}", e.getMessage());
                        status.setRollbackOnly();
                        throw e;
                    } catch (Exception e) {
                        log.error("TxTemplate exception : {}", e.getMessage(), e);
                        status.setRollbackOnly();
                        throw new SysErr();
                    }

                    return status;
                }
        );
    }

    /**
     * 编程事务
     * <p>
     * 有异常则外层捕获
     *
     * @return 方法执行自定义返回值
     */
    public <T> T doInTransaction(final TxReturnCallback<T> callback) {

        T result = defaultTx.execute((TransactionStatus status) -> {
                    try {
                        return callback.executeReturn();
                    } catch (BizErr e) {
                        log.info("BizErr caught in transaction : {}", e.getCode());
                        status.setRollbackOnly();
                        throw e;
                    } catch (DuplicateKeyException e) {
                        log.warn("TxTemplate duplicateKeyException : {}", e.getMessage());
                        status.setRollbackOnly();
                        throw e;
                    } catch (Exception e) {
                        log.error("TxTemplate exception : {}", e.getMessage(), e);
                        status.setRollbackOnly();
                        throw new SysErr();
                    }
                }
        );

        return result;
    }


}



