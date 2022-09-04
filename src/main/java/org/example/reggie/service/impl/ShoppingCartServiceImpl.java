package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.reggie.entity.ShoppingCart;
import org.example.reggie.mapper.ShoppingCartMapper;
import org.example.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public void clean(Long userId) {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);

        baseMapper.delete(shoppingCartLambdaQueryWrapper);
    }

    @Override
    public List<ShoppingCart> getByUser(Long userId) {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new QueryWrapper<ShoppingCart>().lambda()
                .eq(ShoppingCart::getUserId, userId)
                .orderByDesc(ShoppingCart::getCreateTime);

        return baseMapper.selectList(shoppingCartLambdaQueryWrapper);
    }
}
