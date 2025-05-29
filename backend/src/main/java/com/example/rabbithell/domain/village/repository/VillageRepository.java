package com.example.rabbithell.domain.village.repository;

import static com.example.rabbithell.domain.village.exception.code.VillageExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.exception.VillageException;
import com.example.rabbithell.domain.village.exception.code.VillageExceptionCode;

public interface VillageRepository extends JpaRepository<Village, Long> {
    default Village findByIdOrElseThrow(Long villageId){
        return findById(villageId)
            .orElseThrow(() -> new VillageException(VILLAGE_NOT_EXIST));
    }
}
