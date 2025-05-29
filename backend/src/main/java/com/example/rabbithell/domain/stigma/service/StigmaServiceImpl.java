package com.example.rabbithell.domain.stigma.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.stigma.dto.request.CreateStigmaRequest;
import com.example.rabbithell.domain.stigma.dto.response.CreateStigamResponse;
import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigma.repository.StigmaRepository;

@RequiredArgsConstructor
@Service
public class StigmaServiceImpl implements StigmaService{

    private final StigmaRepository stigmaRepository;

    @Override
    public CreateStigamResponse create(CreateStigmaRequest request) {
        Stigma stigma = Stigma.builder()
            .name(request.name())
            .ratio(request.ratio())
            .description(request.description())
            .build();

        Stigma savedStigma = stigmaRepository.save(stigma);

        return CreateStigamResponse.from(stigma);
    }
}
