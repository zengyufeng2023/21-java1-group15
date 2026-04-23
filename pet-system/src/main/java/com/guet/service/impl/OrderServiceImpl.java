package com.guet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.common.BaseContext;
import com.guet.common.CustomException;
import com.guet.dto.OrdersDto;
import com.guet.entity.*;
import com.guet.mapper.OrderMapper;
import com.guet.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;

    /**
     * 用户端用户下单
     *
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户的id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }

        //订单号
        long orderId = IdWorker.getId();

        //遍历得到账单总金额,原子操作
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);//关联订单
            orderDetail.setNumber(item.getNumber());
            orderDetail.setProductType(item.getProductType());
            orderDetail.setProductId(item.getProductId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        //向订单表插入数据，一条数据。向订单表设置属性
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);//待派送
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
            + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
            + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
            + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        this.save(orders);

        //向订单明细表插入数据，可能多条
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);

    }

    /**
     * 用户端再来一单
     *
     * @param map
     */
    @Override
    public void again(Map<String, String> map) {
        Long orderId = Long.valueOf(map.get("id"));
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> details = orderDetailService.list(queryWrapper);
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCarts = details.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(item, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCartService.saveBatch(shoppingCarts);
    }

    /**
     * 管理端修改订单状态
     *
     * @param map
     */
    @Override
    public void changeStatus(Map<String, String> map) {
        int status = Integer.parseInt(map.get("status"));
        Long orderId = Long.valueOf(map.get("id"));
        //log.info("修改订单状态:status={status},id={id}", status, orderId);
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId, orderId);
        updateWrapper.set(Orders::getStatus, status);
        orderService.update(updateWrapper);
    }


}
