package com.purchase.transaction.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.purchase.transaction.dto.TransactionDto;
import com.purchase.transaction.entity.PurchaseTransaction;
import com.purchase.transaction.service.TransactionService;


@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;

	// creating a logger
	Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	//API mapping to store the purchase transaction
	@RequestMapping(value ="/createTransaction", method=RequestMethod.POST)
	public ResponseEntity<PurchaseTransaction> createTransaction(@RequestBody PurchaseTransaction purchaseTransaction){
		logger.info("To store the new transaction");
		purchaseTransaction = transactionService.createTransaction(purchaseTransaction);
		return new ResponseEntity<PurchaseTransaction>(purchaseTransaction,HttpStatus.CREATED);		
	}
	
	//API mapping to retrieve all the transactions with the converted amount for the provided country's currency
	@RequestMapping(value = "/transaction/{country}/{currency}", method = RequestMethod.GET)
	public List<TransactionDto> retrievePurchaseTransaction(@PathVariable("country") String country,
			@PathVariable("currency") String currency) {
		logger.info("To retrieve all the transactions with the converted amount for the provided country's "+country+" currency "+currency);
		String url = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
		List<TransactionDto> convertedTransactions = transactionService.retrievePurchaseTransaction(url,country,currency);
		return convertedTransactions;
	}

}
