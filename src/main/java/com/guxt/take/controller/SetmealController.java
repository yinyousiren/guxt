package com.guxt.take.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.dto.SetmealDto;
import com.guxt.take.entity.Setmeal;
import com.guxt.take.service.SetmealDishService;
import com.guxt.take.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveListCommands;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);

        return setmealService.pageQuery(pageInfo,name);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDishes(setmealDto);
        return R.success("新增套餐成功！");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByWithDish(id);
        if (setmealDto != null){
            return R.success(setmealDto);
        }
        return R.error("没有查询到对应套餐信息");
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDishes(setmealDto);
        return R.success("套餐信息修改成功！");
    }
    @PostMapping(value = {"/status/0","/status/1"})
    public R<String> status(@RequestParam("id") Long[] id){
        for (Long aLong : id) {
            setmealService.updateStatusById(aLong);
        }
        return R.success("套餐状态修改成功");
    }
    @DeleteMapping
    public R<String> delete(Long[] id){
        for (Long aLong : id) {
            setmealService.delete(aLong);
        }
        return R.success("删除套餐成功！");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        return setmealService.findSetmealByCategoryId(setmeal);
    }



}
