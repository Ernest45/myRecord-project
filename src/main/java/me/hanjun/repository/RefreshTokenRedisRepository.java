package me.hanjun.repository;

import me.hanjun.domain.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedis,String> {
}
