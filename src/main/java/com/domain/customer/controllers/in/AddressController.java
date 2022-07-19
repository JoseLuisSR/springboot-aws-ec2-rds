package com.domain.customer.controllers.in;

import com.domain.customer.entities.Address;
import com.domain.customer.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping(path = "${addresses.context.path}")
public class AddressController implements AddressAPI{

    @Value("${addresses.context.path}")
    public String addressContextPath;

    public static final String ADDRESS_NOT_FOUND = "Address with id %d Not Found.";

    public static final String ADDRESS_SERVER_ERROR_MESSAGE = "Internal Server Error creating the address " +
            "with country %s, state %s and city %s.";

    @Autowired
    public AddressService addressService;

    @PostMapping
    public ResponseEntity createAddress(Address address) {

        return Optional.of(addressService.create(address))
                .map(addressCreated)
                .orElseThrow(() -> addressServerError.apply(address));
    }

    @GetMapping(path = "${addresses.by.id}")
    public ResponseEntity getAddress(Integer addressId) {

        return addressService.readById(addressId)
                .map(addressOkWithContent)
                .orElseThrow(() -> addressNotFound.apply(addressId));
    }

    @PatchMapping(path = "${addresses.by.id}")
    public ResponseEntity partialUpdateAddress(Integer addressId, Address updateAddress) {

        return addressService.readById(addressId)
                .map(address -> updateAddressFields.apply(address, updateAddress))
                .map(addressService::update)
                .map(addressOkWithContent)
                .orElseThrow(() -> addressNotFound.apply(addressId));
    }

    @PutMapping(path = "${addresses.by.id}")
    public ResponseEntity totalUpdateAddress(Integer addressId, Address updateAddress) {

        return addressService.readById(addressId)
                .map(address -> updateAddressFields.apply(address, updateAddress))
                .map(addressService::update)
                .map(address -> addressNoContent.get())
                .orElseGet(() -> createAddress(updateAddress));
    }

    @DeleteMapping(path = "${addresses.by.id}")
    public ResponseEntity deleteAddress(Integer addressId) {

        return addressService.readById(addressId).stream().
                peek(address -> addressService.delete(addressId))
                .findFirst()
                .map(address -> addressOkWithOutContent.get())
                .orElseThrow(()-> addressNotFound.apply(addressId));
    }

    public String buildAddressLocationHeader(Integer addressId){
        return String.format(addressContextPath + "/%d", addressId);
    }

    Function<Integer, ResponseEntity> addressCreated = addressId -> ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.LOCATION, buildAddressLocationHeader(addressId))
            .build();

    Function<Address, ResponseEntity> addressOkWithContent = address -> ResponseEntity.status(HttpStatus.OK)
            .body(address);

    Supplier<ResponseEntity> addressOkWithOutContent = () -> ResponseEntity.status(HttpStatus.OK)
            .build();

    Supplier<ResponseEntity> addressNoContent = () -> ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();

    Function<Integer, ResponseStatusException> addressNotFound = addressId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(ADDRESS_NOT_FOUND, addressId));

    Function<Address, ResponseStatusException> addressServerError = address -> new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            String.format(ADDRESS_SERVER_ERROR_MESSAGE, address.getCountry(), address.getState(), address.getCity()));

    BiFunction<Address, Address, Address> updateAddressFields = (address, updateAddress) -> {
        address.setCountry(updateAddress.getCountry());
        address.setState(updateAddress.getCountry());
        address.setCity(updateAddress.getCity());
        address.setAddress(updateAddress.getAddress());
        return address;
    };
}
