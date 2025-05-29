package com.example.rabbithell.domain.battle.service;

import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.village.exception.VillageException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleService {

    BattleService battleService;
    CharacterRepository characterRepository;



    public GetBattleFieldsResponse getBattleFields(AuthUser authUser, Long characterId) {

        Character character = verifyCharacter(authUser, characterId);

        Set<BattleFieldType> unlockedRareMaps = character.getUnlockedRareMaps();

        return new GetBattleFieldsResponse(unlockedRareMaps);
    }


    private Character verifyCharacter(AuthUser authUser, Long characterId) {

        Character character = characterRepository.findByIdOrElseThrow(characterId);

        if (!Objects.equals(character.getUser().getId(), authUser.getUserId())) {
            throw new VillageException(CHARACTER_FORBIDDEN);
        }

        return character;
    }
}

