package com.domain.customer.controllers.in;

import com.domain.customer.entities.Address;
import com.domain.customer.entities.Customer;
import com.domain.customer.entities.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerAddressAPI {

    @Operation(summary = "Create Address.",
            description = "Create Address.",
            tags={ "Customer Addresses" })
    @ApiResponse(responseCode = "201",
            description = "Create Address.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)),
            headers = {
                    @Header(name = HttpHeaders.LOCATION,
                            schema = @Schema(type = "string"),
                            description = "Location Header")})
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity createAddress(@PathVariable("customerId") final String customerId,
                                 @RequestBody final Address newAddress);

    @Operation(summary = "Read Address.",
            description = "Read Address by Customer Id and Address Id.",
            tags={ "Customer Addresses" })
    @ApiResponse(responseCode = "200",
            description = "Customer and Address Found.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Address.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer or Address Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity<Address> getAddress(@PathVariable("customerId") final String customerId,
                                       @PathVariable("addressId") final Integer addressId);

    @Operation(summary = "Partial Update Customer and Address.",
            description = "Partial Update Customer and Address by Customer Id and Address Id.",
            tags={ "Customer Addresses" })
    @ApiResponse(responseCode = "200",
            description = "Customer and Address Updated.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer or Address Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity<Address> partialUpdateAddress(@PathVariable("customerId") final String customerId,
                                                 @PathVariable("addressId") final Integer addressId,
                                                 @RequestBody final Address updateAddress);

    @Operation(summary = "Total Update or Create Address.",
            description = "Total update or Create Address.",
            tags={ "Customer Addresses" })
    @ApiResponse(responseCode = "201",
            description = "Address Created.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)),
            headers = {
                    @Header(name = HttpHeaders.LOCATION,
                            schema = @Schema(type = "string"),
                            description = "Location Header")})
    @ApiResponse(responseCode = "204",
            description = "Address Updated.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer or Address Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity totalUpdateAddress(@PathVariable("customerId") final String customerId,
                                  @PathVariable("addressId") final Integer addressId,
                                  @RequestBody final Address updateAddress);

}
