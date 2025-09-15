package com.prem.ecommerce.Model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses") 
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 3, message= "Building name must be 3 characters..!")
    private String buildingName;

    @NotBlank
    @Size(min = 3, message= "Street name must be 3 characters..!")
    private String street;

    @NotBlank
    @Size(min = 3, message= "City name must be 3 characters..!")
    private String city;

    @NotBlank
    @Size(min = 2, message= "State name must be 2 characters..!")
    private String state;

    @NotBlank
    @Size(min = 2, message= "Country name must be 2 characters..!")
    private String country;

    @NotBlank
    @Size(min = 6, message= "Pincode must be 6 characters..!")
    private String pincode;

    public Address(String buildingName, String street, String city, String state, String country,  String pincode) {
        this.buildingName = buildingName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }


    @ManyToMany(mappedBy = "addresses", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    List<User> users = new ArrayList<>();

    

}
