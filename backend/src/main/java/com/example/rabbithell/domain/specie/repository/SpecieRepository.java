package com.example.rabbithell.domain.specie.repository;

import static com.example.rabbithell.domain.specie.exception.code.SpecieExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.exception.SpecieException;

public interface SpecieRepository extends JpaRepository<Specie, Long> {
    default Specie findByIdOrElseThrow(Long specieId){
        return findById(specieId)
            .orElseThrow(() -> new SpecieException(SPECIE_NOT_FOUND));
    }

}
