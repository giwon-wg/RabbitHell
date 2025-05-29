package com.example.rabbithell.domain.character.service;

import org.springframework.stereotype.Service;

import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.entity.Character;
import com.example.rabbithell.domain.character.repository.CharacterRepository;
import com.example.rabbithell.domain.kingdom.entity.Kingdom;
import com.example.rabbithell.domain.kingdom.repository.KingdomRepository;
import com.example.rabbithell.domain.specie.entity.Specie;
import com.example.rabbithell.domain.specie.repository.SpecieRepository;
import com.example.rabbithell.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService{

    private final CharacterRepository characterRepository;
    private final KingdomRepository kingdomRepository;
    private final SpecieRepository specieRepository;

    @Override
    public void createCharacter(User user, CreateCharacterRequest request) {

        Kingdom kingdom = kingdomRepository.findByIdOrElseThrow(request.kingdomId());
        Specie specie = specieRepository.findByIdOrElseThrow(request.speciesId());

        Character character = Character.builder()
            .user(user)
            .kingdom(kingdom)
            .specie(specie)
            .name(request.name())
            .job("전사?")
            .level(0)
            .exp(0)
            .stamina(2000)
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
            .skillPonint(0)
            .currentVillage(1)
            .build();

        characterRepository.save(character);

    }



}
