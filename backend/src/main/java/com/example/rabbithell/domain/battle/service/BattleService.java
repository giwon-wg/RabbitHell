package com.example.rabbithell.domain.battle.service;

import static com.example.rabbithell.domain.character.exception.code.CharacterExceptionCode.*;

import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.battle.dto.response.GetBattleFieldsResponse;
import com.example.rabbithell.domain.battle.type.BattleFieldType;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.exception.CharacterException;
import com.example.rabbithell.domain.character.repository.CharacterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BattleService {

    CharacterRepository characterRepository;

    public GetBattleFieldsResponse getBattleFields(AuthUser authUser, Long characterId) {

        Character character = verifyCharacter(authUser, characterId);

        Set<BattleFieldType> maps = character.getUnlockedRareMaps();

        maps.add(BattleFieldType.PLAIN);
        maps.add(BattleFieldType.MOUNTAIN);
        maps.add(BattleFieldType.FOREST);
        maps.add(BattleFieldType.DESERT);

        return new GetBattleFieldsResponse(maps);
    }

    private Character verifyCharacter(AuthUser authUser, Long characterId) {

        Character character = characterRepository.findByIdOrElseThrow(characterId);

        if (!Objects.equals(character.getUser().getId(), authUser.getUserId())) {
            throw new CharacterException(CHARACTER_NOT_FOUND);
        }

        return character;
    }
}

