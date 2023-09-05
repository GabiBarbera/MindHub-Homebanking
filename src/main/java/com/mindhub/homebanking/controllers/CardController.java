package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardService cardService;
    @Autowired
    ClientService clientService;

    public static String CardNumber() {
        return getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000);
    }

    public static int getCardCVV() {
        int cvv = getRandomNumber(100, 1000);
        return cvv;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @RequestMapping("/cards")
    public List<CardDTO> getClients() {
        return cardService.getClientsDTO();
    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getClients(Authentication authentication) {
        return cardService.findAllAuth(authentication.getName());
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        if (type == null || color == null) {
            return new ResponseEntity<>("No data", HttpStatus.FORBIDDEN);
        }
        String cardNumber;
        int cvvNumber = getCardCVV();
        do {
            cardNumber = CardNumber();
        } while (cardService.findByNumber(cardNumber) != null);
        for (Card card : client.getCards()) {
            if (card.getCardColor().equals(color) && card.getType().equals(type)) {
                return new ResponseEntity<>("Card already exists", HttpStatus.FORBIDDEN);
            }
        }
        Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), type, color, cardNumber, cvvNumber, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCard(newCard);
        cardService.addCard(newCard);
        return new ResponseEntity<>("Card created", HttpStatus.CREATED);
    }
}
