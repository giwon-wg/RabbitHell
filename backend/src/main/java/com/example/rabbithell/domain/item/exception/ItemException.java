package com.example.rabbithell.domain.item.exception;

import com.example.rabbithell.common.exception.BaseException;
import com.example.rabbithell.domain.item.exception.code.ItemExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ItemException extends BaseException {

    private final ItemExceptionCode errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    public ItemException(ItemExceptionCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

}
