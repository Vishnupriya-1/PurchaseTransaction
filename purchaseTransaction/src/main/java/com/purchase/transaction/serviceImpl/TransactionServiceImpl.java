package com.purchase.transaction.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purchase.transaction.dto.TransactionDto;
import com.purchase.transaction.dto.TreasuryData;
import com.purchase.transaction.entity.PurchaseTransaction;
import com.purchase.transaction.repository.TransactionRepository;
import com.purchase.transaction.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionRepository transactionRepository;
	

	public PurchaseTransaction createTransaction(PurchaseTransaction purchaseTransaction)
	{
		transactionRepository.save(purchaseTransaction);
		return purchaseTransaction;
	}
	
	public List<TransactionDto> retrievePurchaseTransaction(String url,String country, String currency)
	{		
		List<TransactionDto> dtoList = new ArrayList<>();
		ResponseEntity<String> responseEntity;
		try {
			
			RestTemplate restTemplate = new RestTemplate();
			responseEntity = restTemplate.getForEntity(url, String.class);
		}
		catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while fetching external API");
        }
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode properties;
		try {
			properties = objectMapper.readTree(responseEntity.getBody()).get("data");
			TreasuryData[]  treasuryData = objectMapper.readValue(properties.toString(), TreasuryData[].class);
			for(TreasuryData data : treasuryData)
			{				
				if(data.getCountry().equalsIgnoreCase(country) && data.getCurrency().equalsIgnoreCase(currency))
				{					
					String exchangeRate = data.getExchange_rate();
					List<PurchaseTransaction> purchaseTransactionList;
					try {
						purchaseTransactionList = transactionRepository.findAll();
					}
					catch(Exception e)
					{
						throw new RuntimeException("No purchase transactions available");
					}
					LocalDate startDate = LocalDate.now().minus(Period.ofMonths(6));
					if(purchaseTransactionList.size() > 0)
					{
						for(PurchaseTransaction transaction : purchaseTransactionList)
						{
							if (new java.sql.Date(transaction.getTransactionDate().getTime()).toLocalDate().isAfter(startDate))
							{
								//logger.info("Transaction within 6 months");
								TransactionDto dto = new TransactionDto();
								dto.setId(transaction.getId().toString());
								dto.setDescription(transaction.getDescription());
								dto.setTransactionDate(transaction.getTransactionDate().toString());
								dto.setPurchaseAmount(transaction.getPurchaseAmount().toString());
								dto.setExchangeRate(exchangeRate);
								dto.setConvertedAmount(transaction.getPurchaseAmount().multiply(new BigDecimal(exchangeRate)).setScale(2, BigDecimal.ROUND_HALF_UP)
										.toString());
								dtoList.add(dto);
							}
							else
							{
								//logger.info("purchase not within 6 months");
								TransactionDto dto = new TransactionDto();
								dto.setId(transaction.getId().toString());
								dto.setDescription(transaction.getDescription());
								dto.setTransactionDate(transaction.getTransactionDate().toString());
								dto.setPurchaseAmount(transaction.getPurchaseAmount().toString());
								dto.setErrorMsg("purchase cannot be converted to the target currency");
								dtoList.add(dto);
							}
							
						}	
				}
			 } 
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		 
		return dtoList;
	}
}
