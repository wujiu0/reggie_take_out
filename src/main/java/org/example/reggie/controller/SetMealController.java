package org.example.reggie.controller;

import org.example.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setMeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;


}
