package com.domain.customer.controllers.in;

import com.domain.customer.entities.Address;
import com.domain.customer.entities.Customer;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@RestController
@RequestMapping(path = "${customers.address.context.path}")
public class CustomerAddressController implements CustomerAddressAPI{

    @Autowired
    private CustomerService customerService;

    @Value("${customers.context.path}")
    private String customerContextPath;

    public static final String ADDRESS_SUB_RESOURCE = "addresses";

    public static final String ADDRESS_NOT_FOUND_MSG = "Address with id %d not found on Customer's address List.";

    public static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found with id %s.";

    public static final String CUSTOMER_ADDRESSES_NOT_FOUND_MSG = "Customer with id %s, address list not found.";

    @PostMapping
    public ResponseEntity createAddress(@PathVariable("customerId") final String customerId,
                                        @RequestBody final Address newAddress){

        return customerService.readById(customerId).stream()
                .peek(newAddress::setCustomer)
                .peek(customer -> customer.getAddresses().add(newAddress))
                .map(customer -> customerService.update(customer))
                .map(customer -> getLastAddress.apply(customer)
                        .orElseThrow(() -> customerAddressesNotFound.apply(customerId))) // 404, Customer's address list Not Found.
                .findFirst()
                .map(address -> addressCreated.apply(address.getCustomer().getId(), address.getId()))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @GetMapping(path = "${customers.address.by.id}")
    public ResponseEntity getAddress(@PathVariable("customerId") final String customerId,
                                     @PathVariable("addressId") final Integer addressId){

        return customerService.readById(customerId)
                .map(customer -> findAddressById.apply(customer, addressId)
                        .orElseThrow(() -> addressNotFound.apply(addressId)))// 404, Address Not Found.
                .map(address -> addressOk.apply(address))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @PatchMapping(path = "${customers.address.by.id}")
    public ResponseEntity partialUpdateAddress(@PathVariable("customerId") final String customerId,
                                               @PathVariable("addressId") final Integer addressId,
                                               @RequestBody final Address updateAddress){

        return customerService.readById(customerId).stream()
                .peek(updateAddress::setCustomer)
                .peek(customer -> findAddressById.apply(customer, addressId)
                        .map(address -> updateAddressFields.apply(address, updateAddress))
                        .map(List::of)
                        .orElseThrow(() -> addressNotFound.apply(addressId))) // 404, Address Not Found.
                .peek(customer -> customerService.update(customer))
                .findFirst()
                .map(customer -> addressOk.apply(findAddressById.apply(customer, addressId).get()))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @PutMapping(path = "${customers.address.by.id}")
    public ResponseEntity totalUpdateAddress(@PathVariable("customerId") final String customerId,
                                             @PathVariable("addressId") final Integer addressId,
                                             @RequestBody final Address updateAddress){

        return customerService.readById(customerId)
                .map(customer -> findAddressById.apply(customer, addressId)
                        .map(address -> partialUpdateAddress(customerId, addressId, updateAddress))
                        .orElseGet(() -> createAddress(customerId, updateAddress)))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
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

    Function<Integer, ResponseStatusException> addressNotFound = addressId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(ADDRESS_NOT_FOUND_MSG,addressId));

    Function<String, ResponseStatusException> customerNotFound = customerId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(CUSTOMER_NOT_FOUND_MSG, customerId));

    Function<String, ResponseStatusException> customerAddressesNotFound = customerId -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format(CUSTOMER_ADDRESSES_NOT_FOUND_MSG, customerId));

    BiFunction<Address, Address, Address> updateAddressFields = (address, updateAddress) -> {
        address.setCountry(updateAddress.getCountry());
        address.setState(updateAddress.getState());
        address.setCity(updateAddress.getCity());
        address.setAddress(updateAddress.getAddress());
        address.setCustomer(updateAddress.getCustomer());
        return address;
    };

    BiFunction<Customer, Integer, Optional<Address>> findAddressById = (customer, addressId) ->
            customer.getAddresses()
                    .stream()
                    .filter(address -> address.getId().equals(addressId))
                    .findFirst();

    Function<Customer, Optional<Address>> getLastAddress = customer ->
            customer.getAddresses()
                    .stream()
                    .reduce((first, second) -> second);
}