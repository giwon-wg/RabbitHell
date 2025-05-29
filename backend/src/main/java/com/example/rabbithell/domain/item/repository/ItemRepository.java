package com.example.rabbithell.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
