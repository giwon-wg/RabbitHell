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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Kingdom {

	// 왕국 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	// 왕국 이름
    @Column(nullable = false)
    private String kingdomName;

	// 왕국 설명
    @Column(nullable = false)
    private String kingdomDetail;


	@OneToMany
    @JoinColumn(name = "kingdom_id")
    private List<Village> villages = new ArrayList<>();


	@Builder
	public Kingdom(String kingdomName, String kingdomDetail, List<Village> villages) {
	    this.kingdomName = kingdomName;
	    this.kingdomDetail = kingdomDetail;
	    this.villages = villages != null ? villages : new ArrayList<>();
	}
}
