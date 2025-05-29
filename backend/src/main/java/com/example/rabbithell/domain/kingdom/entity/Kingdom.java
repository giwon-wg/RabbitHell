package com.example.rabbithell.domain.kingdom.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.rabbithell.domain.village.entity.Village;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Kingdom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kingdomName;

    @Column(nullable = false)
    private String kingdomDetail;

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "kingdom_id")
    private List<Village> villages = new ArrayList<>();

}
