package com.domain.customer.services;

import com.domain.customer.entities.Customer;
import com.domain.customer.controllers.out.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public String create(Customer customer){

        customer.setId(UUID.randomUUID().toString());
        customerRepository.save(customer);
        return customer.getId();
    }

    public Optional<Customer> readById(String id){

        return customerRepository.findById(id);
    }

    public Customer update(Customer customer){

        String id = Optional.ofNullable(customer.getId())
                .orElse(UUID.randomUUID().toString());
        customer.setId(id);

        return customerRepository.save(customer);
    }

    public void deleteById(String id) throws EmptyResultDataAccessException {

        customerRepository.deleteById(id);
    }
}
