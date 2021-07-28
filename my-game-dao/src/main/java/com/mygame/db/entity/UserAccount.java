package com.mygame.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "UserAccount")
public class UserAccount {
    @Id
    private String openId;
    private long userId;
    private long createTime;
    private String ip;
    // 记录不同分区的角色信息
    private Map<String, ZonePlayerInfo> zonePlayerInfo = new HashMap<>();


    @Data
    public static class ZonePlayerInfo {
        private long playerId;
        private long lastEnterTime;

        public ZonePlayerInfo() {
        }

        public ZonePlayerInfo (long playerId, long lastEnterTime) {
            super();
            this.playerId = playerId;
            this.lastEnterTime = lastEnterTime;
        }

        @Override
        public String toString() {
            return "ZoneInfo [playerId=" + playerId + ", lastEnterTime=" + lastEnterTime + "]";
        }
    }
}
