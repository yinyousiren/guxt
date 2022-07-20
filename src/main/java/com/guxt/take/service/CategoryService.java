package com.guxt.take.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxt.take.common.R;
import com.guxt.take.entity.Category;


import java.util.List;

public interface CategoryService extends IService<Category> {
    /**
     * 分页查询
     * @param pageInfo
     * @return
     */
    R<Page> pageQuery(Page pageInfo);

    /**
     * 根据ID删除分类
     * @param id
     */
    void delete(Long id);

    R<List<Category>> findCategoryByQuery(Category category);
}
