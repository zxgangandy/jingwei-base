package io.jingwei.common.lock;

import java.util.concurrent.TimeUnit;

public interface Lock {

    void lock(String lockKey);


    void lockInterruptibly(String lockKey) throws InterruptedException;


    boolean tryLock(String lockKey);

    boolean tryLock(String lockKey, long time, TimeUnit unit) throws InterruptedException;


    void unlock(String lockKey);
}
