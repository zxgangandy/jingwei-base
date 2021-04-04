package io.jingwei.base.idgen;


import io.jingwei.base.idgen.impl.CachedUidGenerator;
import io.jingwei.base.idgen.impl.DefaultUidGenerator;
import io.jingwei.base.idgen.impl.UidProperties;
import io.jingwei.base.idgen.worker.DisposableWorkerIdAssigner;
import io.jingwei.base.idgen.worker.WorkerIdAssigner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * UID 的自动配置
 *
 * @author Andy
 * @date 2019.11.18 10:57
 */
@Configuration
@ConditionalOnClass({ DefaultUidGenerator.class, CachedUidGenerator.class })
@EnableConfigurationProperties(UidProperties.class)
@MapperScan(basePackages = {"io.jingwei.base.idgen.worker.mapper"})
@ComponentScan(basePackages = {"io.jingwei.base.idgen.worker"})
public class UidAutoConfigure {

	@Autowired
	private UidProperties uidProperties;

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	DefaultUidGenerator defaultUidGenerator() {
		return new DefaultUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@Lazy
	CachedUidGenerator cachedUidGenerator() {
		return new CachedUidGenerator(uidProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	WorkerIdAssigner workerIdAssigner() {
		return new DisposableWorkerIdAssigner();
	}
}
