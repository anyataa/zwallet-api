package com.zwallet.zwalletapi.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ExceptionNotFound extends Exception {

    public ExceptionNotFound(String message) {
        super(message);

    };

}
