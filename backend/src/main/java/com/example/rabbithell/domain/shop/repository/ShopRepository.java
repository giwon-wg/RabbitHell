package com.example.rabbithell.domain.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.shop.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
