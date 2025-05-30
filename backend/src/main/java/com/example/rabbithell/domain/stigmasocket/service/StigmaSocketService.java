package com.example.rabbithell.domain.stigmasocket.service;

import com.example.rabbithell.domain.stigmasocket.dto.request.CreateStigmaSocketRequest;
import com.example.rabbithell.domain.stigmasocket.dto.response.StigmaSocketResponse;

public interface StigmaSocketService {
	StigmaSocketResponse createStigmaSocket(CreateStigmaSocketRequest request);
}
