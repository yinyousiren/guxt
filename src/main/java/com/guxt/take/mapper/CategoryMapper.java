package com.guxt.take.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guxt.take.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
