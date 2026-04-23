package com.guet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.ShoppingCart;
import com.guet.mapper.ShoppingCartMapper;
import com.guet.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
