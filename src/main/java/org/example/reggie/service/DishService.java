package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.dto.DishDto;
import org.example.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据
     * @param dishDto
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    void updateByIdWithFlavor(DishDto dishDto);

    void remove(List<Long> ids);
}
