package com.purchase.transaction.controller;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purchase.transaction.dto.TransactionDto;
import com.purchase.transaction.entity.PurchaseTransaction;
import com.purchase.transaction.service.TransactionService;

@SpringJUnitConfig
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    //Test to store the transaction
    @Test
    public void testCreateTransaction() throws Exception {
        PurchaseTransaction transaction = new PurchaseTransaction();
        transaction.setDescription("Test transaction");
        transaction.setTransactionDate(new java.sql.Date(new Date().getTime()));
        transaction.setPurchaseAmount(BigDecimal.valueOf(100));

        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        mockMvc.perform(post("/createTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test transaction"))
                .andExpect(jsonPath("$.purchaseAmount").value(100));
    }

    //Test to retrieve the purchased transaction with the converted amount for the provided country's currency
    @Test
    public void testRetrievePurchaseTransaction() throws Exception {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId("1");
        transactionDto.setDescription("Transaction 1");
        transactionDto.setTransactionDate(new java.sql.Date(new Date().getTime()).toString());
        transactionDto.setPurchaseAmount("100.00");
        transactionDto.setExchangeRate("1.903");
        
        transactionDtoList.add(transactionDto);
        
        BigDecimal exchangeRate = BigDecimal.valueOf(1.903); 
        BigDecimal purchaseAmount = BigDecimal.valueOf(100.00);
        BigDecimal expectedConvertedAmount = purchaseAmount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);

 	   when(transactionService.retrievePurchaseTransaction(anyString(), anyString(), anyString()))
       .thenReturn(transactionDtoList);
 	  mockMvc.perform(get("/transaction/Australia/Dollar"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(1))
      .andExpect(jsonPath("$[0].id").value("1"))
      .andExpect(jsonPath("$[0].description").value("Transaction 1"))
      .andExpect(jsonPath("$[0].transactionDate").exists())
      .andExpect(jsonPath("$[0].purchaseAmount").value("100.00"))
      .andExpect(jsonPath("$[0].exchangeRate").value("1.903"))
      .andExpect(jsonPath("$[0].convertedAmount").value(expectedConvertedAmount.toString()));
    }
    
    //Test to store the transaction with empty request body
    @Test
    public void testCreateTransactionNegativeCase() throws Exception {
        mockMvc.perform(post("/createTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")) // Empty request body
                .andExpect(status().isBadRequest());
    }

    //Test to retrieve the purchased transactions which has server error
    @Test
    public void testRetrievePurchaseTransactionNegativeCase() throws Exception {
        when(transactionService.retrievePurchaseTransaction(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Failed to retrieve purchase transactions"));

        mockMvc.perform(get("/transaction/Australia/Dollar"))
                .andExpect(status().isInternalServerError());
    }
    
}

