package me.hanjun.domain;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshTokenRedis", timeToLive = 1209600)
public class RefreshTokenRedis {

    @Id
    private String refreshTokenRedis;
    private Long userId;

    public RefreshTokenRedis(String refreshTokenRedis, Long userId) {
        this.refreshTokenRedis = refreshTokenRedis;
        this.userId = userId;
    }
}
