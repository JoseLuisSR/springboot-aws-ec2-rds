package com.domain.customer.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Getter
@Setter
@Entity
@Table(name = "Addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(name = "customer_id",
            nullable = false)
    private String customerId;

    public Address updateAddressFields(Address updateAddress){

        this.setCountry(updateAddress.getCountry());
        this.setState(updateAddress.getState());
        this.setCity(updateAddress.getCity());
        this.setAddress(updateAddress.getAddress());
        return this;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == this)
            return true;

        if(!(obj instanceof Address))
            return false;

        Address address = (Address) obj;

        if(this.getId().equals(address.getId()))
            return true;

        return false;
    }
}
