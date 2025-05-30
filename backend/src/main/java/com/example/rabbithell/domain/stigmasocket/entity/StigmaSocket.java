package com.example.rabbithell.domain.stigmasocket.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigmasocket.enums.StigmaSocketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class StigmaSocket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stigma_id", nullable = false)
    private Stigma stigma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(nullable = false)
    private StigmaSocketStatus stigmaSocketStatus;
}
