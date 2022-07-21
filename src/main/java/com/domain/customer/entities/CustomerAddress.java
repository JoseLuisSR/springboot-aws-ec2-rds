package com.domain.customer.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "Customers")
public class CustomerAddress {

    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @OneToMany(mappedBy = "customerAddress", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Address> addresses;

    public void removeAddress(Address address) {
        this.addresses.remove(address);
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public Optional<Address> findAddressById(Integer addressId) {

        return this.addresses.stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();
    }

    public Optional<Address> getLastAddress() {

        return this.addresses.stream()
                .reduce((first, second) -> second);
    }

    public void updateAddress(Address updateAddress){
        this.addresses.replaceAll(address ->
                address.equals(updateAddress) ? address.updateAddressFields(updateAddress) : address);
    }
}
