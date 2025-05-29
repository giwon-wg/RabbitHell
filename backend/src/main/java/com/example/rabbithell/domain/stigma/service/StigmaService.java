package com.example.rabbithell.domain.stigma.service;

import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.CreateStigamResponse;

public interface StigmaService {
    CreateStigamResponse create(CreateStigmaRequest request);
}
