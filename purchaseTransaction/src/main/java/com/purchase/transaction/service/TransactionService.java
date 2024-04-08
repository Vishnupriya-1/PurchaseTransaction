package com.purchase.transaction.service;

import java.util.List;

import com.purchase.transaction.dto.TransactionDto;
import com.purchase.transaction.entity.PurchaseTransaction;

public interface TransactionService {
	
	public PurchaseTransaction createTransaction(PurchaseTransaction transaction);
	
	public List<TransactionDto> retrievePurchaseTransaction(String url,String country, String currency);

}
