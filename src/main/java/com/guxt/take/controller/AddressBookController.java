package com.guxt.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.guxt.take.common.R;
import com.guxt.take.entity.AddressBook;
import com.guxt.take.service.AddressBookService;
import com.guxt.take.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/list")
    public R<List<AddressBook>> addressList(AddressBook addressBook){
        return addressBookService.findAddressByUserId(addressBook);
    }

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){

        Long userId = UserThreadLocal.get().getId();
        addressBook.setUserId(userId);
        return addressBookService.saveAddress(addressBook);
    }
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.UpdateAddressByUserId(addressBook);
        return R.success("修改地址成功！");
    }
    @DeleteMapping
    public R<String> deleteAddress(Long id){

        addressBookService.removeById(id);
        return R.success("删除地址成功！");
    }
    @PutMapping("/default")
    public R<String> setDefaultAddress(@RequestBody AddressBook addressBook){
        addressBookService.setDefaultAddress(addressBook);
        return R.success("设置默认地址成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultById(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,UserThreadLocal.get().getId()).eq(AddressBook::getIsDefault,1);
        AddressBook byId = addressBookService.getOne(queryWrapper);

        if (byId ==null){
            return R.error("没有找到默认地址！");
        }
        return R.success(byId);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBookServiceById = addressBookService.getById(id);
        if (addressBookServiceById != null){
            return R.success(addressBookServiceById);
        }
        return R.error("没有查到对应地址信息！");
    }


}
