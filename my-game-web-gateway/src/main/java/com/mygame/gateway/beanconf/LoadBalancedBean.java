package com.mygame.gateway.beanconf;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.mygame.gateway.balance.GameCenterBalanceRule;
import com.mygame.gateway.balance.UserLoadBalancerClientFilter;
import com.netflix.loadbalancer.IRule;

@Configuration
public class LoadBalancedBean {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserLoadBalancerClientFilter userLoadBalanceClientFilter(LoadBalancerClient client, LoadBalancerProperties properties) {
        return new UserLoadBalancerClientFilter(client, properties);
    }

    @Bean
    public IRule balanceRule() {
        return new GameCenterBalanceRule();
    }
}
