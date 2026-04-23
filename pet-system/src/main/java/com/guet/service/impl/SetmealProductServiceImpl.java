package com.guet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.SetmealProduct;
import com.guet.mapper.SetmealProductMapper;
import com.guet.service.SetmealProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SetmealProductServiceImpl extends ServiceImpl<SetmealProductMapper, SetmealProduct> implements SetmealProductService {
}
