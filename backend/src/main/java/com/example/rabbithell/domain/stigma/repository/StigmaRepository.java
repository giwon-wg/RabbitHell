package com.example.rabbithell.domain.stigma.repository;

import static com.example.rabbithell.domain.stigma.exception.code.StigmaExceptionCode.STIGMA_NOT_FOUND;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.stigma.entity.Stigma;
import com.example.rabbithell.domain.stigma.exception.StigmaException;

public interface StigmaRepository extends JpaRepository<Stigma, Long>, StigmaQueryRepository {

    default Stigma findByIdOrElseThrow (Long id) {
return findById(id).orElseThrow(() -> new StigmaException(STIGMA_NOT_FOUND));
    }
}
