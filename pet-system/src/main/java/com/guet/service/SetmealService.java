package com.guet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.dto.ProductDto;
import com.guet.dto.SetmealDto;
import com.guet.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {


    //新增套餐，同时需要保存套餐和商品的关联管理
    public void saveWithProduct(SetmealDto setmealDto);

    //根据id删除宠物，同时删除对应的属性数据
    public void remove(List<Long> ids);

    //根据id更新套餐信息，同时更新对应的类型信息
    public void updateWithType(SetmealDto setmealDto);

    //根据id修改产品状态
    public void status(Integer status, List<Long> ids);
}
