package io.jingwei.base.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaManagementAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public EurekaManagementController eurekaManageController(ApplicationInfoManager applicationInfoManager) {
        EurekaManagementController controller = new EurekaManagementController();
        controller.setApplicationInfoManager(applicationInfoManager);
        return controller;
    }
}
