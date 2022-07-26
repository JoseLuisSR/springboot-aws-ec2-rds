package com.domain.customer.services;

import com.domain.customer.repositories.CustomerAddressRepository;
import com.domain.customer.entities.CustomerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerAddressService {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public String create(CustomerAddress customerAddress) {

        customerAddress.setId(UUID.randomUUID().toString());
        customerAddressRepository.save(customerAddress);
        return customerAddress.getId();
    }

    public Optional<CustomerAddress> findById(final String customerId) {

        return customerAddressRepository.findById(customerId);
    }

    public CustomerAddress update(final CustomerAddress customerAddress){

        return customerAddressRepository.save(customerAddress);
    }

    public void deleteById(final String customerId) {

        customerAddressRepository.deleteById(customerId);
    }
}
