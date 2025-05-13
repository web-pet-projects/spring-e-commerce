package com.ecommerce.project.service;

import com.ecommerce.project.payload.AddressDTO;

import java.util.List;


public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, Long userId);

    List<AddressDTO> fetchAllAddresses();

    AddressDTO fetchAddressById(Long addressId);

    List<AddressDTO> fetchAllAddressesByUser(Long userId);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String removeAddress(Long addressId);
}
