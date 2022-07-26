package com.domain.customer.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Getter
@Setter
@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    public Customer updateCustomerFields(Customer customer){

        this.setFirstName(customer.getFirstName());
        this.setLastName(customer.getLastName());
        this.setAge(customer.getAge());
        return this;
    }

}
