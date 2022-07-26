package com.domain.customer.repositories;

import com.domain.customer.entities.CustomerAddress;
import org.springframework.data.repository.CrudRepository;

public interface CustomerAddressRepository extends CrudRepository<CustomerAddress, String> {
}
