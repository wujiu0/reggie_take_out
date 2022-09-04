package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.BaseContext;
import org.example.reggie.common.R;
import org.example.reggie.entity.Orders;
import org.example.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order) {
        log.info("提交订单：{}", order);
        orderService.submit(order);

        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<Orders>> userPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        orderService.page(ordersPage, ordersLambdaQueryWrapper);

        return R.success(ordersPage);
    }

    @GetMapping("/page")
    public R<Page<Orders>> page(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        log.info("订单查询：订单号:{},{}->{}", number, beginTime, endTime);
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(number != null, Orders::getNumber, number)
                .between(StringUtils.hasText(beginTime), Orders::getOrderTime, beginTime, endTime);
        orderService.page(ordersPage, ordersLambdaQueryWrapper);
        return R.success(ordersPage);
    }

    @PutMapping
    public R<String> update(@RequestBody Orders order) {
        orderService.updateById(order);

        return R.success("修改成功");
    }
}
