package com.domain.customer.controllers;

import com.domain.customer.entities.Customer;
import com.domain.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "${customers.context.path}")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Value("${customers.context.path}")
    private String contextPath;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCustomer(@RequestBody Customer customer){

        String id = customerService.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, String.format("/%s/%s", contextPath, id))
                .build();
    }

    @GetMapping(path = "${customers.by.id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id){

        Customer customer;

        try{
            customer = customerService.readById(id);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(customer);
    }

    @PatchMapping(path = "${customers.by.id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody Customer customer){

        if(!customerService.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();

        customer.setId(id);
        customer = customerService.update(customer);

        return ResponseEntity.status(HttpStatus.OK)
                .body(customer);
    }

    @PutMapping
    public ResponseEntity updateCustomer(@RequestBody Customer customer){

        Boolean exists = customerService.existsById(customer.getId());
        customer = customerService.update(customer);

        if(exists)
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, String.format("/%s/%s", contextPath, customer.getId()))
                .build();
    }

    @DeleteMapping(path = "${customers.by.id}")
    public ResponseEntity deleteCustomer(@PathVariable String id){

        try{
            customerService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @GetMapping(path = "${customers.all}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Iterable<Customer>> getAllCustomers(){

        Iterable<Customer> response = customerService.readAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
