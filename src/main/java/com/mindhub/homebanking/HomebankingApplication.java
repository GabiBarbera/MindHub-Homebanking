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
	LocalDate date1 = LocalDate.now().plusDays(1);
	LocalDateTime date3 = LocalDateTime.now().plusDays(4).plusHours(8).plusMinutes(40);
	LocalDateTime date2 = LocalDateTime.now().plusHours(3).plusMinutes(15);
    LocalDateTime date4 = LocalDateTime.now().plusDays(6).plusHours(2).plusMinutes(23);
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			Loan loanMortgage = new Loan("Mortgage", 500000.0, Arrays.asList(12, 24, 36, 48, 60));
			Loan loanPersonal = new Loan("Personal", 100000.0, Arrays.asList(6,12,24));
			Loan loanAutomotive = new Loan("Automotive", 300000.0, Arrays.asList(6,12,24,36));
			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanAutomotive);

			Client client = new Client("Gabriel", "Barbera","gabriel.barberaa@gmail.com");
			Account account1 = new Account("VIN001",this.date, 5000);
			Account account2 = new Account("VIN002",this.date1,7500);
			Transaction transaction1 = new Transaction(-1500,"Shopping",date2, TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(+2500,"Food",date3,TransactionType.CREDIT);
			Transaction transaction5 = new Transaction(-5000,"Dinner",date4,TransactionType.DEBIT);
			ClientLoan clientLoan1 = new ClientLoan(400000.0,60);
			ClientLoan clientLoan2 = new ClientLoan(50000.0,12);
			client.addAccount(account1);
			client.addAccount(account2);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction5);
			client.addClientLoan(clientLoan1);
			client.addClientLoan(clientLoan2);
			loanMortgage.addClientLoan(clientLoan1);
			loanPersonal.addClientLoan(clientLoan2);
			repositoryClient.save(client);
			accountRepository.save(account1);
			accountRepository.save(account2);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction5);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			Client client2 = new Client("Bruno", "Ferreira","fbrunomarcos@gmail.com");
			Account account3 = new Account("VIN003",this.date, 50000);
			Account account4 = new Account("VIN004",this.date1,75000);
			Transaction transaction3 = new Transaction(2000,"Food",date2,TransactionType.CREDIT);
			Transaction transaction4 = new Transaction(-10000,"Car",date3,TransactionType.DEBIT);
			ClientLoan clientLoan3 = new ClientLoan(10000.0,24);
			ClientLoan clientLoan4 = new ClientLoan(20000.0,36);
			client2.addAccount(account3);
			client2.addAccount(account4);
			account4.addTransaction(transaction3);
			account4.addTransaction(transaction4);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			loanPersonal.addClientLoan(clientLoan3);
			loanAutomotive.addClientLoan(clientLoan4);
			repositoryClient.save(client2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
		};
	}
}


