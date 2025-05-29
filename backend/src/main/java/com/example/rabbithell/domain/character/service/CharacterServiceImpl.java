package com.example.rabbithell.domain.character.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.dto.response.AllCharacterResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterInfoResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterPersonalInfoResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterPublicInfoResponse;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.repository.SpecieRepository;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService{

    private final CharacterRepository characterRepository;
    private final KingdomRepository kingdomRepository;
    private final SpecieRepository specieRepository;
    private final UserRepository userRepository;

    @Override
    public Long createCharacter(AuthUser authUser, CreateCharacterRequest request) {

        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());
        Kingdom kingdom = kingdomRepository.findByIdOrElseThrow(request.kingdomId());
        Specie specie = specieRepository.findByIdOrElseThrow(request.speciesId());


        Character character = Character.builder()
            .user(user)
            .kingdom(kingdom)
            .specie(specie)
            .name(request.name())
            .job("무능한 토끼")
            .level(0)
            .exp(0)
            .stamina(1000)
            .hp(100)
            .mp(50)
            .strength(10)
            .agility(10)
            .intelligence(10)
            .focus(10)
            .luck(10)
            .warriorPoint(0)
            .thiefPoint(0)
            .wizardPoint(0)
            .archerPoint(0)
            .cash(0L)
            .saving(0L)
            .skillPoint(0)
            .currentVillage(1L)
            .build();

        characterRepository.save(character);

        return character.getId();
    }

    @Override
    public CharacterInfoResponse characterInfo(Long characterId, AuthUser authUser) {

        Character character = characterRepository.findByIdOrElseThrow(characterId);

        boolean isOwner = character.getUser().getId().equals(authUser.getUserId());

        if (isOwner) {
            return CharacterPersonalInfoResponse.from(character);
        } else {
            return CharacterPublicInfoResponse.from(character);
        }
    }

    @Override
    public List<AllCharacterResponse> getAllCharacter(Long authUserId) {

        return characterRepository.findByUser_Id(authUserId)
            .stream()
            .map(character -> new AllCharacterResponse(
                character.getUser().getId(),
                character.getUser().getName(),
                character.getId(),
                character.getName(),
                character.getKingdom().getId(),
                character.getKingdom().getKingdomName(),
                character.getSpecie().getId(),
                character.getSpecie().getSpeciesName(),
                character.getJob(),
                character.getLevel(),
                character.getStamina(),
                character.getMaxHp(),
                character.getMaxMp(),
                character.getStrength(),
                character.getAgility(),
                character.getIntelligence(),
                character.getFocus(),
                character.getLuck(),
                character.getCurrentVillage()
            ))
            .toList();
    }
}
