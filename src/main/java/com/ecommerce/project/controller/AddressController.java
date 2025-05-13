package com.ecommerce.project.controller;

import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AddressController {
    private final AddressService addressService;
    private final AuthUtils authUtils;

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO response = addressService.createAddress(addressDTO, authUtils.loggedInUser().getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressDTOList = addressService.fetchAllAddresses();
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.fetchAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.FOUND);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {

        List<AddressDTO> addressDTOList = addressService.fetchAllAddressesByUser(authUtils.loggedInUser().getUserId());
        return new ResponseEntity<>(addressDTOList, HttpStatus.FOUND);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.removeAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.NO_CONTENT);
    }
}
