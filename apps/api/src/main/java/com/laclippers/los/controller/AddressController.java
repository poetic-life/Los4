package com.laclippers.los.controller;

import com.laclippers.los.common.BusinessException;
import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Address;
import com.laclippers.los.repository.AddressRepository;
import com.laclippers.los.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    public Result<List<Address>> list() {
        return Result.ok(addressRepository.findByUserId(requireUserId()));
    }

    @PostMapping
    public Result<Address> create(@RequestBody Address address) {
        address.setId(null);
        address.setUserId(requireUserId());
        if (address.getIsDefault() == null) {
            address.setIsDefault(false);
        }
        return Result.ok(addressRepository.save(address));
    }

    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id, @RequestBody Address address) {
        Address exist = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "地址不存在"));
        exist.setName(address.getName());
        exist.setPhone(address.getPhone());
        exist.setAddress(address.getAddress());
        if (address.getIsDefault() != null) {
            exist.setIsDefault(address.getIsDefault());
        }
        return Result.ok(addressRepository.save(exist));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressRepository.deleteById(id);
        return Result.ok();
    }

    private Long requireUserId() {
        Long userId = AuthUtil.currentUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        return userId;
    }
}