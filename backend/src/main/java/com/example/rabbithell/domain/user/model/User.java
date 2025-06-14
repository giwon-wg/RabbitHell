package com.example.rabbithell.domain.user.model;

import com.example.rabbithell.common.audit.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private boolean isDeleted = false;

	@Builder
	public User(String name, String email, String password, Role role, boolean isDeleted) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.isDeleted = isDeleted;
	}

	public enum Role {
		USER, ADMIN
	}

	public void markAsDeleted() {
		this.isDeleted = true;
	}

	public void updateNickname(String nickname) {
		this.name = nickname;
	}
}
