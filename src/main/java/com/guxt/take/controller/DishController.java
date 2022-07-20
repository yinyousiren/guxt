package com.guxt.take.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.dto.DishDto;
import com.guxt.take.entity.Dish;
import com.guxt.take.service.DishFlavorService;
import com.guxt.take.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import java.util.List;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);

        return dishService.pageQuery(pageInfo,name);
    }
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");

    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        return R.success("菜品信息修改成功！");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByWithFlavor(id);
        if (dishDto != null){
            return R.success(dishDto);
        }
        return R.error("没有查询到对应菜品信息");
    }
    @PostMapping(value = {"/status/0","/status/1"})
    public R<String> status(@RequestParam("id") Long[] id){
        for (Long aLong : id) {
            dishService.updateStatusById(aLong);
        }
        return R.success("菜品状态修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        return dishService.findDishByCategoryId(dish);
    }
    @DeleteMapping
    public R<String> delete(Long[] id){
        for (Long aLong : id) {
            dishService.delete(aLong);
        }
        return R.success("删除菜品成功！");
    }

}
