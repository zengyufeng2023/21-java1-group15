package com.guet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.common.CustomException;
import com.guet.entity.Category;
import com.guet.entity.Product;
import com.guet.entity.Setmeal;
import com.guet.mapper.CategoryMapper;
import com.guet.service.CategoryService;
import com.guet.service.ProductService;
import com.guet.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private ProductService productService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {

        LambdaQueryWrapper<Product> productLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询宠物商品
        productLambdaQueryWrapper.eq(Product::getCategoryId, id);
        int count1 = productService.count(productLambdaQueryWrapper);

        //查询当前分类是否关联了宠物，如果已经关联，抛出一个业务异常
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了商品，不能删除");
        }


        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
