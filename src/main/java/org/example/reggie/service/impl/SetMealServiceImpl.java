package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.CustomException;
import org.example.reggie.dto.SetmealDto;
import org.example.reggie.entity.SetMeal;
import org.example.reggie.entity.SetmealDish;
import org.example.reggie.mapper.SetMealMapper;
import org.example.reggie.service.SetMealDishService;
import org.example.reggie.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;

    @Override
    public void saveWithDishes(SetmealDto setmealDto) {

//        保存套餐信息
        super.save(setmealDto);
//        保存套餐菜品对应关系
        Long id = setmealDto.getId();
        setmealDto.getSetmealDishes().forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });
        setMealDishService.saveBatch(setmealDto.getSetmealDishes());

    }

    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.in(SetMeal::getId, ids)
                .eq(SetMeal::getStatus, 1);
        long count = this.count(setMealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖，无法删除");
        }

//        如果可以删除，先删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setMealDishService.remove(setmealDishLambdaQueryWrapper);

//        再删除套餐表中的数据
        this.removeByIds(ids);
    }

    @Override
    public SetmealDto getWithDish(Long id) {
//        根据id查询菜品
        SetMeal setMeal = this.getById(id);
//        查询对应的菜品信息
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setMealDishService.list(setmealDishLambdaQueryWrapper);
//        封装dto
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setMeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {

//        修改套餐信息
        super.updateById(setmealDto);

//        删除原有套餐菜品对应关系
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setMealDishService.remove(setmealDishLambdaQueryWrapper);

//        修改套餐菜品对应关系
        Long id = setmealDto.getId();
        setmealDto.getSetmealDishes().forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });
        setMealDishService.saveBatch(setmealDto.getSetmealDishes());
    }
}
