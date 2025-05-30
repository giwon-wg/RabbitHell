package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.exception.InventoryException;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	default Inventory findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new InventoryException(USER_MISMATCH));
	}

}
