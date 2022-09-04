package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.example.reggie.dto.DishDto;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.DishFlavor;
import org.example.reggie.service.CategoryService;
import org.example.reggie.service.DishFlavorService;
import org.example.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param name     模糊查询的名称参数
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(String name, Integer page, Integer pageSize) {
        log.info("{},{},{}", name, page, pageSize);


        Page<Dish> pageInfo = new Page<>(page, pageSize);

//        条件构造
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Dish::getName, name)
                .orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(page, dishDtoPage, "records");

//        遍历查询结果获取分类id，并根据id查询分类name，填充至dto
        List<DishDto> dishDtoList = pageInfo.getRecords().stream().map(item -> {
            DishDto dishDto = new DishDto();

//        为什么要取出来复制，操作 再收集重新set呢  注意此处是从DishPage中获取id，而返回值是dishDtoPage，两者的Records类型不同
            BeanUtils.copyProperties(item, dishDto);

            dishDto.setCategoryName(categoryService.getById(dishDto.getCategoryId()).getName());
            return dishDto;

        }).toList();

        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("添加菜品：{}", dishDto);
        dishService.saveWithFlavor(dishDto);

        return R.success("菜品添加成功");
    }


    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除菜品：id{}", ids);
        dishService.remove(ids);

        return R.success("删除菜品成功");
    }


    @GetMapping("/{id}")
    public R<DishDto> findById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateByIdWithFlavor(dishDto);

        return R.success("修改成功");
    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
//        查询菜品信息
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(queryWrapper);

//            查询口味数据，封装至dto内
        List<DishDto> dishDtoList = dishList.stream().map(dishItem -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishItem, dishDto);

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishItem.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).toList();
//        查询口味数据
        return R.success(dishDtoList);
    }


    /**
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> statusDisable(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Dish> setMealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setMealLambdaUpdateWrapper.in(Dish::getId, ids).set(Dish::getStatus, 1);
        dishService.update(setMealLambdaUpdateWrapper);
        return R.success("套餐已停售");
    }

    @PostMapping("/status/0")
    public R<String> statusEnable(@RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Dish> setMealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        setMealLambdaUpdateWrapper.in(Dish::getId, ids).set(Dish::getStatus, 0);
        dishService.update(setMealLambdaUpdateWrapper);
        return R.success("套餐已停售");
    }


}
