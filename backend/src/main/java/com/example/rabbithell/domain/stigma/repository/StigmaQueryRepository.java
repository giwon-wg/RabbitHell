package com.example.rabbithell.domain.stigma.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.rabbithell.domain.stigma.dto.request.StigmaCond;
import com.example.rabbithell.domain.stigma.entity.Stigma;

public interface StigmaQueryRepository {
    List<Stigma> findAllByCondition(StigmaCond cond, Pageable pageable);

    long countByCondition(StigmaCond cond);
}
