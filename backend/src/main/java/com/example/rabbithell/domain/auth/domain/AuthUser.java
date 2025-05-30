package com.example.rabbithell.domain.auth.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class AuthUser implements UserDetails {

	private final Long userId;
	private final String role;
	private final Long expeditionId;
	private final String expeditionName;

	public AuthUser(Long userId, String role, Long expeditionId, String expeditionName) {
		this.userId = userId;
		this.role = role;
		this.expeditionId = expeditionId;
		this.expeditionName = expeditionName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role));
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return String.valueOf(userId);
	}

	public Long getExpeditionId() {
		return expeditionId;
	}

	public String getExpeditionName() {
		return expeditionName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
