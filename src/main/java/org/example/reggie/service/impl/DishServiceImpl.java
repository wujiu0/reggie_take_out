package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.CustomException;
import org.example.reggie.dto.DishDto;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.DishFlavor;
import org.example.reggie.entity.SetmealDish;
import org.example.reggie.mapper.DishMapper;
import org.example.reggie.service.DishFlavorService;
import org.example.reggie.service.DishService;
import org.example.reggie.service.SetMealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    SetMealDishService setMealDishService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
//        保存菜品的基本信息到dish表
        this.save(dishDto);

        Long id = dishDto.getId();
        dishDto.getFlavors().forEach((item) -> {
            item.setDishId(id);
        });

//        保存菜品的口味信息到dishFlavor表
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
//        查询菜品基本信息
        Dish dish = this.getById(id);
//        查询菜品相关口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);

        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public void updateByIdWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        Long id = dishDto.getId();
        dishDto.getFlavors().forEach((item) -> {
            item.setDishId(id);
        });

//        保存菜品的口味信息到dishFlavor表
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    @Override
    public void remove(List<Long> ids) {
//        在套餐菜品关系表中查询，当前菜品是否有关联套餐
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId, ids);
        if (setMealDishService.count(setmealDishLambdaQueryWrapper) > 0) {
            throw new CustomException("当前菜品关联了套餐,无法删除");
        }

//        删除菜品
        super.removeBatchByIds(ids);
//        删除菜品口味信息
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

    }
}
