package com.example.rabbithell.domain.clover.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.clover.dto.response.CloverNameResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverPublicResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;
import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.clover.repository.CloverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloverServiceImpl implements CloverService {

	private final CloverRepository cloverRepository;

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
}
