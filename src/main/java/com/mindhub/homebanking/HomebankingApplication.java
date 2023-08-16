package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {
	LocalDate date = LocalDate.now();
	LocalDate dateYears = LocalDate.now().plusYears(5);
	LocalDate date1 = LocalDate.now().plusDays(1);
	LocalDateTime date3 = LocalDateTime.now().plusDays(4).plusHours(8).plusMinutes(40);
	LocalDateTime date2 = LocalDateTime.now().plusHours(3).plusMinutes(15);
    LocalDateTime date4 = LocalDateTime.now().plusDays(6).plusHours(2).plusMinutes(23);
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Loan loanMortgage = new Loan("Mortgage", 500000.0, Arrays.asList(12, 24, 36, 48, 60));
			Loan loanPersonal = new Loan("Personal", 100000.0, Arrays.asList(6,12,24));
			Loan loanAutomotive = new Loan("Automotive", 300000.0, Arrays.asList(6,12,24,36));
			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotive);

			Client gabrielBarbera = new Client("Gabriel", "Barbera","gabriel.barberaa@gmail.com");
			Account account1 = new Account("VIN001",this.date, 5000);
			Account account2 = new Account("VIN002",this.date1,7500);
			Transaction transaction1 = new Transaction(-1500,"Shopping",date2, TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(+2500,"Food",date3,TransactionType.CREDIT);
			Transaction transaction5 = new Transaction(-5000,"Dinner",date4,TransactionType.DEBIT);
			ClientLoan gabrielBarberaLoanM = new ClientLoan(400000.0,60);
			ClientLoan gabrielBarberaLoanP = new ClientLoan(50000.0,12);
			Card cardGolGb = new Card("Gabriel Barbera",CardType.DEBIT,CardColor.GOLD,"1234-1234-1234-1234",123,date,dateYears);
			Card cardTitaniumGb = new Card("Gabriel Barbera",CardType.CREDIT,CardColor.TITANIUM,"4321-4321-4321-4321",321,date,dateYears);
			Card cardSilverGb = new Card("Gabriel Barbera",CardType.CREDIT,CardColor.SILVER,"9876-9876-9876-9876",987,date,dateYears);
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

			Client brunoFerreira = new Client("Bruno", "Ferreira","fbrunomarcos@gmail.com");
			Account account3 = new Account("VIN003",this.date, 50000);
			Account account4 = new Account("VIN004",this.date1,75000);
			Transaction transaction3 = new Transaction(2000,"Food",date2,TransactionType.CREDIT);
			Transaction transaction4 = new Transaction(-10000,"Car",date3,TransactionType.DEBIT);
			ClientLoan brunoFerreiraLoanP = new ClientLoan(10000.0,24);
			ClientLoan brunoFerreiraLoanA = new ClientLoan(20000.0,36);
			Card cardSilverBf = new Card("Bruno Ferreira",CardType.DEBIT,CardColor.SILVER,"4567-4567-4567-4567",345,date,dateYears);
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


