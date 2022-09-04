package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.BaseContext;
import org.example.reggie.common.R;
import org.example.reggie.entity.ShoppingCart;
import org.example.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加至购物车:{}", shoppingCart);

//        设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());

//        查询当前菜品是否在购物车内
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();
        if (dishId == null) {
//            添加的是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        } else {
//            添加的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (shoppingCartServiceOne == null) {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        } else {
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(++number);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }

        return R.success(shoppingCartServiceOne);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("减少购物车菜品数量:{}", shoppingCart);

//        设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());

//        查询当前购物车内的菜品
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();
        if (dishId == null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        } else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        Integer number = shoppingCartServiceOne.getNumber();
        if (number == 1) {
//            购物车内只剩下一件，直接删除
            shoppingCartService.removeById(shoppingCartServiceOne);
        } else {
            shoppingCartServiceOne.setNumber(--number);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }

        return R.success(shoppingCartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {

        List<ShoppingCart> list = shoppingCartService.getByUser(BaseContext.getCurrentId());
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {

        shoppingCartService.clean(BaseContext.getCurrentId());
        return R.success("购物车清空成功");
    }

}

