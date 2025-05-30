package com.example.rabbithell.domain.stigma.service;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigmaResponse;

public interface StigmaService {

    StigmaResponse createStigma(CreateStigmaRequest request);

    PageResponse<StigmaResponse> findAllStigmaByCond(int pageNumber, int size, StigmaCond cond);

    StigmaResponse findStigmaById(Long stigmaId);

    void updateStigma(Long stigmaId, UpdateStigmaRequest request);

    void deleteStigma(Long stigmaId);
}
