package com.domain.customer.controllers;

import com.domain.customer.entities.Address;
import com.domain.customer.entities.Customer;
import com.domain.customer.services.AddressService;
import com.domain.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@RestController
@RequestMapping(path = "${customers.address.context.path}")
public class CustomerAddressController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;

    @Value("${customers.context.path}")
    private String customerContextPath;

    public static final String ADDRESS_SUB_RESOURCE = "addresses";

    public static final String ADDRESS_NOT_FOUND_MSG = "Address not found on Customer address List";

    public static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found with id";

    @PostMapping
    public ResponseEntity createAddress(@PathVariable("customerId") final String customerId,
                                        @RequestBody final Address newAddress){

        Customer customer = customerService.readById(customerId)
                .orElseThrow( () -> customerNotFound.apply(customerId)); // 404, Not Found.

        return Optional.of(newAddress).stream()
                .peek(address -> address.setCustomer(customer))
                .findFirst()
                .map(address -> addressService.create(address))
                .map(addressId -> addressCreated.apply(customerId, addressId))
                .get();
    }

    @GetMapping(path = "${customers.address.by.id}")
    public ResponseEntity<Address> getAddress(@PathVariable("customerId") final String customerId,
                                              @PathVariable("addressId") final Integer addressId){

        Customer customer = customerService.readById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Not Found.

        return customer.getAddresses().stream()
                .filter( a -> a.getId().equals(addressId))
                .findFirst()
                .map(address -> addressOk.apply(address))
                .orElseThrow(() -> addressNotFound.apply(addressId)); // 404, Not Found

    }

    @PatchMapping(path = "${customers.address.by.id}")
    public ResponseEntity<Address> updateAddress(@PathVariable("customerId") final String customerId,
                                                 @PathVariable("addressId") final Integer addressId,
                                                 @RequestBody final Address updateAddress){

        Customer customer = customerService.readById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Not Found.

        return customer.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .map(a -> updateAddress)
                .peek(a -> a.setId(addressId))
                .peek(a -> a.setCustomer(customer))
                .peek(a -> addressService.update(a))
                .findFirst()
                .map( a -> addressOk.apply(a))
                .orElseThrow(() -> addressNotFound.apply(addressId)); // 404, Not Found
    }

    @PutMapping(path = "${customers.address.by.id}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") final String customerId,
                                         @PathVariable("addressId") final Integer addressId,
                                         @RequestBody final Address updateAddress){

        Customer customer = customerService.readById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Not Found.

        try{

            return customer.getAddresses().stream()
                    .filter(a -> a.getId().equals(addressId))
                    .map(a -> updateAddress)
                    .peek(a -> a.setId(addressId))
                    .peek(a -> a.setCustomer(customer))
                    .peek(a -> addressService.update(a))
                    .findFirst()
                    .map( a -> addressAccepted.apply(a))
                    .orElseThrow(() -> addressNotFound.apply(addressId)); // 404, Not Found

        }catch (ResponseStatusException e){

            return Optional.of(updateAddress).stream()
                    .peek(address -> address.setCustomer(customer))
                    .findFirst()
                    .map(address -> addressService.create(address))
                    .map(id -> addressCreated.apply(customerId, id))
                    .get();

        }
    }

    @DeleteMapping(path = "${customers.address.by.id}")
    public ResponseEntity deleteAddress(@PathVariable("customerId") final String customerId,
                                        @PathVariable("addressId") final Integer addressId){

        Customer customer = customerService.readById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Not Found.

        return customer.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .peek(address -> addressService.delete(addressId))
                .findFirst()
                .map(addressOk)
                .orElseThrow(() -> addressNotFound.apply(addressId)); // 404, Not Found.
    }

    public String buildAddressLocationHeader(final String customerId, final Integer addressId){

        return String.format("/%s/%s/%s/%s", customerContextPath,
                customerId,
                ADDRESS_SUB_RESOURCE,
                addressId.toString());
    }

    BiFunction<String, Integer, ResponseEntity> addressCreated = (customerId, addressId) -> ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.LOCATION, buildAddressLocationHeader(customerId, addressId))
            .build();

    Function<Address, ResponseEntity> addressOk = address -> ResponseEntity.status(HttpStatus.OK)
            .body(address);

    Function<Address, ResponseEntity> addressAccepted = address -> ResponseEntity.status(HttpStatus.ACCEPTED)
            .build();

    Function<Integer, ResponseStatusException> addressNotFound = addressId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            this.ADDRESS_NOT_FOUND_MSG + " " + addressId);

    Function<String, ResponseStatusException> customerNotFound = customerId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            this.CUSTOMER_NOT_FOUND_MSG + " " + customerId);

}
