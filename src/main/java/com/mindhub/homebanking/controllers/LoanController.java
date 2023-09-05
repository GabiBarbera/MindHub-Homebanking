package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping(path = "/loans")
    public List<LoanDTO> getLoans() {
        List<LoanDTO> loans = loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
        return loans;
    }


//@Transactional
//@RequestMapping(path = "/loans", method = RequestMethod.POST)
//public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
//    Client client = clientRepository.findByEmail(authentication.getName());
//    Loan loan = loanRepository.findById(loanApplicationDTO.getId()).orElse(null);
//
//    if (client == null) {
//        return new ResponseEntity<>("Client no found", HttpStatus.FORBIDDEN);
//    }
//
//}

}
