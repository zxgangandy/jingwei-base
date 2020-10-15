package io.jingwei.common.lock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * lock 的自动配置
 *
 * @author Andy
 * @date 2019.11.18 10:57
 */
@Configuration
@EnableConfigurationProperties(LockProperties.class)
public class DBLockAutoConfigure {

	@Autowired
	private LockProperties lockProperties;

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	public DBLock dbLock() {
		return new DBLock(lockProperties);
	}

}
