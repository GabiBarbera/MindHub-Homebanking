package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public List<CardDTO> getClientsDTO() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @Override
    public List<CardDTO> findAllAuth(String email) {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @Override
    public void addCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }
}
