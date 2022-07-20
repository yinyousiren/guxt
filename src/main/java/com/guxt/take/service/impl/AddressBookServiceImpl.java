package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.common.R;
import com.guxt.take.entity.AddressBook;
import com.guxt.take.mapper.AddressBookMapper;
import com.guxt.take.service.AddressBookService;
import com.guxt.take.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private HttpServletRequest request;
    @Override
    public R<List<AddressBook>> findAddressByUserId(AddressBook addressBook) {
        addressBook.setUserId(UserThreadLocal.get().getId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBooks = addressBookMapper.selectList(queryWrapper);


        return R.success(addressBooks);
    }

    @Override
    public R<AddressBook> saveAddress(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
        return R.success(addressBook);
    }

    @Override
    public void UpdateAddressByUserId(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    @Override
    public void setDefaultAddress(AddressBook addressBook) {
        Long userId = UserThreadLocal.get().getId();
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,userId);
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookMapper.update(addressBook,updateWrapper);

        addressBook.setIsDefault(1);
        addressBookMapper.updateById(addressBook);



    }
}
