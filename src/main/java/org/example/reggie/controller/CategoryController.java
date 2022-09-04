package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.example.reggie.entity.Category;
import org.example.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);

        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);

        Page<Category> pageInfo = new Page<>();
//        条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        添加排序条件
        queryWrapper.orderByAsc(Category::getType).orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        log.info("删除分类：{}", id);


        categoryService.remove(id);
        return R.success("删除成功");

    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
