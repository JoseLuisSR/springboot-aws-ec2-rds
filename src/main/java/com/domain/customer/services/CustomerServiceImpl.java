package com.domain.customer.services;

import com.domain.customer.controllers.CustomerService;
import com.domain.customer.entities.Customer;
import com.domain.customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public String createCustomer(Customer customer){

        customer.setId(UUID.randomUUID().toString());
        customerRepository.save(customer);
        return customer.getId().toString();
    }

    @Override
    public Iterable<Customer> readCustomers() {
        return customerRepository.findAll();
    }


}
