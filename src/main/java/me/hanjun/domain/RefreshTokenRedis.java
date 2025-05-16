package me.hanjun.domain;


import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshTokenRedis", timeToLive = 1209600)
public class RefreshTokenRedis {

    @Id
    private String refreshTokenRedis;