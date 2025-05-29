package com.example.rabbithell.domain.item.repository;

import static com.example.rabbithell.domain.item.exception.code.ItemExceptionCode.NO_SUCH_ITEM;

import com.example.rabbithell.domain.item.entity.Item;
import com.example.rabbithell.domain.item.exception.ItemException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ItemException(NO_SUCH_ITEM));
    }

}
