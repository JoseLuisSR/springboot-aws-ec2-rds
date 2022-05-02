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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

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
                .header(HttpHeaders.LOCATION, buildCustomerLocationHeader(id))
                .build();
    }

    @GetMapping(path = "${customers.by.id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String id){

        Customer customer = customerService.readById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer not found with id " + id)); // 404, Not Found.

        return ResponseEntity.status(HttpStatus.OK)
                .body(customer);
    }

    @PatchMapping(path = "${customers.by.id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id,
                                                   @RequestBody Customer customer){

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
                .header(HttpHeaders.LOCATION, buildCustomerLocationHeader(customer.getId()))
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

    public String buildCustomerLocationHeader(String customerId){

        return String.format("/%s/%s", contextPath,
                customerId);
    }

}
