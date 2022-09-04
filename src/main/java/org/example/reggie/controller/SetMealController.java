package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.example.reggie.dto.SetmealDto;
import org.example.reggie.entity.SetMeal;
import org.example.reggie.service.CategoryService;
import org.example.reggie.service.SetMealDishService;
import org.example.reggie.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetMealDishService setMealDishService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        log.info("新增套餐{}", setmealDto);
        setMealService.saveWithDishes(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        log.info("name:{},page:{},pageSize:{}", name, page, pageSize);

//        查询套餐信息
        Page<SetMeal> setMealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.like(StringUtils.hasText(name), SetMeal::getName, name)
                .orderByAsc(SetMeal::getUpdateTime);
        setMealService.page(setMealPage, setMealLambdaQueryWrapper);


//        封装套餐分类
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setMealPage, setmealDtoPage, "records");
        List<SetmealDto> setmealDtoList = setMealPage.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            setmealDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return setmealDto;
        }).toList();
        setmealDtoPage.setRecords(setmealDtoList);

        return R.success(setmealDtoPage);
    }


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);

        setMealService.removeWithDish(ids);

        return R.success("删除成功");
    }

    @PostMapping("/status/1")
    public R<String> statusDisable(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<SetMeal> setMealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setMealLambdaUpdateWrapper.in(SetMeal::getId, ids).set(SetMeal::getStatus, 1);
        setMealService.update(setMealLambdaUpdateWrapper);
        return R.success("套餐已停售");
    }

    @PostMapping("/status/0")
    public R<String> statusEnable(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<SetMeal> setMealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setMealLambdaUpdateWrapper.in(SetMeal::getId, ids).set(SetMeal::getStatus, 0);
        setMealService.update(setMealLambdaUpdateWrapper);
        return R.success("套餐已启用");
    }

    //    todo
//        套餐修改
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息修改{}", setmealDto);
        setMealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }



    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setMealService.getWithDish(id);
        return R.success(setmealDto);
    }

    @GetMapping("/list")
    public R<List<SetMeal>> list(SetMeal setMeal) {

        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(setMeal.getCategoryId() != null, SetMeal::getCategoryId, setMeal.getCategoryId())
                .eq(setMeal.getStatus() != null, SetMeal::getStatus, setMeal.getStatus());
        List<SetMeal> setMealList = setMealService.list(setMealLambdaQueryWrapper);

        return R.success(setMealList);
    }
}
