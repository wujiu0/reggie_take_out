package org.example.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 清空购物车
     * @param userId 当前用户ID
     */
    void clean(Long userId);

    /**
     * 获取当前用户购物车
     * @param userId 当前用户ID
     * @return
     */
    List<ShoppingCart> getByUser(Long userId);
}
