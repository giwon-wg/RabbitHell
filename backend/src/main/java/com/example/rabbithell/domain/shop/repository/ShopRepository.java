package com.example.rabbithell.domain.shop.repository;

import static com.example.rabbithell.domain.shop.exception.code.ShopExceptionCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.exception.ShopException;

public interface ShopRepository extends JpaRepository<Shop, Long> {

	default Shop findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ShopException(SHOP_NOT_FOUND));
	}

}
