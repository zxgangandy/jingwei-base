package io.jingwei.common.lock;

import io.jingwei.common.lock.entity.SqlLock;
import io.jingwei.common.lock.service.ISqlLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DBLock implements Lock {

    /**
     * 阻塞式获取锁时，不停地重复尝试获取锁的默认时间间隔
     */
    private static long DEFAULT_TRY_LOCK_INTERVAL = 1000;

    /**
     * lock_key字段在数据库表中定义的长度
     */
    public static int DEFAULT_LOCK_KEY_MAX_LENGTH = 64;

    /***
     * 锁的配置类
     */
    private LockProperties lockProperties;

    @Autowired
    private ISqlLockService sqlLockService;

    private Map<String, String> ownerMap;



    public DBLock(LockProperties lockProperties) {
        this.lockProperties   = lockProperties;
        this.ownerMap         = new ConcurrentHashMap<>();
    }

    /**
     *  清理一下过期的数据数据
     */
    @PostConstruct
    private void init() {
        sqlLockService.lambdaUpdate()
                .lt(SqlLock::getExpire, "TIMESTAMPDIFF(SECOND, create_time, NOW())")
                .remove();
    }

    @Override
    public void lock(String lockKey) {
        while (true) {
            try {
                lockInterruptibly(lockKey);
                return;
            } catch (InterruptedException e) {
                log.warn("lock interrupted, {}", e.getMessage());
                continue;
            }
        }
    }

    @Override
    public void lockInterruptibly(String lockKey) throws InterruptedException {
        while (!tryLock(lockKey)) {
            Thread.sleep(DEFAULT_TRY_LOCK_INTERVAL);
        }
    }

    @Override
    public boolean tryLock(String lockKey) {
        if (StringUtils.isEmpty(lockKey) || lockKey.length() > DEFAULT_LOCK_KEY_MAX_LENGTH) {
            throw new InvalidParameterException("lockKey is empty or lockKey.length() > " + DEFAULT_LOCK_KEY_MAX_LENGTH);
        }

        String oldOwner = ownerMap.get(lockKey);
        if (oldOwner != null) {
            // already hold a lock
            return true;
        }

        String owner = UUID.randomUUID().toString();

        SqlLock sqlLock = new SqlLock();
        sqlLock.setLockKey(lockKey)
                .setOwner(owner)
                .setExpire(lockProperties.getExpire());

        boolean result =  sqlLockService.insertLock(sqlLock);
        if (result) {
            ownerMap.put(lockKey, owner);
        }

        return result;
    }

    @Override
    public boolean tryLock(String lockKey, long time, TimeUnit unit) throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        long waitTime = unit.toMillis(time);

        while (!tryLock(lockKey)) {
            if (System.currentTimeMillis() - startTimestamp > waitTime) {
                return false;
            }
            Thread.sleep(DEFAULT_TRY_LOCK_INTERVAL);
        }

        return true;
    }

    @Override
    public void unlock(String lockKey) {
        String owner = ownerMap.get(lockKey);
        if (owner == null) {
            throw new IllegalMonitorStateException("Can't call unlock() before tryLock()/lock()/lockInterruptibly()");
        }

        ownerMap.remove(lockKey);

        sqlLockService.lambdaUpdate()
                .eq(SqlLock::getLockKey, lockKey)
                .eq(SqlLock::getOwner, owner)
                .remove();
    }


}
