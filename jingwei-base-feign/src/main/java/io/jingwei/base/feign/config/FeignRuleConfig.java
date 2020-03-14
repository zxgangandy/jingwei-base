package io.jingwei.base.feign.config;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author michael
 * @date 2019/8/15 6:27 PM
 */

@Configuration
@RibbonClients(defaultConfiguration = {FeignRule.class, RibbonPing.class})
public class FeignRuleConfig {
}
