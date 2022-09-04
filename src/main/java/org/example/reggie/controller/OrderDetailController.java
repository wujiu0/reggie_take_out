package org.example.reggie.controller;

import org.example.reggie.entity.OrderDetail;
import org.example.reggie.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;


}
