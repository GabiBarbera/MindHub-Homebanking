package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardService {
    List<CardDTO> getClientsDTO();

    List<CardDTO> findAllAuth(String email);

    void addCard(Card card);

    Card findByNumber(String number);

    Card findById(long id);
}
