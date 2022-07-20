package com.guxt.take.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guxt.take.common.R;
import com.guxt.take.entity.Category;
import com.guxt.take.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);
        return R.success("新增分类成功！");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //构造分页构造器
        Page pageInfo = new Page<>(page,pageSize);
        return categoryService.pageQuery(pageInfo);
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        categoryService.delete(id);
        return R.success("删除成功！");
    }

    /**
     * 根据ID修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("分类信息修改成功");

    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        return categoryService.findCategoryByQuery(category);
    }

}
