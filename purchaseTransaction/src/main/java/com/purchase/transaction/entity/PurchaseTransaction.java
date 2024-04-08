package com.purchase.transaction.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PURCHASETRANSACTION")
public class PurchaseTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Column(name="description",length = 50)
	private String description;

	@Column(name="transaction_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date transactionDate;

	@Column(name="purchase_amount")
	private BigDecimal purchaseAmount;
		

	public PurchaseTransaction() {
		super();
	}

	public PurchaseTransaction(Long id, String description, Date transactionDate, BigDecimal purchaseAmount) {
		super();
		this.id = id;
		this.description = description;
		this.transactionDate = transactionDate;
		this.purchaseAmount = purchaseAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(BigDecimal purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	
	
	@Override
	public String toString() {
		return "PurchaseTransaction [id=" + id + ", description=" + description + ", transactionDate=" + transactionDate
				+ ", purchaseAmount=" + purchaseAmount + "]";
	}

}
