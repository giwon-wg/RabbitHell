package com.example.rabbithell.domain.clover.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.dto.response.CloverNameResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverPublicResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloverServiceImpl implements CloverService {

	private final CloverRepository cloverRepository;
	private final UserRepository userRepository;
	private final UserService userService;

	@Transactional(readOnly = true)
	@Override
	public CloverResponse getMyClover(Long userId) {
		Clover clover = cloverRepository.findByUserIdOrElseThrow(userId);
		return CloverResponse.from(clover);
	}

	@Transactional(readOnly = true)
	@Override
	public CloverPublicResponse getCloverById(Long cloverId) {
		Clover clover = cloverRepository.findByIdOrElseThrow(cloverId);
		return CloverPublicResponse.from(clover);

	}

	@Transactional(readOnly = true)
	@Override
	public List<CloverNameResponse> getAllCloverNames() {
		return cloverRepository.findAll().stream()
			.map(c -> new CloverNameResponse(c.getId(), c.getName()))
			.toList();
	}

	@Override
	public Map<String, Object> getCloverInfoForMiniToken(Long userId) {

		User user = userRepository.findByIdOrElseThrow(userId);

		Map<String, Object> result = new HashMap<>();

		result.put("hasClover", cloverRepository.existsByUserId(userId));
		result.put("nickname", user.getName());

		cloverRepository.findByUserId(userId)
			.ifPresent(clover -> result.put("cloverName", clover.getName()));

		return result;
	}
}
