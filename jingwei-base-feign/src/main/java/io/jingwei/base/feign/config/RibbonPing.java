package io.jingwei.base.feign.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.niws.loadbalancer.NIWSDiscoveryPing;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RibbonPing extends NIWSDiscoveryPing {
    /**
     * 主要为了根据ping的结果以及ping时instance的状态
     * @param server  -- 需要的服务信息
     * @return  -- 返回true将进入upServer列表
     */
    public boolean isAlive(Server server) {
        if (server instanceof DiscoveryEnabledServer) {
            return isAlive((DiscoveryEnabledServer) server);
        } else {
            log.info("invalid server");
            return false;
        }
    }

    private boolean isAlive(DiscoveryEnabledServer server) {
        boolean isAlive = super.isAlive(server);
        log.info("{} execute ping result is {}, server origin status is {}", server, isAlive, getStatus(server.getInstanceInfo()));
        return isAlive;
    }

    /**
     * 获取服务实例的状态
     * @param instanceInfo  -- 服务实例信息
     * @return  --
     */
    private String getStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null || instanceInfo.getStatus() == null) {
            return null;
        }
        return instanceInfo.getStatus().name();
    }
}
