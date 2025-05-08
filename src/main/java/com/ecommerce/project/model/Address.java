package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Size(min = 5)
    private String buildingName;

    @Size(min = 5)
    private String city;

    @Size(min = 5)
    private String country;

    @Size(min = 5)
    private String pinCode;

    @Size(min = 5)
    private String state;

    @Size(min = 5)
    private String street;
}
