package com.guxt.take.dto;

import com.guxt.take.entity.Setmeal;
import com.guxt.take.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
