package com.example.rabbithell.domain.character.service;

import java.util.List;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.character.dto.response.AllCharacterResponse;
import com.example.rabbithell.domain.character.dto.response.CharacterInfoResponse;

public interface CharacterService {

    Long createCharacter(AuthUser authUser, CreateCharacterRequest request);

    CharacterInfoResponse characterInfo(Long characterId, AuthUser authUser);

    List<AllCharacterResponse> getAllCharacter(Long authUserId);

}
