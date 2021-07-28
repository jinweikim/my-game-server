package com.mygame.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Document(collection = "Player")
public class Player {
    @Id
    private long playerId;
    private String nickName;
    private int level;
    private long lastLoginTime;
    private long createTime;
    private ConcurrentHashMap<String, String> heros = new ConcurrentHashMap<>();

    @Override
    public String toString() {
        return "Player [playerId=" + playerId + ", nickname=" + nickName + ", level=" + level + ", lastLoginTime=" + lastLoginTime + "]";
    }
}
