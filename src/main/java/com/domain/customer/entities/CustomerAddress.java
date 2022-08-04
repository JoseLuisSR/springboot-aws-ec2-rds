package com.domain.customer.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
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

    @Getter
    @Setter
    @Entity
    @Table(name = "Addresses")
    public static class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id", nullable = false)
        @JsonBackReference
        private CustomerAddress customerAddress;

        @Column(nullable = false)
        private String country;

        @Column(nullable = false)
        private String state;

        @Column(nullable = false)
        private String city;

        @Column(nullable = false)
        private String address;

        public Address updateAddressFields(Address address){

            this.setCountry(address.getCountry());
            this.setState(address.getState());
            this.setCity(address.getCity());
            this.setAddress(address.getAddress());
            return this;
        }

        @Override
        public boolean equals(Object obj) {

            if(obj == this)
                return true;

            if(!(obj instanceof com.domain.customer.entities.Address))
                return false;

            com.domain.customer.entities.Address address = (com.domain.customer.entities.Address) obj;

            if(this.getId().equals(address.getId()))
                return true;

            return false;
        }
    }
}
