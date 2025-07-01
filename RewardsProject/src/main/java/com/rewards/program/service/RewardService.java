package com.rewards.program.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.exception.ResourceNotFoundException;
import com.rewards.program.model.Transaction;
import com.rewards.program.repository.TransactionRepository;

@Service
public class RewardService {

	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transaction> getLastThreeMonthsTransactions(){
		LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
		List<Transaction> transactions = transactionRepository.findByTransactionDateAfter(threeMonthsAgo);
		if(transactions.isEmpty()) {
			throw new ResourceNotFoundException("No Transactions found in last three months");
		}
		return transactions;
	}

	public List<Transaction> getCustomerTransactions(Long customerId){
		List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
		if(transactions.isEmpty()) {
			throw new ResourceNotFoundException("No Transactions found for customerId :" + customerId);
		}
		return transactions;
	}

	public List<Transaction> getCustomerTransactionsByMonth(Long customerId, YearMonth month){
		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();
		List <Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
		if(transactions.isEmpty()) {
			throw new ResourceNotFoundException("No Transactions found for customerId :" + customerId + "in" + month);
		}
		return transactions;
	}

	public RewardSummaryDTO calculateRewards(Long customerId) {
		List<Transaction> transactions = getCustomerTransactions(customerId);
		Map<String, Integer> montlyPoints = new HashMap<>();
		int totalPoints=0;
		for(Transaction transaction : transactions) {
			int points = RewardCalculator.calculatePoints(transaction.getAmount());
			String month = transaction.getTransactionDate().getMonth().toString();
			montlyPoints.put(month, montlyPoints.getOrDefault(month, 0) + points);
			totalPoints += points;
		}
		return new RewardSummaryDTO(customerId, montlyPoints, totalPoints);
	}
}
