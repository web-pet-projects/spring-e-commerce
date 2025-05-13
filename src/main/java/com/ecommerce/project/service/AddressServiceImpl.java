package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repository.AddressRepository;
import com.ecommerce.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<Address> userAddresses = addressRepository.findAllByUserUserId(userId);
        Address address = modelMapper.map(addressDTO, Address.class);
        if (userAddresses.contains(address)) {
            throw new APIException("Address already exists");
        }
        address.setUser(user);
        addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> fetchAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new APIException("No address found");
        }
        return addresses.stream()
                .map(a -> modelMapper.map(a, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO fetchAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> fetchAllAddressesByUser(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserUserId(userId);
        if (addresses.isEmpty()) {
            throw new APIException("No address found");
        }
        return addresses.stream().map(a -> modelMapper.map(a, AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        if (addressDTO.getUserId() != null) {
            User user = userRepository.findById(addressDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", addressDTO.getUserId()));
            address.setUser(user);
        }
        addressDTO.setId(addressId);
        modelMapper.map(addressDTO, address);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public String removeAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        addressRepository.delete(address);
        return "Address with ID " + addressId + " is removed";
    }
}
