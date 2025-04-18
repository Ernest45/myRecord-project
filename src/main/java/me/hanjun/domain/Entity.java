package me.hanjun.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@jakarta.persistence.Entity
public class Entity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
