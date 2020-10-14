package com.domain.customer.controllers;

import com.domain.customer.entities.Customer;

public interface CustomerService {

    public String createCustomer(Customer customer);

    public Iterable<Customer> readCustomers();
}
