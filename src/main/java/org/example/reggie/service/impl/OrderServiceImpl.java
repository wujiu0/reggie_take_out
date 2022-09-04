package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.BaseContext;
import org.example.reggie.entity.*;
import org.example.reggie.mapper.OrderMapper;
import org.example.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;

    @Override
    @Transactional
    public void submit(Orders order) {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
//        生成订单ID
        long orderId = IdWorker.getId();

//        查询当前购物车
        List<ShoppingCart> shoppingCartList = shoppingCartService.getByUser(userId);
//        计算总金额，并生成订单明细
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            amount.addAndGet(shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber())).intValue());
            return orderDetail;
        }).toList();


//        插入订单数据
        order.setId(orderId);
        order.setUserId(userId);
        order.setNumber(String.valueOf(orderId));
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setStatus(2);
        order.setAmount(new BigDecimal(amount.get()));
        order.setUserName(user.getName());
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(user.getPhone());
        order.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
        );

        this.save(order);

//        插入订单明细
        orderDetailService.saveBatch(orderDetailList);
//        清空购物车
        shoppingCartService.clean(userId);
    }
}
