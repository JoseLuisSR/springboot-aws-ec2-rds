package com.domain.customer.controllers.in;

import com.domain.customer.entities.CustomerAddress;
import com.domain.customer.entities.CustomerAddress.Address;
import com.domain.customer.services.CustomerAddressService;
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

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping(path = "${customers.address.context.path}")
public class CustomerAddressController implements CustomerAddressAPI{

    public static final String ADDRESS_SUB_RESOURCE = "addresses";

    public static final String ADDRESS_NOT_FOUND_MSG = "Address with id %d not found on Customer's address List.";

    public static final String CUSTOMER_NOT_FOUND_MSG = "Customer not found with id %s.";

    public static final String CUSTOMER_ADDRESSES_NOT_FOUND_MSG = "Customer with id %s, address list not found.";

    @Autowired
    private CustomerAddressService customerAddressService;

    @Value("${customers.context.path}")
    private String customerContextPath;

    @PostMapping
    public ResponseEntity createAddress(@PathVariable("customerId") final String customerId,
                                        @RequestBody final Address newAddress){

        return customerAddressService.findById(customerId).stream()
                .peek(newAddress::setCustomerAddress)
                .peek(customerAddress -> customerAddress.addAddress(newAddress))
                .map(customerAddress -> customerAddressService.update(customerAddress))
                .map(customerAddress -> customerAddress.getLastAddress()
                        .orElseThrow(() -> customerAddressesNotFound.apply(customerId))) // 404, Customer's address list Not Found.
                .findFirst()
                .map(address -> addressCreated.apply(address.getCustomerAddress().getId(), address.getId()))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @GetMapping(path = "${customers.address.by.id}")
    public ResponseEntity getAddress(@PathVariable("customerId") final String customerId,
                                     @PathVariable("addressId") final Integer addressId){

        return customerAddressService.findById(customerId)
                .map(customerAddress -> customerAddress.findAddressById(addressId)
                        .orElseThrow(() -> addressNotFound.apply(addressId)))// 404, Address Not Found.
                .map(address -> addressOkWithContent.apply(address))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @PatchMapping(path = "${customers.address.by.id}")
    public ResponseEntity partialUpdateAddress(@PathVariable("customerId") final String customerId,
                                               @PathVariable("addressId") final Integer addressId,
                                               @RequestBody final Address updateAddress){

        return customerAddressService.findById(customerId).stream()
                .peek(updateAddress::setCustomerAddress)
                .peek(customerAddress -> customerAddress.findAddressById(addressId)
                        .map(address -> address.updateAddressFields(updateAddress))
                        .map(List::of)
                        .orElseThrow(() -> addressNotFound.apply(addressId))) // 404, Address Not Found.
                .peek(customerAddress -> customerAddressService.update(customerAddress))
                .findFirst()
                .map(customer -> addressOkWithContent.apply(customer.findAddressById(addressId).get()))
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.
    }

    @PutMapping(path = "${customers.address.by.id}")
    public ResponseEntity totalUpdateAddress(@PathVariable("customerId") final String customerId,
                                             @PathVariable("addressId") final Integer addressId,
                                             @RequestBody final Address updateAddress){

        CustomerAddress customerAddress = customerAddressService.findById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.

        List<Address> addresses = customerAddress.findAddressById(addressId)
                .map(address -> address.updateAddressFields(updateAddress))
                .map(List::of)
                .orElse(List.of());

        if(addresses.isEmpty()){
            return createAddress(customerId, updateAddress);
        } else {
            customerAddress.updateAddress(updateAddress);
            customerAddressService.update(customerAddress);
            return addressNoContent.get();
        }

    }

    @DeleteMapping(path = "${customers.address.by.id}")
    public ResponseEntity deleteAddress(String customerId, Integer addressId) {

        CustomerAddress customerAddress = customerAddressService.findById(customerId)
                .orElseThrow(() -> customerNotFound.apply(customerId)); // 404, Customer Not Found.

        Address address = customerAddress.findAddressById(addressId)
                .orElseThrow(() -> addressNotFound.apply(addressId)); // 404, Address Not Found.

        customerAddress.removeAddress(address);
        customerAddressService.update(customerAddress);

        return addressOkWithOutContent.get();
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

    Function<Address, ResponseEntity> addressOkWithContent = address -> ResponseEntity.status(HttpStatus.OK)
            .body(address);

    Supplier<ResponseEntity> addressOkWithOutContent = () -> ResponseEntity.status(HttpStatus.OK)
            .build();

    Supplier<ResponseEntity> addressNoContent = () -> ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();

    Function<Integer, ResponseStatusException> addressNotFound = addressId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(ADDRESS_NOT_FOUND_MSG,addressId));

    Function<String, ResponseStatusException> customerNotFound = customerId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(CUSTOMER_NOT_FOUND_MSG, customerId));

    Function<String, ResponseStatusException> customerAddressesNotFound = customerId -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format(CUSTOMER_ADDRESSES_NOT_FOUND_MSG, customerId));

}
