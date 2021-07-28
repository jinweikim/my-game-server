package com.mygame.dao;

import com.mygame.db.entity.Player;
import com.mygame.db.repository.PlayerRepository;
import com.mygame.redis.EnumRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerDao extends AbstractDao<Player, Long>{
    @Autowired
    private PlayerRepository plaerRepository;

    @Override
    protected EnumRedisKey getRedisKey() {
        return EnumRedisKey.PLAYER_INFO;
    }

    @Override
    protected MongoRepository<Player, Long> getMongoRepository() {
        return plaerRepository;
    }

    @Override
    protected Class<Player> getEntityClass() {
        return Player.class;
    }
}
