package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.reggie.common.CustomException;
import org.example.reggie.entity.Category;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.SetMeal;
import org.example.reggie.mapper.CategoryMapper;
import org.example.reggie.service.CategoryService;
import org.example.reggie.service.DishService;
import org.example.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;


    @Override
    public void remove(Long id) {
//        查询当前分类是否已经关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long countDish = dishService.count(dishLambdaQueryWrapper);
//        已经关联菜品，抛出一个业务异常
        if (countDish > 0) {
            throw new CustomException("当前分类关联了菜品，无法删除");
        }

//        查询当前分类是否已经关联套餐
        LambdaQueryWrapper<SetMeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
        setMealQueryWrapper.eq(SetMeal::getId, id);
        long countSetMeal = setMealService.count(setMealQueryWrapper);
//        已经关联套餐，抛出异常
        if (countSetMeal > 0) {
            throw new CustomException("当前分类关联了套餐，无法删除");
        }

//        正常删除分类
        super.removeById(id);
    }
}
