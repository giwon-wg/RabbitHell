package com.example.rabbithell.domain.kingdom.repository;

import static com.example.rabbithell.domain.kingdom.exception.code.KingdomExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.exception.KingdomException;

public interface KingdomRepository extends JpaRepository<Kingdom, Long> {
    default Kingdom findByIdOrElseThrow(Long kingdomId){
        return findById(kingdomId)
            .orElseThrow(()-> new KingdomException(KINGDOM_NOT_FOUND));
    }
}
