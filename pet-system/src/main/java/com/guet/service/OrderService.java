package com.guet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.common.R;
import com.guet.entity.Orders;

import java.util.Map;

public interface OrderService extends IService<Orders> {

    //用户端用户下单
    public void submit(Orders orders);

    //用户端再来一单
    public void again(Map<String, String> map);

    //管理端修改订单状态
    public void changeStatus(Map<String, String> map);


}
