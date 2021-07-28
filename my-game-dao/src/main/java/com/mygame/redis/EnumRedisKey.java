package com.mygame.redis;

import org.apache.commons.lang.StringUtils;

import java.time.Duration;

public enum EnumRedisKey {
        USER_ID_INCR(null), // 自增 id
        USER_ACCOUNT(Duration.ofDays(7)),
        PLAYER_ID_INCR(null),
        PLAYER_NICKNAME(null),
        PLAYER_INFO(Duration.ofDays(7)),
        ARENA(Duration.ofDays(7)),
        ;
        private Duration timeout;
        private EnumRedisKey(Duration timeout) {
            this.timeout = timeout;
        }

        public String getKey(String id) {
            if (StringUtils.isEmpty(id)) {
                throw new IllegalArgumentException("参数不能为空");
            }
            return this.name() + "_" + id;
        }

        public Duration getTimeout() {
            return timeout;
        }

        public String getKey() {
            return this.name();
        }

}
