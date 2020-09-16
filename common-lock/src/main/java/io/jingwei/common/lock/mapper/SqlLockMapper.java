package io.jingwei.common.lock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.jingwei.common.lock.entity.SqlLock;

/**
 * <p>
 * 分布式锁 Mapper 接口
 * </p>
 *
 * @author Andy
 * @since 2020-06-04
 */
public interface SqlLockMapper extends BaseMapper<SqlLock> {
    int insertLock(SqlLock sqlLock);
}
