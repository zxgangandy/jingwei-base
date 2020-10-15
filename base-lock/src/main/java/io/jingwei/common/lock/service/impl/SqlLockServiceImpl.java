package io.jingwei.common.lock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.jingwei.common.lock.entity.SqlLock;
import io.jingwei.common.lock.mapper.SqlLockMapper;
import io.jingwei.common.lock.service.ISqlLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分布式锁 服务实现类
 * </p>
 *
 * @author Andy
 * @since 2020-06-04
 */
@Service
public class SqlLockServiceImpl extends ServiceImpl<SqlLockMapper, SqlLock> implements ISqlLockService {
    @Autowired
    private SqlLockMapper sqlLockMapper;

    @Override
    public boolean insertLock(SqlLock sqlLock) {
        return SqlHelper.retBool(sqlLockMapper.insertLock(sqlLock));
    }
}
