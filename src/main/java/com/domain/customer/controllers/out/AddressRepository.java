package com.domain.customer.controllers.out;

import com.domain.customer.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Integer> {
}
