package com.domain.customer.services;

import com.domain.customer.entities.Customer;
import com.domain.customer.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String EMPTY = "";

    @Autowired
    CustomerRepository customerRepository;

    public String create(Customer customer){

        logger.info("Create Customer");
        customer.setId(UUID.randomUUID().toString());
        customerRepository.save(customer);
        return customer.getId();
    }

    public Customer readById(String id) throws NoSuchElementException{
        return customerRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Iterable<Customer> readAll() {

        logger.info("Read Customer");
        return customerRepository.findAll();
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

    public Boolean existsById(String id){
        return customerRepository.existsById(Optional.ofNullable(id)
                .orElse(EMPTY));
    }

}
