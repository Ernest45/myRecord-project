package me.hanjun.repository;

import me.hanjun.domain.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRepository extends JpaRepository<Entity,Long> {
}
