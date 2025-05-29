package com.example.rabbithell.domain.shop.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.village.entity.Village;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shop")
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    private Village village;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void update(Village village, String name) {
        this.village = village;
        this.name = name;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

}
