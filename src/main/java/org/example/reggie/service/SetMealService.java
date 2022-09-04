package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.dto.SetmealDto;
import org.example.reggie.entity.SetMeal;

import java.util.List;

public interface SetMealService extends IService<SetMeal> {
    /**
     * 保存套餐即对应菜品信息
     * @param setmealDto
     */
    void saveWithDishes(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐和对应的菜品
     * @param id
     * @return
     */
    SetmealDto getWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);

}
