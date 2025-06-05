package com.example.rabbithell.domain.inventory.repository;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryExceptionCode.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.clover.entity.Clover;
import com.example.rabbithell.domain.inventory.entity.Inventory;
import com.example.rabbithell.domain.inventory.exception.InventoryException;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	default Inventory findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new InventoryException(USER_MISMATCH));
	}

	Optional<Inventory> findByClover(Clover clover);

	default Inventory findByCloverOrElseThrow(Clover clover) {
		return findByClover(clover).orElseThrow(() -> new InventoryException(INVENTORY_NOT_FOUND));
	}

	int findCapacityById(Long id);
}
