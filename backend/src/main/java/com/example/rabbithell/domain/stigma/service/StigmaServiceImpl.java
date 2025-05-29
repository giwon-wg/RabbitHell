package com.example.rabbithell.domain.stigma.service;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.request.UpdateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.StigamResponse;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigma.repository.StigmaRepository;

@RequiredArgsConstructor
@Service
public class StigmaServiceImpl implements StigmaService{

    private final StigmaRepository stigmaRepository;

    @Transactional
    @Override
    public StigamResponse create(CreateStigmaRequest request) {
        Stigma stigma = Stigma.builder()
            .name(request.name())
            .ratio(request.ratio())
            .description(request.description())
            .build();

        stigma.initIsDeleted();

        Stigma savedStigma = stigmaRepository.save(stigma);

        return StigamResponse.from(stigma);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StigamResponse> findAll(int pageNumber, int size) {

        //PageNUmber가 음수 일때에 대한 방어 코드
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        List<Stigma> stigmaList = stigmaRepository.findAll(pageable).getContent();
        return stigmaList.stream().map(d -> StigamResponse.from(d)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public StigamResponse findById(Long stigmaId) {
        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);
        return StigamResponse.from(stigma);
    }

    @Transactional
    @Override
    public void update(Long stigmaId, UpdateStigmaRequest request) {

        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);

        if (request.name() != null) stigma.changeName(request.name());
        if (request.ratio() != null) stigma.changeRatio(request.ratio());
        if (request.description() != null) stigma.changeDescription(request.description());
    }

    @Transactional
    @Override
    public void delete(Long stigmaId) {
        Stigma stigma = stigmaRepository.findByIdOrElseThrow(stigmaId);
        stigma.delete();
    }
}
