package org.example.reggie.dto;


import lombok.Data;
import org.example.reggie.entity.SetMeal;
import org.example.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends SetMeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
