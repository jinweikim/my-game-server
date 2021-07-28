package com.mygame.gateway.filter;


import com.mygame.common.error.TokenException;
import com.mygame.common.utils.CommonField;
import com.mygame.common.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @ClassName: TokenVerifyFilter
 * @Description: token验证的filter，用户登陆成功之后，以后再访问服务需要对token进行验证。
 */
@Service
public class TokenVerifyFilter implements GlobalFilter, Ordered {

    @Autowired
    private FilterConfig filterConfig;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    private Logger logger = LoggerFactory.getLogger(TokenVerifyFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUri = exchange.getRequest().getURI().getPath();
        List<String> whiteRequestUris = filterConfig.getWhiteRequestUri();
        if (whiteRequestUris.contains(requestUri)) {
//            logger.info("请求位于白名单，跳过验证");
            logger.info("{} 请求位于白名单，跳过验证", requestUri);
//            System.out.println("请求位于白名单，跳过验证");
            return chain.filter(exchange);// 如果请求的uri在白名单中，则跳过验证。
        }

        String token = exchange.getRequest().getHeaders().getFirst(CommonField.TOKEN);
        if (StringUtils.isEmpty(token)) {
            logger.debug("{} 请求验证失败,token为空", requestUri);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            JWTUtil.TokenBody tokenBody = JWTUtil.getTokenBody(token);
            // 把token中的openId和userId添加到Header中，转发到后面的服务。
            ServerHttpRequest request = exchange.getRequest().mutate().header(CommonField.OPEN_ID, tokenBody.getOpenId()).header(CommonField.USER_ID, String.valueOf(tokenBody.getUserId())).build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();
            return chain.filter(newExchange);
        } catch (TokenException e) {
            logger.debug("{} 请求验证失败,token非法");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }

}
