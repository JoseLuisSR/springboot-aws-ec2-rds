package com.domain.customer.services;

import com.domain.customer.entities.Address;
import com.domain.customer.controllers.out.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Integer create(Address address){

        return addressRepository.save(address)
                .getId();
    }

    public Optional<Address> readById(Integer addressId){

        return addressRepository.findById(addressId);
    }

    public Address update(Address address){

        return addressRepository.save(address);
    }

    public void delete(Integer addressId){

        addressRepository.deleteById(addressId);
    }

}
