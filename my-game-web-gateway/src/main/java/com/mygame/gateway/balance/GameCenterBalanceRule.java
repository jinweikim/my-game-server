package com.mygame.gateway.balance;


import java.util.List;
import org.apache.commons.lang.math.RandomUtils;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

/**
 *
 * @ClassName: GameCenterBalanceRule
 * @Description: 根据某个key的hashCode对服务进行负载均衡。同一个用户id的请求，都转发到同一个服务实例上面。
 */
public class GameCenterBalanceRule extends AbstractLoadBalancerRule {

    @Override
    public Server choose(Object key) {

        List<Server> servers = this.getLoadBalancer().getReachableServers();
        if (servers.isEmpty()) {
            return null;
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        if (key == null) {
            return randomChoose(servers);
        }
        return hashKeyChoose(servers, key);
    }
    /**
     *
     * <p>Description:随机返回一个服务实例 </p>
     * @param servers
     * @return
     */
    private Server randomChoose(List<Server> servers) {
        int randomIndex = RandomUtils.nextInt(servers.size());
        return servers.get(randomIndex);
    }
    /**
     *
     * <p>Description:使用key的hash值，和服务实例数量求余，选择一个服务实例 </p>
     * @param servers
     * @param key
     */
    private Server hashKeyChoose(List<Server> servers, Object key) {
        int hashCode = Math.abs(key.hashCode());
        int index = hashCode % servers.size();
        return servers.get(index);

    }

    @Override
    public void initWithNiwsConfig(IClientConfig config) {

    }

}
