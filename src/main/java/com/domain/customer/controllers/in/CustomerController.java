package com.domain.customer.controllers.in;

import com.domain.customer.entities.Customer;
import com.domain.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "${customers.context.path}")
public class CustomerController implements CustomerAPI{

    @Autowired
    private CustomerService customerService;

    @Value("${customers.context.path}")
    private String contextPath;

    public static final String CUSTOMER_NOT_FOUND_MSG = "Customer with id %s not found.";

    public static final String CUSTOMER_SERVER_ERROR_MSG = "Internal Server Error creating the customer " +
            "with first name %s and last name %s.";

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createCustomer(@RequestBody final Customer customer){

        return Optional.of(customerService.create(customer))
                .map(customerId -> customerCreated.apply(customerId))
                .orElseThrow(() -> customerServerError.apply(customer));
    }

    @GetMapping(path = "${customers.by.id}")
    public ResponseEntity getCustomer(@PathVariable final String customerId){

        return customerService.readById(customerId)
                .map(customer -> customerOk.apply(customer))
                .orElseThrow(() -> customerNotFound.apply(customerId));
    }

    @PatchMapping(path = "${customers.by.id}")
    public ResponseEntity partialUpdateCustomer(@PathVariable final String customerId,
                                                @RequestBody final Customer updateCustomer){

        return customerService.readById(customerId)
                .map(customer -> updateCustomerFields.apply(customer, updateCustomer))
                .map(customer -> customerService.update(customer))
                .map(customerOk)
                .orElseThrow(() -> customerNotFound.apply(customerId));
    }

    @PutMapping(path = "${customers.by.id}")
    public ResponseEntity totalUpdateCustomer(@PathVariable final String customerId,
                                              @RequestBody final Customer updateCustomer){

        return customerService.readById(customerId)
                .map(customer -> updateCustomerFields.apply(customer, updateCustomer))
                .map(customer -> customerService.update(customer))
                .map(customerNotContent)
                .orElseGet(() -> createCustomer(updateCustomer));
    }

    @DeleteMapping(path = "${customers.by.id}")
    public ResponseEntity deleteCustomer(@PathVariable final String customerId){

        return customerService.readById(customerId)
                .stream()
                .peek(customer -> customerService.deleteById(customerId))
                .map(customer -> ResponseEntity.status(HttpStatus.OK)
                        .build())
                .findFirst()
                .orElseThrow(() -> customerNotFound.apply(customerId));
    }

    public String buildCustomerLocationHeader(String customerId){

        return String.format("/%s/%s", contextPath,
                customerId);
    }

    Function<String, ResponseEntity> customerCreated = customerId -> ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.LOCATION, buildCustomerLocationHeader(customerId))
            .build();

    Function<Customer, ResponseEntity> customerOk = customer -> ResponseEntity.status(HttpStatus.OK)
            .body(customer);

    Function<String, ResponseStatusException> customerNotFound = customerId -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            String.format(CUSTOMER_NOT_FOUND_MSG, customerId));

    Function<Customer, ResponseEntity> customerNotContent = customerId -> ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();

    Function<Customer, ResponseStatusException> customerServerError = customer -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            String.format(CUSTOMER_SERVER_ERROR_MSG, customer.getFirstName(), customer.getLastName()));

    BiFunction<Customer, Customer, Customer> updateCustomerFields = (customer, updateCustomer) -> {
        customer.setFirstName(updateCustomer.getFirstName());
        customer.setLastName(updateCustomer.getLastName());
        customer.setAge(updateCustomer.getAge());
        return customer;
    };

}
