package org.example.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.entity.OrderDetail;
import org.example.reggie.mapper.OrderDetailMapper;
import org.example.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
