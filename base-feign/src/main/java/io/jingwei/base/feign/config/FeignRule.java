package io.jingwei.base.feign.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * ref:https://github.com/spring-cloud/spring-cloud-netflix/issues/2686
 * https://cloud.spring.io/spring-cloud-static/Edgware.SR1/single/spring-cloud.html#_customizing_default_for_all_ribbon_clients
 * @RibbonClients(defaultConfiguration = FeignRule.class)
 * /**
 * @ RibbonClient
 *  * Declarative configuration for a ribbon client. Add this annotation to any
 *  * <code>@Configuration</code> and then inject a {@link SpringClientFactory} to access the
 *  * client that is created.
 *  *
 *  * @author Dave Syer
 *  @ RibbonClients
 *   * Convenience annotation that allows user to combine multiple <code>@RibbonClient</code>
 *  * annotations on a single class (including in Java 7).
 *  */
@Slf4j
public class FeignRule extends RoundRobinRule {

    @Override
    public Server choose(Object key) {
        Server server = super.choose(key);
        if (Objects.isNull(server)) {
            log.info("server is null");
            return null;
        }
        log.info("feign rule ---> choose key:{}, final server ip:{}", key, server.getHostPort());
        return server;
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        Server chooseServer = super.choose(lb, key);

        List<Server> reachableServers = lb.getReachableServers();
        List<Server> allServers = lb.getAllServers();
        int upCount = reachableServers.size();
        int serverCount = allServers.size();
        log.info("upCount:{}, serverCount:{}", upCount, serverCount);
        for (Server server : allServers) {
            if (server!=null && server instanceof DiscoveryEnabledServer){
                DiscoveryEnabledServer dServer = (DiscoveryEnabledServer)server;
                InstanceInfo instanceInfo = dServer.getInstanceInfo();
                if (instanceInfo!=null){
                    InstanceInfo.InstanceStatus status = instanceInfo.getStatus();
                    if (status!=null){
                        log.info("server:{}, status:{}", server.getHostPort(), ((DiscoveryEnabledServer) server).getInstanceInfo().getStatus());
                    }
                }
            }
        }

        return chooseServer;
    }
}
