package com.guet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guet.common.R;
import com.guet.dto.ProductDto;
import com.guet.dto.SetmealDto;
import com.guet.entity.*;
import com.guet.mapper.ProductTypeMapper;
import com.guet.service.CategoryService;
import com.guet.service.ProductService;
import com.guet.service.ProductTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductTypeService productTypeService;


    /**
     * 新增商品
     *
     * @param productDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody ProductDto productDto) {
        log.info(productDto.toString());

        productService.saveWithType(productDto);

        return R.success("新增产品成功");
    }

    /**
     * 宠物产品信息分页
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Product> pageInfo = new Page<>(page, pageSize);
        Page<ProductDto> productDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤器
        queryWrapper.like(name != null, Product::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Product::getUpdateTime);

        //执行分页查询
        productService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, productDtoPage, "records");

        List<Product> records = pageInfo.getRecords();

        List<ProductDto> list = records.stream().map((item) -> {
            ProductDto productDto = new ProductDto();

            BeanUtils.copyProperties(item, productDto);//对象拷贝

            Long categoryId = item.getCategoryId();//获取分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                productDto.setCategoryName(categoryName);
            }

            return productDto;

        }).collect(Collectors.toList());


        productDtoPage.setRecords(list);

        return R.success(productDtoPage);

    }

    /**
     * 根据查询商品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<ProductDto> get(@PathVariable Long id) {
        ProductDto productDto = productService.getByIdWithType(id);

        return R.success(productDto);

    }

    /**
     * 修改产品信息
     *
     * @param productDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody ProductDto productDto) {
        log.info(productDto.toString());

        productService.updateWithType(productDto);

        return R.success("修改产品信息成功");
    }

    /**
     * 删除产品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除产品ids为{}：" + ids);

        productService.remove(ids);

        return R.success("删除产品成功");
    }

    /**
     * 根据id修改产品状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("产品status：{},产品ids：{}", status, ids);
        productService.status(status, ids);

        return R.success("修改产品状态成功");
    }


    /**
     * 根据条件查询对应产品数据
     *
     * @param product
     * @return
     */
   /* @GetMapping("/list")
    public R<List<Product>> list(Product product){

        //构造查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(product.getCategoryId() != null,
                Product::getCategoryId,product.getCategoryId());

        //添加条件，查询
        queryWrapper.eq(Product::getStatus,1);
        queryWrapper.orderByDesc(Product::getSort).orderByDesc(Product::getUpdateTime);
        List<Product> list = productService.list(queryWrapper);

        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<ProductDto>> list(Product product) {
        //构造查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(product.getCategoryId() !=
            null, Product::getCategoryId, product.getCategoryId());
        //添加条件，查询状态为1（起售状态）的商品
        queryWrapper.eq(Product::getStatus, 1);

        //添加排序条件
        queryWrapper.orderByAsc(Product::getSort).orderByDesc(Product::getUpdateTime);

        List<Product> list = productService.list(queryWrapper);

        List<ProductDto> productDtoList = list.stream().map((item) -> {
            ProductDto productDto = new ProductDto();

            BeanUtils.copyProperties(item, productDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                productDto.setCategoryName(categoryName);
            }

            //当前商品的id
            Long productId = item.getId();
            LambdaQueryWrapper<ProductType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProductType::getProductId, productId);
            //SQL:select * from product_flavor where product_id = ?
            List<ProductType> productTypeList = productTypeService.list(lambdaQueryWrapper);
            productDto.setTypes(productTypeList);
            return productDto;
        }).collect(Collectors.toList());

        return R.success(productDtoList);
    }


}
