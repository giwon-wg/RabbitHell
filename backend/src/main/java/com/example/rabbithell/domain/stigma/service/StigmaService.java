package com.example.rabbithell.domain.stigma.service;

import java.util.List;

import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigamResponse;

public interface StigmaService {

    StigamResponse create(CreateStigmaRequest request);

    List<StigamResponse> findAll(int pageNumber, int size);

    StigamResponse findById(Long stigmaId);

    void update(Long stigmaId, UpdateStigmaRequest request);
}
