package com.mygame.gateway.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gateway.filter")
@Data
public class FilterConfig {
    /**
     * 请求权限验证白名单，在这个白名单中的所有配置不需要进行权限验证
     */
    private List<String> whiteRequestUri;
    /**
     * 全局限流器每秒钟产生的令牌数
     */
    private double globalRequestRateCount;
    /**
     * 单个用户限流器每秒产生的令牌数
     */
    private double userRequestRateCount;
    /**
     * 最大用户缓存数量
     */
    private int cacheUserMaxCount;
    /**
     * 每个用户缓存的超时时间，超过个时间，从缓存中清除。单位毫秒
     */
    private int cacheUserTimeout;


//    public int getCacheUserMaxCount() {
//        return cacheUserMaxCount;
//    }
//
//    public void setCacheUserMaxCount(int cacheUserMaxCount) {
//        this.cacheUserMaxCount = cacheUserMaxCount;
//    }
//
//    public int getCacheUserTimeout() {
//        return cacheUserTimeout;
//    }
//
//    public void setCacheUserTimeout(int cacheUserTimeout) {
//        this.cacheUserTimeout = cacheUserTimeout;
//    }
//
//    public double getUserRequestRateCount() {
//        return userRequestRateCount;
//    }
//
//    public void setUserRequestRateCount(double userRequestRateCount) {
//        this.userRequestRateCount = userRequestRateCount;
//    }
//
//    public double getGlobalRequestRateCount() {
//        return globalRequestRateCount;
//    }
//
//    public void setGlobalRequestRateCount(double globalRequestRateCount) {
//        this.globalRequestRateCount = globalRequestRateCount;
//    }
//
//    public List<String> getWhiteRequestUri() {
//        return whiteRequestUri;
//    }
//
//    public void setWhiteRequestUri(List<String> whiteRequestUri) {
//        this.whiteRequestUri = whiteRequestUri;
//    }
}
