package org.example.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.reggie.entity.SetMeal;
import org.example.reggie.entity.SetmealDish;

@Mapper
public interface SetMealDishMapper extends BaseMapper<SetmealDish> {

}
