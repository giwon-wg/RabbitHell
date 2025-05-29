package com.example.rabbithell.domain.stigma.entity;

import java.math.BigDecimal;

import com.example.rabbithell.common.audit.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Stigma extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 5, scale = 4, nullable = false)
    private BigDecimal ratio;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void initIsDeleted () {
        this.isDeleted = false;
    }
}
