package com.domain.customer.controllers;

import com.domain.customer.entities.Customer;
import com.domain.customer.entities.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerAPI {

    @Operation(summary = "Create Customer.",
            description = "Create Customer.",
            tags={ "Customers" })
    @ApiResponse(responseCode = "201",
            description = "Customer Created.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)),
            headers = {
            @Header(name = HttpHeaders.LOCATION,
                    schema = @Schema(type = "string"),
                    description = "Location Header")})
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity createCustomer(@Parameter(description = "Customer JSON.")
                                  @RequestBody final Customer customer);

    @Operation(summary = "Read Customer by Id.",
            description = "Read Customer by Id.",
            tags={ "Customers" })
    @ApiResponse(responseCode = "200",
            description = "Customer Found.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity<Customer> getCustomer(@Parameter(description = "Customer Identification.")
                                         @PathVariable final String id);

    @Operation(summary = "Partial Update Customer.",
            description = "Partial update the Customer.",
            tags={ "Customers" })
    @ApiResponse(responseCode = "200",
            description = "Customer Updated.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity<Customer> partialUpdateCustomer(@PathVariable String customerId,
                                                   @RequestBody Customer customer);

    @Operation(summary = "Total Update or Create Customer.",
            description = "Total update or create the Customer.",
            tags={ "Customers" })
    @ApiResponse(responseCode = "201",
            description = "Customer Created.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)),
            headers = {
                    @Header(name = HttpHeaders.LOCATION,
                            schema = @Schema(type = "string"),
                            description = "Location Header")})
    @ApiResponse(responseCode = "204",
            description = "Customer Updated.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity totalUpdateCustomer(@PathVariable String customerId,
                                       @RequestBody Customer customer);

    @Operation(summary = "Delete Customer by Id.",
            description = "Delete Customer by Id.",
            tags={ "Customers" })
    @ApiResponse(responseCode = "200",
            description = "Customer Deleted.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DefaultHttpResponseFactory.class)))
    @ApiResponse(responseCode = "404",
            description = "Customer Not Found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "500",
            description = "Internal Server Error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    ResponseEntity deleteCustomer(@PathVariable String id);
}
