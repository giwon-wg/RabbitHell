package com.example.rabbithell.domain.character.service;

import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.dto.response.CharacterInfoResponse;
import com.example.rabbithell.domain.user.model.User;

public interface CharacterService {

    Long createCharacter(User user, CreateCharacterRequest request);

    CharacterInfoResponse characterInfo(Long characterId, User user);

}
