package com.domain.customer.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ErrorMessage {

    private Date timestamp;

    private HttpStatus status;

    private String error;

    private String message;

    private String path;
}
