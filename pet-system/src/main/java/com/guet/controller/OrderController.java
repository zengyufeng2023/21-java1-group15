package com.guet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guet.common.BaseContext;
import com.guet.common.R;
import com.guet.dto.OrdersDto;
import com.guet.entity.OrderDetail;
import com.guet.entity.Orders;
import com.guet.entity.User;
import com.guet.service.OrderDetailService;
import com.guet.service.OrderService;
import com.guet.service.ShoppingCartService;
import com.guet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单管理
 */

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    /**
     * 用户端用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据:{}", orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 用户端分页查询订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        log.info("订单数据page：{}  ,页数pageSize：{}", page, pageSize);
        //获取当前id
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //查询当前用户id订单数据
        queryWrapper.eq(userId != null, Orders::getUserId, userId);
        //按时间降序排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        List<OrdersDto> list = pageInfo.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            //获取orderId,然后根据这个id，去orderDetail表中查数据
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> details = orderDetailService.list(wrapper);
            BeanUtils.copyProperties(item, ordersDto);
            //之后set一下属性
            ordersDto.setOrderDetails(details);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        ordersDtoPage.setRecords(list);
        //日志输出看一下
        log.info("list:{}", list);
        return R.success(ordersDtoPage);
    }

    /**
     * 用户端再来一单
     *
     * @param map
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Map<String, String> map) {
        log.info("map：", map);
        orderService.again(map);
        return R.success("喜欢吃就再来一单吖~");
    }

    /**
     * 管理端查询订单列表
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        log.info("订单信息page：{},pageSize：{},number：{},beginTime：{},endTime：{}", page, pageSize, number, beginTime,
            endTime);
        //获取当前id
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //按时间降序排序
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //订单号
        queryWrapper.eq(number != null, Orders::getId, number);
        //时间段，大于开始，小于结束
        queryWrapper.gt(!StringUtils.isEmpty(beginTime), Orders::getOrderTime, beginTime)
            .lt(!StringUtils.isEmpty(endTime), Orders::getOrderTime, endTime);
        orderService.page(pageInfo, queryWrapper);
        List<OrdersDto> list = pageInfo.getRecords().stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            //获取orderId,然后根据这个id，去orderDetail表中查数据
            Long orderId = item.getId();
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> details = orderDetailService.list(wrapper);
            BeanUtils.copyProperties(item, ordersDto);
            //之后set一下属性
            ordersDto.setOrderDetails(details);
            return ordersDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        ordersDtoPage.setRecords(list);
        //日志输出看一下
        log.info("list:{}", list);
        return R.success(ordersDtoPage);
    }

    /**
     * 管理端修改订单状态（取消，派送，完成）
     *
     * @param map
     * @return
     */
    @PutMapping
    public R<String> changeStatus(@RequestBody Map<String, String> map) {
        log.info("订单状态信息map：{}", map);
        orderService.changeStatus(map);
        return R.success("订单状态修改成功");
    }
}


