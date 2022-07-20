package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.dto.DishDto;
import com.guxt.take.entity.Dish;


import java.util.List;

public interface DishService extends IService<Dish> {
    /**
     * 菜品分页查询
     * @param pageInfo
     * @param name
     * @return
     */
    R<Page> pageQuery(Page pageInfo, String name);

    /**
     * 新增菜品，同时插入菜品对应的口味
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据菜品ID查口味列表
     * @param id
     * @return
     */
    DishDto getByWithFlavor(Long id);

    /**
     * 修改菜品，同时修改菜品对应的口味
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 根据ID修改菜品状态
     * @param id
     * @param id
     */
    void updateStatusById(Long id);


    /**
     * 通过分类ID查找对应的菜品
     * @param dish
     * @return
     */
    R<List<DishDto>> findDishByCategoryId(Dish dish);

    /**
     * 删除菜品
     * @param aLong
     */
    void delete(Long aLong);
}
