package com.example.rabbithell.domain.monster.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Monster {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String rating;

    @Column(nullable = false)
    private Long hp;

    @Column(nullable = false)
    private Long attack;

    @Column(nullable = false)
    private Long defence;

    @Column(nullable = false)
    private Long speed;
}
