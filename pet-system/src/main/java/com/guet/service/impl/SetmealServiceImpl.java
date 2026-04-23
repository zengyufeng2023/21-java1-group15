package com.guet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.common.CustomException;
import com.guet.dto.ProductDto;
import com.guet.dto.SetmealDto;
import com.guet.entity.ProductType;
import com.guet.entity.Setmeal;
import com.guet.entity.SetmealProduct;
import com.guet.mapper.SetmealMapper;
import com.guet.service.SetmealProductService;
import com.guet.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealProductService setmealProductService;

    /**
     * 新增套餐，同时需要保存套餐和商品的关联管理
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithProduct(SetmealDto setmealDto) {
        //保存套餐基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        List<SetmealProduct> setmealProducts = setmealDto.getSetmealProducts();
        setmealProducts = setmealProducts.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和产品的关联信息，操作setmeal_product,执行insert操作
        setmealProductService.saveBatch(setmealProducts);


    }

    /**
     * 根据id删除对应的类型数据
     *
     * @param ids
     */
    @Override
    @Transactional
    public void remove(List<Long> ids) {
        //查询产品状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，删除产品表中的数据 product
        this.removeByIds(ids);

    }

    /**
     * 根据套餐id更新信息，同时更新对应的类型信息
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithType(SetmealDto setmealDto) {
        //更新product表基本信息
        this.updateById(setmealDto);

        //清理当前产品对应的类型信息 product_type表的delete操作
        LambdaQueryWrapper<SetmealProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealProduct::getProductId, setmealDto.getId());

        setmealProductService.remove(queryWrapper);

        //添加当前提交过来的类型的数据 product_type表的insert操作
        List<SetmealProduct> types = setmealDto.getSetmealProducts();

        types = types.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealProductService.saveBatch(types);
    }



    /**
     * 根据id修改套餐状态
     *
     * @param status
     * @param ids
     */
    @Override
    public void status(Integer status, List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids != null, Setmeal::getId, ids);
        updateWrapper.set(Setmeal::getStatus, status);
        setmealService.update(updateWrapper);
    }
}
