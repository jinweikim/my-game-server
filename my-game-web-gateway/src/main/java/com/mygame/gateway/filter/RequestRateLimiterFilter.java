package com.mygame.gateway.filter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.mygame.common.utils.CommonField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class RequestRateLimiterFilter implements GlobalFilter, Ordered {
    @Autowired
    private FilterConfig filterConfig;
    private RateLimiter globalRateLimiter;
    private LoadingCache<String, RateLimiter> userRateLimiterCache;
    private Logger logger = LoggerFactory.getLogger(RequestRateLimiterFilter.class);

    @PostConstruct
    public void init() { // 初始化
        double permitsSecond = filterConfig.getGlobalRequestRateCount();
        globalRateLimiter = RateLimiter.create(permitsSecond);
        // 创建用户 cache
        long maximumSize = filterConfig.getCacheUserMaxCount();
        long duration = filterConfig.getCacheUserTimeout();
        userRateLimiterCache = CacheBuilder.newBuilder().maximumSize(maximumSize).expireAfterAccess(duration, TimeUnit.MILLISECONDS).build(new CacheLoader<String, RateLimiter>() {
            @Override
            public RateLimiter load(String key) throws Exception {
                // 不存在限流器就创建一个。
                double permitsPerSecond = filterConfig.getUserRequestRateCount();
                RateLimiter newRateLimiter = RateLimiter.create(permitsPerSecond);
                return newRateLimiter;
            }
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String openId = exchange.getRequest().getHeaders().getFirst(CommonField.OPEN_ID);
        System.out.println("in request rate filter" + openId);
        if (!StringUtils.isEmpty(openId)) {
            try{
                RateLimiter userRateLimiter = userRateLimiterCache.get(openId);
                if (!userRateLimiter.tryAcquire()) {
                    this.tooManyRequest(exchange, chain);
                }
            } catch (ExecutionException e) {
                logger.error("限流异常", e);
                return this.tooManyRequest(exchange, chain);
            }
        }
        if (!globalRateLimiter.tryAcquire()) {
            return this.tooManyRequest(exchange, chain);
        }
        return chain.filter(exchange);
    }

    public Mono<Void> tooManyRequest(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.debug("请求太多，触发限流");
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        return exchange.getResponse().setComplete();
    }
}
