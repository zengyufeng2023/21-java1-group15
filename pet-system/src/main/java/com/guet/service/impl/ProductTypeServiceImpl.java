package com.guet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.ProductType;
import com.guet.mapper.ProductTypeMapper;
import com.guet.service.ProductTypeService;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {
}
