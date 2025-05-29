package com.example.rabbithell.domain.character.service;

import com.example.rabbithell.domain.character.dto.request.CreateCharacterRequest;
import com.example.rabbithell.domain.user.model.User;

public interface CharacterService {

    void createCharacter(User user, CreateCharacterRequest request);

}
