package com.guet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guet.common.CustomException;
import com.guet.common.R;
import com.guet.dto.ProductDto;
import com.guet.dto.SetmealDto;
import com.guet.entity.Category;
import com.guet.entity.Product;
import com.guet.entity.Setmeal;
import com.guet.entity.SetmealProduct;
import com.guet.service.CategoryService;
import com.guet.service.ProductService;
import com.guet.service.SetmealProductService;
import com.guet.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealProductService setmealProductService;

    @Autowired
    private ProductService productService;


    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息{}", setmealDto);

        // setmealProducts: 套餐商品没加上去，categoryName：套餐分类没加上去
        setmealService.saveWithProduct(setmealDto);
        return R.success("新增套餐成功");
    }


    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤器
        queryWrapper.like(name != null, Setmeal::getName, name);
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //分页查询
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }


    /**
     * 取商品分类对应的套餐
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, 1);
        //排序
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        return R.success(setmealList);
    }

    /**
     * 获取套餐的全部商品
     *
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public R<List<ProductDto>> showSetmealProduct(@PathVariable Long id) {
        //条件构造器
        LambdaQueryWrapper<SetmealProduct> queryWrapper = new LambdaQueryWrapper<>();
        //手里的数据只有setmealId
        queryWrapper.eq(SetmealProduct::getSetmealId, id);
        //查询数据
        List<SetmealProduct> records = setmealProductService.list(queryWrapper);
        List<ProductDto> dtoList = records.stream().map((item) -> {
            ProductDto productDto = new ProductDto();
            //copy数据
            BeanUtils.copyProperties(item, productDto);
            //查询对应商品id
            Long productId = item.getProductId();
            //根据商品id获取具体商品数据，这里要自动装配 productService
            Product product = productService.getById(productId);
            //其实主要数据是要那个图片，不过我们这里多copy一点也没事
            BeanUtils.copyProperties(product, productDto);
            return productDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }


    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        if (setmeal == null) {
            throw new CustomException("套餐信息不存在，请刷新重试");
        }
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealProduct::getSetmealId, id);
        List<SetmealProduct> setmealProducts = setmealProductService.list(queryWrapper);
        setmealDto.setSetmealProducts(setmealProducts);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<Setmeal> updateWithProduct(@RequestBody SetmealDto setmealDto) {
        List<SetmealProduct> setmealProducts = setmealDto.getSetmealProducts();
        Long setmealId = setmealDto.getId();
        //先根据id把setmealProduct表中对应套餐的数据删了
        LambdaQueryWrapper<SetmealProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealProduct::getSetmealId, setmealId);
        setmealProductService.remove(queryWrapper);
        //然后在重新添加
        setmealProducts = setmealProducts.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        //更新套餐数据
        setmealService.updateById(setmealDto);
        //更新套餐对应商品数据
        setmealProductService.saveBatch(setmealProducts);
        return R.success(setmealDto);
    }


    /**
     * 根据id修改套餐状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("产品status：{},产品ids：{}", status, ids);
        setmealService.status(status, ids);

        return R.success("修改产品状态成功");
    }


    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除产品id:{}", ids);
        setmealService.remove(ids);
        return R.success("宠物数据删除成功");

    }
}
