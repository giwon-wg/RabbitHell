package com.example.rabbithell.domain.clover.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.clover.dto.response.CloverNameResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverPublicResponse;
import com.example.rabbithell.domain.clover.dto.response.CloverResponse;

public interface CloverService {

	CloverResponse getMyClover(Long userId);

	CloverPublicResponse getCloverById(Long cloverId);

	List<CloverNameResponse> getAllCloverNames();

	Map<String, Object> getCloverInfoForMiniToken(Long userId);


}
