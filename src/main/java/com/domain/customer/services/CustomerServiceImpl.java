package com.domain.customer.services;

import com.domain.customer.controllers.CustomerService;
import com.domain.customer.entities.Customer;
import com.domain.customer.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public String createCustomer(Customer customer){

        logger.info("Create Customer");
        customer.setId(UUID.randomUUID().toString());
        customerRepository.save(customer);
        return customer.getId().toString();
    }

    @Override
    public Iterable<Customer> readCustomers() {

        logger.info("Read Customer");
        return customerRepository.findAll();
    }


}
