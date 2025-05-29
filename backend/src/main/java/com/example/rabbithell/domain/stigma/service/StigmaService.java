package com.example.rabbithell.domain.stigma.service;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigmaResponse;

public interface StigmaService {

    StigmaResponse create(CreateStigmaRequest request);

    PageResponse<StigmaResponse> findAllByCond(int pageNumber, int size, StigmaCond cond);

    StigmaResponse findById(Long stigmaId);

    void update(Long stigmaId, UpdateStigmaRequest request);

    void delete(Long stigmaId);
}
