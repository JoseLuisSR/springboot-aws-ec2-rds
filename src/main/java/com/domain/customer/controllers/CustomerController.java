package com.domain.customer.controllers;

import com.domain.customer.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${customers.context.path}")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCustomer(@RequestBody Customer customer){
        String response = customerService.createCustomer(customer);
        if ( response.isEmpty() )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity readCustomers(){
        Iterable<Customer> response = customerService.readCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
