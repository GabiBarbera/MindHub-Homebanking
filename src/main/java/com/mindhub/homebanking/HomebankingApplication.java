package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {
	LocalDate date = LocalDate.now();
	LocalDate date1 = LocalDate.now().plusDays(1);
	LocalDateTime date3 = LocalDateTime.now().plusDays(4);
	LocalDateTime date2 = LocalDateTime.now();
    LocalDateTime date4 = LocalDateTime.now().plusDays(6);
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client client = new Client("Gabriel", "Barbera","gabriel.barberaa@gmail.com");
			Account account1 = new Account("VIN001",this.date, 5000);
			Account account2 = new Account("VIN002",this.date1,7500);
			Transaction transaction1 = new Transaction(-1500,"Shopping",date2, TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(+2500,"Food",date3,TransactionType.CREDIT);
			Transaction transaction5 = new Transaction(-5000,"Dinner",date4,TransactionType.DEBIT);
			client.addAccount(account1);
			client.addAccount(account2);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction5);
			repositoryClient.save(client);
			accountRepository.save(account1);
			accountRepository.save(account2);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction5);

			Client client2 = new Client("Bruno", "Ferreira","fbrunomarcos@gmail.com");
			Account account3 = new Account("VIN003",this.date, 50000);
			Account account4 = new Account("VIN004",this.date1,75000);
			Transaction transaction3 = new Transaction(2000,"Food",date2,TransactionType.CREDIT);
			Transaction transaction4 = new Transaction(-10000,"Car",date3,TransactionType.DEBIT);
			client2.addAccount(account3);
			client2.addAccount(account4);
			account4.addTransaction(transaction3);
			account4.addTransaction(transaction4);
			repositoryClient.save(client2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
		};
	}
}


