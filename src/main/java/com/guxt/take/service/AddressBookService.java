package com.guxt.take.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.entity.AddressBook;
import java.util.List;


public interface AddressBookService extends IService<AddressBook> {
    /**
     * 查询指定用户全部地址
     * @param addressBook
     * @return
     */
  R<List<AddressBook>> findAddressByUserId(AddressBook addressBook);

    /**
     * 新增收获地址
     * @param addressBook
     */
    R<AddressBook> saveAddress(AddressBook addressBook);


    /**
     * 修改收获地址
     * @param addressBook
     */
    void UpdateAddressByUserId(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefaultAddress(AddressBook addressBook);
}
