package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.dto.SetmealDto;
import com.guxt.take.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDto
     */
    void saveWithDishes(SetmealDto setmealDto);

    /**
     * 套餐分页查询
     * @param pageInfo
     * @param name
     * @return
     */
    R<Page> pageQuery(Page pageInfo, String name);

    /**
     * 修改套餐回显信息
     * @param id
     * @return
     */
    SetmealDto getByWithDish(Long id);

    /**
     * 修改套餐信息
     * @param setmealDto
     */
    void updateWithDishes(SetmealDto setmealDto);

    /**
     * 通过ID修改套餐售卖状态
     * @param id
     */
    void updateStatusById(Long id);

    /**
     * 根据ID删除套餐
     * @param id
     */
    void delete(Long id);

    /**
     * 前台通过分类ID查找套餐信息，展示在前台页面
     * @param setmeal
     * @return
     */
    R<List<Setmeal>> findSetmealByCategoryId(Setmeal setmeal);
}
