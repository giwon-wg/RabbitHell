package com.example.rabbithell.domain.stigma.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.common.dto.response.PageResponse;
import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigmaResponse;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigma.repository.StigmaRepository;

@RequiredArgsConstructor
@Service
public class StigmaServiceImpl implements StigmaService{

    private final StigmaRepository stigmaRepository;

    @Transactional
    @Override
    public StigmaResponse createStigma(CreateStigmaRequest request) {
        Stigma stigma = Stigma.builder()
            .name(request.name())
            .ratio(request.ratio())
            .description(request.description()).isDeleted(false)
            .build();

        Stigma savedStigma = stigmaRepository.save(stigma);

        return StigmaResponse.from(stigma);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<StigmaResponse> findAllStigmaByCond(int pageNumber, int size, StigmaCond cond) {

        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        List<Stigma> stigmaList = stigmaRepository.findAllByCondition(cond, pageable);
        long count = stigmaRepository.countByCondition(cond);
        List<StigmaResponse> responseList = stigmaList.stream()
            .map(StigmaResponse::from)
            .toList();
        PageImpl<StigmaResponse> responsePage = new PageImpl<>(responseList, pageable, count);

        return PageResponse.of(responseList, responsePage);
    }

    @Transactional(readOnly = true)
    @Override
    public StigmaResponse findStigmaById(Long stigmaId) {
        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);
        return StigmaResponse.from(stigma);
    }

    @Transactional
    @Override
    public void updateStigma(Long stigmaId, UpdateStigmaRequest request) {

        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);

        if (request.name() != null) stigma.changeName(request.name());
        if (request.ratio() != null) stigma.changeRatio(request.ratio());
        if (request.description() != null) stigma.changeDescription(request.description());
    }

    @Transactional
    @Override
    public void deleteStigma(Long stigmaId) {
        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);
        stigma.markAsDeleted();
    }
}
