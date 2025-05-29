package com.example.rabbithell.domain.village.service;

import static com.example.rabbithell.domain.village.exception.code.VillageExceptionCode.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.village.entity.Village;
import com.example.rabbithell.domain.village.exception.VillageException;
import com.example.rabbithell.domain.village.repository.VillageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VillageService {

    private final VillageRepository villageRepository;
    private final CharacterRepository characterRepository;


    @Transactional
    public void moveVillage(AuthUser authUser, Long characterId, Long villageId) {
        Character character = verifyCharacter(authUser, characterId);

        Village currentVillage = villageRepository.findByIdOrElseThrow(Long.valueOf(character.getCurrentVillage()));
        Village targetVillage = villageRepository.findByIdOrElseThrow(villageId);

        boolean isConnected = currentVillage.getConnections().stream()
            .anyMatch(conn -> conn.getToVillage().getId().equals(targetVillage.getId()));

        if (!isConnected) {
            throw new VillageException(VILLAGE_NOT_CONNECTED);
        }

        character.updateCurrentVillage(targetVillage);
    }




    private Character verifyCharacter(AuthUser authUser, Long characterId) {

        Character character = characterRepository.findByIdOrElseThrow(characterId);

        if(!Objects.equals(character.getUser().getId(), authUser.getUserId())){
            throw new VillageException(CHARACTER_FORBIDDEN);
        }

        return character;
    }

    @Transactional
    public void saveMoney(AuthUser authUser, Long characterId, Long saveMoney) {

        Character character = verifyCharacter(authUser, characterId);

        if(saveMoney < 1000){
            throw new VillageException(BELOW_MINIMUM);
        }

        if(character.getCash() < saveMoney){
            throw new VillageException(NOT_ENOUGH_MONEY);
        }

        character.saveToBank(saveMoney);
    }


    @Transactional
    public void withdrawMoney(AuthUser authUser, Long characterId, Long withdrawMoney) {

        Character character = verifyCharacter(authUser, characterId);

        if(withdrawMoney < 1000){
            throw new VillageException(BELOW_MINIMUM);
        }

        if(character.getSaving() < withdrawMoney){
            throw new VillageException(NOT_ENOUGH_MONEY);
        }

        character.withdrawFromBank(withdrawMoney);

    }

    @Transactional
    public void cureCharacter(AuthUser authUser, Long characterId) {

        Long cureCost = 0L;

        Character character = verifyCharacter(authUser, characterId);

        if(cureCost > character.getSaving()){
            if(cureCost > character.getCash()){
                throw new VillageException(NOT_ENOUGH_MONEY);
            }
            character.useMoneyFromSaving(cureCost);
        }else{
            character.useMoneyFromCash(cureCost);
        }

        character.refill();

    }
}
