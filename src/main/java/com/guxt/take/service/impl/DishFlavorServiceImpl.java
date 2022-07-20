package com.guxt.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxt.take.entity.DishFlavor;
import com.guxt.take.mapper.DishFlavorMapper;
import com.guxt.take.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
