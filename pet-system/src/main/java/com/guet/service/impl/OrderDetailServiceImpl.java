package com.guet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.OrderDetail;
import com.guet.mapper.OrderDetailMapper;
import com.guet.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper,OrderDetail>implements OrderDetailService {
}
