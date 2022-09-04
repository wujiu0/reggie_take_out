package org.example.reggie.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.reggie.entity.Dish;
import org.example.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
