package com.purchase.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purchase.transaction.entity.PurchaseTransaction;

public interface TransactionRepository extends JpaRepository<PurchaseTransaction, Long> {
	
	@Query("select u from PurchaseTransaction u where u.description = :description")
	PurchaseTransaction findByDescription(@Param("description") String description);

}
