package com.guet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.dto.ProductDto;
import com.guet.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ProductService extends IService<Product> {

    //新增宠物产品，同时插入产品对应的类型数据，需要操作两张表：product product_type
    public void saveWithType(ProductDto productDto);

    //根据id查询产品，同时查询产品对应的类型数据
    public ProductDto getByIdWithType(Long id);

    //根据id更新产品信息，同时更新对应的类型信息
    public void updateWithType(ProductDto productDto);

    //根据id删除产品，同时删除对应的类型数据
    public void remove(List<Long> ids);

    //根据id修改产品状态
    public void status(Integer status, List<Long> ids);
}
