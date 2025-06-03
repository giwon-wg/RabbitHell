package com.example.rabbithell.domain.village.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Village {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String villageName;

	@OneToMany(mappedBy = "fromVillage")
	private List<VillageConnection> connections = new ArrayList<>();

	public Village(String villageName) {
		this.villageName = villageName;
	}

	public void addConnection(VillageConnection villageConnection) {
		this.connections.add(villageConnection);
	}
}
