package com.example.rabbithell.domain.shop.repository;

import static com.example.rabbithell.domain.shop.exception.code.ShopExceptionCode.NO_SUCH_SHOP;

import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.exception.ShopException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    default Shop findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ShopException(NO_SUCH_SHOP));
    }

}
