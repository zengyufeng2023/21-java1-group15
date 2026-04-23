package com.guet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.common.CustomException;
import com.guet.dto.ProductDto;
import com.guet.entity.Product;
import com.guet.entity.ProductType;
import com.guet.mapper.ProductMapper;
import com.guet.service.ProductService;
import com.guet.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private ProductService productService;

    /**
     * 新增产品，同时保存对应的类型数据
     *
     * @param productDto
     */
    @Transactional
    public void saveWithType(ProductDto productDto) {
        //保存产品基本信息到产品表
        this.save(productDto);

        Long productId = productDto.getId();//产品id

        //产品类型
        List<ProductType> types = productDto.getTypes();
        types = types.stream().map((item) -> {
            item.setProductId(productId);
            return item;
        }).collect(Collectors.toList());

        //保存产品类型数据到产品类型数据表  product_type
        productTypeService.saveBatch(types);
    }

    /**
     * 根据id查询产品，同时查询产品对应的类型数据
     *
     * @param id
     * @return
     */
    @Override
    public ProductDto getByIdWithType(Long id) {
        //查询产品基本信息，从product表查询
        Product product = this.getById(id);

        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);

        //查询当前产品对应的类型信息，从product_type表查询
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductType::getProductId, product.getId());
        List<ProductType> types = productTypeService.list(queryWrapper);
        productDto.setTypes(types);


        return productDto;
    }

    /**
     * 根据产品id更新信息，同时更新对应的类型信息
     *
     * @param productDto
     */
    @Override
    @Transactional
    public void updateWithType(ProductDto productDto) {
        //更新product表基本信息
        this.updateById(productDto);

        //清理当前产品对应的类型信息 product_type表的delete操作
        LambdaQueryWrapper<ProductType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductType::getProductId, productDto.getId());

        productTypeService.remove(queryWrapper);

        //添加当前提交过来的类型的数据 product_type表的insert操作
        List<ProductType> types = productDto.getTypes();

        types = types.stream().map((item) -> {
            item.setProductId(productDto.getId());
            return item;
        }).collect(Collectors.toList());

        productTypeService.saveBatch(types);
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
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Product::getId, ids);
        queryWrapper.eq(Product::getStatus, 1);
        int count = this.count(queryWrapper);
        if (count > 0) {
            //如果不能删除，抛出一个业务异常
            throw new CustomException("产品正在售卖中，不能删除");
        }

        //如果可以删除，删除产品表中的数据 product
        this.removeByIds(ids);

    }

    /**
     * 根据id修改产品状态
     *
     * @param status
     * @param ids
     */
    @Override
    public void status(Integer status, List<Long> ids) {
        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids != null, Product::getId, ids);
        updateWrapper.set(Product::getStatus, status);
        productService.update(updateWrapper);
    }


}
