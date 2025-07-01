package com.rewards.program.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.model.Transaction;
import com.rewards.program.service.RewardService;

@RestController
@RequestMapping("/rewards/")
public class RewardsController {

	@Autowired
	private RewardService rewardService;

	@GetMapping("/transactions/recent")
	public List<Transaction> getRecentTransactions(){
		return rewardService.getLastThreeMonthsTransactions();
	}

	@GetMapping("/transactions/{customerId}")
	public List<Transaction> getCustomerTransactions(@PathVariable Long customerId){
		return rewardService.getCustomerTransactions(customerId);
	}

	@GetMapping("/transactions/{customerId}/{month}")
	public List<Transaction> getCustomerTransactionsByMonth(@PathVariable Long customerId, @PathVariable String month){
		YearMonth yearMonth = YearMonth.parse(month);
		return rewardService.getCustomerTransactionsByMonth(customerId, yearMonth);
	}
	
	@GetMapping("/summary/{customerId}")
	public RewardSummaryDTO getRewardSummary(@PathVariable Long customerId) {
		return rewardService.calculateRewards(customerId);
	}

}
