package com.example.rabbithell.domain.shop.service;

import static com.example.rabbithell.domain.shop.exception.code.ShopExceptionCode.NO_SUCH_SHOP;

import com.example.rabbithell.domain.shop.dto.response.ShopResponse;
import com.example.rabbithell.domain.shop.entity.Shop;
import com.example.rabbithell.domain.shop.exception.ShopException;
import com.example.rabbithell.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    @Override
    public ShopResponse findShopById(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new ShopException(NO_SUCH_SHOP));
        return ShopResponse.fromEntity(shop);
    }

}
