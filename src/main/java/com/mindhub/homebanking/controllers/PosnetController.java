package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PosnetDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class PosnetController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/payment/card")
    public ResponseEntity<Object> paymentCard(@RequestBody PosnetDTO posnetDTO) {
        LocalDate localDate = LocalDate.now();

        if (posnetDTO.getNumber().isBlank()) {
            return new ResponseEntity<>("Se necesita una cuenta", HttpStatus.FORBIDDEN);
        }
        Card card = cardService.findByNumber(posnetDTO.getNumber());
        if (card == null) {
            return new ResponseEntity<>("la tarjeta no existe", HttpStatus.FORBIDDEN);
        }
        if (!card.isActive()) {
            return new ResponseEntity<>("la tarjeta esta desactivada", HttpStatus.FORBIDDEN);

        }
        if (card.getThruDate().isBefore(localDate)) {
            return new ResponseEntity<>("Su tarjeta esta vencida", HttpStatus.FORBIDDEN);
        }
        if (posnetDTO.getAmount() <= 0) {
            return new ResponseEntity<>("El monto no puede ser 0 o negati o", HttpStatus.FORBIDDEN);
        }
        if (posnetDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Se necesita una descripcion", HttpStatus.FORBIDDEN);
        }
        if (posnetDTO.getCvv() != card.getCvv()) {
            return new ResponseEntity<>("El cvv no es correcto", HttpStatus.FORBIDDEN);
        }
        long idClient = card.getOwner().getId();
        Client client = clientService.findByIdNoDTO(idClient);
        Set<Account> accountList = client.getAccounts();
        Account maxBalanceAccount = accountList.stream().reduce((ac2, ac3) -> ac2.getBalance() > ac3.getBalance() ? ac2 : ac3).orElse(null);
        if (maxBalanceAccount.getBalance() < posnetDTO.getAmount()) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_GATEWAY);
        } else {
            maxBalanceAccount.setBalance(maxBalanceAccount.getBalance() - posnetDTO.getAmount());
            Transaction transaction = new Transaction(posnetDTO.getAmount() * -1, posnetDTO.getDescription(), LocalDateTime.now(), TransactionType.DEBIT
                    , maxBalanceAccount.getBalance(), true);
            maxBalanceAccount.addTransaction(transaction);
            transactionService.addTransaction(transaction);
            accountService.addAccount(maxBalanceAccount);
        }
        return new ResponseEntity<>("Your payment was successful", HttpStatus.OK);
    }
}