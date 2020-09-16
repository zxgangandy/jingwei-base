package io.jingwei.common.lock.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.jingwei.common.lock.entity.SqlLock;

/**
 * <p>
 * 分布式锁 服务类
 * </p>
 *
 * @author Andy
 * @since 2020-06-04
 */
public interface ISqlLockService extends IService<SqlLock> {

    boolean insertLock(SqlLock sqlLock);

}
