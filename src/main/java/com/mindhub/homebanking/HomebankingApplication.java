package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {
    private final LocalDate date = LocalDate.now().plusMonths(1);
    private final LocalDate date5 = LocalDate.now().plusMonths(3);
    private final LocalDate date6 = LocalDate.now().plusMonths(2);
    private final LocalDate dateYears = LocalDate.now().plusMonths(2).plusYears(5);
    private final LocalDate dateYears2 = LocalDate.now().plusYears(3);
    private final LocalDate dateYears3 = LocalDate.now().plusMonths(1).plusYears(4);
    private final LocalDate date1 = LocalDate.now().plusMonths(2);
    private final LocalDateTime date3 = LocalDateTime.now().plusDays(4).plusHours(8).plusMinutes(40);
    private final LocalDateTime date2 = LocalDateTime.now().plusHours(3).plusMinutes(15);
    private final LocalDateTime date4 = LocalDateTime.now().plusDays(6).plusHours(2).plusMinutes(23);
    @Autowired
    private PasswordEncoder passwordEnconder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> {
            Loan loanMortgage = new Loan("Mortgage", 500000.0, Arrays.asList(12, 24, 36, 48, 60));
            Loan loanPersonal = new Loan("Personal", 100000.0, Arrays.asList(6, 12, 24));
            Loan loanAutomotive = new Loan("Automotive", 300000.0, Arrays.asList(6, 12, 24, 36));
            loanRepository.save(loanMortgage);
            loanRepository.save(loanPersonal);
            loanRepository.save(loanAutomotive);

            Client admin = new Client("admin", "admin", "admin@admin", passwordEnconder.encode("1234"));
            repositoryClient.save(admin);

            Client gabrielBarbera = new Client("Gabriel", "Barbera", "gabriel.barberaa@gmail.com", passwordEnconder.encode("1234"));
            Account account1 = new Account("VIN001", this.date, 5000);
            Account account2 = new Account("VIN002", this.date1, 7500);
            Transaction transaction1 = new Transaction(-1500, "Shopping", date2, TransactionType.DEBIT);
            Transaction transaction2 = new Transaction(+2500, "Food", date3, TransactionType.CREDIT);
            Transaction transaction5 = new Transaction(-5000, "Dinner", date4, TransactionType.DEBIT);
            ClientLoan gabrielBarberaLoanM = new ClientLoan(400000.0, 60);
            ClientLoan gabrielBarberaLoanP = new ClientLoan(50000.0, 12);
            Card cardGolGb = new Card("Gabriel Barbera", CardType.DEBIT, CardColor.GOLD, "1234-1234-1234-1234", 123, date6, dateYears);
            Card cardTitaniumGb = new Card("Gabriel Barbera", CardType.CREDIT, CardColor.TITANIUM, "4321-4321-4321-4321", 476, date1, dateYears3);
            Card cardSilverGb = new Card("Gabriel Barbera", CardType.CREDIT, CardColor.SILVER, "9876-9876-9876-9876", 983, date5, dateYears2);
            Card cardSilverGb2 = new Card("Gabriel Barbera", CardType.DEBIT, CardColor.SILVER, "4567-4567-7654-7654", 654, date, dateYears2);
            gabrielBarbera.addAccount(account1);
            gabrielBarbera.addAccount(account2);
            account1.addTransaction(transaction1);
            account1.addTransaction(transaction2);
            account1.addTransaction(transaction5);
            gabrielBarbera.addClientLoan(gabrielBarberaLoanM);
            gabrielBarbera.addClientLoan(gabrielBarberaLoanP);
            loanMortgage.addClientLoan(gabrielBarberaLoanM);
            loanPersonal.addClientLoan(gabrielBarberaLoanP);
            gabrielBarbera.addCard(cardGolGb);
            gabrielBarbera.addCard(cardTitaniumGb);
            gabrielBarbera.addCard(cardSilverGb);
            gabrielBarbera.addCard(cardSilverGb2);
            repositoryClient.save(gabrielBarbera);
            accountRepository.save(account1);
            accountRepository.save(account2);
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction5);
            clientLoanRepository.save(gabrielBarberaLoanM);
            clientLoanRepository.save(gabrielBarberaLoanP);
            cardRepository.save(cardGolGb);
            cardRepository.save(cardTitaniumGb);
            cardRepository.save(cardSilverGb);
            cardRepository.save(cardSilverGb2);

            Client brunoFerreira = new Client("Bruno", "Ferreira", "fbrunomarcos@gmail.com", passwordEnconder.encode("1234"));
            Account account3 = new Account("VIN003", this.date, 50000);
            Account account4 = new Account("VIN004", this.date1, 75000);
            Transaction transaction3 = new Transaction(2000, "Food", date2, TransactionType.CREDIT);
            Transaction transaction4 = new Transaction(-10000, "Car", date3, TransactionType.DEBIT);
            ClientLoan brunoFerreiraLoanP = new ClientLoan(10000.0, 24);
            ClientLoan brunoFerreiraLoanA = new ClientLoan(20000.0, 36);
            Card cardSilverBf = new Card("Bruno Ferreira", CardType.DEBIT, CardColor.SILVER, "4567-4567-4567-4567", 345, date, dateYears);
            brunoFerreira.addAccount(account3);
            brunoFerreira.addAccount(account4);
            account4.addTransaction(transaction3);
            account4.addTransaction(transaction4);
            brunoFerreira.addClientLoan(brunoFerreiraLoanP);
            brunoFerreira.addClientLoan(brunoFerreiraLoanA);
            loanPersonal.addClientLoan(brunoFerreiraLoanP);
            loanAutomotive.addClientLoan(brunoFerreiraLoanA);
            brunoFerreira.addCard(cardSilverBf);
            repositoryClient.save(brunoFerreira);
            accountRepository.save(account3);
            accountRepository.save(account4);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            clientLoanRepository.save(brunoFerreiraLoanP);
            clientLoanRepository.save(brunoFerreiraLoanA);
            cardRepository.save(cardSilverBf);
        };
    }
}


