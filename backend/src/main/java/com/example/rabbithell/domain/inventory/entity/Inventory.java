package com.example.rabbithell.domain.inventory.entity;

import static com.example.rabbithell.domain.inventory.exception.code.InventoryExceptionCode.*;

import com.example.rabbithell.domain.inventory.exception.InventoryException;
import com.example.rabbithell.domain.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long id; // User의 PK를 그대로 사용(Shared PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId // id 값을 user_id로부터 매핑
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer capacity; // 용량

    public void setUser(User user) {
        this.user = user;
        this.id = user.getId();
    }

    public void expand(int amount) {
        if (amount <= 0) {
            throw new InventoryException(AMOUNT_TOO_SMALL);
        }
        // TODO: 골드 소모 로직 추가
        this.capacity += amount;
    }

}
